<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { User, Lock, UserFilled, Camera, Picture, Clock } from "@element-plus/icons-vue";
import type { FormInstance } from "element-plus";
import useUserStore from "@/stores/userStore";
import http from "@/utils/http";
import { roles } from "@/utils/menu.js";
import config from "@/config/index.js";

const userStore = useUserStore();
const router = useRouter();
const loginFormRef = ref<FormInstance>();
const loading = ref(false);

const loginForm = reactive({
  username: "caiya",
  password: "123"
});

const loginRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
};

const handleLogin = async () => {
  if (!loginFormRef.value) return;

  try {
    await loginFormRef.value.validate();
    loading.value = true;

    const res = await http.post("/login", loginForm);
      if (res.code == 200) {
        const role = res.data.role;
        userStore.setToken(res.data.token),
        userStore.setUserInfo(res.data),
        userStore.setRoleFlag(role),
        ElMessage.success("登录成功");
        if (role == "admin") {
          router.push("/");
        } else {
          router.push("/front/home");
        }
      } else if (res.code === 520) {
        ElMessage.error("无权限");
      }

    
  } catch (error) {
    console.error("登录失败:", error);
    ElMessage.error("登录失败，请检查用户名和密码");
  } finally {
    loading.value = false;
  }
};

// 跳转到注册页面
const goToRegister = () => {
  router.push("/register");
};
</script>

<template>
  <div class="login-container">
    <!-- 左侧品牌介绍区域 -->
    <div class="brand-section">
      <div class="brand-content">
        <div class="brand-header">
          <h1 class="brand-title">{{ config.frontName }}</h1>
        </div>
        
        <div class="features">
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><Camera /></el-icon>
            </div>
            <div class="feature-content">
              <h3>生态观察</h3>
              <p>记录珍稀动物，分享动物体验</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><Picture /></el-icon>
            </div>
            <div class="feature-content">
              <h3>影像记录</h3>
              <p>高清影像，定格美好瞬间</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="feature-content">
              <h3>生态保护</h3>
              <p>共同守护动物天堂</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 右侧登录表单区域 -->
    <div class="login-section">
      <div class="login-box">
        <div class="login-header">
          <h2>欢迎回来</h2>
          <p>{{ config.welcome }}</p>
        </div>
        
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              :prefix-icon="User"
              size="large"
              class="login-input"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              class="login-input"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              {{ loading ? "登录中..." : "登录账号" }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="login-footer">
          <span>还没有账号？</span>
          <el-button type="text" @click="goToRegister" class="register-link">
            立即注册
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  background: url("@/assets/bg.png") no-repeat;
  background-size: cover;
  position: relative;
  overflow: hidden;
  align-items: center;
  justify-content: center;
  padding: 40px;
  box-sizing: border-box;
}

/* 左侧品牌区域 */
.brand-section {
  width: 500px;
  height: 500px;
  display: flex;
  align-items: stretch;
  justify-content: flex-end;
  padding: 0;
  position: relative;
  z-index: 1;
}

.brand-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding: 40px;
  background: linear-gradient(135deg, rgba(42, 157, 143, 0.9), rgba(42, 157, 143, 0.7));
  border-radius: 20px 0 0 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-right: none;
  position: relative;
  z-index: 2;
}

.brand-header {
  margin-bottom: 60px;
}

.brand-title {
  font-size: 42px;
  font-weight: 700;
  color: #ffffff;
  margin: 0 0 10px 0;
  letter-spacing: 2px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.brand-main {
  margin-bottom: 80px;
}

.main-slogan {
  font-size: 36px;
  font-weight: 600;
  color: var(--primary-dark);
  line-height: 1.2;
  margin: 0 0 20px 0;
}

.brand-desc {
  font-size: 16px;
  color: var(--secondary-color);
  margin: 0;
  text-align: center;
  line-height: 1.5;
}

.features {
  space-y: 30px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 30px;
}

.feature-icon {
  width: 50px;
  height: 50px;
  background: var(--primary-color);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  flex-shrink: 0;
}

.feature-icon .el-icon {
  font-size: 24px;
  color: #ffffff;
}

.feature-content h3 {
  font-size: 18px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 8px 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.feature-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
  line-height: 1.4;
}

/* 右侧登录区域 */
.login-section {
  width: 500px;
  height: 500px;
  display: flex;
  align-items: stretch;
  justify-content: flex-start;
  padding: 0;
  position: relative;
  z-index: 1;
}

.login-box {
  width: 100%;
  height: 100%;
  max-width: none;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 0 20px 20px 0;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-left: none;
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-header {
  margin-bottom: 40px;
  text-align: center;
}

.login-header h2 {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.login-header p {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0;
}

.login-form {
  margin-bottom: 24px;
}

.login-input {
  margin-bottom: 20px;
}

.login-input :deep(.el-input__wrapper) {
  height: 48px;
  border-radius: 8px;
  border: 1px solid var(--border-light);
  font-size: 15px;
}

.login-input :deep(.el-input__wrapper:focus-within) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(42, 157, 143, 0.1);
}

.login-input :deep(.el-select) {
  width: 100%;
}

.login-input :deep(.el-select .el-input__wrapper) {
  height: 48px;
  border-radius: 8px;
  border: 1px solid var(--border-light);
  font-size: 15px;
}

.login-input :deep(.el-select .el-input__wrapper:focus-within) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(42, 157, 143, 0.1);
}

.login-btn {
  width: 100%;
  height: 48px;
  background: var(--primary-color);
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
}

.login-btn:hover {
  background: var(--primary-dark);
}

.login-footer {
  text-align: center;
  font-size: 14px;
  color: var(--text-secondary);
}

.register-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
  padding: 0;
}

.register-link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .brand-section {
    width: 100%;
    min-height: 40vh;
    justify-content: center;
    padding-right: 0;
  }
  
  .login-section {
    width: 100%;
    min-height: 60vh;
    justify-content: center;
    padding: 20px;
  }
  
  .brand-content {
    max-width: 90%;
  }
  
  .brand-title {
    font-size: 36px;
  }
  
  .main-slogan {
    font-size: 28px;
  }
}

@media (max-width: 1200px) {
  .brand-section {
    padding-right: 20px;
  }
  
  .login-section {
    padding-left: 20px;
  }
}
</style>
