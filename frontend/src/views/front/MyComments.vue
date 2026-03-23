<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import useUserStore from "@/stores/userStore";
import http from '@/utils/http'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatDotRound } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/system';

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
const comments = ref([])
const total = ref(0)

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 12
})

// 获取评论列表
const getComments = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const res = await http.get('/postsComment/page', {
      params: {
        pageNum: pageParams.value.pageNum,
        pageSize: pageParams.value.pageSize,
        userId: currentUser.value.id
      }
    })
    
    if (res.code === 200) {
      comments.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error('获取评论列表失败')
    }
  } catch (error) {
    console.error('获取评论列表失败:', error)
    ElMessage.error('获取评论列表失败')
  } finally {
    loading.value = false
  }
}

// 跳转到帖子详情页面
const handlePostClick = (comment: any) => {
  if (comment.postsId) {
    router.push({
      path: '/front/post-detail',
      query: { id: comment.postsId }
    })
  } else {
    ElMessage.warning('帖子信息不完整')
  }
}

// 删除评论
const delRow = async (id: number) => {
  if (!id) {
    ElMessage.warning('评论ID不存在')
    return
  }

  await ElMessageBox.confirm('确定删除该评论吗？', '删除评论', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  })

  try {
    const res = await http.get('/postsComment/del?id=' + id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getComments()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    console.error('删除评论失败:', error)
    ElMessage.error('删除评论失败')
  }
}

// 分页处理
const handleCurrentChange = (page: number) => {
  pageParams.value.pageNum = page
  getComments()
}

const handleSizeChange = (size: number) => {
  pageParams.value.pageSize = size
  pageParams.value.pageNum = 1
  getComments()
}

// 页面初始化
onMounted(() => {
  if (isLoggedIn.value) {
    getComments()
  } else {
    ElMessage.warning('请先登录')
    router.push('/login')
  }
})
</script>

<template>
  <div class="my-comments-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">
          <el-icon class="title-icon">
            <ChatDotRound />
          </el-icon>
          我的评论
        </h1>
        <p class="page-description">查看您发表的所有评论</p>
      </div>

      <!-- 页面内容 -->
      <div class="page-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="comments-grid">
            <div v-for="n in 12" :key="n" class="comment-skeleton">
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

        <!-- 评论列表 -->
        <div v-else-if="comments.length > 0" class="comments-grid">
          <div
            v-for="comment in comments"
            :key="comment.id"
            class="comment-card"
          >
            <div class="card-content">
              <div class="card-header">
                <h3 class="card-title" @click="handlePostClick(comment)">
                  {{ comment.postsTitle || '未知帖子' }}
                </h3>
                <span class="card-type">评论</span>
              </div>
              
              <div class="comment-content">
                <p class="comment-text">{{ comment.content || '暂无内容' }}</p>
              </div>
              
              <div class="card-info">
                <div class="info-item">
                  <span class="info-icon">👤</span>
                  <span class="info-label">评论用户:</span>
                  <span>{{ comment.userName || currentUser?.nickName || '未知用户' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-icon">📝</span>
                  <span class="info-label">帖子标题:</span>
                  <span class="post-title-link" @click="handlePostClick(comment)">
                    {{ comment.postsTitle || '未知帖子' }}
                  </span>
                </div>
                <div class="info-item" v-if="comment.pid && comment.pid > 0">
                  <span class="info-icon">💬</span>
                  <span class="info-label">回复类型:</span>
                  <span>回复评论</span>
                </div>
                <div class="info-item" v-else>
                  <span class="info-icon">💬</span>
                  <span class="info-label">评论类型:</span>
                  <span>主评论</span>
                </div>
                <div class="info-item">
                  <span class="info-icon">📅</span>
                  <span class="info-label">评论时间:</span>
                  <span>{{ formatDate(comment.createTime) }}</span>
                </div>
              </div>
              <div style="text-align: right;">
                <el-button type="danger" @click="delRow(comment.id)">删除</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty 
            description="暂无评论"
            :image-size="120"
          >
            <template #image>
              <div class="empty-icon">
                <el-icon size="60" color="#E6A23C">
                  <ChatDotRound />
                </el-icon>
              </div>
            </template>
            <el-button type="primary" @click="router.push('/front/community')">
              去社区发表评论
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
.my-comments-page {
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
      background: linear-gradient(135deg, #E6A23C 0%, #F56C6C 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;

      .title-icon {
        color: #E6A23C;
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

  .page-content {
    .loading-container,
    .comments-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 24px;
      margin-bottom: 40px;
    }

    .comment-card {
      background: white;
      border-radius: 16px;
      overflow: hidden;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      border: 1px solid #f0f0f0;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
        border-color: #E6A23C;
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
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0;
            line-height: 1.4;
            flex: 1;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            cursor: pointer;
            transition: color 0.3s ease;

            &:hover {
              color: #E6A23C;
            }
          }

          .card-type {
            background: linear-gradient(135deg, #E6A23C, #F56C6C);
            color: white;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            white-space: nowrap;
            flex-shrink: 0;
          }
        }

        .comment-content {
          margin-bottom: 16px;

          .comment-text {
            font-size: 15px;
            color: #2c3e50;
            margin: 0;
            line-height: 1.6;
            display: -webkit-box;
            -webkit-line-clamp: 4;
            -webkit-box-orient: vertical;
            overflow: hidden;
            background: #f8f9fa;
            padding: 12px 16px;
            border-radius: 8px;
            border-left: 4px solid #E6A23C;
          }
        }

        .card-info {
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

            .post-title-link {
              color: #E6A23C !important;
              cursor: pointer;
              transition: color 0.3s ease;

              &:hover {
                color: #F56C6C !important;
                text-decoration: underline;
              }
            }
          }
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
  .my-comments-page {
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

    .comments-grid {
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 16px;
    }

    .comment-card {
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
  .my-comments-page {
    .comments-grid {
      grid-template-columns: 1fr;
    }

    .comment-card {
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