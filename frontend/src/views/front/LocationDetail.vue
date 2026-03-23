<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, ArrowLeft, Sunny, InfoFilled, Document, Location, MapLocation } from '@element-plus/icons-vue'
import http from '@/utils/http'
import useUserStore from '@/stores/userStore'
import { getImageUrl, stripHtml } from '../../utils/system'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 页面数据
const locationDetail = ref({})
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

// 获取动物地点详情
const getLocationDetail = async () => {
  const locationId = route.query.id
  if (!locationId) {
    ElMessage.error('动物地点ID不能为空')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/animalLocation/getById?id=${locationId}`)
    if (res.code === 200) {
      locationDetail.value = res.data
      // 获取点赞和收藏状态
      await checkLikeStatus()
      await checkCollectStatus()
    } else {
      ElMessage.error('获取动物地点详情失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 检查点赞状态
const checkLikeStatus = async () => {
  if (!isLoggedIn.value || !locationDetail.value.id) return
  
  try {
    const res = await http.get(`/like/checkUserLike?userId=${currentUser.value.id}&type=location&targetId=${locationDetail.value.id}`)
    if (res.code === 200) {
      isLiked.value = res.data
    }
  } catch (error) {
    console.error('检查点赞状态失败:', error)
  }
}

// 检查收藏状态
const checkCollectStatus = async () => {
  if (!isLoggedIn.value || !locationDetail.value.id) return
  
  try {
    const res = await http.get(`/collect/isCollected?userId=${currentUser.value.id}&type=location&targetId=${locationDetail.value.id}`)
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
      const res = await http.post(`/like/cancelLike?userId=${currentUser.value.id}&type=location&targetId=${locationDetail.value.id}`)
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
        type: 'location',
        targetId: locationDetail.value.id
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
      const res = await http.post(`/collect/cancelCollect?userId=${currentUser.value.id}&type=location&targetId=${locationDetail.value.id}`)
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
        type: 'location',
        targetId: locationDetail.value.id
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

// 处理常见动物显示
const commonAnimalsList = computed(() => {
  if (!locationDetail.value.commonAnimals) return []
  return locationDetail.value.commonAnimals.split(',').filter(animal => animal.trim())
})

// 处理设施信息显示
const facilitiesList = computed(() => {
  if (!locationDetail.value.facilities) return []
  return locationDetail.value.facilities.split(',').filter(facility => facility.trim())
})

// 跳转地址详情
const handleMapLocation = () => {
  router.push({
    path: '/front/map',
    query: {
      lat: locationDetail.value.lat,
      lon: locationDetail.value.lon,
      name: locationDetail.value.name
    }
  })
}

onMounted(() => {
  getLocationDetail()
})
</script>

<template>
  <div class="location-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 详情内容 -->
    <div v-else-if="locationDetail.id" class="detail-content">
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
            type="primary"
            :icon="MapLocation"
            @click="handleMapLocation"
            class="action-btn"
          >
            地址信息
          </el-button>

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

      <!-- 动物地点基本信息 -->
      <div class="location-info-card">
        <div class="location-image">
          <img 
            :src="getImageUrl(locationDetail.imgUrl)" 
            :alt="locationDetail.name"
            @error="$event.target.src = '/default-location.jpg'"
          />
        </div>
        
        <div class="location-basic-info">
          <h1 class="location-name">{{ locationDetail.name }}</h1>
          
          <!-- 地理位置信息 -->
          <div class="location-coordinates">
            <div class="coordinate-item">
              <el-icon class="coordinate-icon"><Location /></el-icon>
              <span class="coordinate-label">经纬度：</span>
              <span class="coordinate-value">{{ locationDetail.lat }}, {{ locationDetail.lon }}</span>
            </div>
          </div>

          <!-- 最佳动物季节信息 -->
          <div v-if="locationDetail.bestSeason" class="location-season-info">
            <div class="season-icon">🌸</div>
            <div class="season-content">
              <div class="season-label">最佳动物季节</div>
              <div class="season-value">{{ locationDetail.bestSeason }}</div>
            </div>
          </div>

          <!-- 常见动物 -->
          <div v-if="commonAnimalsList.length > 0" class="common-animals">
            <h4 class="animals-title">
              <el-icon><Document /></el-icon>
              常见动物
            </h4>
            <div class="animals-tags">
              <el-tag 
                v-for="animal in commonAnimalsList" 
                :key="animal" 
                type="success" 
                size="small"
                class="animal-tag"
              >
                {{ animal }}
              </el-tag>
            </div>
          </div>

          <!-- 设施信息 -->
          <div v-if="facilitiesList.length > 0" class="facilities-info">
            <h4 class="facilities-title">
              <el-icon><InfoFilled /></el-icon>
              设施信息
            </h4>
            <div class="facilities-tags">
              <el-tag 
                v-for="facility in facilitiesList" 
                :key="facility" 
                type="info" 
                size="small"
                class="facility-tag"
              >
                {{ facility }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>

      <!-- 详细信息 -->
      <div class="detail-sections">
        <!-- 地点描述 -->
        <div v-if="locationDetail.description" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><InfoFilled /></el-icon>
            地点介绍
          </h3>
          <div class="section-content">
            {{ stripHtml(locationDetail.description) }}
          </div>
        </div>
      </div>
    </div>

    <!-- 数据为空 -->
    <div v-else class="empty-state">
      <el-empty description="未找到相关动物地点信息" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.location-detail-container {
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

  .location-info-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 30px;
    margin-bottom: 32px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    display: flex;
    gap: 30px;

    .location-image {
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

    .location-basic-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .location-name {
        font-size: 2.5rem;
        font-weight: 700;
        color: #2c3e50;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        margin-bottom: 20px;
      }

      .location-coordinates {
        margin-bottom: 20px;

        .coordinate-item {
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 12px 16px;
          background: rgba(103, 126, 234, 0.1);
          border-radius: 12px;
          border-left: 4px solid #667eea;

          .coordinate-icon {
            color: #667eea;
            font-size: 18px;
          }

          .coordinate-label {
            font-weight: 600;
            color: #2c3e50;
          }

          .coordinate-value {
            color: #7f8c8d;
            font-family: monospace;
          }
        }
      }

      .location-season-info {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 16px;
        background: rgba(103, 126, 234, 0.1);
        border-radius: 12px;
        border-left: 4px solid #667eea;
        margin-bottom: 20px;

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

      .common-animals {
        margin-bottom: 20px;

        .animals-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 1.1rem;
          font-weight: 600;
          color: #2c3e50;
          margin-bottom: 12px;
        }

        .animals-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;

          .animal-tag {
            border-radius: 15px;
            padding: 5px 12px;
          }
        }
      }

      .facilities-info {
        .facilities-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 1.1rem;
          font-weight: 600;
          color: #2c3e50;
          margin-bottom: 12px;
        }

        .facilities-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;

          .facility-tag {
            border-radius: 15px;
            padding: 5px 12px;
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
  .location-detail-container {
    padding: 15px;

    .header-actions {
      flex-direction: column;
      gap: 15px;
      align-items: stretch;

      .action-buttons {
        justify-content: center;
      }
    }

    .location-info-card {
      flex-direction: column;
      text-align: center;

      .location-image {
        width: 100%;
        max-width: 300px;
        margin: 0 auto;
      }

      .location-basic-info {
        .location-name {
          font-size: 2rem;
        }

        .common-animals .animals-tags,
        .facilities-info .facilities-tags {
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
.location-info-card,
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
