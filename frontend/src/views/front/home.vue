<script setup>
import { ref, onMounted } from 'vue'
import http from '@/utils/http'
import { getImageUrl } from '@/utils/system'
import AnimalCard from '@/components/AnimalCard.vue'
import PostCard from '@/components/PostCard.vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

// 路由实例
const router = useRouter()

// 轮播图数据
const bannerList = ref([])
// 动物数据
const animalList = ref([])
// 帖子数据
const postList = ref([])

// 加载状态
const loading = ref({
  banner: false,
  animal: false,
  post: false
})

// 获取轮播图数据
const getBannerList = async () => {
  loading.value.banner = true
  try {
    const res = await http.get('/banner/list')
    if (res.code === 200) {
      bannerList.value = res.data || []
    }
  } catch (error) {
    console.error('获取轮播图失败:', error)
  } finally {
    loading.value.banner = false
  }
}

// 获取动物数据
const getAnimalList = async () => {
  loading.value.animal = true
  try {
    const res = await http.get('/animal/page?pageNum=1&pageSize=8')
    if (res.code === 200) {
      animalList.value = res.data.records || []
    }
  } catch (error) {
    console.error('获取动物数据失败:', error)
  } finally {
    loading.value.animal = false
  }
}

// 获取帖子数据
const getPostList = async () => {
  loading.value.post = true
  try {
    const res = await http.get('/forumPost/page?pageNum=1&pageSize=6')
    if (res.code === 200) {
      postList.value = res.data.records || []
    }
  } catch (error) {
    console.error('获取帖子数据失败:', error)
  } finally {
    loading.value.post = false
  }
}

// 动物卡片点击事件
const handleAnimalClick = (animal) => {
  console.log('点击动物:', animal)
  // 这里可以跳转到动物详情页
}

// 跳转到动物列表页面
const goToAnimalList = () => {
  router.push('/front/animal-list')
}

// 跳转到社区页面
const goToCommunity = () => {
  router.push('/front/community')
}

onMounted(async () => {
  await Promise.all([
    getBannerList(),
    getAnimalList(),
    getPostList()
  ])
})
</script>

<template>
  <div class="home-container">
    <!-- 轮播图区域 -->
    <div class="banner-box">
      <section class="banner-section">
      <el-carousel 
        v-if="bannerList.length > 0" 
        height="600px" 
        :interval="4000"
        indicator-position="outside"
      >
        <el-carousel-item v-for="banner in bannerList" :key="banner.id">
          <div class="banner-item">
            <el-image
              :src="getImageUrl(banner.url)"
              :alt="banner.title"
              fit="cover"
              class="banner-image"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="banner-overlay">
              <h2 class="banner-title">{{ banner.title }}</h2>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      <div v-else-if="loading.banner" class="loading-container">
        <el-skeleton animated>
          <template #template>
            <el-skeleton-item variant="image" style="width: 100%; height: 400px;" />
          </template>
        </el-skeleton>
      </div>
      <div v-else class="empty-banner">
        <el-empty description="暂无轮播图" />
      </div>
    </section>
    </div>

    <!-- 动物展示区域 -->
    <section class="animals-section">
      <div class="section-header">
        <div class="header-left">
          <div class="title-with-icon">
            <el-icon class="section-icon" size="32">
              <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                <path fill="currentColor" d="M832 64H192c-17.7 0-32 14.3-32 32v832c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 32-32V96c0-17.7-14.3-32-32-32zM668 345.9L621.5 312 572 347.4V124h96v221.9zM476 124h96v221.9L525.5 312 476 345.9V124zm-144 0h96v221.9L381.5 312 332 345.9V124zM172 896V128h96v768h-96zm584 0V128h96v768h-96z"/>
              </svg>
            </el-icon>
            <h2 class="section-title">精彩动物</h2>
          </div>
          <p class="section-subtitle">探索奇幻动物园丰富的动物资源</p>
        </div>
        <div class="header-right">
          <el-button type="primary" link @click="goToAnimalList" class="more-btn">
            查看更多
            <el-icon class="ml-1">
              <ArrowRight />
            </el-icon>
          </el-button>
        </div>
      </div>
      
      <div v-if="loading.animal" class="loading-grid">
        <el-skeleton v-for="i in 8" :key="i" animated>
          <template #template>
            <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
            <div style="padding: 14px;">
              <el-skeleton-item variant="h3" style="width: 50%;" />
              <el-skeleton-item variant="text" />
              <el-skeleton-item variant="text" style="width: 30%;" />
            </div>
          </template>
        </el-skeleton>
      </div>
      <div v-else-if="animalList.length > 0" class="animals-grid">
        <AnimalCard
          v-for="animal in animalList"
          :key="animal.id"
          :animal="animal"
          @click="handleAnimalClick"
        />
      </div>
      <div v-else class="empty-content">
        <el-empty description="暂无动物数据" />
      </div>
    </section>

    <!-- 帖子展示区域 -->
    <section class="posts-section">
      <div class="section-header">
        <div class="header-left">
          <div class="title-with-icon">
            <el-icon class="section-icon" size="32">
              <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                <path fill="currentColor" d="M880 112H144c-17.7 0-32 14.3-32 32v736c0 17.7 14.3 32 32 32h736c17.7 0 32-14.3 32-32V144c0-17.7-14.3-32-32-32zM513.1 518.1l-192 161c-5.2 4.4-13.1.7-13.1-6.1v-62.7c0-2.3 1.1-4.6 2.9-6.1L420.7 512l-109.8-92.2a7.63 7.63 0 0 1-2.9-6.1V351c0-6.8 7.9-10.5 13.1-6.1l192 161c3.9 3.2 3.9 9.1 0 12.2zM716 673c0 4.4-3.4 8-7.5 8h-185c-4.1 0-7.5-3.6-7.5-8v-48c0-4.4 3.4-8 7.5-8h185c4.1 0 7.5 3.6 7.5 8v48z"/>
              </svg>
            </el-icon>
            <h2 class="section-title">热门帖子</h2>
          </div>
          <p class="section-subtitle">分享动物体验与心得</p>
        </div>
        <div class="header-right">
          <el-button type="primary" link @click="goToCommunity" class="more-btn">
            查看更多
            <el-icon class="ml-1">
              <ArrowRight />
            </el-icon>
          </el-button>
        </div>
      </div>

      <div v-if="loading.post" class="loading-grid">
        <el-skeleton v-for="i in 6" :key="i" animated>
          <template #template>
            <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
            <div style="padding: 14px;">
              <el-skeleton-item variant="h3" style="width: 60%;" />
              <el-skeleton-item variant="text" />
              <el-skeleton-item variant="text" />
              <el-skeleton-item variant="text" style="width: 40%;" />
            </div>
          </template>
        </el-skeleton>
      </div>
      <div v-else-if="postList.length > 0" class="posts-grid">
        <PostCard
          v-for="post in postList"
          :key="post.id"
          :post="post"
        />
      </div>
      <div v-else class="empty-content">
        <el-empty description="暂无帖子数据" />
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
.home-container {
  min-height: 100vh;
  background: #f5f7fa;
}

// 轮播图区域
.banner-section {
  width: 1200px;
  margin: 0 auto;
  border-radius: 20px;
  overflow: hidden;
  padding-top: 20px;

  .banner-item {
    position: relative;
    width: 100%;
    height: 600px;
    border-radius: 20px;
    overflow: hidden;
  }

  .banner-image {
    width: 100%;
    height: 100%;
  }

  .banner-overlay {
    position: absolute;
    top: 50%; /* 从顶部50%位置开始 */
    left: 50%; /* 从左侧50%位置开始 */
    transform: translate(-50%, -50%); /* 通过平移实现真正居中 */
    // background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
    padding: 40px 60px 30px;
    color: white;
    text-align: center; /* 文本内容居中 */
  }

  .banner-title {
    font-size: 32px;
    font-weight: 600;
    margin: 0;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
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

// 通用区域样式
.animals-section,
.posts-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 60px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 40px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f2f5;
}

.header-left {
  flex: 1;
}

.title-with-icon {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.section-icon {
  color: #409eff;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  filter: drop-shadow(0 2px 4px rgba(64, 158, 255, 0.2));
}

.section-title {
  font-size: 36px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  margin: 0;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.section-subtitle {
  font-size: 16px;
  color: var(--el-text-color-regular);
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
}

.more-btn {
  font-size: 16px;
  font-weight: 500;
  padding: 12px 24px;
  border-radius: 25px;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  color: white;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(64, 158, 255, 0.4);
  }
  
  .el-icon {
    transition: transform 0.3s ease;
  }
  
  &:hover .el-icon {
    transform: translateX(4px);
  }
}

// 网格布局
.animals-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
}

.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

// 加载和空状态
.loading-container,
.empty-banner,
.empty-content {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}

.empty-banner {
  min-height: 400px;
  background: var(--el-fill-color-lighter);
}

// 响应式设计
@media (max-width: 768px) {
  .banner-overlay {
    padding: 20px 30px 20px;
  }

  .banner-title {
    font-size: 24px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .header-right {
    align-self: stretch;
  }
  
  .more-btn {
    width: 100%;
    justify-content: center;
  }

  .section-title {
    font-size: 28px;
  }
  
  .title-with-icon {
    gap: 8px;
  }
  
  .section-icon {
    font-size: 24px !important;
  }

  .animals-grid,
  .posts-grid,
  .loading-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .animals-section,
  .posts-section {
    padding: 0 16px 40px;
  }
}

@media (max-width: 480px) {
  .banner-title {
    font-size: 20px;
  }

  .section-title {
    font-size: 24px;
  }
  
  .section-icon {
    font-size: 20px !important;
  }
  
  .more-btn {
    font-size: 14px;
    padding: 10px 20px;
  }
}
</style>