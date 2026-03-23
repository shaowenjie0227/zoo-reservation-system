<script setup lang="ts">
import http from "@/utils/http";
import { ElMessage } from "element-plus";
import { onMounted, ref, onBeforeUnmount, shallowRef } from "vue";
import { getImageUrl } from "@/utils/system";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig } from "@wangeditor/editor";
import { IToolbarConfig } from '@wangeditor/editor'
import "@wangeditor/editor/dist/css/style.css";
import Config from "@/config/index";
import { useRouter } from "vue-router";
import { Plus } from "@element-plus/icons-vue";

const router = useRouter();

// 初始表单数据
const initForm = {
  title: "",
  content: "",
  imgUrl: "",
  userId: null,
  travelId: null,
  viewCount: 0
};

// 表单数据
const form = ref({ ...initForm });

// 景点列表
const travelList = ref([]);

// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");
// 攻略分类列表
const categoryList = ref([]);

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入攻略标题', trigger: 'blur' }],
  content: [{ required: false, message: '请输入攻略内容', trigger: 'blur' }],
  travelId: [{ required: true, message: '请选择景点', trigger: 'change' }],
};

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

// 确认按钮
const submit = async () => {
  const res = await http.post(`/travelGuide/add`, form.value);
  if (res.code === 200) {
    ElMessage.success("发布成功");
    router.push('/front/my-travel-guide');
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
  await getTravelList();
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
      <h2>新增动物攻略</h2>
      <el-button @click="cancel">返回列表</el-button>
    </div>
    
    <el-form :model="form" :rules="rules" label-width="120" ref="formRef">
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

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>