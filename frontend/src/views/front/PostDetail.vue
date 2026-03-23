
<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Clock, View, StarFilled, Star, Collection, FolderAdd, Delete, ChatDotRound } from '@element-plus/icons-vue'
import http from '@/utils/http'
import useUserStore from "@/stores/userStore";
import { getImageUrl } from '@/utils/system'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const loading = ref(true)
const postDetail = ref(null)
const comments = ref([])
const newComment = ref('')
const commentLoading = ref(false)
const likeLoading = ref(false)
const collectLoading = ref(false)
const isLiked = ref(false)
const isCollected = ref(false)
const replyingTo = ref(null) // 当前回复的评论
const replyContent = ref('') // 回复内容
const replyLoading = ref(false) // 回复加载状态

// 计算属性
const postId = computed(() => route.query.id)
const currentUser = computed(() => userStore.userInfo)

// 获取帖子详情
const getPostDetail = async () => {
  try {
    loading.value = true
    const res = await http.get(`/forumPost/getByIdVO?id=${postId.value}`)
    if (res.code === 200) {
      postDetail.value = res.data
      // 获取评论
      await getComments()
      // 检查点赞和收藏状态
      await checkLikeStatus()
      await checkCollectStatus()
    } else {
      ElMessage.error(res.message || '获取帖子详情失败')
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const getComments = async () => {
  try {
    // 获取所有评论数据，不使用后端分级处理
    const res = await http.get(`/postsComment/getByPostsId?postsId=${postId.value}`)
    if (res.code === 200) {
      // 过滤出当前帖子的评论
      const allComments = res.data || []
      const postComments = allComments.filter(comment => comment.postsId == postId.value)
      
      // 前端处理分级
      comments.value = buildCommentTree(postComments)
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

// 检查点赞状态
const checkLikeStatus = async () => {
  if (!currentUser.value) return
  try {
    const res = await http.get(`/like/checkUserLike?userId=${currentUser.value.id}&type=2&targetId=${postId.value}`)
    if (res.code === 200) {
      isLiked.value = res.data
    }
  } catch (error) {
    console.error('检查点赞状态失败:', error)
  }
}

// 检查收藏状态
const checkCollectStatus = async () => {
  if (!currentUser.value) return
  try {
    const res = await http.get(`/collect/isCollected?userId=${currentUser.value.id}&type=2&targetId=${postId.value}`)
    if (res.code === 200) {
      isCollected.value = res.data
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 切换点赞状态
const toggleLike = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    likeLoading.value = true
    if (isLiked.value) {
      // 取消点赞
      const res = await http.post(`/like/cancelLike?userId=${currentUser.value.id}&type=2&targetId=${postId.value}`)
      if (res.code === 200) {
        isLiked.value = false
        ElMessage.success('取消点赞成功')
      } else {
        ElMessage.error(res.message || '取消点赞失败')
      }
    } else {
      // 新增点赞
      const res = await http.post('/like/add', {
        userId: currentUser.value.id,
        targetId: postId.value,
        type: '2' // 帖子类型
      })
      if (res.code === 200) {
        isLiked.value = true
        ElMessage.success('点赞成功')
      } else {
        ElMessage.error(res.message || '点赞失败')
      }
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    likeLoading.value = false
  }
}

// 切换收藏状态
const toggleCollect = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    collectLoading.value = true
    if (isCollected.value) {
      // 取消收藏
      const res = await http.post(`/collect/cancelCollect?userId=${currentUser.value.id}&type=2&targetId=${postId.value}`)
      if (res.code === 200) {
        isCollected.value = false
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error(res.message || '取消收藏失败')
      }
    } else {
      // 新增收藏
      const res = await http.post('/collect/add', {
        userId: currentUser.value.id,
        targetId: postId.value,
        type: '2' // 帖子类型
      })
      if (res.code === 200) {
        isCollected.value = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error(res.message || '收藏失败')
      }
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    collectLoading.value = false
  }
}

// 提交评论
const submitComment = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  try {
    commentLoading.value = true
    const res = await http.post('/postsComment/add', {
      postsId: postId.value,
      content: newComment.value.trim()
    })
    
    if (res.code === 200) {
      ElMessage.success('评论发表成功')
      newComment.value = ''
      await getComments() // 重新获取评论列表
    } else {
      ElMessage.error(res.message || '评论发表失败')
    }
  } catch (error) {
    console.error('评论发表失败:', error)
    ElMessage.error('评论发表失败')
  } finally {
    commentLoading.value = false
  }
}

// 开始回复评论
const startReply = (comment) => {
  replyingTo.value = comment
  replyContent.value = ''
}

// 取消回复
const cancelReply = () => {
  replyingTo.value = null
  replyContent.value = ''
}

// 提交回复
const submitReply = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  
  try {
    replyLoading.value = true
    const res = await http.post('/postsComment/reply', {
      postsId: postId.value,
      content: replyContent.value.trim(),
      pid: replyingTo.value.id
    })
    
    if (res.code === 200) {
      ElMessage.success('回复发表成功')
      cancelReply()
      await getComments() // 重新获取评论列表
    } else {
      ElMessage.error(res.message || '回复发表失败')
    }
  } catch (error) {
    console.error('回复发表失败:', error)
    ElMessage.error('回复发表失败')
  } finally {
    replyLoading.value = false
  }
}

// 删除评论
const deleteComment = async (commentId) => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await http.get(`/postsComment/del?id=${commentId}`)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      await getComments() // 重新获取评论列表
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评论失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 前端构建评论树形结构（二级结构）
const buildCommentTree = (commentList) => {
  if (!commentList || commentList.length === 0) {
    return []
  }
  
  // 创建用户信息映射，用于显示用户名
  const userMap = new Map()
  
  // 获取所有用户信息（这里简化处理，实际可能需要调用用户接口）
  commentList.forEach(comment => {
    if (!userMap.has(comment.userId)) {
      userMap.set(comment.userId, {
        id: comment.userId,
        userName: comment.userName || '匿名用户'
      })
    }
  })
  
  // 创建ID到评论的映射
  const commentMap = new Map()
  commentList.forEach(comment => {
    comment.children = []
    comment.replyToUser = null // 回复的用户信息
    commentMap.set(comment.id, comment)
  })
  
  // 根评论列表（pid为null或0的评论）
  const rootComments = []
  
  for (const comment of commentList) {
    const pid = comment.pid
    if (!pid || pid === 0) {
      // 根评论
      rootComments.push(comment)
    } else {
      // 子评论，找到父评论
      const parentComment = commentMap.get(pid)
      if (parentComment) {
        // 设置回复的用户信息
        comment.replyToUser = userMap.get(parentComment.userId)
        
        // 只支持二级结构，如果父评论也是子评论，则将当前评论添加到根评论下
        if (parentComment.pid && parentComment.pid !== 0) {
          // 找到根评论
          let rootComment = parentComment
          while (rootComment.pid && rootComment.pid !== 0) {
            rootComment = commentMap.get(rootComment.pid)
            if (!rootComment) break
          }
          if (rootComment) {
            comment.replyToUser = userMap.get(parentComment.userId)
            rootComment.children.push(comment)
          }
        } else {
          // 直接添加到父评论下
          parentComment.children.push(comment)
        }
      }
    }
  }
  
  // 按时间排序
  rootComments.sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
  rootComments.forEach(comment => {
    comment.children.sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
  })
  
  return rootComments
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 页面初始化
onMounted(() => {
  if (postId.value) {
    getPostDetail()
  } else {
    ElMessage.error('帖子ID不存在')
    router.go(-1)
  }
})
</script>


<template>
  <div class="post-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 帖子详情内容 -->
    <div v-else-if="postDetail" class="detail-content">
      <!-- 帖子头部信息 -->
      <div class="post-header">
        <h1 class="post-title">{{ postDetail.title }}</h1>
        <div class="post-meta">
          <div class="meta-left">
            <span class="author">
              <el-icon><User /></el-icon>
              {{ postDetail.userNickName || '匿名用户' }}
            </span>
            <span class="time">
              <el-icon><Clock /></el-icon>
              {{ formatTime(postDetail.createTime) }}
            </span>
            <span class="views">
              <el-icon><View /></el-icon>
              {{ postDetail.viewCount || 0 }} 次浏览
            </span>
          </div>
          <div class="meta-right">
            <!-- 点赞按钮 -->
            <el-button 
              :type="isLiked ? 'danger' : 'default'" 
              @click="toggleLike"
              :loading="likeLoading"
              size="small"
            >
              <el-icon><StarFilled v-if="isLiked" /><Star v-else /></el-icon>
              {{ isLiked ? '取消点赞' : '点赞' }}
            </el-button>
            <!-- 收藏按钮 -->
            <el-button 
              :type="isCollected ? 'warning' : 'default'" 
              @click="toggleCollect"
              :loading="collectLoading"
              size="small"
            >
              <el-icon><Collection v-if="isCollected" /><FolderAdd v-else /></el-icon>
              {{ isCollected ? '取消收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 帖子简介 -->
      <div v-if="postDetail.intro" class="post-intro">
        <p>{{ postDetail.intro }}</p>
      </div>

      <!-- 相关信息 -->
      <div class="post-info">
        <div v-if="postDetail.animalName" class="info-item">
          <span class="label">相关动物：</span>
          <span class="value">{{ postDetail.animalName }}</span>
        </div>
        <div v-if="postDetail.locationName" class="info-item">
          <span class="label">动物地点：</span>
          <span class="value">{{ postDetail.locationName }}</span>
        </div>
        <div v-if="postDetail.observationTime" class="info-item">
          <span class="label">动物时间：</span>
          <span class="value">{{ formatTime(postDetail.observationTime) }}</span>
        </div>
      </div>

      <!-- 帖子图片 -->
      <div v-if="postDetail.imgUrl" class="post-image">
        <el-image 
          :src="getImageUrl(postDetail.imgUrl)" 
          :preview-src-list="[getImageUrl(postDetail.imgUrl)]"
          fit="cover"
          class="detail-img"
        />
      </div>

      <!-- 帖子内容 -->
      <div class="post-content">
        <div class="content-text" v-html="postDetail.content"></div>
      </div>

      <!-- 评论区域 -->
      <div class="comments-section">
        <h3>评论区</h3>
        
        <!-- 发表评论 -->
        <div class="comment-form">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
            maxlength="500"
            show-word-limit
          />
          <div class="comment-actions">
            <el-button type="primary" @click="submitComment" :loading="commentLoading">
              发表评论
            </el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div v-if="comments.length === 0" class="no-comments">
            暂无评论，快来抢沙发吧！
          </div>
          <div v-else>
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-header">
                <span class="comment-author">{{ comment.userName || '匿名用户' }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                <div class="comment-actions">
                  <el-button 
                    type="text" 
                    size="small" 
                    @click="startReply(comment)"
                    :icon="ChatDotRound"
                  >
                    回复
                  </el-button>
                  <el-button 
                    v-if="currentUser && currentUser.id === comment.userId"
                    type="text" 
                    size="small" 
                    @click="deleteComment(comment.id)"
                    :icon="Delete"
                    class="delete-btn"
                  >
                    删除
                  </el-button>
                </div>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              
              <!-- 回复表单 -->
              <div v-if="replyingTo && replyingTo.id === comment.id" class="reply-form">
                <el-input
                  v-model="replyContent"
                  type="textarea"
                  :rows="2"
                  :placeholder="`回复 ${comment.userName || '匿名用户'}...`"
                  maxlength="300"
                  show-word-limit
                />
                <div class="reply-actions">
                  <el-button size="small" @click="cancelReply">取消</el-button>
                  <el-button type="primary" size="small" @click="submitReply" :loading="replyLoading">
                    发表回复
                  </el-button>
                </div>
              </div>
              
              <!-- 子评论列表 -->
              <div v-if="comment.children && comment.children.length > 0" class="sub-comments">
                <div v-for="subComment in comment.children" :key="subComment.id" class="sub-comment-item">
                  <div class="comment-header">
                    <span class="comment-author">{{ subComment.userName || '匿名用户' }}</span>
                    <span v-if="subComment.replyToUser" class="reply-to">
                      回复 <span class="reply-target">@{{ subComment.replyToUser.userName }}</span>
                    </span>
                    <span class="comment-time">{{ formatTime(subComment.createTime) }}</span>
                    <div class="comment-actions">
                      <el-button 
                        type="text" 
                        size="small" 
                        @click="startReply(subComment)"
                        :icon="ChatDotRound"
                      >
                        回复
                      </el-button>
                      <el-button 
                        v-if="currentUser && currentUser.id === subComment.userId"
                        type="text" 
                        size="small" 
                        @click="deleteComment(subComment.id)"
                        :icon="Delete"
                        class="delete-btn"
                      >
                        删除
                      </el-button>
                    </div>
                  </div>
                  <div class="comment-content">{{ subComment.content }}</div>
                  
                  <!-- 子评论回复表单 -->
                  <div v-if="replyingTo && replyingTo.id === subComment.id" class="reply-form">
                    <el-input
                      v-model="replyContent"
                      type="textarea"
                      :rows="2"
                      :placeholder="`回复 ${subComment.userName || '匿名用户'}...`"
                      maxlength="300"
                      show-word-limit
                    />
                    <div class="reply-actions">
                      <el-button size="small" @click="cancelReply">取消</el-button>
                      <el-button type="primary" size="small" @click="submitReply" :loading="replyLoading">
                        发表回复
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else class="error-container">
      <el-empty description="帖子不存在或已被删除" />
      <el-button type="primary" @click="$router.go(-1)">返回上一页</el-button>
    </div>
  </div>
</template>

<style scoped>
.post-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.loading-container {
  padding: 40px;
}

.detail-content {
  padding: 20px 0;
}

.post-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.post-title {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 15px 0;
  line-height: 1.4;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.meta-left span {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #909399;
  font-size: 14px;
}

.meta-right {
  display: flex;
  gap: 10px;
}

.post-intro {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 4px solid #409eff;
}

.post-intro p {
  margin: 0;
  color: #606266;
  font-size: 16px;
  line-height: 1.6;
}

.post-info {
  margin-bottom: 25px;
  padding: 15px;
  background: #fafafa;
  border-radius: 6px;
}

.info-item {
  display: flex;
  margin-bottom: 8px;
  font-size: 14px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-item .label {
  font-weight: bold;
  color: #606266;
  min-width: 80px;
}

.info-item .value {
  color: #303133;
}

.post-image {
  margin-bottom: 25px;
  text-align: center;
}

.detail-img {
  max-width: 100%;
  max-height: 500px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.post-content {
  margin-bottom: 40px;
}

.content-text {
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
  word-wrap: break-word;
}

.content-text :deep(p) {
  margin-bottom: 15px;
}

.content-text :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

.comments-section {
  border-top: 1px solid #ebeef5;
  padding-top: 30px;
}

.comments-section h3 {
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #303133;
}

.comment-form {
  margin-bottom: 30px;
}

.comment-actions {
  margin-top: 10px;
  text-align: right;
}

.comments-list {
  margin-top: 20px;
}

.no-comments {
  text-align: center;
  color: #909399;
  padding: 40px;
  font-size: 16px;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: bold;
  color: #409eff;
}

.comment-time {
  color: #909399;
  font-size: 12px;
}

.comment-actions {
  display: flex;
  gap: 8px;
}

.delete-btn {
  color: #f56c6c !important;
}

.delete-btn:hover {
  color: #f78989 !important;
}

.comment-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 10px;
}

.reply-form {
  margin-top: 10px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

.reply-actions {
  margin-top: 8px;
  text-align: right;
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.reply-to {
  color: #909399;
  font-size: 12px;
  margin: 0 8px;
}

.reply-target {
  color: #409eff;
  font-weight: bold;
}

.sub-comments {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #e4e7ed;
}

.sub-comment-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.sub-comment-item:last-child {
  border-bottom: none;
}

.error-container {
  text-align: center;
  padding: 60px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .post-detail {
    margin: 10px;
    padding: 15px;
  }
  
  .post-title {
    font-size: 24px;
  }
  
  .post-meta {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .meta-left {
    gap: 15px;
  }
  
  .meta-right {
    margin-top: 10px;
  }
  
  .info-item {
    flex-direction: column;
  }
  
  .info-item .label {
    margin-bottom: 4px;
  }
}
</style>