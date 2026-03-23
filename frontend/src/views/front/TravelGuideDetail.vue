<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, InfoFilled, Document, User, LocationInformation, Calendar, View } from '@element-plus/icons-vue'
import http from '@/utils/http'
import { getImageUrl, formatDate } from '@/utils/system'

const route = useRoute()
const router = useRouter()

// 页面数据
const travelGuideDetail = ref({})
const loading = ref(false)

// 获取旅游攻略详情
const getTravelGuideDetail = async () => {
  const guideId = route.query.id
  if (!guideId) {
    ElMessage.error('攻略ID不能为空')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/travelGuide/getById?id=${guideId}`)
    if (res.code === 200) {
      travelGuideDetail.value = res.data
    } else {
      ElMessage.error('获取攻略详情失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 格式化创建时间
const formattedCreateTime = computed(() => {
  if (!travelGuideDetail.value.createTime) return ''
  return formatDate(travelGuideDetail.value.createTime)
})

onMounted(() => {
  getTravelGuideDetail()
})
</script>

<template>
  <div class="travel-guide-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 详情内容 -->
    <div v-else-if="travelGuideDetail.id" class="detail-content">
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
      </div>

      <!-- 攻略基本信息 -->
      <div class="guide-info-card">
        <div class="guide-image">
          <img 
            :src="getImageUrl(travelGuideDetail.imgUrl)" 
            :alt="travelGuideDetail.title"
            @error="$event.target.src = '/default-guide.jpg'"
          />
        </div>
        
        <div class="guide-basic-info">
          <h1 class="guide-title">{{ travelGuideDetail.title }}</h1>
          
          <!-- 攻略元信息 -->
          <div class="guide-meta">
            <div v-if="travelGuideDetail.userNickName" class="meta-item">
              <el-icon class="meta-icon"><User /></el-icon>
              <span class="meta-label">作者：</span>
              <span class="meta-value">{{ travelGuideDetail.userNickName }}</span>
            </div>
            
            <div v-if="travelGuideDetail.travelName" class="meta-item">
              <el-icon class="meta-icon"><LocationInformation /></el-icon>
              <span class="meta-label">景点：</span>
              <span class="meta-value">{{ travelGuideDetail.travelName }}</span>
            </div>
            
            <div v-if="travelGuideDetail.travelLocation" class="meta-item">
              <el-icon class="meta-icon"><LocationInformation /></el-icon>
              <span class="meta-label">位置：</span>
              <span class="meta-value">{{ travelGuideDetail.travelLocation }}</span>
            </div>
            
            <div class="meta-item">
              <el-icon class="meta-icon"><View /></el-icon>
              <span class="meta-label">浏览量：</span>
              <span class="meta-value">{{ travelGuideDetail.viewCount || 0 }}</span>
            </div>
            
            <div class="meta-item">
              <el-icon class="meta-icon"><Calendar /></el-icon>
              <span class="meta-label">发布时间：</span>
              <span class="meta-value">{{ formattedCreateTime }}</span>
            </div>
          </div>

          <!-- 评分信息 -->
          <div v-if="travelGuideDetail.rating" class="rating-section">
            <div class="rating-label">攻略评分：</div>
            <div class="rating-display">
              <el-rate
                v-model="travelGuideDetail.rating"
                :disabled="true"
                :show-score="true"
                :score-template="'{value} 分'"
                size="large"
                class="rating-stars"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 攻略详细内容 -->
      <div class="detail-sections">
        <div v-if="travelGuideDetail.content" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><Document /></el-icon>
            攻略详情
          </h3>
          <div class="section-content rich-content" v-html="travelGuideDetail.content"></div>
        </div>
      </div>
    </div>

    <!-- 数据为空 -->
    <div v-else class="empty-state">
      <el-empty description="未找到相关攻略信息" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.travel-guide-detail-container {
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
    justify-content: flex-start;
    align-items: center;
    margin-bottom: 32px;

    .back-btn {
      border-radius: 25px;
      padding: 12px 24px;
      font-weight: 600;
    }
  }

  .guide-info-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 32px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    display: flex;
    gap: 40px;
    align-items: flex-start;

    .guide-image {
      flex-shrink: 0;
      width: 400px;
      height: 300px;
      border-radius: 16px;
      overflow: hidden;
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);

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

    .guide-basic-info {
      flex: 1;
      
      .guide-title {
        font-size: 2.5rem;
        font-weight: 700;
        color: #2c3e50;
        margin-bottom: 24px;
        line-height: 1.2;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }

      .guide-meta {
        display: flex;
        flex-direction: column;
        gap: 16px;
        margin-bottom: 32px;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 1rem;

          .meta-icon {
            color: #667eea;
            font-size: 1.2rem;
          }

          .meta-label {
            font-weight: 600;
            color: #34495e;
            min-width: 80px;
          }

          .meta-value {
            color: #2c3e50;
            font-weight: 500;
          }
        }
      }

      .rating-section {
        padding: 24px;
        background: linear-gradient(135deg, #ffeaa7 0%, #fab1a0 100%);
        border-radius: 16px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

        .rating-label {
          font-size: 1.1rem;
          font-weight: 600;
          color: #2d3436;
          margin-bottom: 12px;
        }

        .rating-display {
          .rating-stars {
            :deep(.el-rate__text) {
              font-size: 1.1rem;
              font-weight: 600;
              color: #2d3436;
            }
          }
        }
      }
    }
  }

  .detail-sections {
    .detail-section {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 20px;
      padding: 40px;
      margin-bottom: 24px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
      backdrop-filter: blur(10px);

      .section-title {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 1.5rem;
        font-weight: 700;
        color: #2c3e50;
        margin-bottom: 24px;
        padding-bottom: 16px;
        border-bottom: 3px solid #667eea;

        .section-icon {
          color: #667eea;
          font-size: 1.8rem;
        }
      }

      .section-content {
        font-size: 1rem;
        line-height: 1.8;
        color: #2c3e50;

        &.rich-content {
          :deep(p) {
            margin-bottom: 16px;
            line-height: 1.8;
          }

          :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
            margin: 24px 0 16px 0;
            font-weight: 600;
            color: #2c3e50;
          }

          :deep(img) {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            margin: 16px 0;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
          }

          :deep(ul), :deep(ol) {
            margin: 16px 0;
            padding-left: 24px;

            li {
              margin-bottom: 8px;
              line-height: 1.6;
            }
          }

          :deep(blockquote) {
            margin: 16px 0;
            padding: 16px 20px;
            background: #f8f9fa;
            border-left: 4px solid #667eea;
            border-radius: 0 8px 8px 0;
            font-style: italic;
          }

          :deep(code) {
            background: #f1f3f4;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Courier New', monospace;
          }

          :deep(pre) {
            background: #f8f9fa;
            padding: 16px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 16px 0;

            code {
              background: none;
              padding: 0;
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
    border-radius: 20px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .travel-guide-detail-container {
    .guide-info-card {
      flex-direction: column;
      
      .guide-image {
        width: 100%;
        max-width: 500px;
        margin: 0 auto;
      }
    }
  }
}

@media (max-width: 768px) {
  .travel-guide-detail-container {
    padding: 20px 10px;

    .guide-info-card {
      padding: 24px;
      
      .guide-basic-info {
        .guide-title {
          font-size: 2rem;
        }

        .guide-meta {
          .meta-item {
            font-size: 0.9rem;
          }
        }
      }
    }

    .detail-sections {
      .detail-section {
        padding: 24px;

        .section-title {
          font-size: 1.3rem;
        }
      }
    }
  }
}
</style>