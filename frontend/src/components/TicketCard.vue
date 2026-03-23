<script setup>
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Money, Clock, InfoFilled, Ticket, Warning, Check } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  ticket: {
    type: Object,
    required: true
  }
})

const router = useRouter()

// 格式化价格显示
const formatPrice = (price) => {
  if (!price) return '免费'
  return price.toString().includes('￥') ? price : `￥${price}`
}

// 格式化状态显示
const getStatusText = (status) => {
  const statusMap = {
    1: '正常销售',
    0: '暂时停售'
  }
  return statusMap[status] || '未知状态'
}

// 获取状态样式类
const getStatusClass = (status) => {
  return status === 1 ? 'status-active' : 'status-inactive'
}

// 获取库存状态
const getInventoryStatus = (inventory) => {
  if (!inventory || inventory <= 0) return 'sold-out'
  if (inventory <= 10) return 'low-stock'
  return 'in-stock'
}

// 处理购买点击
const handlePurchase = () => {
  if (props.ticket.status !== 1) {
    ElMessage.warning('该门票暂时停售')
    return
  }
  if (!props.ticket.inventory || props.ticket.inventory <= 0) {
    ElMessage.warning('门票已售罄')
    return
  }
  
  // 跳转到门票下单页面
  router.push({
    path: '/front/ticket-order',
    query: {
      ticketId: props.ticket.id
    }
  })
}
</script>

<template>
  <div class="ticket-card" :class="{ 'sold-out': !ticket.inventory || ticket.inventory <= 0 }">

    <!-- 卡片头部 -->
    <div class="card-header">
      <div class="ticket-icon">
        <el-icon><Ticket /></el-icon>
      </div>
      <h3 class="ticket-name">{{ ticket.name }}</h3>
    </div>

    <!-- 卡片内容 -->
    <div class="card-content">
      <!-- 价格区域 -->
      <div class="price-section">
        <div class="price-container">
          <div class="price-box">
            <div v-if="ticket?.oriPrice && ticket.oriPrice !== ticket.price" class="original-price">
              <span class="original-label">原价</span>
              <span class="original-amount">￥{{ ticket.oriPrice }}</span>
            </div>
            <div class="current-price">
              <span class="price-label">现价</span>
              <div class="price-main">
                <span class="currency">￥</span>
                <span class="amount">{{ ticket?.price || 0 }}</span>
              </div>
            </div>
          </div>
          <div v-if="ticket?.oriPrice && ticket.oriPrice !== ticket.price" class="savings-info">
            <div class="discount-tag">
              <span class="discount-text">{{ Math.round((1 - ticket.price / ticket.oriPrice) * 100) }}% OFF</span>
            </div>
            <div class="savings-amount">
              省 ￥{{ (ticket.oriPrice - ticket.price).toFixed(0) }}
            </div>
          </div>
        </div>
      </div>

      <!-- 门票信息 -->
      <div class="ticket-info">
        <div class="info-item">
          <el-icon class="info-icon"><Clock /></el-icon>
          <span class="info-text">购买后{{ ticket.validityDays || 30 }}天有效</span>
        </div>
        
        <div v-if="ticket?.description" class="info-item">
          <el-icon class="info-icon"><InfoFilled /></el-icon>
          <span class="info-text">{{ ticket.description }}</span>
        </div>
        
        <div class="info-item inventory-item" :class="getInventoryStatus(ticket.inventory)">
          <el-icon class="info-icon"><Ticket /></el-icon>
          <span class="info-text">
            剩余 <span class="inventory-count">{{ ticket?.inventory || 0 }}</span> 张
          </span>
        </div>
      </div>
    </div>
    
    <!-- 卡片底部 -->
    <div class="card-footer">
      <el-button 
        type="primary" 
        size="large"
        class="purchase-btn"
        :class="{ 'btn-gradient': ticket.status === 1 && ticket.inventory > 0 }"
        :disabled="ticket.status !== 1 || !ticket.inventory || ticket.inventory <= 0"
        @click="handlePurchase"
      >
        <el-icon><Money /></el-icon>
        <span>
          {{ ticket.status !== 1 ? '暂时停售' : 
             (!ticket.inventory || ticket.inventory <= 0) ? '已售罄' : '立即购买' }}
        </span>
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.ticket-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  border: 2px solid transparent;
  border-radius: 20px;
  padding: 14px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  height: 460px;
  display: flex;
  flex-direction: column;
  background-clip: padding-box;
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%) translateY(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) translateY(100%) rotate(45deg);
  }
}

.ticket-card.sold-out {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  opacity: 0.7;
}

.ticket-card.sold-out:hover {
  transform: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

/* 状态标签 */
.status-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
  z-index: 2;
}

.status-badge.status-active {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.status-badge.status-inactive {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

/* 卡片头部 */
.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.ticket-icon {
  width: 30px;
  height: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 15px;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
  position: relative;
  overflow: hidden;
}

.ticket-icon::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: rotate(45deg);
  transition: all 0.6s ease;
  opacity: 0;
}

.ticket-card:hover .ticket-icon::before {
  opacity: 1;
  animation: iconShimmer 1s ease-in-out;
}

@keyframes iconShimmer {
  0% {
    transform: translateX(-100%) translateY(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) translateY(100%) rotate(45deg);
  }
}

.ticket-name {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
  line-height: 1.3;
  flex: 1;
}

/* 卡片内容 */
.card-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 价格区域 */
.price-section {
  padding: 24px;
  background: linear-gradient(135deg, #fef7ff 0%, #f3e8ff 50%, #ede9fe 100%);
  border-radius: 16px;
  border: 2px solid #e879f9;
  position: relative;
  overflow: hidden;
}

.price-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #f59e0b, #ef4444, #ec4899);
}

.price-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.price-box {
  flex: 1;
}

.original-price {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  padding: 6px 12px;
  background: rgba(239, 68, 68, 0.1);
  border-radius: 8px;
  border: 1px dashed #ef4444;
}

.original-label {
  font-size: 12px;
  color: #7f1d1d;
  font-weight: 600;
  background: #fecaca;
  padding: 2px 6px;
  border-radius: 4px;
}

.original-amount {
  font-size: 16px;
  color: #991b1b;
  text-decoration: line-through;
  font-weight: 600;
  position: relative;
}

.original-amount::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 2px;
  background: #ef4444;
  transform: translateY(-50%) rotate(-5deg);
}

.current-price {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.price-label {
  font-size: 12px;
  color: #059669;
  font-weight: 700;
  background: linear-gradient(135deg, #d1fae5, #a7f3d0);
  padding: 3px 8px;
  border-radius: 6px;
  align-self: flex-start;
  border: 1px solid #10b981;
}

.price-main {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.currency {
  font-size: 20px;
  color: #dc2626;
  font-weight: 700;
}

.amount {
  font-size: 36px;
  color: #dc2626;
  font-weight: 900;
  line-height: 1;
  text-shadow: 2px 2px 4px rgba(220, 38, 38, 0.2);
}

.savings-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.discount-tag {
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: white;
  padding: 8px 16px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 800;
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.4);
  transform: rotate(-5deg);
  position: relative;
}

.discount-tag::before {
  content: '🔥';
  position: absolute;
  left: -8px;
  top: -8px;
  font-size: 16px;
}

.discount-text {
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.savings-amount {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

/* 门票信息 */
.ticket-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
}

.info-icon {
  color: #3b82f6;
  font-size: 16px;
  flex-shrink: 0;
}

.info-text {
  color: #475569;
  font-size: 14px;
  line-height: 1.5;
}

.inventory-item.sold-out .info-icon {
  color: #ef4444;
}

.inventory-item.sold-out .inventory-count {
  color: #ef4444;
  font-weight: 700;
}

.inventory-item.low-stock .info-icon {
  color: #f59e0b;
}

.inventory-item.low-stock .inventory-count {
  color: #f59e0b;
  font-weight: 700;
}

.inventory-item.in-stock .info-icon {
  color: #10b981;
}

.inventory-item.in-stock .inventory-count {
  color: #10b981;
  font-weight: 700;
}

.inventory-count {
  font-weight: 700;
  font-size: 16px;
}

/* 卡片底部 */
.card-footer {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

.purchase-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.purchase-btn.btn-gradient {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  border: none;
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
}

.purchase-btn.btn-gradient:hover {
  background: linear-gradient(135deg, #2563eb, #1e40af);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
  transform: translateY(-2px);
}

.purchase-btn:disabled {
  background: #e2e8f0;
  color: #94a3b8;
  border: 1px solid #cbd5e1;
  cursor: not-allowed;
  box-shadow: none;
}

.purchase-btn:disabled:hover {
  transform: none;
  box-shadow: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ticket-card {
    padding: 24px;
    border-radius: 16px;
  }
  
  .card-header {
    margin-bottom: 16px;
  }
  
  .ticket-icon {
    width: 48px;
    height: 48px;
    font-size: 24px;
    border-radius: 12px;
  }
  
  .ticket-name {
    font-size: 18px;
  }
  
  .price-section {
    padding: 20px;
    border-radius: 12px;
  }
  
  .price-container {
    flex-direction: column;
    gap: 12px;
  }
  
  .savings-info {
    align-items: flex-start;
    flex-direction: row;
    gap: 12px;
  }
  
  .amount {
    font-size: 32px;
  }
  
  .purchase-btn {
    height: 44px;
    font-size: 15px;
  }
  
  .status-badge {
    top: 12px;
    right: 12px;
    padding: 4px 8px;
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .ticket-card {
    padding: 20px;
  }
  
  .price-section {
    padding: 16px;
  }
  
  .price-container {
    gap: 8px;
  }
  
  .savings-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 6px;
  }
  
  .discount-tag {
    transform: none;
    font-size: 12px;
    padding: 6px 12px;
  }
  
  .amount {
    font-size: 28px;
  }
}
</style>