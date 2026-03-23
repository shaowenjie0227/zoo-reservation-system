<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/http'
import LocationCard from '@/components/LocationCard.vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'

// 搜索参数
const searchParams = ref({
  pageNum: 1,
  pageSize: 12,
  name: ''
})

// 页面数据
const loading = ref(false)
const locationList = ref([])
const total = ref(0)

// 获取动物地点列表
const getLocationList = async () => {
  loading.value = true
  try {
    const res = await http.get(
      `/animalLocation/page?pageNum=${searchParams.value.pageNum}&pageSize=${searchParams.value.pageSize}&name=${searchParams.value.name}`
    )
    if (res.code === 200) {
      locationList.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error('获取动物地点数据失败')
    }
  } catch (error) {
    console.error('获取动物地点数据失败:', error)
    ElMessage.error('获取动物地点数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索功能
const handleSearch = () => {
  searchParams.value.pageNum = 1
  getLocationList()
}

// 重置搜索
const handleReset = () => {
  searchParams.value.name = ''
  searchParams.value.pageNum = 1
  getLocationList()
}

// 处理分页变化
const handleCurrentChange = (page: number) => {
  searchParams.value.pageNum = page
  getLocationList()
}

const handleSizeChange = (size: number) => {
  searchParams.value.pageSize = size
  searchParams.value.pageNum = 1
  getLocationList()
}
// 页面初始化
onMounted(() => {
  getLocationList()
})
</script>

<template>
  <div class="location-list-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">动物园动物地点</h1>
        <p class="page-description">发现动物园最佳动物地点，探索自然生态之美</p>
      </div>

      <!-- 搜索区域 -->
      <div class="search-section">
        <div class="search-container">
          <el-input
            v-model="searchParams.name"
            placeholder="搜索地点名称..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
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
          <div class="location-grid">
            <div v-for="n in 12" :key="n" class="location-skeleton">
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

        <!-- 地点列表 -->
        <div v-else-if="locationList.length > 0" class="location-grid">
          <LocationCard
            v-for="location in locationList"
            :key="location.id"
            :location="location"
          />
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty 
            :description="searchParams.name ? '未找到相关地点信息' : '暂无动物地点数据'"
            :image-size="120"
          >
            <template #image>
              <div class="empty-icon">🏞️</div>
            </template>
            <el-button v-if="searchParams.name" type="primary" @click="handleReset">
              查看全部
            </el-button>
          </el-empty>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination-container">
          <el-pagination
            v-model:current-page="searchParams.pageNum"
            v-model:page-size="searchParams.pageSize"
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
.location-list-page {
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

    .page-stats {
      display: flex;
      justify-content: center;
      gap: 32px;
      flex-wrap: wrap;

      .stat-item {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 12px 20px;
        background: rgba(255, 255, 255, 0.9);
        border-radius: 25px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        backdrop-filter: blur(10px);

        .stat-icon {
          font-size: 20px;
        }

        .stat-text {
          font-size: 14px;
          font-weight: 600;
          color: #34495e;
        }
      }
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
    .location-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 24px;
      margin-bottom: 40px;
    }

    .location-skeleton {
      background: white;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .loading-container {
      .location-grid {
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

// 响应式设计
@media (max-width: 1200px) {
  .location-list-page {
    .page-content .location-grid {
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
  }
}

@media (max-width: 768px) {
  .location-list-page {
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

      .page-stats {
        gap: 16px;

        .stat-item {
          padding: 8px 16px;

          .stat-text {
            font-size: 13px;
          }
        }
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
      .location-grid {
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
  .location-list-page {
    .page-content .location-grid {
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