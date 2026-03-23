<script setup lang="ts">
import http from "@/utils/http";
import { Plus, Search, Refresh, Edit, Delete, Picture as IconPicture } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { computed, onMounted, ref, onBeforeUnmount, shallowRef } from "vue";
import { hasPermission } from "@/utils/system";
import { getImageUrl } from "@/utils/system";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig } from "@wangeditor/editor";
import { IToolbarConfig } from '@wangeditor/editor'
import "@wangeditor/editor/dist/css/style.css";
import Config from "@/config/index";
import useUserStore from "../../stores/userStore";

const { userInfo } = useUserStore();

const initParams = {
  pageNum: 1,
  pageSize: 10,
  title: "",
  travelId: null,
  categoryId: null,
};

// 搜索条件
const params = ref({ ...initParams });

// 初始表单数据
const initForm = {
  title: "",
  content: "",
  imgUrl: "",
  userId: null,
  travelId: null,
  categoryId: null,
  viewCount: 0
};

// 表单数据
const form = ref({ ...initForm });

// 景点列表
const travelList = ref([]);

// 分类列表
const categoryList = ref([]);

// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入攻略标题', trigger: 'blur' }],
  content: [{ required: false, message: '请输入攻略内容', trigger: 'blur' }],
  travelId: [{ required: true, message: '请选择景点', trigger: 'change' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
};

// 表格数据
const tableData = ref([]);

// 表格数据总数
const total = ref(0);

// 控制弹窗显示
const visible = ref(false);

// 控制内容查看弹窗显示
const contentVisible = ref(false);
// 当前查看的内容
const currentContent = ref("");

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const toolbarConfig: Partial<IToolbarConfig> = {} // 工具栏配置
const editorConfig: Partial<IEditorConfig> = {  // 编辑器配置
    MENU_CONF: {
        uploadImage: { // 上传图片的配置
            server: `${Config.baseUrl}/file/wang/upload`, // 上传图片的网址
            fieldName: 'file', // 上传文件的名称
        }
    }
}
const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

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

// 显示内容弹窗
const showContent = (content) => {
  currentContent.value = content;
  contentVisible.value = true;
};

// 获取景点列表
const getTravelList = async () => {
  const res = await http.get("/travel/list");
  if (res.code === 200) {
    travelList.value = res.data;
  }
};

// 获取分类列表
const getCategoryList = async () => {
  const res = await http.get("/travelGuideCategory/list");
  if (res.code === 200) {
    categoryList.value = res.data;
  }
};

// 获取表格数据
const getTableData = async () => {
  let url = `/travelGuide/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&userId=${userInfo.id}`;
  if (params.value.title) {
    url += `&title=${params.value.title}`;
  }
  if (params.value.travelId) {
    url += `&travelId=${params.value.travelId}`;
  }
  
  const res = await http.get(url);
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
  const res = await http.get(`/travelGuide/del?id=${id}`);
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
  const res = await http.post("/travelGuide/batchDel", selectedIds.value);
  if (res.code === 200) {
    ElMessage.success("批量删除成功");
    multipleSelection.value = [];
    await getTableData();
  } else {
    ElMessage.error("批量删除失败");
  }
};

// 多选
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection;
};

// 确认按钮
const submit = async () => {
  if (isEdit.value) {
    const res = await http.post(`/travelGuide/edit?id=${form.value.id}`, form.value);
    if (res.code === 200) {
      ElMessage.success("编辑成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("编辑失败");
    }
  } else {
    const res = await http.post(`/travelGuide/add`, form.value);
    if (res.code === 200) {
      ElMessage.success("新增成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("新增失败");
    }
  }
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
  form.value.imgUrl = response.data;
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
  await getTravelList();
  await getCategoryList();
  await getTableData();
});

onBeforeUnmount(() => {
  editorRef.value?.destroy()
  editorRef.value = null
})
</script>

<template>
 <div style="width: 80%; margin: 0 auto;">
    <header class="flex">
      <div class="flex form-item">
        <p class="label-text">攻略标题：</p>
        <el-input v-model="params.title" placeholder="请输入攻略标题" />
      </div>
      <div class="flex form-item">
        <p class="label-text">景点：</p>
        <el-select v-model="params.travelId" placeholder="请选择景点" clearable style="width: 200px;">
          <el-option
            v-for="travel in travelList"
            :key="travel.id"
            :label="travel.name"
            :value="travel.id"
          />
        </el-select>
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
          :icon="Plus"
          type="primary"
          @click="handleAdd"
          >新增</el-button
        >
        <el-button
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
        <el-table-column prop="title" label="攻略标题" width="200" />
        <el-table-column prop="imgUrl" label="封面图片" width="100">
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
                    <icon-picture />
                  </el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="userNickName" label="作者" width="120" />
        <el-table-column prop="travelName" label="景点名称" width="150" />
        <el-table-column prop="travelLocation" label="景点位置" width="150" />
        <el-table-column label="攻略内容" width="100">
          <template #default="scope">
            <el-button type="primary" size="small" @click="showContent(scope.row.content)">查看全部</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="240">
          <template #default="scope">
            <el-button
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
    <el-dialog v-model="visible" :title="dialogTitle" width="800" top="2%" style="max-height: 800px; overflow: hidden; overflow-y: scroll;">
      <el-form :model="form" :rules="rules" label-width="120">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="攻略标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入攻略标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="景点" prop="travelId">
              <el-select v-model="form.travelId" placeholder="请选择景点" style="width: 100%">
                <el-option
                  v-for="travel in travelList"
                  :key="travel.id"
                  :label="travel.name"
                  :value="travel.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="攻略分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择攻略分类" style="width: 100%">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面图片" prop="imgUrl">
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
          </el-col>
        </el-row>
        <!-- 内容字段放在最下面 -->
        <el-form-item label="攻略内容" prop="content">
          <div style="width: 100%;border: 1px solid #ccc">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
            />
            <Editor
              style="height: 300px; overflow-y: hidden;"
              v-model="form.content"
              :defaultConfig="editorConfig"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit()">确认</el-button>
          <el-button @click="visible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 内容查看弹窗 -->
    <el-dialog
      v-model="contentVisible"
      title="攻略内容"
      width="800px"
      :before-close="() => contentVisible = false"
    >
      <div v-html="currentContent" class="content-display"></div>
      <template #footer>
        <el-button @click="contentVisible = false">关闭</el-button>
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

.content-display {
  max-height: 400px;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.flex-center {
  display: flex;
  align-items: center;
  gap: 4px;
}

.view-icon {
  font-size: 14px;
  color: #409eff;
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--el-fill-color-light);
}

:deep(.w-e-text-container) {
  height: 300px !important;
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

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
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
</style>