<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, InfoFilled, Document, Calendar, View } from '@element-plus/icons-vue'
import http from '@/utils/http'
import { getImageUrl } from '../../utils/system'

const route = useRoute()
const router = useRouter()

// 页面数据
const newsDetail = ref({})
const loading = ref(false)

// 获取新闻详情
const getNewsDetail = async () => {
  const newsId = route.query.id
  if (!newsId) {
    ElMessage.error('新闻ID不能为空')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/conservationNews/getById?id=${newsId}`)
    if (res.code === 200) {
      newsDetail.value = res.data
    } else {
      ElMessage.error('获取新闻详情失败')
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

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 获取类型标签颜色
const getTypeColor = (type) => {
  const colorMap = {
    '新闻': 'primary',
    '政策': 'success',
    '法规': 'warning'
  }
  return colorMap[type] || 'info'
}

onMounted(() => {
  getNewsDetail()
})
</script>

<template>
  <div class="news-detail-page">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 详情内容 -->
    <div v-else-if="newsDetail.id" class="page-content">
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

      <!-- 新闻基本信息 -->
      <div class="news-info-card">
        <div class="news-image" v-if="newsDetail.imgUrl">
          <img 
            :src="getImageUrl(newsDetail.imgUrl)" 
            :alt="newsDetail.title"
            @error="$event.target.src = '/default-news.jpg'"
          />
        </div>
        
        <div class="news-basic-info">
          <h1 class="news-title">{{ newsDetail.title }}</h1>
          
          <div class="news-meta">
            <el-tag 
              :type="getTypeColor(newsDetail.type)" 
              size="large"
              class="type-tag"
            >
              {{ newsDetail.type }}
            </el-tag>
            
            <div class="meta-info">
              <div class="meta-item">
                <el-icon><Calendar /></el-icon>
                <span>{{ formatDate(newsDetail.createTime) }}</span>
              </div>
              
              <div class="meta-item">
                <el-icon><View /></el-icon>
                <span>{{ newsDetail.viewCount || 0 }} 次浏览</span>
              </div>
              
              <div v-if="newsDetail.source" class="meta-item">
                <span class="source-label">来源：</span>
                <span class="source-value">{{ newsDetail.source }}</span>
              </div>
            </div>
          </div>

          <!-- 简介 -->
          <div v-if="newsDetail.intro" class="news-intro">
            <div class="intro-icon">📰</div>
            <div class="intro-content">
              <div class="intro-label">新闻简介</div>
              <div class="intro-value">{{ newsDetail.intro }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 详细内容 -->
      <div class="detail-sections">
        <!-- 内容 -->
        <div v-if="newsDetail.content" class="detail-section">
          <h3 class="section-title">
            <el-icon class="section-icon"><Document /></el-icon>
            详细内容
          </h3>
          <div class="section-content" v-html="newsDetail.content">
          </div>
        </div>
      </div>
    </div>

    <!-- 数据为空 -->
    <div v-else class="empty-state">
      <el-empty description="未找到相关新闻信息" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.news-detail-page {
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

  .page-content {
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

  .news-info-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 30px;
    margin-bottom: 32px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    display: flex;
    gap: 30px;

    .news-image {
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

    .news-basic-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .news-title {
        font-size: 2.5rem;
        font-weight: 700;
        color: #2c3e50;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        margin-bottom: 20px;
        line-height: 1.2;
      }

      .news-meta {
        margin-bottom: 25px;

        .type-tag {
          border-radius: 20px;
          padding: 8px 16px;
          font-weight: 600;
          margin-bottom: 15px;
        }

        .meta-info {
          display: flex;
          flex-wrap: wrap;
          gap: 20px;
          align-items: center;

          .meta-item {
            display: flex;
            align-items: center;
            gap: 5px;
            color: #7f8c8d;
            font-size: 0.9rem;

            .el-icon {
              color: #667eea;
            }

            .source-label {
              font-weight: 600;
            }

            .source-value {
              color: #2c3e50;
              font-weight: 500;
            }
          }
        }
      }

      .news-intro {
        display: flex;
        align-items: flex-start;
        gap: 12px;
        padding: 20px;
        background: rgba(103, 126, 234, 0.1);
        border-radius: 12px;
        border-left: 4px solid #667eea;

        .intro-icon {
          font-size: 24px;
          color: #667eea;
          margin-top: 2px;
        }

        .intro-content {
          flex: 1;

          .intro-label {
            font-size: 14px;
            color: #7f8c8d;
            margin-bottom: 8px;
            font-weight: 600;
          }

          .intro-value {
            font-size: 16px;
            color: #2c3e50;
            line-height: 1.6;
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
        margin-bottom: 20px;
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
        color: #555;
        line-height: 1.8;
        font-size: 1rem;

        :deep(p) {
          margin-bottom: 16px;
        }

        :deep(img) {
          max-width: 100%;
          height: auto;
          border-radius: 8px;
          margin: 16px 0;
        }

        :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
          color: #2c3e50;
          margin: 20px 0 12px 0;
        }

        :deep(ul), :deep(ol) {
          padding-left: 20px;
          margin-bottom: 16px;
        }

        :deep(li) {
          margin-bottom: 8px;
        }

        :deep(blockquote) {
          border-left: 4px solid #667eea;
          padding-left: 16px;
          margin: 16px 0;
          color: #7f8c8d;
          font-style: italic;
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
  .news-detail-page {
    padding: 15px;

    .header-actions {
      margin-bottom: 20px;
    }

    .news-info-card {
      flex-direction: column;
      text-align: center;
      padding: 20px;

      .news-image {
        width: 100%;
        max-width: 300px;
        margin: 0 auto 20px auto;
      }

      .news-basic-info {
        .news-title {
          font-size: 2rem;
        }

        .news-meta {
          .meta-info {
            justify-content: center;
            flex-direction: column;
            gap: 10px;
          }
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
.news-info-card,
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