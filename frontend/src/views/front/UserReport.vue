<script setup>
import http from "@/utils/http";
import { Search, Refresh, Plus, View } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getImageUrl } from "@/utils/system";
import { computed, onMounted, ref } from "vue";
import useUserStore from "@/stores/userStore";
import Config from "@/config/index";
import { Picture as IconPicture } from "@element-plus/icons-vue";

const userStore = useUserStore();

const initParams = {
  pageNum: 1,
  pageSize: 10,
  title: "",
  status: "",
};

// 搜索条件
const params = ref({ ...initParams });
// 表格数据
const tableData = ref([]);
// 表格数据总数
const total = ref(0);
// 新增弹窗显示
const addVisible = ref(false);
// 新增表单
const addForm = ref({
  title: "",
  description: "",
  location: "",
  occurrenceTime: "",
  imgUrl: "",
  contactInfo: "",
  isAnonymous: false
});

// 状态字典
const statusDict = {
  0: { text: "待处理", type: "warning" },
  1: { text: "处理中", type: "primary" },
  2: { text: "处理完成", type: "success" }
};

// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");

// 获取表格数据
const getTableData = async () => {
  const userId = userStore.userInfo?.id;
  
  const res = await http.get(
    `/report/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&title=${params.value.title}&status=${params.value.status}&userId=${userId}`
  );
  if (res.code === 200) {
    tableData.value = res.data.records;
    total.value = res.data.total;
  }
};

// 搜索
const handleSearch = async () => {
  await getTableData();
};

// 重置
const handleReset = () => {
  params.value = { ...initParams };
  getTableData();
};

// 显示新增弹窗
const showAddDialog = () => {
  addForm.value = {
    title: "",
    description: "",
    location: "",
    occurrenceTime: "",
    imgUrl: "",
    contactInfo: "",
    isAnonymous: false
  };
  imageUrl.value = "";
  addVisible.value = true;
};

// 提交新增
const submitAdd = async () => {
  if (!addForm.value.title.trim()) {
    ElMessage.warning("请输入标题");
    return;
  }
  if (!addForm.value.description.trim()) {
    ElMessage.warning("请输入描述");
    return;
  }
  
  const formData = { ...addForm.value };
  
  // 如果是匿名举报，清空联系方式
  if (formData.isAnonymous) {
    formData.contactInfo = "";
  }
  
  // 添加用户ID
  formData.userId = userStore.userInfo?.id;
  
  const res = await http.post("/report/add", formData);
  
  if (res.code === 200) {
    ElMessage.success("举报提交成功");
    addVisible.value = false;
    await getTableData();
  } else {
    ElMessage.error("举报提交失败");
  }
};

// 查看详情
const viewDetail = (row) => {
  ElMessageBox.alert(
    `<div style="text-align: left;">
      <p><strong>标题：</strong>${row.title}</p>
      <p><strong>描述：</strong>${row.description}</p>
      <p><strong>地点：</strong>${row.location || '未填写'}</p>
      <p><strong>发生时间：</strong>${row.occurrenceTime || '未填写'}</p>
      <p><strong>联系方式：</strong>${row.contactInfo || '未填写'}</p>
      <p><strong>举报类型：</strong>${row.isAnonymous ? '匿名举报' : '实名举报'}</p>
      <p><strong>状态：</strong>${statusDict[row.status].text}</p>
      ${row.processResult ? `<p><strong>处理结果：</strong>${row.processResult}</p>` : ''}
    </div>`,
    '举报详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确定'
    }
  );
};

/** 文件上传前的钩子函数 */
const beforeAvatarUpload = (rawFile) => {
  if (!rawFile.type.includes("image")) {
    ElMessage.error("文件类型错误，只允许上传图片文件");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("图片超过2MB限制");
    return false;
  }
  return true;
};

/** 文件上传成功回调 */
const handleAvatarSuccess = (response) => {
  addForm.value.imgUrl = response.data;
  imageUrl.value = `${Config.baseUrl}${response.data}`;
};

const handleSizeChange = (val) => {
  params.value.pageSize = val;
  getTableData();
};

const handleCurrentChange = (val) => {
  params.value.pageNum = val;
  getTableData();
};

onMounted(async () => {
  await getTableData();
});
</script>

<template>
  <div class="user-report-container">
    <div class="page-header">
      <h2>我的举报</h2>
      <p>您可以在这里查看和提交举报信息</p>
    </div>
    
    <div class="search-section">
      <div class="flex">
        <div class="flex form-item">
          <p class="label-text">标题：</p>
          <el-input v-model="params.title" placeholder="请输入标题" />
        </div>
        <div class="flex form-item">
          <p class="label-text">状态：</p>
          <el-select v-model="params.status" placeholder="请选择状态" clearable style="width: 200px;">
            <el-option label="待处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="处理完成" :value="2" />
          </el-select>
        </div>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button type="warning" :icon="Refresh" @click="handleReset">重置</el-button>
      </div>
    </div>
    
    <div class="content-section">
      <div class="flex op-box">
        <el-button
          :icon="Plus"
          type="primary"
          @click="showAddDialog"
        >新增举报</el-button>
      </div>
      
      <!-- 表格 -->
      <el-table
        :data="tableData"
        border
        class="report-table"
      >
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="location" label="地点" width="120" />
        <el-table-column prop="occurrenceTime" label="发生时间" width="180" />
        <el-table-column prop="isAnonymous" label="举报类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isAnonymous ? 'info' : 'success'">
              {{ scope.row.isAnonymous ? '匿名' : '实名' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="statusDict[scope.row.status].type">
              {{ statusDict[scope.row.status].text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="imgUrl" label="图片证据" width="100">
          <template #default="scope">
            <el-image
              v-if="scope.row.imgUrl"
              :src="getImageUrl(scope.row.imgUrl)"
              :preview-src-list="[getImageUrl(scope.row.imgUrl)]"
              :preview-teleported="true"
              :hide-on-click-modal="true"
              :lazy="true"
              fit="cover"
              class="evidence-image"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="processResult" label="处理结果" width="200" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.processResult">{{ scope.row.processResult }}</span>
            <span v-else class="no-result">暂无处理结果</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="120">
          <template #default="scope">
            <el-button
              type="info"
              :icon="View"
              size="small"
              @click="viewDetail(scope.row)"
            >查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="params.pageNum"
          v-model:page-size="params.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <!-- 新增举报弹窗 -->
    <el-dialog
      v-model="addVisible"
      title="新增举报"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="addForm" label-width="100px" class="add-form">
        <el-form-item label="举报类型" required>
          <el-radio-group v-model="addForm.isAnonymous">
            <el-radio :label="false">实名举报</el-radio>
            <el-radio :label="true">匿名举报</el-radio>
          </el-radio-group>
          <div class="form-tip">
            <span v-if="addForm.isAnonymous" class="tip-text">匿名举报将不会显示您的个人信息</span>
            <span v-else class="tip-text">实名举报有助于我们更好地处理问题</span>
          </div>
        </el-form-item>
        
        <el-form-item label="标题" required>
          <el-input
            v-model="addForm.title"
            placeholder="请输入举报标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="描述" required>
          <el-input
            v-model="addForm.description"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }"
            placeholder="请详细描述举报内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="地点">
          <el-input
            v-model="addForm.location"
            placeholder="请输入事发地点"
            maxlength="100"
          />
        </el-form-item>
        
        <el-form-item label="发生时间">
          <el-date-picker
            v-model="addForm.occurrenceTime"
            type="datetime"
            placeholder="请选择发生时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="图片证据">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadData"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <el-image
              v-if="imageUrl"
              :src="imageUrl"
              style="width: 100%; height: 100%"
            />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
          <div class="form-tip">
            <span class="tip-text">支持jpg、png格式，文件大小不超过2MB</span>
          </div>
        </el-form-item>
        
        <el-form-item v-if="!addForm.isAnonymous" label="联系方式">
          <el-input
            v-model="addForm.contactInfo"
            placeholder="请输入您的联系方式"
            maxlength="50"
          />
          <div class="form-tip">
            <span class="tip-text">联系方式仅用于处理举报，不会公开</span>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitAdd">提交举报</el-button>
          <el-button @click="addVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.user-report-container {
  padding: 20px;
  background: #f8f9fa;
  min-height: calc(100vh - 70px);
}

.page-header {
  background: #ffffff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: var(--primary-color);
    font-size: 24px;
    font-weight: 600;
  }
  
  p {
    margin: 0;
    color: #666;
    font-size: 14px;
  }
}

.search-section {
  background: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.content-section {
  background: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.label-text {
  color: #606266;
  width: 80px;
  font-weight: 500;
}

.form-item {
  margin-right: 20px;
}

.op-box {
  margin-bottom: 20px;
}

.report-table {
  border-radius: 8px;
  overflow: hidden;
}

.evidence-image {
  width: 55px;
  height: 55px;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.add-form {
  .form-tip {
    margin-top: 4px;
    
    .tip-text {
      font-size: 12px;
      color: #909399;
    }
  }
}

.avatar-uploader {
  width: 178px;
  height: 178px;
  overflow: hidden;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;

  &:hover {
    border-color: var(--el-color-primary);
  }
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}

.no-result {
  color: #909399;
  font-style: italic;
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--el-fill-color-light);
}

:deep(.el-button--primary) {
  background: var(--primary-color);
  border-color: var(--primary-color);
  
  &:hover {
    background: #a83330;
    border-color: #a83330;
  }
}

:deep(.el-tag--primary) {
  background-color: rgba(198, 62, 58, 0.1);
  border-color: var(--primary-color);
  color: var(--primary-color);
}
</style>