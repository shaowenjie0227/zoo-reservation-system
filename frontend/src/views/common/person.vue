<script setup>
import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { Picture as IconPicture } from "@element-plus/icons-vue";
import useUserStore from "@/stores/userStore";
import { getImageUrl } from "@/utils/system";
import Config from "@/config";
import { genderDict } from "@/config/dict";
import http from "@/utils/http";

const userStore = useUserStore();
const form = ref({
  username: "",
  nickName: "",
  age: 18,
  gender: 0,
  avatar: "",
  profile: "",
  roleFlag: "",
});

// 图片上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");

// 获取用户信息
const getUserInfo = async () => {
  try {
    const res = await http.get("/user/current");
    if (res.code === 200) {
      form.value = JSON.parse(JSON.stringify(res.data));
      imageUrl.value = form.value.avatar ? getImageUrl(form.value.avatar) : "";
      userStore.userInfo = res.data;
    }
  } catch (error) {
    console.error("获取用户信息失败:", error);
  }
};

// 保存用户信息
const saveUserInfo = async () => {
  try {
    const res = await http.post("/user/edit", form.value);
    if (res.code === 200) {
      ElMessage.success("保存成功");
      await getUserInfo();
    } else {
      ElMessage.error("保存失败");
    }
  } catch (error) {
    console.error("保存用户信息失败:", error);
    ElMessage.error("保存失败");
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
  if (response?.code === 200) {
    form.value.avatar = response.data;
    imageUrl.value = getImageUrl(response.data);
  }
};

onMounted(() => {
  getUserInfo();
});
</script>

<template>
  <div class="person-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">个人信息</h2>
      <p class="page-subtitle">管理您的个人资料和偏好设置</p>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-wrapper">
      <!-- 头像上传卡片 -->
      <div class="avatar-card">
        <div class="avatar-section">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadData"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <el-image v-if="imageUrl" :src="imageUrl" fit="cover" class="avatar">
              <template #error>
                <div class="image-slot">
                  <el-icon><icon-picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div v-else class="avatar-placeholder">
              <el-icon class="avatar-uploader-icon">
                <icon-picture />
              </el-icon>
            </div>
            <div class="upload-overlay">
              <el-icon class="upload-icon">
                <icon-picture />
              </el-icon>
              <span class="upload-text">更换头像</span>
            </div>
          </el-upload>
          <div class="upload-tip">
            支持 JPG、PNG 格式，文件大小不超过 2MB
          </div>
        </div>
      </div>

      <!-- 表单卡片 -->
      <div class="form-card">
        <div class="card-header">
          <h3 class="card-title">基本信息</h3>
        </div>
        <el-form :model="form" label-width="100px" class="form-section">
          <el-form-item label="账号" class="form-item">
            <el-input v-model="form.username" disabled class="disabled-input" />
          </el-form-item>
          <el-form-item label="昵称" class="form-item">
            <el-input v-model="form.nickName" placeholder="请输入昵称" />
          </el-form-item>
          <el-form-item label="年龄" class="form-item">
            <el-input-number v-model="form.age" :min="1" :max="120" />
          </el-form-item>
          <el-form-item label="性别" class="form-item">
            <el-radio-group v-model="form.gender" class="gender-group">
              <el-radio
                v-for="(label, value) in genderDict"
                :key="value"
                :label="Number(value)"
                class="gender-radio"
                >{{ label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
          <el-form-item label="个人介绍" class="form-item">
            <el-input
              type="textarea"
              v-model="form.profile"
              :autosize="{ minRows: 4, maxRows: 8 }"
              placeholder="请输入个人介绍..."
              class="profile-textarea"
            />
          </el-form-item>
        </el-form>

        <!-- 保存按钮 -->
        <div class="button-section">
          <el-button type="primary" @click="saveUserInfo" class="save-button">
            保存修改
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.person-container {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-tertiary) 0%, var(--bg-secondary) 100%);
  padding: 24px;

  .page-header {
    text-align: center;
    margin-bottom: 32px;
    
    .page-title {
      font-size: 28px;
      font-weight: 600;
      color: var(--primary-color);
      margin: 0 0 8px 0;
      text-shadow: 0 2px 4px rgba(42, 157, 143, 0.1);
    }
    
    .page-subtitle {
      font-size: 14px;
      color: var(--text-secondary);
      margin: 0;
      opacity: 0.8;
    }
  }

  .content-wrapper {
    max-width: 800px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 300px 1fr;
    gap: 24px;
    
    @media (max-width: 768px) {
      grid-template-columns: 1fr;
      max-width: 500px;
    }
  }

  .avatar-card {
    background: var(--bg-primary);
    border-radius: 16px;
    padding: 32px 24px;
    box-shadow: 0 4px 20px rgba(42, 157, 143, 0.08);
    border: 1px solid rgba(42, 157, 143, 0.1);
    height: fit-content;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 30px rgba(42, 157, 143, 0.12);
    }

    .avatar-section {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .avatar-uploader {
      position: relative;
      width: 160px;
      height: 160px;
      border: 3px solid var(--primary-color);
      border-radius: 50%;
      cursor: pointer;
      overflow: hidden;
      transition: all 0.3s ease;
      background: linear-gradient(135deg, var(--primary-light), var(--primary-color));

      &:hover {
        border-color: var(--primary-dark);
        transform: scale(1.05);
        
        .upload-overlay {
          opacity: 1;
        }
      }

      .avatar {
        width: 100%;
        height: 100%;
        display: block;
        border-radius: 50%;
      }

      .avatar-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, var(--primary-lighter), var(--primary-light));
      }

      .avatar-uploader-icon {
        font-size: 48px;
        color: white;
      }

      .upload-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(42, 157, 143, 0.8);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: all 0.3s ease;
        border-radius: 50%;

        .upload-icon {
          font-size: 24px;
          color: white;
          margin-bottom: 4px;
        }

        .upload-text {
          font-size: 12px;
          color: white;
          font-weight: 500;
        }
      }
    }

    .upload-tip {
      font-size: 12px;
      color: var(--text-tertiary);
      margin-top: 16px;
      line-height: 1.5;
      text-align: center;
      padding: 8px 16px;
      background: var(--bg-accent);
      border-radius: 8px;
      border: 1px solid rgba(42, 157, 143, 0.1);
    }

    .image-slot {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      background: var(--bg-accent);
      color: var(--primary-color);
      border-radius: 50%;
    }
  }

  .form-card {
    background: var(--bg-primary);
    border-radius: 16px;
    padding: 32px;
    box-shadow: 0 4px 20px rgba(42, 157, 143, 0.08);
    border: 1px solid rgba(42, 157, 143, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 30px rgba(42, 157, 143, 0.12);
    }

    .card-header {
      margin-bottom: 24px;
      padding-bottom: 16px;
      border-bottom: 2px solid var(--bg-accent);

      .card-title {
        font-size: 20px;
        font-weight: 600;
        color: var(--primary-color);
        margin: 0;
        display: flex;
        align-items: center;
        
        &::before {
          content: '';
          width: 4px;
          height: 20px;
          background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
          border-radius: 2px;
          margin-right: 12px;
        }
      }
    }

    .form-section {
      .form-item {
        margin-bottom: 24px;

        :deep(.el-form-item__label) {
          color: var(--text-primary);
          font-weight: 500;
          font-size: 14px;
        }

        :deep(.el-form-item__content) {
          width: 100%;
        }

        :deep(.el-input__wrapper) {
          border-radius: 8px;
          border: 1px solid var(--border-light);
          transition: all 0.3s ease;
          background: var(--bg-secondary);

          &:hover {
            border-color: var(--primary-light);
          }

          &.is-focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 2px rgba(42, 157, 143, 0.1);
          }
        }

        :deep(.el-input__inner) {
          color: var(--text-primary);
          
          &::placeholder {
            color: var(--text-tertiary);
          }
        }

        :deep(.el-textarea__inner) {
          border-radius: 8px;
          border: 1px solid var(--border-light);
          background: var(--bg-secondary);
          color: var(--text-primary);
          transition: all 0.3s ease;
          
          &:hover {
            border-color: var(--primary-light);
          }

          &:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 2px rgba(42, 157, 143, 0.1);
          }

          &::placeholder {
            color: var(--text-tertiary);
          }
        }

        :deep(.el-input-number) {
          width: 100%;
          
          .el-input__wrapper {
            width: 100%;
          }
        }

        &.disabled-input {
          :deep(.el-input__wrapper) {
            background: var(--gray-100);
            border-color: var(--border-light);
          }
          
          :deep(.el-input__inner) {
            color: var(--text-tertiary);
          }
        }
      }

      .gender-group {
        :deep(.el-radio) {
          margin-right: 24px;
          
          .el-radio__input.is-checked .el-radio__inner {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
          }
          
          .el-radio__input.is-checked + .el-radio__label {
            color: var(--primary-color);
          }
          
          .el-radio__inner:hover {
            border-color: var(--primary-light);
          }
          
          .el-radio__label {
            color: var(--text-primary);
            font-weight: 500;
          }
        }
      }
    }

    .button-section {
      text-align: center;
      margin-top: 32px;
      padding-top: 24px;
      border-top: 1px solid var(--border-light);

      .save-button {
        padding: 12px 32px;
        font-size: 16px;
        font-weight: 500;
        border-radius: 8px;
        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
        border: none;
        transition: all 0.3s ease;
        box-shadow: 0 4px 12px rgba(42, 157, 143, 0.3);

        &:hover {
          background: linear-gradient(135deg, var(--primary-dark), var(--primary-color));
          transform: translateY(-2px);
          box-shadow: 0 6px 20px rgba(42, 157, 143, 0.4);
        }

        &:active {
          transform: translateY(0);
        }
      }
    }
  }
}
</style>
