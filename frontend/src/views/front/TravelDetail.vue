<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, ArrowLeft, Sunny, InfoFilled, Document, Location, Phone, Money, Ticket } from '@element-plus/icons-vue'
import http from '@/utils/http'
import useUserStore from '@/stores/userStore'
import { getImageUrl } from '../../utils/system'
import TicketCard from '@/components/TicketCard.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 页面数据
const travelDetail = ref({})
const loading = ref(false)
const isLiked = ref(false)
const isCollected = ref(false)
const tickets = ref([])
const ticketsLoading = ref(false)

// 用户信息
const currentUser = computed(() => {
  try {
    const userInfo = userStore.userInfo
    if (typeof userInfo === 'string') {
      return JSON.parse(userInfo)
    }
    return userInfo || {}
  } catch {
    return {}
  }
})

// 检查是否已登录
const isLoggedIn = computed(() => {
  return !!userStore.token && !!currentUser.value.id
})

// 获取旅游景点详情
const getTravelDetail = async () => {
  const travelId = route.query.id
  if (!travelId) {
    ElMessage.error('景点ID不能为空')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/travel/getById?id=${travelId}`)
    if (res.code === 200) {
      travelDetail.value = res.data
      // 获取点赞和收藏状态
      await checkLikeStatus()
      await checkCollectStatus()
      // 获取门票信息
      await getTicketsByTravelId(travelId)
    } else {
      ElMessage.error('获取景点详情失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 检查点赞状态
const checkLikeStatus = async () => {
  if (!isLoggedIn.value || !travelDetail.value.id) return
  
  try {
    const res = await http.get(`/like/checkUserLike?userId=${currentUser.value.id}&type=travel&targetId=${travelDetail.value.id}`)
    if (res.code === 200) {
      isLiked.value = res.data
    }
  } catch (error) {
    console.error('检查点赞状态失败:', error)
  }
}

// 检查收藏状态
const checkCollectStatus = async () => {
  if (!isLoggedIn.value || !travelDetail.value.id) return
  
  try {
    const res = await http.get(`/collect/isCollected?userId=${currentUser.value.id}&type=travel&targetId=${travelDetail.value.id}`)
    if (res.code === 200) {
      isCollected.value = res.data
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 点赞/取消点赞
const handleLike = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (isLiked.value) {
      // 取消点赞
      const res = await http.post(`/like/cancelLike?userId=${currentUser.value.id}&type=travel&targetId=${travelDetail.value.id}`)
      if (res.code === 200) {
        isLiked.value = false
        ElMessage.success('取消点赞成功')
      } else {
        ElMessage.error('取消点赞失败')
      }
    } else {
      // 点赞
      const res = await http.post('/like/add', {
        userId: currentUser.value.id,
        type: 'travel',
        targetId: travelDetail.value.id
      })
      if (res.code === 200) {
        isLiked.value = true
        ElMessage.success('点赞成功')
      } else {
        ElMessage.error('点赞失败')
      }
    }
  } catch (error) {
    ElMessage.error('操作失败，请稍后重试')
  }
}

// 收藏/取消收藏
const handleCollect = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (isCollected.value) {
      // 取消收藏
      const res = await http.post(`/collect/cancelCollect?userId=${currentUser.value.id}&type=travel&targetId=${travelDetail.value.id}`)
      if (res.code === 200) {
        isCollected.value = false
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error('取消收藏失败')
      }
    } else {
      // 收藏
      const res = await http.post('/collect/add', {
        userId: currentUser.value.id,
        type: 'travel',
        targetId: travelDetail.value.id
      })
      if (res.code === 200) {
        isCollected.value = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error('收藏失败')
      }
    }
  } catch (error) {
    ElMessage.error('操作失败，请稍后重试')
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 格式化评分显示
const formatRating = (rating) => {
  if (!rating) return '暂无评分'
  return `${rating}/5.0`
}

// 获取景点门票列表
const getTicketsByTravelId = async (travelId) => {
  if (!travelId) return
  
  ticketsLoading.value = true
  try {
    const res = await http.get(`/ticket/getByTravelId?travelId=${travelId}`)
    if (res.code === 200) {
      tickets.value = res.data || []
    } else {
      console.error('获取门票信息失败:', res.message)
    }
  } catch (error) {
    console.error('获取门票信息失败:', error)
  } finally {
    ticketsLoading.value = false
  }
}

// 处理门票购买
const handleTicketPurchase = (ticket) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  // 这里可以跳转到门票购买页面或打开购买弹窗
  ElMessage.success(`准备购买门票：${ticket.name}`)
  // TODO: 实现门票购买逻辑
}

onMounted(() => {
  getTravelDetail()
})
</script>

<template>
  <div class="travel-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 详情内容 -->
    <div v-else-if="travelDetail.id" class="detail-content">
      <!-- 头部操作栏 -->
      <div class="header-actions">
        <el-button 
          type="primary" 
          :icon="ArrowLeft" 
          @click="goBack"
          class="back-btn"
        >
          返回
        </el-button>
        
        <div class="action-buttons">
          <el-button
            :type="isLiked ? 'danger' : 'default'"
            :icon="Sunny"
            @click="handleLike"
            class="action-btn"
          >
            {{ isLiked ? '已点赞' : '点赞' }}
          </el-button>
          
          <el-button
            :type="isCollected ? 'warning' : 'default'"
            :icon="Star"
            @click="handleCollect"
            class="action-btn"
          >
            {{ isCollected ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </div>

      <!-- 景点基本信息 -->
      <div class="travel-info-card">
        <div class="travel-image">
          <img 
            :src="getImageUrl(travelDetail.imgUrl)" 
            :alt="travelDetail.name"
            @error="$event.target.src = '/default-travel.jpg'"
          />
        </div>
        
        <div class="travel-basic-info">
          <h1 class="travel-name">{{ travelDetail.name }}</h1>
          <div class="travel-meta">
            <div class="meta-item">
              <el-icon class="meta-icon"><Location /></el-icon>
              <span class="meta-text">{{ travelDetail.location || '位置信息暂无' }}</span>
            </div>
            <div class="meta-item">
              <span class="rating-label">评分:</span>
              <span class="rating-value">{{ formatRating(travelDetail.rating) }}</span>
            </div>
            <div class="meta-item">
              <span class="view-count">
                {{ travelDetail.viewCount || 0 }} 次浏览
              </span>
            </div>
          </div>

          <!-- 价格和联系方式 -->
          <div class="travel-extra-info">
            <div v-if="travelDetail.price" class="price-info">
              <el-icon class="info-icon"><Money /></el-icon>
              <div class="info-content">
                <div class="info-label">价格</div>
                <div class="info-value">{{ travelDetail.price }}</div>
              </div>
            </div>
            
            <div v-if="travelDetail.phone" class="contact-info">
              <el-icon class="info-icon"><Phone /></el-icon>
              <div class="info-content">
                <div class="info-label">联系方式</div>
                <div class="info-value">{{ travelDetail.phone }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 详细信息 -->
      <div class="detail-sections">
        <!-- 门票信息 -->
        <div v-if="tickets.length > 0 || ticketsLoading" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><Ticket /></el-icon>
            门票信息
          </h3>
          <div class="section-content">
            <div v-if="ticketsLoading" class="tickets-loading">
              <el-skeleton :rows="2" animated />
            </div>
            <div v-else-if="tickets.length > 0" class="tickets-grid">
              <TicketCard 
                v-for="ticket in tickets" 
                :key="ticket.id" 
                :ticket="ticket"
                @purchase="handleTicketPurchase"
              />
            </div>
            <div v-else class="no-tickets">
              <el-empty description="暂无门票信息" :image-size="80" />
            </div>
          </div>
        </div>

        <!-- 简介 -->
        <div v-if="travelDetail.intro" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><InfoFilled /></el-icon>
            景点简介
          </h3>
          <div class="section-content">
            {{ travelDetail.intro }}
          </div>
        </div>

        <!-- 详细描述 -->
        <div v-if="travelDetail.description" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><Document /></el-icon>
            详细描述
          </h3>
          <div class="section-content" v-html="travelDetail.description">
          </div>
        </div>
      </div>
    </div>

    <!-- 数据为空 -->
    <div v-else class="empty-state">
      <el-empty description="未找到相关景点信息" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.travel-detail-container {
  min-height: calc(100vh - 70px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 40px 20px;

  .loading-container {
    max-width: 1400px;
    margin: 0 auto;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 30px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }

  .detail-content {
    max-width: 1400px;
    margin: 0 auto;
  }

  .header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 32px;

    .back-btn {
      border-radius: 25px;
      padding: 12px 24px;
      font-weight: 600;
    }

    .action-buttons {
      display: flex;
      gap: 12px;

      .action-btn {
        border-radius: 25px;
        padding: 12px 24px;
        font-weight: 600;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }
      }
    }
  }

  .travel-info-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 30px;
    margin-bottom: 32px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    display: flex;
    gap: 30px;

    .travel-image {
      flex-shrink: 0;
      width: 300px;
      height: 300px;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.3s ease;

        &:hover {
          transform: scale(1.05);
        }
      }
    }

    .travel-basic-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .travel-name {
        font-size: 2.5rem;
        font-weight: 700;
        color: #2c3e50;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        margin-bottom: 15px;
      }

      .travel-meta {
        display: flex;
        flex-direction: column;
        gap: 12px;
        margin-bottom: 20px;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 8px;

          .meta-icon {
            color: #667eea;
            font-size: 16px;
          }

          .meta-text {
            color: #7f8c8d;
            font-size: 14px;
          }

          .rating-label {
            color: #7f8c8d;
            font-size: 14px;
          }

          .rating-value {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 4px 12px;
            border-radius: 15px;
            font-weight: 600;
            font-size: 14px;
          }

          .view-count {
            color: #7f8c8d;
            font-size: 14px;
          }
        }
      }

      .travel-extra-info {
        display: flex;
        flex-direction: column;
        gap: 16px;

        .price-info,
        .contact-info {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 16px;
          background: rgba(103, 126, 234, 0.1);
          border-radius: 12px;
          border-left: 4px solid #667eea;

          .info-icon {
            font-size: 24px;
            color: #667eea;
          }

          .info-content {
            .info-label {
              font-size: 14px;
              color: #7f8c8d;
              margin-bottom: 4px;
            }

            .info-value {
              font-size: 16px;
              font-weight: 600;
              color: #2c3e50;
            }
          }
        }
      }
    }
  }

  .detail-sections {
    .detail-section {
      background: rgba(255, 255, 255, 0.9);
      border-radius: 16px;
      padding: 30px;
      margin-bottom: 24px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

      .section-title {
        font-size: 1.5rem;
        font-weight: 600;
        color: #2c3e50;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 2px solid #f0f0f0;
        display: flex;
        align-items: center;
        gap: 8px;

        .section-icon {
          color: #667eea;
          font-size: 1.2rem;
        }
      }

      .section-content {
        color: #7f8c8d;
        line-height: 1.8;
        font-size: 1rem;

        .tickets-loading {
          padding: 20px 0;
        }

        .tickets-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
          gap: 20px;
          margin-top: 20px;
        }

        .no-tickets {
          text-align: center;
          padding: 40px 0;
          color: #909399;
        }
      }
    }
  }

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 400px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    :deep(.el-empty__description) {
      color: #7f8c8d;
      font-size: 16px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .travel-detail-container {
    padding: 15px;

    .header-actions {
      flex-direction: column;
      gap: 15px;
      align-items: stretch;

      .action-buttons {
        justify-content: center;
      }
    }

    .travel-info-card {
      flex-direction: column;
      text-align: center;

      .travel-image {
        width: 100%;
        max-width: 300px;
        margin: 0 auto;
      }

      .travel-basic-info {
        .travel-name {
          font-size: 2rem;
        }

        .travel-meta {
          align-items: center;

          .meta-item {
            justify-content: center;
          }
        }

        .travel-extra-info {
          align-items: center;
        }
      }
    }

    .detail-sections {
      .detail-section {
        padding: 20px;

        .section-title {
          font-size: 1.3rem;
        }

        .section-content {
          .tickets-grid {
            grid-template-columns: 1fr;
            gap: 16px;
          }
        }
      }
    }
  }
}

// 动画效果
.travel-info-card,
.detail-section {
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>