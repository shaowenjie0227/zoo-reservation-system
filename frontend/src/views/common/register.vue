<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { User, Lock, Camera, Picture, Clock } from "@element-plus/icons-vue";
import type { FormInstance } from "element-plus";
import http from "@/utils/http";
import config from "@/config/index.js";

const router = useRouter();
const registerFormRef = ref<FormInstance>();
const loading = ref(false);

const registerForm = reactive({
  username: "",
  password: "",
  confirmPassword: ""
});

// 确认密码验证
const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'));
  } else {
    callback();
  }
};

const registerRules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: "blur" }
  ]
};

const handleRegister = async () => {
  if (!registerFormRef.value) return;

  try {
    await registerFormRef.value.validate();
    loading.value = true;

    const res = await http.post("/register", {
      username: registerForm.username,
      password: registerForm.password
    });
    
    if (res.code === 200) {
      ElMessage.success("注册成功，请登录");
      await router.push("/login");
    } else {
      ElMessage.error(res.message || "注册失败");
    }
  } catch (error) {
    console.error("注册失败:", error);
    ElMessage.error("注册失败，请稍后重试");
  } finally {
    loading.value = false;
  }
};

// 跳转到登录页面
const goToLogin = () => {
  router.push("/login");
};
</script>

<template>
  <div class="register-container">
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
              <h3>生态记录</h3>
              <p>记录奇幻动物园珍稀动物生态</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><Picture /></el-icon>
            </div>
            <div class="feature-content">
              <h3>影像分享</h3>
              <p>分享动物摄影作品</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="feature-content">
              <h3>生态保护</h3>
              <p>参与候鸟保护行动</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 右侧注册表单区域 -->
    <div class="register-section">
      <div class="register-box">
        <div class="register-header">
          <h2>创建账号</h2>
          <p>{{ config.welcome }}</p>
        </div>
        
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="register-form"
          @keyup.enter="handleRegister"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              :prefix-icon="User"
              size="large"
              class="register-input"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              class="register-input"
            />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              class="register-input"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="register-btn"
              :loading="loading"
              @click="handleRegister"
            >
              {{ loading ? "注册中..." : "创建账号" }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="register-footer">
          <span>已有账号？</span>
          <el-button type="text" @click="goToLogin" class="login-link">
            立即登录
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  min-height: 100vh;
  background: url("@/assets/bg.png") no-repeat;
  background-size: cover;
  position: relative;
  overflow: hidden;
  align-items: center;
  justify-content: center;
  padding: 40px;
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
  color: #ffffff;
  line-height: 1.2;
  margin: 0 0 20px 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
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

/* 右侧注册区域 */
.register-section {
  width: 500px;
  height: 500px;
  display: flex;
  align-items: stretch;
  justify-content: flex-start;
  padding: 0;
  position: relative;
  z-index: 1;
}

.register-box {
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

.register-header {
  margin-bottom: 40px;
  text-align: center;
}

.register-header h2 {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.register-header p {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0;
}

.register-form {
  margin-bottom: 24px;
}

.register-input {
  margin-bottom: 20px;
}

.register-input :deep(.el-input__wrapper) {
  height: 48px;
  border-radius: 8px;
  border: 1px solid var(--border-light);
  font-size: 15px;
}

.register-input :deep(.el-input__wrapper:focus-within) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(42, 157, 143, 0.1);
}

.register-btn {
  width: 100%;
  height: 48px;
  background: var(--primary-color);
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
}

.register-btn:hover {
  background: var(--primary-dark);
}

.register-footer {
  text-align: center;
  font-size: 14px;
  color: var(--text-secondary);
}

.login-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
  padding: 0;
}

.login-link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-container {
    flex-direction: column;
  }
  
  .brand-section {
    width: 100%;
    min-height: 40vh;
    justify-content: center;
    padding-right: 0;
  }
  
  .register-section {
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
  
  .register-section {
    padding-left: 20px;
  }
}
</style>