<script setup>
import { ref, onMounted, computed } from 'vue'
import http from '@/utils/http'
import { getImageUrl } from '@/utils/system'
import { ElMessage } from 'element-plus'
import { Search, View, Refresh } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const initParams = {
  pageNum: 1,
  pageSize: 12,
  title: "",
  type: "",
  status: 1, // 只显示启用状态的新闻
}

// 搜索条件
const params = ref({ ...initParams })
// 表格数据
const newsList = ref([])
// 表格数据总数
const total = ref(0)
// 加载状态
const loading = ref(false)

// 新闻类型选项
const typeOptions = [
  { label: "全部", value: "" },
  { label: "生态保护", value: "生态保护" },
  { label: "环境治理", value: "环境治理" },
  { label: "野生动物保护", value: "野生动物保护" },
  { label: "湿地保护", value: "湿地保护" },
  { label: "科研成果", value: "科研成果" },
  { label: "政策法规", value: "政策法规" },
]

// 获取新闻数据
const getNewsList = async () => {
  loading.value = true
  try {
    const res = await http.get(
      `/conservationNews/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&title=${params.value.title}&type=${params.value.type}&status=${params.value.status}`
    )
    if (res.code === 200) {
      newsList.value = res.data.records || []
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取生态资讯失败:', error)
    ElMessage.error('获取生态资讯失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = async () => {
  params.value.pageNum = 1
  await getNewsList()
}

// 重置
const handleReset = () => {
  params.value = { ...initParams }
  getNewsList()
}

// 查看详情
const handleViewDetail = (news) => {
  // 跳转到详情页面
  router.push({
    path: '/front/conservation-news-detail',
    query: { id: news.id }
  })
}

// 分页处理
const handleSizeChange = (val) => {
  params.value.pageSize = val
  params.value.pageNum = 1
  getNewsList()
}

const handleCurrentChange = (val) => {
  params.value.pageNum = val
  getNewsList()
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString('zh-CN')
}

onMounted(async () => {
  await getNewsList()
})
</script>

<template>
  <div class="conservation-news-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">生态资讯</h1>
        <p class="page-description">了解最新的生态保护动态与环境治理成果</p>
      </div>

      <!-- 搜索区域 -->
      <div class="search-section">
        <div class="search-container">
          <el-input
            v-model="params.title"
            placeholder="搜索资讯标题..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-select
            v-model="params.type"
            placeholder="选择类型"
            clearable
            style="width: 200px;"
          >
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <el-button type="primary" :icon="Search" @click="handleSearch">
            搜索
          </el-button>
          <el-button :icon="Refresh" @click="handleReset">
            重置
          </el-button>
        </div>
      </div>

      <!-- 页面内容 -->
      <div class="page-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="news-grid">
            <div v-for="n in 12" :key="n" class="news-skeleton">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
                  <div style="padding: 16px;">
                    <el-skeleton-item variant="h3" style="width: 60%;" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-top: 8px;" />
                    <el-skeleton-item variant="text" style="width: 80%; margin-top: 8px;" />
                  </div>
                </template>
              </el-skeleton>
            </div>
          </div>
        </div>

        <!-- 新闻列表 -->
        <div v-else-if="newsList.length > 0" class="news-grid">
          <div
            v-for="news in newsList"
            :key="news.id"
            class="news-card"
            @click="handleViewDetail(news)"
          >
            <div class="news-image">
              <el-image
                :src="news.imgUrl ? getImageUrl(news.imgUrl) : '/images/default-news.jpg'"
                :alt="news.title"
                fit="cover"
                lazy
              >
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="news-type-tag">{{ news.type }}</div>
            </div>
            <div class="news-content">
              <h3 class="news-title">{{ news.title }}</h3>
              <p class="news-intro">{{ news.intro }}</p>
              <div class="news-meta">
                <div class="meta-left">
                  <span class="news-source">{{ news.source || '官方' }}</span>
                  <span class="news-time">{{ formatTime(news.createTime) }}</span>
                </div>
                <div class="meta-right">
                  <span class="view-count">
                    <el-icon><View /></el-icon>
                    {{ news.viewCount || 0 }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty 
            :description="params.title ? '未找到相关资讯信息' : '暂无生态资讯'"
            :image-size="120"
          >
            <template #image>
              <div class="empty-icon">📰</div>
            </template>
            <el-button v-if="params.title" type="primary" @click="handleReset">
              查看全部
            </el-button>
          </el-empty>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination-container">
          <el-pagination
            v-model:current-page="params.pageNum"
            v-model:page-size="params.pageSize"
            :page-sizes="[12, 24, 36, 48]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.conservation-news-page {
  min-height: calc(100vh - 70px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);

  .page-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 40px 20px;
  }

  .page-header {
    text-align: center;
    margin-bottom: 40px;

    .page-title {
      font-size: 36px;
      font-weight: 700;
      color: #2c3e50;
      margin: 0 0 16px 0;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    .page-description {
      font-size: 18px;
      color: #7f8c8d;
      margin: 0 0 24px 0;
      line-height: 1.6;
    }
  }

  .search-section {
    margin-bottom: 32px;

    .search-container {
      display: flex;
      justify-content: center;
      gap: 12px;
      align-items: center;
      flex-wrap: wrap;

      .search-input {
        width: 320px;
        max-width: 100%;
      }

      :deep(.el-input__wrapper) {
        border-radius: 25px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      }

      .el-button {
        border-radius: 25px;
        padding: 12px 24px;
        font-weight: 600;
      }
    }
  }

  .page-content {
    .news-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 24px;
      margin-bottom: 40px;
    }

    .news-skeleton {
      background: white;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .loading-container {
      .news-grid {
        animation: pulse 1.5s ease-in-out infinite;
      }
    }

    .empty-state {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 400px;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

      .empty-icon {
        font-size: 64px;
        margin-bottom: 16px;
      }

      :deep(.el-empty__description) {
        color: #7f8c8d;
        font-size: 16px;
      }
    }

    .pagination-container {
      display: flex;
      justify-content: center;
      margin-top: 40px;
      padding: 24px;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 16px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

      :deep(.el-pagination) {
        .el-pager li {
          border-radius: 8px;
          margin: 0 2px;
        }

        .btn-prev,
        .btn-next {
          border-radius: 8px;
        }

        .el-pagination__sizes .el-select .el-input {
          border-radius: 8px;
        }
      }
    }
  }
}

// 新闻卡片样式
.news-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  }

  .news-image {
    position: relative;
    height: 200px;
    overflow: hidden;

    .el-image {
      width: 100%;
      height: 100%;
    }

    .news-type-tag {
      position: absolute;
      top: 12px;
      left: 12px;
      background: linear-gradient(135deg, #667eea, #764ba2);
      color: white;
      padding: 4px 12px;
      border-radius: 16px;
      font-size: 12px;
      font-weight: 500;
    }

    .image-slot {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      background: var(--el-fill-color-light);
      color: var(--el-text-color-secondary);
      font-size: 48px;
    }
  }

  .news-content {
    padding: 20px;

    .news-title {
      font-size: 18px;
      font-weight: 600;
      color: #2c3e50;
      margin: 0 0 12px 0;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .news-intro {
      font-size: 14px;
      color: #7f8c8d;
      line-height: 1.6;
      margin: 0 0 16px 0;
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .news-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 12px;
      color: #7f8c8d;

      .meta-left {
        display: flex;
        gap: 12px;
      }

      .news-source {
        color: #667eea;
        font-weight: 500;
      }

      .view-count {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .conservation-news-page {
    .page-content .news-grid {
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
  }
}

@media (max-width: 768px) {
  .conservation-news-page {
    .page-container {
      padding: 20px 15px;
    }

    .page-header {
      margin-bottom: 30px;

      .page-title {
        font-size: 28px;
      }

      .page-description {
        font-size: 16px;
      }
    }

    .search-section {
      .search-container {
        flex-direction: column;
        gap: 12px;

        .search-input {
          width: 100%;
        }

        .el-button {
          width: 100%;
        }
      }
    }

    .page-content {
      .news-grid {
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
        gap: 16px;
      }

      .pagination-container {
        padding: 16px;

        :deep(.el-pagination) {
          .el-pagination__sizes,
          .el-pagination__jump {
            display: none;
          }
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .conservation-news-page {
    .page-content .news-grid {
      grid-template-columns: 1fr;
      gap: 16px;
    }
  }
}

// 动画效果
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

// 骨架屏动画
:deep(.el-skeleton.is-animated .el-skeleton__item) {
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>