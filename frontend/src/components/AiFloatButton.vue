<script setup lang="ts">
import { ref } from 'vue'
import { ChatDotRound } from '@element-plus/icons-vue'
import AiChatDialog from './AiChatDialog.vue'

// 弹窗显示状态
const dialogVisible = ref(false)

// 打开AI问答弹窗
const openChat = () => {
  dialogVisible.value = true
}
</script>

<template>
  <div class="ai-float-button">
    <!-- 悬浮按钮 -->
    <div class="float-btn" @click="openChat">
      <el-icon class="chat-icon">
        <ChatDotRound />
      </el-icon>
      <div class="btn-text">AI助手</div>
    </div>

    <!-- AI问答弹窗 -->
    <AiChatDialog v-model:visible="dialogVisible" />
  </div>
</template>

<style lang="scss" scoped>
.ai-float-button {
  .float-btn {
    position: fixed;
    bottom: 30px;
    right: 30px;
    width: 60px;
    height: 60px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 50%, var(--secondary-color) 100%);
    border-radius: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
    transition: all 0.3s ease;
    z-index: 1000;
    color: white;

    &:hover {
      width: 80px;
      border-radius: 40px;

      .btn-text {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .chat-icon {
      font-size: 24px;
      transition: all 0.3s ease;
    }

    .btn-text {
      font-size: 10px;
      font-weight: 500;
      margin-top: 2px;
      opacity: 0;
      transform: translateY(5px);
      transition: all 0.3s ease;
      white-space: nowrap;
    }

    // 呼吸动画效果
    &::before {
      content: '';
      position: absolute;
      top: -2px;
      left: -2px;
      right: -2px;
      bottom: -2px;
        background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 50%, var(--secondary-color) 100%);
      border-radius: 50%;
      z-index: -1;
      animation: pulse 2s infinite;
      opacity: 0.7;
    }
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.7;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.3;
  }
  100% {
    transform: scale(1);
    opacity: 0.7;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .ai-float-button {
    .float-btn {
      bottom: 20px;
      left: 20px;
      width: 50px;
      height: 50px;

      .chat-icon {
        font-size: 20px;
      }

      &:hover {
        width: 70px;
        border-radius: 35px;
      }
    }
  }
}
</style>