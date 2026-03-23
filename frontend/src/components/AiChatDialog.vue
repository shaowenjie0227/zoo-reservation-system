<script setup lang="ts">
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Promotion, Close, User } from '@element-plus/icons-vue'
import http from '@/utils/http'

// 定义props
const props = defineProps<{
  visible: boolean
}>()

// 定义emits
const emit = defineEmits<{
  'update:visible': [value: boolean]
}>()

// 响应式数据
const loading = ref(false)
const inputMessage = ref('')
const messages = ref<Array<{
  id: string
  type: 'user' | 'ai'
  content: string
  timestamp: Date
}>>([])

// 聊天容器引用
const chatContainer = ref<HTMLElement>()

// 关闭弹窗
const handleClose = () => {
  emit('update:visible', false)
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入问题')
    return
  }

  const userMessage = {
    id: Date.now().toString(),
    type: 'user' as const,
    content: inputMessage.value.trim(),
    timestamp: new Date()
  }

  // 添加用户消息
  messages.value.push(userMessage)
  const query = inputMessage.value.trim()
  inputMessage.value = ''
  
  scrollToBottom()

  try {
    loading.value = true
    
    // 调用AI接口
    const res = await http.post('/ai/send', null, {
      params: { query }
    })

    if (res.code === 200) {
      // 添加AI回复
      const aiMessage = {
        id: (Date.now() + 1).toString(),
        type: 'ai' as const,
        content: res.data || '抱歉，我暂时无法回答这个问题。',
        timestamp: new Date()
      }
      messages.value.push(aiMessage)
    } else {
      ElMessage.error('AI服务暂时不可用，请稍后重试')
    }
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送失败，请检查网络连接')
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

// 处理回车发送
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

// 清空聊天记录
const clearMessages = () => {
  messages.value = []
}

// 初始化欢迎消息
onMounted(() => {
  messages.value = [
    {
      id: 'welcome',
      type: 'ai',
      content: '您好！我是奇幻动物园预约管理系统的AI助手，很高兴为您服务！您可以向我询问关于动物知识、动物技巧、生态保护等相关问题。',
      timestamp: new Date()
    }
  ]
})
</script>

<template>
  <el-dialog
    :model-value="visible"
    @update:model-value="handleClose"
    title="AI智能问答"
    width="600px"
    :before-close="handleClose"
    class="ai-chat-dialog"
  >
    <template #header>
      <div class="dialog-header">
        <div class="header-left">
          <el-icon class="chat-icon"><ChatDotRound /></el-icon>
          <span class="dialog-title">AI智能问答</span>
        </div>
        <div class="header-right">
          <el-button 
            text 
            size="small" 
            @click="clearMessages"
            :disabled="messages.length <= 1"
          >
            清空记录
          </el-button>
        </div>
      </div>
    </template>

    <div class="chat-container">
      <!-- 消息列表 -->
      <div ref="chatContainer" class="messages-container">
        <div
          v-for="message in messages"
          :key="message.id"
          class="message-item"
          :class="message.type"
        >
          <div class="message-avatar">
            <div v-if="message.type === 'user'" class="user-avatar">
              <el-icon><User /></el-icon>
            </div>
            <div v-else class="ai-avatar">
              <el-icon><ChatDotRound /></el-icon>
            </div>
          </div>
          <div class="message-content">
            <div class="message-bubble" v-html="message.content">
            </div>
            <div class="message-time">
              {{ message.timestamp.toLocaleTimeString() }}
            </div>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="message-item ai">
          <div class="message-avatar">
            <div class="ai-avatar">
              <el-icon><ChatDotRound /></el-icon>
            </div>
          </div>
          <div class="message-content">
            <div class="message-bubble loading">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-container">
        <div class="input-wrapper">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="2"
            placeholder="请输入您的问题..."
            :disabled="loading"
            @keydown="handleKeydown"
            class="message-input"
          />
          <el-button
            type="primary"
            :icon="Promotion"
            :loading="loading"
            :disabled="!inputMessage.trim()"
            @click="sendMessage"
            class="send-button"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
.ai-chat-dialog {
  :deep(.el-dialog) {
    border-radius: 12px;
    overflow: hidden;
  }

  :deep(.el-dialog__header) {
    padding: 0;
    margin: 0;
  }

  :deep(.el-dialog__body) {
    padding: 0;
  }

  .dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 50%, var(--secondary-color) 100%);
    color: white;

    .header-left {
      display: flex;
      align-items: center;
      gap: 8px;

      .chat-icon {
        font-size: 20px;
      }

      .dialog-title {
        font-size: 16px;
        font-weight: 600;
      }
    }

    .header-right {
      :deep(.el-button) {
        color: rgba(255, 255, 255, 0.8);
        
        &:hover {
          color: white;
          background: rgba(255, 255, 255, 0.1);
        }
      }
    }
  }

  .chat-container {
    height: 500px;
    display: flex;
    flex-direction: column;
    background: #f8f9fa;

    .messages-container {
      flex: 1;
      overflow-y: auto;
      padding: 16px;
      display: flex;
      flex-direction: column;
      gap: 16px;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 3px;
      }

      &::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 3px;

        &:hover {
          background: #a8a8a8;
        }
      }

      .message-item {
        display: flex;
        gap: 12px;

        &.user {
          flex-direction: row-reverse;

          .message-content {
            align-items: flex-end;

            .message-bubble {
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              color: white;
            }
          }
        }

        &.ai {
          .message-bubble {
            background: white;
            color: #333;
            border: 1px solid #e0e0e0;
          }
        }

        .message-avatar {
          flex-shrink: 0;

          .user-avatar,
          .ai-avatar {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
          }

          .user-avatar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
          }

          .ai-avatar {
            background: #f0f0f0;
            color: #666;
          }
        }

        .message-content {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 4px;
          max-width: 70%;

          .message-bubble {
            padding: 12px 16px;
            border-radius: 18px;
            line-height: 1.5;
            word-wrap: break-word;
            white-space: pre-wrap;

            &.loading {
              padding: 16px;
              
              .typing-indicator {
                display: flex;
                gap: 4px;
                
                span {
                  width: 8px;
                  height: 8px;
                  border-radius: 50%;
                  background: #999;
                  animation: typing 1.4s infinite ease-in-out;
                  
                  &:nth-child(1) { animation-delay: -0.32s; }
                  &:nth-child(2) { animation-delay: -0.16s; }
                }
              }
            }
          }

          .message-time {
            font-size: 12px;
            color: #999;
            padding: 0 4px;
          }
        }
      }
    }

    .input-container {
      border-top: 1px solid #e0e0e0;
      background: white;
      padding: 16px;

      .input-wrapper {
        display: flex;
        gap: 12px;
        align-items: flex-end;

        .message-input {
          flex: 1;

          :deep(.el-textarea__inner) {
            border-radius: 20px;
            border: 1px solid #e0e0e0;
            padding: 12px 16px;
            resize: none;
            font-size: 14px;

            &:focus {
              border-color: #667eea;
              box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
            }
          }
        }

        .send-button {
          border-radius: 20px;
          padding: 12px 20px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border: none;

          &:hover {
            opacity: 0.9;
          }

          &:disabled {
            opacity: 0.5;
          }
        }
      }
    }
  }
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .ai-chat-dialog {
    :deep(.el-dialog) {
      width: 95% !important;
      margin: 5vh auto;
    }

    .chat-container {
      height: 400px;

      .messages-container {
        .message-item {
          .message-content {
            max-width: 85%;
          }
        }
      }

      .input-container {
        .input-wrapper {
          .message-input {
            :deep(.el-textarea__inner) {
              font-size: 16px; // 防止iOS缩放
            }
          }
        }
      }
    }
  }
}
</style>