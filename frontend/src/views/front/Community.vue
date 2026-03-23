<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/http'
import PostCard from '@/components/PostCard.vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

// 搜索参数
const searchParams = ref({
  pageNum: 1,
  pageSize: 9,
  title: '',
  categoryId: ''
})

// 页面数据
const loading = ref(false)
const postList = ref([])
const categoryList = ref([])
const total = ref(0)
const router = useRouter();

// 获取分类列表
const getCategoryList = async () => {
  const res = await http.get("/postCategory/list");
  if (res.code === 200) {
    categoryList.value = res.data;
  }
};

// 获取帖子列表
const getPostList = async () => {
  try {
    loading.value = true
    const res = await http.get(`/forumPost/page?pageNum=${searchParams.value.pageNum}&pageSize=${searchParams.value.pageSize}&title=${searchParams.value.title}&categoryId=${searchParams.value.categoryId || ''}`)
    if (res.code === 200) {
      postList.value = res.data.records
      total.value = res.data.total
    } else {
      ElMessage.error('获取帖子列表失败')
    }
  } catch (error) {
    console.error('获取帖子列表失败:', error)
    ElMessage.error('获取帖子列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = async () => {
  searchParams.value.pageNum = 1
  await getPostList()
}

// 重置搜索
const handleReset = () => {
  searchParams.value.title = ''
  searchParams.value.categoryId = ''
  searchParams.value.pageNum = 1
  getPostList()
}

// 分页处理
const handleCurrentChange = (page: number) => {
  searchParams.value.pageNum = page
  getPostList()
}

const handleSizeChange = (size: number) => {
  searchParams.value.pageSize = size
  searchParams.value.pageNum = 1
  getPostList()
}

// 发布帖子
const handlePublish = () => {
  router.push('/front/posts/publish')
}

// 页面初始化
onMounted(async () => {
  await getCategoryList()
  await getPostList()
})
</script>

<template>
  <div class="community-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">动物社区</h1>
        <p class="page-description">分享动物经验，交流动物知识，记录美好的动物时光</p>
        <div class="stats-info">
          <span class="stat-item">
            <el-icon><Document /></el-icon>
            共 {{ total }} 篇帖子
          </span>
        </div>
      </div>

      <!-- 搜索区域 -->
      <div class="search-section">
        <div class="search-form">
          <el-input
            v-model="searchParams.title"
            placeholder="搜索帖子标题..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <el-select 
            v-model="searchParams.categoryId" 
            placeholder="选择分类" 
            clearable 
            class="search-select"
            @change="handleSearch"
          >
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>

          <el-button type="primary" :icon="Search" @click="handleSearch">
            搜索
          </el-button>
          <el-button :icon="Refresh" @click="handleReset">
            重置
          </el-button>
          <el-button type="primary" :icon="Plus" @click="handlePublish">发布</el-button>
        </div>
      </div>

      <!-- 页面内容 -->
      <div class="page-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="post-skeleton" v-for="i in 6" :key="i">
            <el-skeleton animated>
              <template #template>
                <div class="skeleton-card">
                  <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
                  <div style="padding: 16px;">
                    <el-skeleton-item variant="h3" style="width: 80%;" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-top: 8px;" />
                    <el-skeleton-item variant="text" style="width: 60%; margin-top: 8px;" />
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
        </div>

        <!-- 帖子列表 -->
        <div v-else-if="postList.length > 0" class="post-grid">
          <PostCard
            v-for="post in postList"
            :key="post.id"
            :post="post"
          />
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty description="暂无帖子数据">
            <template #image>
              <el-icon size="100" color="#dcdfe6">
                <Document />
              </el-icon>
            </template>
          </el-empty>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination-container">
          <el-pagination
            v-model:current-page="searchParams.pageNum"
            v-model:page-size="searchParams.pageSize"
            :page-sizes="[6, 12, 24, 48]"
            layout="sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.community-page {
  min-height: calc(100vh - 70px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);

  .page-container {
    max-width: 1200px;
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
      margin: 0 0 20px 0;
      line-height: 1.6;
    }

    .stats-info {
      display: flex;
      justify-content: center;
      gap: 20px;

      .stat-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 14px;
        color: #95a5a6;
        background: rgba(255, 255, 255, 0.8);
        padding: 8px 16px;
        border-radius: 20px;
        backdrop-filter: blur(10px);
      }
    }
  }

  .search-section {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 24px;
    margin-bottom: 32px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    backdrop-filter: blur(10px);

    .search-form {
      display: flex;
      gap: 12px;
      align-items: center;
      justify-content: center;
      flex-wrap: wrap;

      .search-input {
        width: 300px;
        min-width: 200px;
      }

      .search-select {
        width: 180px;
      }
    }
  }

  .page-content {
    .loading-container {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 24px;

      .post-skeleton {
        .skeleton-card {
          background: white;
          border-radius: 12px;
          overflow: hidden;
          box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        }
      }
    }

    .post-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 24px;
      margin-bottom: 40px;

      // 骨架屏动画
      @keyframes skeleton-loading {
        0% {
          background-position: -200px 0;
        }
        100% {
          background-position: calc(200px + 100%) 0;
        }
      }
    }

    .empty-state {
      background: rgba(255, 255, 255, 0.9);
      border-radius: 16px;
      padding: 60px 40px;
      text-align: center;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      backdrop-filter: blur(10px);
    }

    .pagination-container {
      display: flex;
      justify-content: center;
      margin-top: 40px;
      padding: 20px;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 16px;
      backdrop-filter: blur(10px);
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

      :deep(.el-pagination) {
        .el-pager li {
          background: transparent;
          border-radius: 8px;
          transition: all 0.3s ease;

          &:hover {
            background: var(--el-color-primary-light-9);
          }

          &.is-active {
            background: var(--el-color-primary);
            color: white;
          }
        }

        .btn-prev,
        .btn-next {
          background: transparent;
          border-radius: 8px;
          transition: all 0.3s ease;

          &:hover {
            background: var(--el-color-primary-light-9);
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 1024px) {
  .community-page {
    .page-container {
      padding: 30px 15px;
    }

    .page-content {
      .post-grid {
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;
      }

      .loading-container {
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;
      }
    }
  }
}

@media (max-width: 768px) {
  .community-page {
    .page-container {
      padding: 20px 15px;
    }

    .page-header {
      .page-title {
        font-size: 28px;
      }

      .page-description {
        font-size: 16px;
      }

      .stats-info {
        flex-direction: column;
        align-items: center;
        gap: 12px;
      }
    }

    .search-section {
      padding: 20px;

      .search-form {
        flex-direction: column;
        align-items: stretch;

        .search-input {
          width: 100%;
        }

        .el-button {
          width: 100%;
        }
      }
    }

    .page-content {
      .post-grid {
        grid-template-columns: 1fr;
        gap: 16px;
      }

      .loading-container {
        grid-template-columns: 1fr;
        gap: 16px;
      }

      .empty-state {
        padding: 40px 20px;
      }

      .pagination-container {
        padding: 15px;

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
  .community-page {
    .page-header {
      .page-title {
        font-size: 24px;
      }

      .page-description {
        font-size: 14px;
      }
    }

    .search-section {
      padding: 16px;
    }
  }
}
</style>