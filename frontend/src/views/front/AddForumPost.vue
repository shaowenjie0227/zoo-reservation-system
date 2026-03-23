<script setup lang="ts">
import http from "@/utils/http";
import { ElMessage } from "element-plus";
import { onMounted, ref, nextTick, onBeforeUnmount, shallowRef } from "vue";
import { getImageUrl } from "@/utils/system";
import Config from "@/config/index";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig } from "@wangeditor/editor";
import { IToolbarConfig } from '@wangeditor/editor'
import "@wangeditor/editor/dist/css/style.css";
import { useRouter } from "vue-router";
import { Plus } from "@element-plus/icons-vue";

const router = useRouter();

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
  categoryId: [{ required: false, message: '请选择分类', trigger: 'change' }],
};

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

// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");

// 提交表单
const submit = async () => {
  const res = await http.post(`/forumPost/add`, form.value);
  if (res.code === 200) {
    ElMessage.success("发布成功");
    router.push('/front/my-posts');
  } else {
    ElMessage.error("发布失败");
  }
};

// 取消操作
const cancel = () => {
  router.back();
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

onMounted(async () => {
  await getAnimalList();
  await getLocationList();
  await getCategoryList();
});

onBeforeUnmount(() => {
  editorRef.value?.destroy()
  editorRef.value = null
})
</script>

<template>
  <div style="width: 800px; margin: 0 auto; padding: 20px;">
    <div class="page-header">
      <h2>发布论坛帖子</h2>
      <el-button @click="cancel">返回列表</el-button>
    </div>
    
    <el-form :model="form" :rules="rules" label-width="120" ref="formRef">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入帖子标题" />
      </el-form-item>
      
      <el-form-item label="简介" prop="intro">
        <el-input v-model="form.intro" placeholder="请输入帖子简介" />
      </el-form-item>

      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="form.categoryId" placeholder="请选择分类" clearable style="width: 100%">
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="关联动物" prop="animalId">
        <el-select v-model="form.animalId" placeholder="请选择关联动物" clearable style="width: 100%">
          <el-option
            v-for="animal in animalList"
            :key="animal.id"
            :label="animal.name"
            :value="animal.id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="动物地点" prop="locationId">
        <el-select v-model="form.locationId" placeholder="请选择动物地点" clearable style="width: 100%">
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
          style="width: 100%"
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
      
      <el-form-item>
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
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

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

:deep(.w-e-text-container) {
  height: 300px !important;
}
</style>