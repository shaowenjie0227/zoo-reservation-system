<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import useUserStore from "@/stores/userStore";
import http from '@/utils/http'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Star, Location, ChatDotRound, User, Key, Clock, Sunny } from '@element-plus/icons-vue'
import { formatDate, stripHtml } from '@/utils/system';

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
const likes = ref([])
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

// 获取点赞列表
const getLikes = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const res = await http.get('/like/page', {
      params: {
        pageNum: pageParams.value.pageNum,
        pageSize: pageParams.value.pageSize,
        type: currentType.value,
        userId: currentUser.value.id
      }
    })
    
    if (res.code === 200) {
      likes.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error('获取点赞列表失败')
    }
  } catch (error) {
    console.error('获取点赞列表失败:', error)
    ElMessage.error('获取点赞列表失败')
  } finally {
    loading.value = false
  }
}

// 切换类型
const handleTypeChange = (type: string) => {
  currentType.value = type
  pageParams.value.pageNum = 1
  getLikes()
}

// 取消点赞
const handleCancelLike = async (item: any) => {
  try {
    await ElMessageBox.confirm('确定要取消点赞吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await http.post('/like/cancelLike', null, {
      params: {
        userId: currentUser.value.id,
        type: currentType.value,
        targetId: item.targetId
      }
    })

    if (res.code === 200) {
      ElMessage.success('取消点赞成功')
      getLikes()
    } else {
      ElMessage.error('取消点赞失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消点赞失败:', error)
      ElMessage.error('取消点赞失败')
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
  getLikes()
}

const handleSizeChange = (size: number) => {
  pageParams.value.pageSize = size
  pageParams.value.pageNum = 1
  getLikes()
}

// 页面初始化
onMounted(() => {
  if (isLoggedIn.value) {
    getLikes()
  } else {
    ElMessage.warning('请先登录')
    router.push('/login')
  }
})
</script>

<template>
  <div class="my-likes-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">
          <el-icon class="title-icon">
            <Sunny />
          </el-icon>
          我的点赞
        </h1>
        <p class="page-description">查看您点赞过的动物、地点和帖子</p>
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
          <div class="likes-grid">
            <div v-for="n in 12" :key="n" class="like-skeleton">
              <el-skeleton animated>
                <template #template>
                  <div style="padding: 20px;">
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

        <!-- 点赞列表 -->
        <div v-else-if="likes.length > 0" class="likes-grid">
          <div
            v-for="item in likes"
            :key="item.id"
            class="like-card"
            @click="handleItemClick(item)"
          >
            <div class="card-content">
              <div class="card-header">
                <h3 class="card-title">{{ item.targetName || '未知名称' }}</h3>
                <span class="card-type">{{ item.typeName || '未知类型' }}</span>
              </div>
              
              <p class="card-description" v-if="item.description">{{ stripHtml(item.description) }}</p>
              
              <div class="card-info">
                <div class="info-item">
                  <span class="info-icon">👤</span>
                  <span class="info-label">点赞用户:</span>
                  <span>{{ item.userNickName || '未知用户' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-icon">🏷️</span>
                  <span class="info-label">类型:</span>
                  <span>{{ item.typeName || '未知类型' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-icon">🆔</span>
                  <span class="info-label">目标ID:</span>
                  <span>{{ item.targetId }}</span>
                </div>
                <div class="info-item">
                  <span class="info-icon">📅</span>
                  <span class="info-label">点赞时间:</span>
                  <span>{{ formatDate(item.createTime) }}</span>
                </div>
              </div>

              <div class="card-actions">
                <el-button 
                  type="danger" 
                  size="small" 
                  :icon="Delete"
                  @click.stop="handleCancelLike(item)"
                >
                  取消点赞
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty 
            :description="`暂无${typeOptions.find(t => t.value === currentType)?.label}点赞`"
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
.my-likes-page {
  min-height: calc(100vh - 70px);

  .page-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 40px 20px;
  }

  .page-header {
    text-align: center;
    margin-bottom: 40px;

    .page-title {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12px;
      font-size: 36px;
      font-weight: 700;
      color: #2c3e50;
      margin: 0 0 16px 0;
      background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;

      .title-icon {
        color: #ff6b6b;
        font-size: 36px;
      }
    }

    .page-description {
      font-size: 18px;
      color: #7f8c8d;
      margin: 0;
      line-height: 1.6;
    }
  }

  .type-tabs {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-bottom: 40px;
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
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      border: 2px solid transparent;

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
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
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
    .likes-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 24px;
      margin-bottom: 40px;
    }

    .like-card {
      background: white;
      border-radius: 16px;
      overflow: hidden;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      cursor: pointer;
      border: 1px solid #f0f0f0;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
        border-color: #409EFF;
      }

      .card-content {
        padding: 24px;

        .card-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 16px;
          gap: 12px;

          .card-title {
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0;
            line-height: 1.4;
            flex: 1;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }

          .card-type {
            background: linear-gradient(135deg, #409EFF, #67C23A);
            color: white;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            white-space: nowrap;
            flex-shrink: 0;
          }
        }

        .card-description {
          font-size: 14px;
          color: #7f8c8d;
          margin: 0 0 16px 0;
          line-height: 1.6;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
          min-height: 60px;
        }

        .card-info {
          margin-bottom: 20px;

          .info-item {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 8px;
            font-size: 14px;

            .info-icon {
              font-size: 16px;
              width: 20px;
              text-align: center;
              flex-shrink: 0;
            }

            .info-label {
              color: #606266;
              font-weight: 500;
              min-width: 80px;
              flex-shrink: 0;
            }

            span:last-child {
              color: #2c3e50;
              flex: 1;
              word-break: break-all;
            }
          }
        }

        .card-actions {
          display: flex;
          justify-content: flex-end;
          padding-top: 16px;
          border-top: 1px solid #f0f0f0;
        }
      }
    }

    .empty-state {
      text-align: center;
      padding: 60px 20px;

      .empty-icon {
        margin-bottom: 20px;
      }
    }

    .pagination-container {
      display: flex;
      justify-content: center;
      margin-top: 40px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .my-likes-page {
    .page-container {
      padding: 20px 16px;
    }

    .page-header {
      .page-title {
        font-size: 28px;

        .title-icon {
          font-size: 28px;
        }
      }

      .page-description {
        font-size: 16px;
      }
    }

    .type-tabs {
      gap: 12px;

      .type-tab {
        padding: 10px 16px;
        font-size: 14px;
      }
    }

    .likes-grid {
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 16px;
    }

    .like-card {
      .card-content {
        padding: 20px;

        .card-header {
          flex-direction: column;
          align-items: flex-start;
          gap: 8px;

          .card-type {
            align-self: flex-end;
          }
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .my-likes-page {
    .likes-grid {
      grid-template-columns: 1fr;
    }

    .type-tabs {
      .type-tab {
        flex: 1;
        justify-content: center;
        min-width: 0;
      }
    }

    .like-card {
      .card-content {
        padding: 16px;

        .card-info {
          .info-item {
            flex-direction: column;
            align-items: flex-start;
            gap: 4px;

            .info-label {
              min-width: auto;
            }
          }
        }
      }
    }
  }
}
</style>
