<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import useUserStore from "@/stores/userStore"
import http from '@/utils/http'
import { ElMessage } from 'element-plus'
import VueQrcode from 'vue-qrcode'
import { 
  Ticket, 
  User, 
  Phone, 
  CreditCard, 
  ShoppingCart, 
  Location, 
  Calendar,
  Star,
  Money,
  ArrowLeft,
  Close
} from '@element-plus/icons-vue'

// 用户状态管理
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

// 当前用户信息
const currentUser = computed(() => {
  try {
    return userStore.userInfo ?? null
  } catch {
    return null
  }
})

// 页面数据
const loading = ref(false)
const submitting = ref(false)
const ticketInfo = ref<any>(null)
const orderFormRef = ref()
const ticketId = computed(() => {
  return route.query.ticketId;
})

// 支付弹窗相关数据
const showPaymentDialog = ref(false)
const paymentQrCode = ref('')
const countdown = ref(60)
const countdownTimer = ref(null)

// 下单表单数据
const orderForm = ref({
  ticketId: 0,
  payNum: 1,
  contactName: '',
  contactPhone: '',
  cardNo: ''
})

// 表单验证规则
const formRules = {
  payNum: [
    { required: true, message: '请选择购买数量', trigger: 'change' },
    { type: 'number', min: 1, message: '购买数量至少为1张', trigger: 'change' }
  ],
  contactName: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2到20个字符', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系人手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  // cardNo: [
  //   { required: true, message: '请输入身份证号', trigger: 'blur' },
  //   { pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  // ]
}

// 计算总价
const totalPrice = computed(() => {
  if (!ticketInfo.value || !orderForm.value.payNum) return 0
  return ticketInfo.value.price * orderForm.value.payNum
})

// 获取门票详情
const fetchTicketInfo = async () => {
  if (!ticketId.value) {
    ElMessage.error('门票ID不能为空')
    router.push('/front/tickets')
    return
  }
  
  loading.value = true
  try {
    const response = await http.get(`/ticket/getById?id=${ticketId.value}`)
    if (response.code === 200) {
      ticketInfo.value = response.data
    } else {
      ElMessage.error(response.msg || '获取门票信息失败')
      router.push('/front/tickets')
    }
  } catch (error) {
    console.error('获取门票信息失败:', error)
    ElMessage.error('获取门票信息失败')
    router.push('/front/tickets')
  } finally {
    loading.value = false
  }
}

// 开始支付流程
const submitOrder = async () => {
  // 表单验证
  if (!orderFormRef.value) {
    ElMessage.error('表单未初始化')
    return
  }
  
  try {
    await orderFormRef.value.validate()
  } catch (error) {
    ElMessage.warning('请填写完整的订单信息')
    return
  }
  
  // 生成支付二维码内容
  paymentQrCode.value = `ORDER_${Date.now()}_PAY_${totalPrice.value}`
  
  // 显示支付二维码弹窗
  showPaymentDialog.value = true
  
  // 启动倒计时
  startCountdown()
}

// 启动倒计时
const startCountdown = () => {
  countdown.value = 60
  countdownTimer.value = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(countdownTimer.value)
      cancelPayment()
    }
  }, 1000)
}

// 取消支付
const cancelPayment = () => {
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value)
    countdownTimer.value = null
  }
  showPaymentDialog.value = false
  countdown.value = 60
  ElMessage.info('已取消支付')
}

// 确认支付完成，创建订单
const confirmPayment = async () => {
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value)
    countdownTimer.value = null
  }
  
  submitting.value = true
  
  try {
    const orderData = {
      ...orderForm.value,
      ticketId: ticketId.value,
      totalAmount: totalPrice.value
    }
    
    const response = await http.post('/ticketOrder/add', orderData)
    if (response.code === 200) {
      showPaymentDialog.value = false
      ElMessage.success('支付成功！订单已创建')
      // 跳转到我的订单页
      router.push('/front/my-ticket-order')
    } else {
      ElMessage.error(response.msg || '订单创建失败')
    }
  } catch (error) {
    console.error('创建订单失败:', error)
    ElMessage.error('订单创建失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 页面初始化
onMounted(() => {
  fetchTicketInfo()
})

// 组件销毁时清理定时器
onUnmounted(() => {
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value)
    countdownTimer.value = null
  }
})
</script>

<template>
  <div class="ticket-order-page">
    <div class="page-container">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-left">
          <el-button 
            type="primary" 
            :icon="ArrowLeft" 
            @click="goBack"
            class="back-btn"
          >
            返回
          </el-button>
          <h1 class="page-title">门票预订</h1>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton animated>
          <template #template>
            <div class="order-content">
              <div class="ticket-info-section">
                <el-skeleton-item variant="h1" style="width: 60%; margin-bottom: 20px;" />
                <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 12px;" />
                <el-skeleton-item variant="text" style="width: 80%; margin-bottom: 12px;" />
                <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 12px;" />
              </div>
              <div class="order-form-section">
                <el-skeleton-item variant="h3" style="width: 40%; margin-bottom: 20px;" />
                <el-skeleton-item variant="text" style="width: 100%; height: 40px; margin-bottom: 16px;" />
                <el-skeleton-item variant="text" style="width: 100%; height: 40px; margin-bottom: 16px;" />
                <el-skeleton-item variant="text" style="width: 100%; height: 40px; margin-bottom: 16px;" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>

      <!-- 订单内容 -->
      <div v-else-if="ticketInfo" class="order-content">
        <!-- 左侧：门票信息 -->
        <div class="ticket-info-section">
          <div class="ticket-card">
            <div class="ticket-header">
              <div class="ticket-icon">
                <el-icon><Ticket /></el-icon>
              </div>
              <div class="ticket-title">
                <h2>{{ ticketInfo.name }}</h2>
                <p class="ticket-subtitle">{{ ticketInfo.travelName }}</p>
              </div>
            </div>

            <div class="ticket-details">
              <div class="detail-item">
                <el-icon class="detail-icon"><Calendar /></el-icon>
                <span class="detail-label">有效时间:</span>
                <span class="detail-value">{{ ticketInfo.validityDays }}天</span>
              </div>
              
              <div class="detail-item">
                <el-icon class="detail-icon"><Star /></el-icon>
                <span class="detail-label">库存数量:</span>
                <span class="detail-value stock-info" :class="{ 'low-stock': ticketInfo.inventory < 10 }">
                  {{ ticketInfo.inventory }}张
                </span>
              </div>
            </div>

            <div class="price-section">
              <div class="price-container">
                <div class="current-price">
                  <span class="price-label">现价</span>
                  <span class="price-value">¥{{ ticketInfo.price }}</span>
                </div>
                <div v-if="ticketInfo.oriPrice && ticketInfo.oriPrice > ticketInfo.oriPrice" class="original-price">
                  <span class="original-label">原价</span>
                  <span class="original-value">¥{{ ticketInfo.oriPrice }}</span>
                </div>
              </div>
              <div v-if="ticketInfo.oriPrice && ticketInfo.oriPrice > ticketInfo.price" class="discount-info">
                <span class="discount-tag">
                  立省¥{{ (ticketInfo.oriPrice - ticketInfo.price).toFixed(2) }}
                </span>
              </div>
            </div>

            <div class="ticket-description">
              <h4>门票说明</h4>
              <p>{{ ticketInfo.description || '暂无详细说明' }}</p>
            </div>
          </div>
        </div>

        <!-- 右侧：下单表单 -->
        <div class="order-form-section">
          <div class="form-card">
            <div class="form-header">
              <h3>
                <el-icon><ShoppingCart /></el-icon>
                预订信息
              </h3>
            </div>

            <el-form 
              :model="orderForm" 
              :rules="formRules" 
              ref="orderFormRef"
              label-width="100px"
              class="order-form"
            >
              <el-form-item label="购买数量" prop="payNum">
                <el-input-number
                  v-model="orderForm.payNum"
                  :min="1"
                  :max="ticketInfo.inventory"
                  size="large"
                  class="quantity-input"
                />
                <span class="stock-hint">库存：{{ ticketInfo.inventory }}张</span>
              </el-form-item>

              <el-form-item label="联系人姓名" prop="contactName">
                <el-input
                  v-model="orderForm.contactName"
                  placeholder="请输入联系人姓名"
                  size="large"
                  :prefix-icon="User"
                />
              </el-form-item>

              <el-form-item label="联系人手机" prop="contactPhone">
                <el-input
                  v-model="orderForm.contactPhone"
                  placeholder="请输入联系人手机号"
                  size="large"
                  :prefix-icon="Phone"
                />
              </el-form-item>

              <el-form-item label="身份证号" prop="cardNo">
                <el-input
                  v-model="orderForm.cardNo"
                  placeholder="请输入身份证号"
                  size="large"
                  :prefix-icon="CreditCard"
                />
              </el-form-item>
            </el-form>

            <!-- 价格汇总 -->
            <div class="price-summary">
              <div class="summary-row">
                <span class="summary-label">单价：</span>
                <span class="summary-value">¥{{ ticketInfo.currentPrice }}</span>
              </div>
              <div class="summary-row">
                <span class="summary-label">数量：</span>
                <span class="summary-value">{{ orderForm.payNum }}张</span>
              </div>
              <div class="summary-row total-row">
                <span class="summary-label">总计：</span>
                <span class="summary-value total-price">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
            </div>

            <!-- 提交按钮 -->
            <div class="form-actions">
              <el-button
                type="primary"
                size="large"
                :loading="submitting"
                @click="submitOrder"
                class="submit-btn"
                :disabled="ticketInfo.stock < 1"
              >
                <el-icon><Money /></el-icon>
                {{ submitting ? '提交中...' : '立即下单' }}
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 支付二维码弹窗 -->
    <el-dialog
      v-model="showPaymentDialog"
      title="扫码支付"
      width="450px"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      center
      class="payment-dialog"
    >
      <div class="payment-content">
        <!-- 倒计时提示 -->
        <div class="countdown-container">
          <div class="countdown-circle">
            <span class="countdown-text">{{ countdown }}s</span>
          </div>
          <p class="countdown-tip">请在 {{ countdown }} 秒内完成支付</p>
        </div>

        <!-- 二维码区域 -->
        <div class="qr-code-container">
          <VueQrcode
            :value="paymentQrCode"
            :options="{ width: 200, margin: 2 }"
            class="qr-code"
          />
        </div>

        <!-- 支付信息 -->
        <div class="payment-info">
          <h3>请使用微信或支付宝扫码支付</h3>
          <p class="payment-amount">支付金额：¥{{ totalPrice.toFixed(2) }}</p>
          <p class="payment-tip">扫码完成支付后，请点击"支付完成"按钮</p>
        </div>

        <!-- 操作按钮 -->
        <div class="payment-actions">
          <el-button
            size="large"
            @click="cancelPayment"
            class="cancel-btn"
          >
            取消支付
          </el-button>
          <el-button
            type="primary"
            size="large"
            :loading="submitting"
            @click="confirmPayment"
            class="confirm-btn"
          >
            <el-icon><Money /></el-icon>
            {{ submitting ? '处理中...' : '支付完成' }}
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.ticket-order-page {
  min-height: calc(100vh - 70px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);

  .page-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 20px;
  }

  .page-header {
    margin-bottom: 30px;

    .header-left {
      display: flex;
      align-items: center;
      gap: 16px;

      .back-btn {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        border-radius: 8px;
        
        &:hover {
          background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
          transform: translateY(-1px);
        }
      }

      .page-title {
        font-size: 28px;
        font-weight: 700;
        color: #2c3e50;
        margin: 0;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }
    }
  }

  .loading-container {
    background: white;
    border-radius: 16px;
    padding: 40px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }

  .order-content {
    display: grid;
    grid-template-columns: 1fr 400px;
    gap: 30px;
    align-items: start;

    .ticket-info-section {
      .ticket-card {
        background: white;
        border-radius: 16px;
        padding: 30px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        border: 1px solid #e1e8ed;

        .ticket-header {
          display: flex;
          align-items: center;
          gap: 16px;
          margin-bottom: 24px;
          padding-bottom: 20px;
          border-bottom: 2px solid #f0f0f0;

          .ticket-icon {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 24px;
          }

          .ticket-title {
            flex: 1;

            h2 {
              font-size: 24px;
              font-weight: 700;
              color: #2c3e50;
              margin: 0 0 8px 0;
              line-height: 1.3;
            }

            .ticket-subtitle {
              font-size: 16px;
              color: #7f8c8d;
              margin: 0;
            }
          }
        }

        .ticket-details {
          margin-bottom: 24px;

          .detail-item {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 16px;
            padding: 12px;
            background: #f8f9fa;
            border-radius: 8px;

            .detail-icon {
              font-size: 18px;
              color: #409eff;
              flex-shrink: 0;
            }

            .detail-label {
              font-weight: 600;
              color: #34495e;
              min-width: 80px;
            }

            .detail-value {
              color: #2c3e50;
              font-weight: 500;

              &.stock-info {
                color: #67c23a;
                font-weight: 600;

                &.low-stock {
                  color: #f56c6c;
                }
              }
            }
          }
        }

        .price-section {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border-radius: 12px;
          padding: 20px;
          margin-bottom: 24px;
          color: white;

          .price-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;

            .current-price {
              .price-label {
                font-size: 14px;
                opacity: 0.9;
                display: block;
                margin-bottom: 4px;
              }

              .price-value {
                font-size: 32px;
                font-weight: 700;
              }
            }

            .original-price {
              text-align: right;

              .original-label {
                font-size: 12px;
                opacity: 0.7;
                display: block;
                margin-bottom: 4px;
              }

              .original-value {
                font-size: 18px;
                text-decoration: line-through;
                opacity: 0.8;
              }
            }
          }

          .discount-info {
            .discount-tag {
              background: rgba(255, 255, 255, 0.2);
              padding: 4px 12px;
              border-radius: 20px;
              font-size: 14px;
              font-weight: 600;
            }
          }
        }

        .ticket-description {
          h4 {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0 0 12px 0;
          }

          p {
            font-size: 14px;
            color: #7f8c8d;
            line-height: 1.6;
            margin: 0;
          }
        }
      }
    }

    .order-form-section {
      width: 500px;
      .form-card {
        background: white;
        border-radius: 16px;
        padding: 30px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        border: 1px solid #e1e8ed;
        position: sticky;
        top: 20px;

        .form-header {
          margin-bottom: 24px;
          padding-bottom: 16px;
          border-bottom: 2px solid #f0f0f0;

          h3 {
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0;
            display: flex;
            align-items: center;
            gap: 8px;

            .el-icon {
              color: #409eff;
            }
          }
        }

        .order-form {
          margin-bottom: 24px;

          .quantity-input {
            width: 120px;
          }

          .stock-hint {
            margin-left: 12px;
            font-size: 12px;
            color: #7f8c8d;
          }

          :deep(.el-form-item__label) {
            font-weight: 600;
            color: #34495e;
          }

          :deep(.el-input__wrapper) {
            border-radius: 8px;
          }
        }

        .price-summary {
          background: #f8f9fa;
          border-radius: 12px;
          padding: 20px;
          margin-bottom: 24px;

          .summary-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;

            &:last-child {
              margin-bottom: 0;
            }

            &.total-row {
              padding-top: 12px;
              border-top: 2px solid #e1e8ed;
              font-size: 18px;
              font-weight: 600;

              .total-price {
                color: #f56c6c;
                font-size: 24px;
              }
            }

            .summary-label {
              color: #7f8c8d;
              font-weight: 500;
            }

            .summary-value {
              color: #2c3e50;
              font-weight: 600;
            }
          }
        }

        .form-actions {
          .submit-btn {
            width: 100%;
            height: 50px;
            font-size: 16px;
            font-weight: 600;
            background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
            border: none;
            border-radius: 12px;

            &:hover:not(:disabled) {
              background: linear-gradient(135deg, #5daf34 0%, #73c556 100%);
              transform: translateY(-1px);
            }

            &:disabled {
              background: #c0c4cc;
              cursor: not-allowed;
            }
          }
        }
      }
    }
  }
}

// 支付弹窗样式
:deep(.payment-dialog) {
  .el-dialog__header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 20px 24px;
    border-radius: 12px 12px 0 0;

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
    }
  }

  .el-dialog__body {
    padding: 30px 24px;
  }
}

.payment-content {
  text-align: center;

  .countdown-container {
    margin-bottom: 24px;
    
    .countdown-circle {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      background: linear-gradient(135deg, #f56c6c 0%, #ff8a80 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 12px;
      box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);
      
      .countdown-text {
        font-size: 24px;
        font-weight: 700;
        color: white;
      }
    }
    
    .countdown-tip {
      font-size: 16px;
      color: #f56c6c;
      font-weight: 600;
      margin: 0;
    }
  }

  .qr-code-container {
    display: flex;
    justify-content: center;
    margin-bottom: 24px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 12px;
    border: 2px dashed #e1e8ed;

    .qr-code {
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
  }

  .payment-info {
    margin-bottom: 24px;
    
    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #2c3e50;
      margin: 0 0 16px 0;
    }

    .payment-amount {
      font-size: 24px;
      font-weight: 700;
      color: #f56c6c;
      margin: 0 0 12px 0;
    }

    .payment-tip {
      font-size: 14px;
      color: #7f8c8d;
      margin: 0;
    }
  }

  .payment-actions {
    display: flex;
    gap: 16px;
    justify-content: center;

    .cancel-btn {
      flex: 1;
      height: 48px;
      font-size: 16px;
      font-weight: 600;
      border: 2px solid #dcdfe6;
      color: #606266;
      background: white;
      border-radius: 8px;

      &:hover {
        border-color: #c0c4cc;
        color: #409eff;
        background: #f5f7fa;
      }
    }

    .confirm-btn {
      flex: 1;
      height: 48px;
      font-size: 16px;
      font-weight: 600;
      background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
      border: none;
      border-radius: 8px;

      &:hover:not(:disabled) {
        background: linear-gradient(135deg, #5daf34 0%, #73c556 100%);
        transform: translateY(-1px);
      }

      &:disabled {
        background: #c0c4cc;
        cursor: not-allowed;
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .ticket-order-page {
    .order-content {
      grid-template-columns: 1fr 350px;
      gap: 20px;
    }
  }
}

@media (max-width: 768px) {
  .ticket-order-page {
    .page-container {
      padding: 15px;
    }

    .page-header {
      margin-bottom: 20px;

      .header-left {
        .page-title {
          font-size: 24px;
        }
      }
    }

    .order-content {
      grid-template-columns: 1fr;
      gap: 20px;

      .order-form-section .form-card {
        position: static;
      }
    }
  }
}

@media (max-width: 480px) {
  .ticket-order-page {
    .page-container {
      padding: 10px;
    }

    .order-content {
      .ticket-info-section .ticket-card,
      .order-form-section .form-card {
        padding: 20px;
      }
    }
  }
}
</style>