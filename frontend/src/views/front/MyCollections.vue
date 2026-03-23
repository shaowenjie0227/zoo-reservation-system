<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import useUserStore from "@/stores/userStore";
import http from '@/utils/http'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Star, Location, ChatDotRound, User, Key, Clock, Sunny } from '@element-plus/icons-vue'

// 用户状态管理
const userStore = useUserStore()
const router = useRouter()

// 当前用户信息
const currentUser = computed(() => {
  try {
    return userStore.userInfo ?? null
  } catch {
    return null
  }
})

const isLoggedIn = computed(() => {
  return userStore.token && currentUser.value?.id
})

// 页面数据
const loading = ref(false)
const collections = ref([])
const total = ref(0)
const currentType = ref('animal')

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 12
})

// 类型选项
const typeOptions = [
  { value: 'animal', label: '动物', icon: Star, color: '#67C23A' },
  { value: 'location', label: '地点', icon: Location, color: '#409EFF' },
  { value: 'post', label: '帖子', icon: ChatDotRound, color: '#E6A23C' },
  { value: 'travel', label: '景点', icon: Sunny, color: '#F56C6C' }
]

// 获取收藏列表
const getCollections = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const res = await http.get('/collect/page', {
      params: {
        pageNum: pageParams.value.pageNum,
        pageSize: pageParams.value.pageSize,
        type: currentType.value
      }
    })
    
    if (res.code === 200) {
      collections.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error('获取收藏列表失败')
    }
  } catch (error) {
    console.error('获取收藏列表失败:', error)
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 切换类型
const handleTypeChange = (type: string) => {
  currentType.value = type
  pageParams.value.pageNum = 1
  getCollections()
}

// 取消收藏
const handleCancelCollect = async (item: any) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await http.post('/collect/cancelCollect', null, {
      params: {
        userId: currentUser.value.id,
        type: currentType.value,
        targetId: item.targetId
      }
    })

    if (res.code === 200) {
      ElMessage.success('取消收藏成功')
      getCollections()
    } else {
      ElMessage.error('取消收藏失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消收藏失败:', error)
      ElMessage.error('取消收藏失败')
    }
  }
}

// 跳转到详情页面
const handleItemClick = (item: any) => {
  if (currentType.value === 'animal') {
    router.push(`/front/animal-detail/${item.targetId}`)
  } else if (currentType.value === 'location') {
    router.push({
      path: '/front/location-detail',
      query: { id: item.targetId }
    })
  } else if (currentType.value === 'travel') {
    router.push({
      path: '/front/travel-detail',
      query: { id: item.targetId }
    })
  } else if (currentType.value === 'post') {
    // TODO: 跳转到帖子详情页面
    ElMessage.info('帖子详情页面开发中')
  }
}

// 分页处理
const handleCurrentChange = (page: number) => {
  pageParams.value.pageNum = page
  getCollections()
}

const handleSizeChange = (size: number) => {
  pageParams.value.pageSize = size
  pageParams.value.pageNum = 1
  getCollections()
}

// 页面初始化
onMounted(() => {
  if (isLoggedIn.value) {
    getCollections()
  } else {
    ElMessage.warning('请先登录')
    router.push('/login')
  }
})
</script>

<template>
  <div class="my-collections-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">我的收藏</h1>
        <p class="page-description">管理您收藏的动物、地点和帖子</p>
      </div>

      <!-- 类型切换标签 -->
      <div class="type-tabs">
        <div
          v-for="option in typeOptions"
          :key="option.value"
          class="type-tab"
          :class="{ active: currentType === option.value }"
          :style="{ '--tab-color': option.color }"
          @click="handleTypeChange(option.value)"
        >
          <el-icon class="tab-icon">
            <component :is="option.icon" />
          </el-icon>
          <span class="tab-label">{{ option.label }}</span>
          <span class="tab-count" v-if="currentType === option.value">({{ total }})</span>
        </div>
      </div>

      <!-- 页面内容 -->
      <div class="page-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="collections-grid">
            <div v-for="n in 12" :key="n" class="collection-skeleton">
              <el-skeleton animated>
                <template #template>
                  <div style="padding: 24px;">
                    <el-skeleton-item variant="h3" style="width: 60%;" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-top: 12px;" />
                    <el-skeleton-item variant="text" style="width: 80%; margin-top: 8px;" />
                    <el-skeleton-item variant="text" style="width: 90%; margin-top: 8px;" />
                  </div>
                </template>
              </el-skeleton>
            </div>
          </div>
        </div>

        <!-- 收藏列表 -->
        <div v-else-if="collections.length > 0" class="collections-grid">
          <div
            v-for="item in collections"
            :key="item.id"
            class="collection-card"
            @click="handleItemClick(item)"
          >
            <div class="card-content">
              <div class="card-header">
                <h3 class="card-title">{{ item.targetName || '未知名称' }}</h3>
                <span class="card-type">{{ item.typeName || currentType }}</span>
              </div>
              
              <div class="card-info">
                <div class="info-item">
                  <el-icon class="info-icon">
                    <component :is="typeOptions.find(t => t.value === currentType)?.icon" />
                  </el-icon>
                  <span class="info-label">类型:</span>
                  <span>{{ item.typeName || '未知类型' }}</span>
                </div>
                <div class="info-item">
                  <el-icon class="info-icon"><User /></el-icon>
                  <span class="info-label">收藏者:</span>
                  <span>{{ item.userNickName || '未知用户' }}</span>
                </div>
                <div class="info-item">
                  <el-icon class="info-icon"><Key /></el-icon>
                  <span class="info-label">目标ID:</span>
                  <span>{{ item.targetId }}</span>
                </div>
              </div>

              <div class="card-meta">
                <div class="meta-row">
                  <span class="collect-time">
                    <el-icon><Clock /></el-icon>
                    收藏于 {{ item.createTime }}
                  </span>
                </div>
              </div>

              <div class="card-actions">
                <el-button
                  type="danger"
                  :icon="Delete"
                  size="small"
                  class="cancel-btn"
                  @click.stop="handleCancelCollect(item)"
                >
                  取消收藏
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty 
            :description="`暂无${typeOptions.find(t => t.value === currentType)?.label}收藏`"
            :image-size="120"
          >
            <template #image>
              <div class="empty-icon">
                <el-icon size="60" :color="typeOptions.find(t => t.value === currentType)?.color">
                  <component :is="typeOptions.find(t => t.value === currentType)?.icon" />
                </el-icon>
              </div>
            </template>
            <el-button type="primary" @click="router.push('/front/home')">
              去发现更多内容
            </el-button>
          </el-empty>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination-container">
          <el-pagination
            v-model:current-page="pageParams.pageNum"
            v-model:page-size="pageParams.pageSize"
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
.my-collections-page {
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

  .type-tabs {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-bottom: 32px;
    flex-wrap: wrap;

    .type-tab {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 24px;
      background: white;
      border-radius: 25px;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      border: 2px solid transparent;
      font-weight: 600;
      backdrop-filter: blur(10px);

      .tab-icon {
        font-size: 18px;
        color: #7f8c8d;
        transition: color 0.3s ease;
      }

      .tab-label {
        font-size: 16px;
        font-weight: 500;
        color: #2c3e50;
        transition: color 0.3s ease;
      }

      .tab-count {
        font-size: 14px;
        color: var(--tab-color);
        font-weight: 600;
      }

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
      }

      &.active {
        border-color: var(--tab-color);
        background: linear-gradient(135deg, var(--tab-color), var(--tab-color));
        background-size: 100% 2px;
        background-repeat: no-repeat;
        background-position: bottom;
        background-color: white;

        .tab-icon {
          color: var(--tab-color);
        }

        .tab-label {
          color: var(--tab-color);
          font-weight: 600;
        }
      }
    }
  }

  .page-content {
    .loading-container,
    .collections-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
      gap: 24px;
      margin-bottom: 40px;
    }

    .collection-card {
      background: white;
      border-radius: 16px;
      border: 1px solid #e1e8ed;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
      transition: all 0.3s ease;
      cursor: pointer;
      overflow: hidden;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
        border-color: #409eff;
      }

      .card-content {
        padding: 24px;

        .card-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 16px;

          .card-title {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            line-height: 1.4;
            flex: 1;
            margin: 0;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }

          .card-type {
            font-size: 12px;
            color: #fff;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 4px 12px;
            border-radius: 12px;
            margin-left: 12px;
            white-space: nowrap;
            font-weight: 500;
          }
        }

        .card-info {
          display: flex;
          flex-direction: column;
          gap: 8px;
          margin-bottom: 16px;

          .info-item {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 13px;
            color: #7f8c8d;
            background: #f8f9fa;
            padding: 8px 12px;
            border-radius: 8px;

            .info-icon {
              font-size: 14px;
              color: #409eff;
              flex-shrink: 0;
            }

            .info-label {
              font-weight: 500;
              color: #34495e;
              min-width: 50px;
            }

            span:last-child {
              color: #2c3e50;
              font-weight: 500;
            }
          }
        }

        .card-meta {
          margin-bottom: 16px;

          .meta-row {
            display: flex;
            align-items: center;
            gap: 8px;

            .collect-time {
              display: flex;
              align-items: center;
              gap: 6px;
              font-size: 12px;
              color: #95a5a6;

              .el-icon {
                font-size: 14px;
              }
            }
          }
        }

        .card-actions {
          display: flex;
          justify-content: flex-end;
          align-items: center;
          padding-top: 12px;
          border-top: 1px solid #f0f0f0;

          .cancel-btn {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
            border: none;
            color: white;
            font-weight: 500;
            border-radius: 8px;
            padding: 8px 16px;
            
            &:hover {
              background: linear-gradient(135deg, #ff5252 0%, #d32f2f 100%);
              transform: translateY(-1px);
            }
          }
        }
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
        margin-bottom: 20px;
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
  .my-collections-page {
    .page-content .collections-grid {
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
  }
}

@media (max-width: 768px) {
  .my-collections-page {
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

    .type-tabs {
      gap: 8px;
      margin-bottom: 24px;

      .type-tab {
        padding: 8px 16px;
        font-size: 14px;
      }
    }

    .page-content {
      .collections-grid {
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
  .my-collections-page {
    .page-content .collections-grid {
      grid-template-columns: 1fr;
      gap: 16px;
    }

    .type-tabs {
      .type-tab {
        padding: 6px 12px;
        font-size: 13px;
        flex: 1;
        justify-content: center;
        min-width: 0;
      }
    }
  }
}
</style>