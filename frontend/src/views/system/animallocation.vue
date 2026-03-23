<script setup>
import http from "@/utils/http";
import { Plus, Search, Refresh, Edit, Delete } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { computed, onMounted, ref } from "vue";
import { hasPermission } from "@/utils/system";
import { getImageUrl } from "@/utils/system";
import Config from "@/config/index";

const initParams = {
  pageNum: 1,
  pageSize: 10,
  name: "",
};

// 搜索条件
const params = ref({ ...initParams });

// 初始表单数据
const initForm = {
  name: "",
  description: "",
  lat: "",
  lon: "",
  bestSeason: "",
  commonAnimals: "",
  facilities: "",
  imgUrl: "",
};

// 表单数据
const form = ref({ ...initForm });

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入地点名称', trigger: 'blur' }],
  lat: [
    { required: true, message: '请输入纬度', trigger: 'blur' },
    { pattern: /^-?([0-8]?\d(\.\d+)?|90(\.0+)?)$/, message: '请输入有效的纬度值(-90到90)', trigger: 'blur' }
  ],
  lon: [
    { required: true, message: '请输入经度', trigger: 'blur' },
    { pattern: /^-?((1[0-7]\d)|([0-9]?\d))(\.\d+)?$|^-?180(\.0+)?$/, message: '请输入有效的经度值(-180到180)', trigger: 'blur' }
  ],
  description: [{ required: false, message: '请输入描述', trigger: 'blur' }],
  bestSeason: [{ required: false, message: '请输入最佳动物季节', trigger: 'blur' }],
  commonAnimals: [{ required: false, message: '请输入常见动物', trigger: 'blur' }],
  facilities: [{ required: false, message: '请输入设施信息', trigger: 'blur' }],
  imgUrl: [{ required: false, message: '请输入图片链接', trigger: 'blur' }]
};

// 表格数据
const tableData = ref([]);

// 表格数据总数
const total = ref(0);

// 控制弹窗显示
const visible = ref(false);

// 批量删除相关
const multipleSelection = ref([]);
const selectedIds = computed(() => {
  return multipleSelection.value.map((item) => item.id);
});
const hasSelected = computed(() => {
  return multipleSelection.value.length > 0;
});

const isEdit = computed(() => {
  return form.value.id > 0;
});

const dialogTitle = computed(() => {
  return isEdit.value ? "编辑" : "新增";
});

// 获取表格数据
const getTableData = async () => {
  const res = await http.get(
    `/animalLocation/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&name=${params.value.name}`
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

// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");

// 新增
const handleAdd = () => {
  form.value = { ...initForm };
  imageUrl.value = "";
  visible.value = true;
};

// 编辑
const handleEdit = (row) => {
  form.value = { ...row };
  imageUrl.value = row.imgUrl ? getImageUrl(row.imgUrl) : "";
  visible.value = true;
};

// 删除
const delRow = async (id) => {
  const res = await http.get(`/animalLocation/del?id=${id}`);
  if (res.code === 200) {
    ElMessage.success("删除成功");
    await getTableData();
  } else {
    ElMessage.error("删除失败");
  }
};

// 批量删除
const batchDel = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning("请先选择要删除的数据");
    return;
  }
  const res = await http.post("/animalLocation/batchDel", selectedIds.value);
  if (res.code === 200) {
    ElMessage.success("删除成功");
    await getTableData();
  } else {
    ElMessage.error("删除失败");
  }
};

// 提交表单
const submit = async () => {
  const url = isEdit.value ? "/animalLocation/edit" : "/animalLocation/add";
  const method = isEdit.value ? "post" : "post";
  
  let res;
  if (isEdit.value) {
    res = await http.post(`${url}?id=${form.value.id}`, form.value);
  } else {
    res = await http.post(url, form.value);
  }
  
  if (res.code === 200) {
    ElMessage.success(isEdit.value ? "编辑成功" : "新增成功");
    visible.value = false;
    await getTableData();
  } else {
    ElMessage.error(isEdit.value ? "编辑失败" : "新增失败");
  }
};

// 表格选择变化
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

// 分页大小变化
const handleSizeChange = (val) => {
  params.value.pageSize = val;
  getTableData();
};

// 当前页变化
const handleCurrentChange = (val) => {
  params.value.pageNum = val;
  getTableData();
};

onMounted(async () => {
  await getTableData();
});

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
  form.value.imgUrl = response.data;
  imageUrl.value = `${Config.baseUrl}${response.data}`;
};
</script>

<template>
  <div>
    <header class="flex">
      <div class="flex form-item">
        <p class="label-text">地点名称：</p>
        <el-input v-model="params.name" placeholder="请输入地点名称" />
      </div>
      <el-button type="primary" :icon="Search" @click="handleSearch"
        >搜索</el-button
      >
      <el-button type="warning" :icon="Refresh" @click="handleReset"
        >重置</el-button
      >
    </header>
    <main>
      <div class="flex op-box">
        <el-button
          v-if="hasPermission('animallocation', '新增')"
          :icon="Plus"
          type="primary"
          @click="handleAdd"
          >新增</el-button
        >
        <el-button
          v-if="hasPermission('animallocation', '删除')"
          :icon="Delete"
          type="danger"
          :disabled="!hasSelected"
          @click="batchDel"
          >批量删除</el-button
        >
      </div>
      <!-- 表格 -->
      <el-table
        :data="tableData"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="地点名称" width="150" show-overflow-tooltip />
        <el-table-column prop="imgUrl" label="图片" width="100">
          <template #default="scope">
            <el-image
              :src="getImageUrl(scope.row.imgUrl)"
              :preview-src-list="[getImageUrl(scope.row.imgUrl)]"
              :preview-teleported="true"
              :hide-on-click-modal="true"
              :lazy="true"
              :initial-index="0"
              fit="cover"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon>
                    <Picture />
                  </el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" width="200" show-overflow-tooltip />
        <el-table-column prop="lat" label="纬度" width="120" />
        <el-table-column prop="lon" label="经度" width="120" />
        <el-table-column prop="bestSeason" label="最佳动物季节" />
        <el-table-column prop="commonAnimals" label="常见动物" width="200" show-overflow-tooltip />
        <el-table-column prop="facilities" label="设施信息" width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button
              v-if="hasPermission('animallocation', '编辑')"
              type="primary"
              :icon="Edit"
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-popconfirm
              title="您确定要删除吗?"
              @confirm="delRow(scope.row.id)"
            >
              <template #reference>
                <el-button
                  v-if="hasPermission('animallocation', '删除')"
                  type="danger"
                  :icon="Delete"
                  >删除</el-button
                >
              </template>
            </el-popconfirm>
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
    </main>
    <el-dialog v-model="visible" :title="dialogTitle" width="900">
      <el-form :model="form" :rules="rules" label-width="120" ref="formRef">
 <el-form-item label="地点名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入地点名称" />
            </el-form-item>
                    <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度" prop="lat">
              <el-input v-model="form.lat" placeholder="请输入纬度(-90到90)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="lon">
              <el-input v-model="form.lon" placeholder="请输入经度(-180到180)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最佳动物季节" prop="bestSeason">
              <el-input v-model="form.bestSeason" placeholder="请输入最佳动物季节" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设施信息" prop="facilities">
              <el-input v-model="form.facilities" placeholder="请输入设施信息" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input
            type="textarea"
            v-model="form.description"
            :autosize="{ minRows: 3, maxRows: 6 }"
            placeholder="请输入地点描述"
          />
        </el-form-item>
        <el-form-item label="常见动物" prop="commonAnimals">
          <el-input
            type="textarea"
            v-model="form.commonAnimals"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入常见动物，多个动物用逗号分隔"
          />
        </el-form-item>
                <el-form-item label="图片上传" prop="imgUrl">
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
            </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit()">确认</el-button>
          <el-button @click="visible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.label-text {
  color: #606266;
  width: 120px;
}

.form-item {
  margin-right: 20px;
}

.op-box {
  margin: 10px 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--el-fill-color-light);
}

.avatar-uploader {
  width: 178px;
  height: 178px;
  overflow: hidden;
  border: 1px dashed var(--el-border-color);

  &:hover {
    border-color: var(--el-color-primary);
  }
}

.avatar {
  width: 100%;
  height: 100%;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px;
  height: 60px;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

:deep(.el-table .el-image) {
  width: 60px;
  height: 60px;
  border-radius: 4px;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>