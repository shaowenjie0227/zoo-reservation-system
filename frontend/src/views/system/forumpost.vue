<script setup lang="ts">
import http from "@/utils/http";
import { Plus, Search, Refresh, Edit, Delete } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { computed, onMounted, ref, nextTick } from "vue";
import { hasPermission } from "@/utils/system";
import { getImageUrl } from "@/utils/system";
import Config from "@/config/index";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig } from "@wangeditor/editor";
import { IToolbarConfig } from '@wangeditor/editor'
import "@wangeditor/editor/dist/css/style.css";

const initParams = {
  pageNum: 1,
  pageSize: 10,
  title: "",
};

// 搜索条件
const params = ref({ ...initParams });

// 初始表单数据
const initForm = {
  title: "",
  intro: "",
  content: "",
  animalId: "",
  locationId: "",
  observationTime: "",
  imgUrl: "",
  viewCount: 0,
  categoryId: null,
};

// 表单数据
const form = ref({ ...initForm });

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入帖子标题', trigger: 'blur' }],
  intro: [{ required: true, message: '请输入帖子简介', trigger: 'blur' }],
  content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }],
  animalId: [{ required: false, message: '请选择关联动物', trigger: 'change' }],
  locationId: [{ required: false, message: '请选择动物地点', trigger: 'change' }],
  observationTime: [{ required: false, message: '请选择动物时间', trigger: 'change' }],
};

// 表格数据
const tableData = ref([]);

// 表格数据总数
const total = ref(0);

// 控制弹窗显示
const visible = ref(false);

// 动物选项
const animalList = ref([]);

// 动物地点选项
const locationList = ref([]);

// 分类选项
const categoryList = ref([]);

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

// 获取表格数据
const getTableData = async () => {
  const res = await http.get(
    `/forumPost/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&title=${params.value.title}`
  );
  if (res.code === 200) {
    tableData.value = res.data.records;
    total.value = res.data.total;
  }
};

// 获取动物列表
const getAnimalList = async () => {
  const res = await http.get("/animal/list");
  if (res.code === 200) {
    animalList.value = res.data;
  }
};

// 获取动物地点列表
const getLocationList = async () => {
  const res = await http.get("/animalLocation/list");
  if (res.code === 200) {
    locationList.value = res.data;
  }
};

// 获取分类列表
const getCategoryList = async () => {
  const res = await http.get("/postCategory/list");
  if (res.code === 200) {
    categoryList.value = res.data;
  }
};

// 搜索
const handleSearch = async () => {
  params.value.pageNum = 1;
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

// 富文本编辑器实例
let quillEditor = null;

// 新增
const handleAdd = () => {
  form.value = { ...initForm };
  imageUrl.value = "";
  visible.value = true;
  nextTick(() => {
    initQuillEditor();
  });
};

// 编辑
const handleEdit = (row) => {
  form.value = { ...row };
  imageUrl.value = row.imgUrl ? getImageUrl(row.imgUrl) : "";
  visible.value = true;
  nextTick(() => {
    initQuillEditor();
    if (quillEditor && form.value.content) {
      quillEditor.root.innerHTML = form.value.content;
    }
  });
};

// 删除单行
const delRow = async (id) => {
  const res = await http.get(`/forumPost/del?id=${id}`);
  if (res.code === 200) {
    ElMessage.success("删除成功");
    await getTableData();
  } else {
    ElMessage.error("删除失败");
  }
};

// 批量删除
const batchDel = async () => {
  const res = await http.post(`/forumPost/batchDel`, selectedIds.value);
  if (res.code === 200) {
    ElMessage.success("批量删除成功");
    await getTableData();
  } else {
    ElMessage.error("批量删除失败");
  }
};

// 多选
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

// 提交表单
const submit = async () => {
  // 获取富文本编辑器内容
  if (quillEditor) {
    form.value.content = quillEditor.root.innerHTML;
  }
  
  if (isEdit.value) {
    const res = await http.post(`/forumPost/edit?id=${form.value.id}`, form.value);
    if (res.code === 200) {
      ElMessage.success("编辑成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("编辑失败");
    }
  } else {
    const res = await http.post(`/forumPost/add`, form.value);
    if (res.code === 200) {
      ElMessage.success("新增成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("新增失败");
    }
  }
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
  await getAnimalList();
  await getCategoryList();
  await getLocationList();
});

// 初始化富文本编辑器
const initQuillEditor = () => {
  if (quillEditor) {
    return;
  }
  
  const editorElement = document.getElementById('editor');
  if (editorElement) {
    quillEditor = new Quill(editorElement, {
      theme: 'snow',
      modules: {
        toolbar: [
          [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
          ['bold', 'italic', 'underline', 'strike'],
          [{ 'color': [] }, { 'background': [] }],
          [{ 'list': 'ordered'}, { 'list': 'bullet' }],
          [{ 'align': [] }],
          ['link', 'image'],
          ['clean']
        ]
      }
    });
    
    // 监听内容变化
    quillEditor.on('text-change', () => {
      form.value.content = quillEditor.root.innerHTML;
    });
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

onBeforeUnmount(() => {
  editorRef.value?.destroy()
  editorRef.value = null
})
</script>

<template>
  <div>
    <header class="flex">
      <div class="flex form-item">
        <p class="label-text">标题：</p>
        <el-input v-model="params.title" placeholder="请输入帖子标题" />
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
          v-if="hasPermission('forumpost', '新增')"
          :icon="Plus"
          type="primary"
          @click="handleAdd"
          >新增</el-button
        >
        <el-button
          v-if="hasPermission('forumpost', '删除')"
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
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
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
        <el-table-column prop="intro" label="简介" width="200" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="userNickName" label="发布者" width="120" />
        <el-table-column prop="animalName" label="关联动物" />
        <el-table-column prop="locationName" label="动物地点" />
        <el-table-column prop="observationTime" label="动物时间" width="180" />
        <el-table-column prop="viewCount" label="浏览数" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button
              v-if="hasPermission('forumpost', '编辑')"
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
                  v-if="hasPermission('forumpost', '删除')"
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
    <el-dialog v-model="visible" :title="dialogTitle" width="800">
      <el-form :model="form" :rules="rules" label-width="120">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input v-model="form.intro" placeholder="请输入帖子简介" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关联动物" prop="animalId">
          <el-select v-model="form.animalId" placeholder="请选择关联动物" clearable>
            <el-option
              v-for="animal in animalList"
              :key="animal.id"
              :label="animal.name"
              :value="animal.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="动物地点" prop="locationId">
          <el-select v-model="form.locationId" placeholder="请选择动物地点" clearable>
            <el-option
              v-for="location in locationList"
              :key="location.id"
              :label="location.name"
              :value="location.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="动物时间" prop="observationTime">
          <el-date-picker
            v-model="form.observationTime"
            type="datetime"
            placeholder="请选择动物时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
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
        <el-form-item label="浏览数" prop="viewCount">
          <el-input-number v-model="form.viewCount" :min="0" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
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
  </div>
</template>

<style lang="scss" scoped>
.label-text {
  color: #606266;
  width: 80px;
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