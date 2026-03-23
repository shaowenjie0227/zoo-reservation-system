<template>
  <div class="home">
    <!-- 用户信息卡片 -->
    <div class="user-card">
      <div class="user-info">
        <el-avatar :size="60" :src="getImageUrl(userStore.userInfo.avatar)" />
        <div class="user-details">
          <h2>{{ getGreeting() }}，{{ userStore.userInfo.nickName }}！</h2>
          <p class="user-role">{{ userStore.userInfo.role }}</p>
          <p class="current-time">{{ currentTime }}</p>
        </div>
      </div>
      <div class="welcome-message">
        <h3>欢迎来到 {{ Config.systemName }}</h3>
        <p>今天是美好的一天，让我们一起探索奇幻动物园的动物世界吧！</p>
      </div>
    </div>

    <!-- 公告展示区域 -->
    <div class="notice-container">
      <div class="notice-header">
        <h2>系统公告</h2>
        <div class="notice-controls">
          <el-button 
            :icon="ArrowLeft" 
            circle 
            size="small" 
            @click="prevNotice"
            :disabled="noticeList.length <= 1"
          />
          <span class="notice-indicator">{{ currentNoticeIndex + 1 }} / {{ noticeList.length }}</span>
          <el-button 
            :icon="ArrowRight" 
            circle 
            size="small" 
            @click="nextNotice"
            :disabled="noticeList.length <= 1"
          />
        </div>
      </div>
      
      <div class="notice-content" v-if="noticeList.length > 0">
        <transition name="slide" mode="out-in">
          <div :key="currentNoticeIndex" class="current-notice">
            <div class="notice-date">
              {{ noticeList[currentNoticeIndex]?.createTime?.split(" ")[0] }}
            </div>
            <div class="notice-body">
              <h3>{{ noticeList[currentNoticeIndex]?.title }}</h3>
              <div class="commit-info">
                <span>
                  {{ noticeList[currentNoticeIndex]?.createBy }} 发布于
                  {{ noticeList[currentNoticeIndex]?.createTime?.split(" ")[1] }}
                </span>
              </div>
            </div>
          </div>
        </transition>
      </div>
      
      <div v-else class="no-notice">
        <el-empty description="暂无公告信息" />
      </div>
    </div>
  </div>
</template>

<script setup>
import Config from "@/config";
import http from "@/utils/http";
import { onMounted, ref, onUnmounted } from "vue";
import { ArrowLeft, ArrowRight } from "@element-plus/icons-vue";
import useUserStore from "@/stores/userStore";
import { getImageUrl } from "@/utils/system";

// 用户信息
const userStore = useUserStore();

// 当前时间
const currentTime = ref("");
let timeInterval = null;

// 公告列表数据
const noticeList = ref([]);
const currentNoticeIndex = ref(0);
let noticeInterval = null;

// 更新当前时间
const updateTime = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  
  currentTime.value = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 获取问候语
const getGreeting = () => {
  const hour = new Date().getHours();
  if (hour < 6) {
    return "凌晨好";
  } else if (hour < 9) {
    return "早上好";
  } else if (hour < 12) {
    return "上午好";
  } else if (hour < 14) {
    return "中午好";
  } else if (hour < 17) {
    return "下午好";
  } else if (hour < 19) {
    return "傍晚好";
  } else {
    return "晚上好";
  }
};

// 获取公告数据
const getNoticeData = async () => {
  const res = await http.get("/notice/list");
  if (res.code === 200) {
    noticeList.value = res.data;
    if (noticeList.value.length > 0) {
      startAutoSwitch();
    }
  }
};

// 上一条公告
const prevNotice = () => {
  if (noticeList.value.length <= 1) return;
  currentNoticeIndex.value = 
    currentNoticeIndex.value === 0 
      ? noticeList.value.length - 1 
      : currentNoticeIndex.value - 1;
  resetAutoSwitch();
};

// 下一条公告
const nextNotice = () => {
  if (noticeList.value.length <= 1) return;
  currentNoticeIndex.value = 
    currentNoticeIndex.value === noticeList.value.length - 1 
      ? 0 
      : currentNoticeIndex.value + 1;
  resetAutoSwitch();
};

// 开始自动切换
const startAutoSwitch = () => {
  if (noticeList.value.length <= 1) return;
  noticeInterval = setInterval(() => {
    nextNotice();
  }, 5000); // 每5秒切换一次
};

// 重置自动切换
const resetAutoSwitch = () => {
  if (noticeInterval) {
    clearInterval(noticeInterval);
    startAutoSwitch();
  }
};

// 页面加载时获取数据
onMounted(() => {
  getNoticeData();
  updateTime();
  timeInterval = setInterval(updateTime, 1000);
});

// 页面卸载时清理定时器
onUnmounted(() => {
  if (timeInterval) {
    clearInterval(timeInterval);
  }
  if (noticeInterval) {
    clearInterval(noticeInterval);
  }
});
</script>

<style scoped>
.home {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 用户信息卡片 */
.user-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  color: white;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-details {
  margin-left: 20px;
  flex: 1;
}

.user-details h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.user-role {
  margin: 0 0 4px 0;
  font-size: 14px;
  opacity: 0.9;
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 12px;
  border-radius: 12px;
  display: inline-block;
}

.current-time {
  margin: 0;
  font-size: 14px;
  opacity: 0.8;
  font-family: 'Courier New', monospace;
}

.welcome-message {
  text-align: center;
}

.welcome-message h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 500;
}

.welcome-message p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

/* 公告展示区域 */
.notice-container {
  background-color: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.notice-header h2 {
  margin: 0;
  color: var(--el-color-primary);
  font-size: 20px;
  font-weight: 600;
}

.notice-controls {
  display: flex;
  align-items: center;
  gap: 12px;
}

.notice-indicator {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  min-width: 60px;
  text-align: center;
}

.notice-content {
  min-height: 120px;
}

.current-notice {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.notice-date {
  color: var(--el-text-color-secondary);
  font-size: 14px;
  font-weight: 500;
  background-color: #f5f7fa;
  padding: 8px 12px;
  border-radius: 8px;
  white-space: nowrap;
}

.notice-body {
  flex: 1;
}

.notice-body h3 {
  margin: 0 0 8px 0;
  color: var(--el-color-primary);
  font-size: 18px;
  font-weight: 600;
}

.commit-info {
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.no-notice {
  text-align: center;
  padding: 40px 0;
}

/* 切换动画 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home {
    padding: 16px;
  }
  
  .user-card {
    padding: 20px;
  }
  
  .user-info {
    flex-direction: column;
    text-align: center;
  }
  
  .user-details {
    margin-left: 0;
    margin-top: 16px;
  }
  
  .notice-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }
  
  .current-notice {
    flex-direction: column;
    gap: 12px;
  }
  
  .notice-date {
    align-self: flex-start;
  }
}
</style>
