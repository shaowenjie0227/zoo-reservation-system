<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { 
  User, 
  ArrowDown, 
  House,
  Grid,
  Location,
  Camera,
  Document,
  ChatDotRound,
  Notification,
  Warning
} from "@element-plus/icons-vue";
import { getImageUrl } from "@/utils/system";
import config from "@/config";
import useUserStore from "@/stores/userStore";

const router = useRouter();
const route = useRoute();

// 用户信息
const userInfo = ref(null);
const isLoggedIn = computed(() => !!userInfo.value);
const userStore = useUserStore();

// 菜单数据
const menuList = ref([
  { name: "首页", path: "/front/home", icon: House },
  { name: "动物大全", path: "/front/animal-list", icon: Grid },
  { name: "动物地点", path: "/front/location", icon: Location },
  { name: "动物馆", path: "/front/travel-list", icon: Camera },
  { name: "动物攻略", path: "/front/travel-guide-list", icon: Document },
  { name: "社区交流", path: "/front/community", icon: ChatDotRound },
  { name: "动物园资讯", path: "/front/conservation-news", icon: Notification },
  { name: "违规举报", path: "/front/user-report", icon: Warning }
]);
// 当前激活的菜单
const activeMenu = ref("/front/home");
// 下拉菜单选项
const dropdownMenuOptions = ref([
  { title: '个人中心', command: 'profile', icon: User, onClick: () => toPath('/front/person') },
  { title: '我的帖子', command: 'posts', icon: Document, onClick: () => toPath('/front/my-posts') },
  { title: '我的订单', command: 'orders', icon: Grid, onClick: () => toPath('/front/my-ticket-order') },
  { title: '我的攻略', command: 'guides', icon: Camera, onClick: () => toPath('/front/my-travel-guide') },
  { title: '我的评论', command: 'comments', icon: ChatDotRound, onClick: () => toPath('/front/my-comments') },
  { title: '我的点赞', command: 'likes', icon: House, onClick: () => toPath('/front/my-likes') },
  { title: '我的收藏', command: 'collections', icon: Location, onClick: () => toPath('/front/my-collections') },
  { title: '退出登录', command: 'logout', icon: Warning, onClick: () => handleLogout() },
]);

// 获取用户信息
const getUserInfo = () => {
  const token = userStore.token;
  const user = userStore.userInfo;
  console.log("user", user);
  console.log("token", token);
  if (token && user) {
    try {
      userInfo.value = {...user};
    } catch (error) {
      console.error("解析用户信息失败:", error);
    }
  }
};

// 菜单点击
const handleMenuClick = (path: string) => {
  activeMenu.value = path;
  router.push(path);
};

// 登录
const handleLogin = () => {
  router.push("/login");
};

// 注册
const handleRegister = () => {
  router.push("/register");
};

// 退出登录
const handleLogout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("userInfo");
  userInfo.value = null;
  ElMessage.success("退出登录成功");
  router.push("/login");
};

// 跳转路由
const toPath = (path) => {
  router.push(path);
};

onMounted(() => {
  getUserInfo();
});

watch(() => route.path, newPath => {
  activeMenu.value = newPath;
}, {immediate: true})
</script>

<template>
  <header class="header">
    <div class="header-container">
      <!-- 左侧：Logo + 系统名称 -->
      <div class="header-left">
        <div class="logo-section" @click="handleMenuClick('/front/home')">
          <div class="logo-container">
            <img src="../assets/logo.png" alt="Logo" class="logo" />
            <div class="logo-glow"></div>
          </div>
          <h1 class="system-name">
            <span class="name-text">{{ config.frontName }}</span>
            <div class="name-underline"></div>
          </h1>
        </div>
      </div>

      <!-- 中间：菜单栏 -->
      <nav class="header-center">
        <ul class="nav-menu">
          <li
            v-for="menu in menuList"
            :key="menu.path"
            class="nav-item"
            :class="{ active: activeMenu === menu.path }"
            @click="handleMenuClick(menu.path)"
          >
            <el-icon class="menu-icon" :size="18">
              <component :is="menu.icon" />
            </el-icon>
            <span class="menu-text">{{ menu.name }}</span>
          </li>
        </ul>
      </nav>

      <!-- 右侧：登录操作 -->
      <div class="header-right">
        <!-- 未登录状态 -->
        <div v-if="!isLoggedIn" class="auth-buttons">
          <el-button type="primary" @click="handleLogin">
            <el-icon class="mr-1">
              <User />
            </el-icon>
            登录
          </el-button>
          <el-button @click="handleRegister">注册</el-button>
        </div>

        <!-- 已登录状态 -->
        <div v-else class="user-info">
          <el-dropdown @command="handleCommand">
            <div class="user-profile">
              <el-avatar
                :src="userInfo.avatar ? getImageUrl(userInfo.avatar) : ''"
                :icon="User"
                :size="32"
                class="user-avatar"
              />
              <span class="username">{{ userInfo.nickName || userInfo.username }}</span>
              <el-icon class="dropdown-icon">
                <ArrowDown />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item 
                  v-for="item in dropdownMenuOptions" 
                  :key="item.command" 
                  :command="item.command" 
                  @click="item.onClick"
                  class="dropdown-item-with-icon"
                >
                  <el-icon class="dropdown-item-icon">
                    <component :is="item.icon" />
                  </el-icon>
                  {{ item.title }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 50%, var(--secondary-color) 100%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 20px;
  height: 64px;
}

/* Logo区域样式 */
.header-left {
  flex: 0 0 auto;
}

.logo-section {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 12px;
  border-radius: 12px;
}

.logo-section:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.logo-container {
  position: relative;
  margin-right: 12px;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.logo-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(45deg, var(--primary-light), var(--secondary-color), var(--primary-color), var(--primary-lighter));
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
  animation: rotate 3s linear infinite;
}

.logo-section:hover .logo-glow {
  opacity: 0.7;
}

@keyframes rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.system-name {
  position: relative;
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.name-text {
  color: white;
}

.name-underline {
  position: absolute;
  bottom: -4px;
  left: 0;
  height: 2px;
  width: 0;
  background: linear-gradient(90deg, var(--primary-light), var(--secondary-color));
  transition: width 0.3s ease;
}

.logo-section:hover .name-underline {
  width: 100%;
}

/* 导航菜单样式 */
.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: white; /* 改为纯白色，提高可见性 */
  font-weight: 500;
  position: relative;
  overflow: hidden;
}

.nav-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.nav-item:hover::before {
  left: 100%;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.25); /* 增加活跃状态的背景透明度 */
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.menu-icon {
  transition: all 0.3s ease;
}

.nav-item:hover .menu-icon {
  transform: scale(1.1);
  color: var(--primary-light);
}

.menu-text {
  font-size: 14px;
  white-space: nowrap;
}

/* 右侧用户区域样式 */
.header-right {
  flex: 0 0 auto;
}

.auth-buttons {
  display: flex;
  gap: 12px;
}

.auth-buttons .el-button {
  border-radius: 20px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.auth-buttons .el-button--primary {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(42, 157, 143, 0.3);
}

.auth-buttons .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(42, 157, 143, 0.4);
}

.user-info {
  position: relative;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
}

.user-profile:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.user-avatar {
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.user-profile:hover .user-avatar {
  border-color: var(--primary-light);
  box-shadow: 0 0 12px rgba(42, 157, 143, 0.4);
}

.username {
  color: white;
  font-weight: 500;
  font-size: 14px;
}

.dropdown-icon {
  color: rgba(255, 255, 255, 0.7);
  transition: all 0.3s ease;
}

.user-profile:hover .dropdown-icon {
  color: var(--primary-light);
  transform: rotate(180deg);
}

/* 下拉菜单样式 */
:deep(.el-dropdown-menu) {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 8px;
}

:deep(.dropdown-item-with-icon) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.dropdown-item-with-icon:hover) {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  color: white;
  transform: translateX(4px);
}

:deep(.dropdown-item-icon) {
  font-size: 16px;
  transition: all 0.3s ease;
}

:deep(.dropdown-item-with-icon:hover .dropdown-item-icon) {
  transform: scale(1.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-container {
    padding: 0 16px;
    height: 56px;
  }
  
  .logo {
    width: 32px;
    height: 32px;
  }
  
  .system-name {
    font-size: 16px;
  }
  
  .nav-menu {
    gap: 4px;
  }
  
  .nav-item {
    padding: 8px 12px;
  }
  
  .menu-text {
    font-size: 13px;
  }
  
  .auth-buttons .el-button {
    padding: 6px 16px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .nav-menu {
    display: none;
  }
  
  .header-container {
    justify-content: space-between;
  }
}

/* 工具类 */
.mr-1 {
  margin-right: 4px;
}
</style>