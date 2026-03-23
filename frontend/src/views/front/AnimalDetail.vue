<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, ArrowLeft, Sunny, InfoFilled, Document } from '@element-plus/icons-vue'
import http from '@/utils/http'
import useUserStore from '@/stores/userStore'
import { getImageUrl, stripHtml } from '../../utils/system'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 页面数据
const animalDetail = ref({})
const loading = ref(false)
const isLiked = ref(false)
const isCollected = ref(false)

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

// 获取动物详情
const getAnimalDetail = async () => {
  const animalId = route.query.id
  if (!animalId) {
    ElMessage.error('动物ID不能为空')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/animal/getById?id=${animalId}`)
    if (res.code === 200) {
      animalDetail.value = res.data
      // 获取点赞和收藏状态
      await checkLikeStatus()
      await checkCollectStatus()
    } else {
      ElMessage.error('获取动物详情失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 检查点赞状态
const checkLikeStatus = async () => {
  if (!isLoggedIn.value || !animalDetail.value.id) return
  
  try {
    const res = await http.get(`/like/checkUserLike?userId=${currentUser.value.id}&type=animal&targetId=${animalDetail.value.id}`)
    if (res.code === 200) {
      isLiked.value = res.data
    }
  } catch (error) {
    console.error('检查点赞状态失败:', error)
  }
}

// 检查收藏状态
const checkCollectStatus = async () => {
  if (!isLoggedIn.value || !animalDetail.value.id) return
  
  try {
    const res = await http.get(`/collect/isCollected?userId=${currentUser.value.id}&type=animal&targetId=${animalDetail.value.id}`)
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
      const res = await http.post(`/like/cancelLike?userId=${currentUser.value.id}&type=animal&targetId=${animalDetail.value.id}`)
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
        type: 'animal',
        targetId: animalDetail.value.id
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
      const res = await http.post(`/collect/cancelCollect?userId=${currentUser.value.id}&type=animal&targetId=${animalDetail.value.id}`)
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
        type: 'animal',
        targetId: animalDetail.value.id
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

// 处理标签显示
const tagList = computed(() => {
  if (!animalDetail.value.tags) return []
  try {
    return JSON.parse(animalDetail.value.tags)
  } catch {
    return animalDetail.value.tags.split(',').filter(tag => tag.trim())
  }
})

onMounted(() => {
  getAnimalDetail()
})
</script>

<template>
  <div class="animal-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 详情内容 -->
    <div v-else-if="animalDetail.id" class="detail-content">
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

      <!-- 动物基本信息 -->
      <div class="animal-info-card">
        <div class="animal-image">
          <img 
            :src="getImageUrl(animalDetail.coverUrl)" 
            :alt="animalDetail.name"
            @error="$event.target.src = '/default-animal.jpg'"
          />
        </div>
        
        <div class="animal-basic-info">
          <h1 class="animal-name">{{ animalDetail.name }}</h1>
          <div class="animal-meta">
            <span class="animal-type">{{ animalDetail.typeName }}</span>
            <span class="view-count">
              {{ animalDetail.viewCount || 0 }} 次浏览
            </span>
          </div>
          
          <!-- 标签 -->
          <div v-if="tagList.length > 0" class="animal-tags">
            <el-tag 
              v-for="tag in tagList" 
              :key="tag" 
              type="primary" 
              size="small"
              class="tag-item"
            >
              {{ tag }}
            </el-tag>
          </div>

          <!-- 最佳动物季节信息 -->
          <div v-if="animalDetail.season" class="animal-season-info">
            <div class="season-icon">🌸</div>
            <div class="season-content">
              <div class="season-label">最佳动物季节</div>
              <div class="season-value">{{ animalDetail.season }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 详细信息 -->
      <div class="detail-sections">
        <!-- 简介 -->
        <div v-if="animalDetail.intro" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><InfoFilled /></el-icon>
            动物简介
          </h3>
          <div class="section-content">
            {{ animalDetail.intro }}
          </div>
        </div>

        <!-- 描述 -->
        <div v-if="animalDetail.description" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><Document /></el-icon>
            详细描述
          </h3>
          <div class="section-content">
            {{ stripHtml(animalDetail.description) }}
          </div>
        </div>

      </div>
    </div>

    <!-- 数据为空 -->
    <div v-else class="empty-state">
      <el-empty description="未找到相关动物信息" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.animal-detail-container {
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

  .animal-info-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 30px;
    margin-bottom: 32px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    display: flex;
    gap: 30px;

    .animal-image {
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

    .animal-basic-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .animal-name {
        font-size: 2.5rem;
        font-weight: 700;
        color: #2c3e50;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        margin-bottom: 15px;
      }

      .animal-meta {
        display: flex;
        align-items: center;
        gap: 20px;
        margin-bottom: 20px;

        .animal-type {
          background: linear-gradient(135deg, #667eea, #764ba2);
          color: white;
          padding: 8px 16px;
          border-radius: 20px;
          font-weight: 600;
        }

        .view-count {
          display: flex;
          align-items: center;
          gap: 5px;
          color: #7f8c8d;
          font-size: 0.9rem;
        }
      }

      .animal-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        margin-bottom: 20px;

        .tag-item {
          border-radius: 15px;
          padding: 5px 12px;
        }
      }

      .animal-season-info {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 16px;
        background: rgba(103, 126, 234, 0.1);
        border-radius: 12px;
        border-left: 4px solid #667eea;

        .season-icon {
          font-size: 24px;
          color: #667eea;
        }

        .season-content {
          .season-label {
            font-size: 14px;
            color: #7f8c8d;
            margin-bottom: 4px;
          }

          .season-value {
            font-size: 16px;
            font-weight: 600;
            color: #2c3e50;
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

        &.season-info {
          display: flex;
          align-items: center;

          .season-highlight {
            display: flex;
            align-items: center;
            gap: 8px;
            padding: 12px 16px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 25px;
            color: white;

            .season-text {
              font-weight: 600;
              font-size: 16px;
            }

            .season-desc {
              font-size: 14px;
              opacity: 0.9;
            }
          }
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
  .animal-detail-container {
    padding: 15px;

    .header-actions {
      flex-direction: column;
      gap: 15px;
      align-items: stretch;

      .action-buttons {
        justify-content: center;
      }
    }

    .animal-info-card {
      flex-direction: column;
      text-align: center;

      .animal-image {
        width: 100%;
        max-width: 300px;
        margin: 0 auto;
      }

      .animal-basic-info {
        .animal-name {
          font-size: 2rem;
        }

        .animal-meta {
          justify-content: center;
          flex-wrap: wrap;
        }

        .animal-tags {
          justify-content: center;
        }
      }
    }

    .detail-sections {
      .detail-section {
        padding: 20px;

        .section-title {
          font-size: 1.3rem;
        }
      }
    }
  }
}

// 动画效果
.animal-info-card,
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
