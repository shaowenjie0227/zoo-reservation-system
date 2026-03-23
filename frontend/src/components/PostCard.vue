<script setup>
import { computed } from 'vue'
import { getImageUrl } from '@/utils/system'
import { useRouter } from 'vue-router'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const handleClick = () => {
  router.push({
    path: "/front/posts/detail",
    query: {
      id: props.post.id
    }
  })
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}
</script>

<template>
  <div class="post-card" @click="handleClick">
    <div class="post-image" v-if="post.imgUrl">
      <el-image
        :src="getImageUrl(post.imgUrl)"
        :alt="post.title"
        fit="cover"
        :lazy="true"
      >
        <template #error>
          <div class="image-slot">
            <el-icon>
              <Picture />
            </el-icon>
          </div>
        </template>
      </el-image>
    </div>
    <div class="post-content">
      <h3 class="post-title">{{ post.title }}</h3>
      <p class="post-intro">{{ post.intro }}</p>
      <div class="post-meta">
        <div class="meta-left">
          <span class="author" v-if="post.userNickName">
            <el-icon><User /></el-icon>
            {{ post.userNickName }}
          </span>
          <span class="animal-name" v-if="post.animalName">
            <el-icon><Compass /></el-icon>
            {{ post.animalName }}
          </span>
          <span class="location" v-if="post.locationName">
            <el-icon><Location /></el-icon>
            {{ post.locationName }}
          </span>
        </div>
        <div class="meta-right">
          <span class="view-count" v-if="post.viewCount">
            <el-icon><View /></el-icon>
            {{ post.viewCount }}
          </span>
          <span class="date">
            {{ formatDate(post.observationTime || post.createTime) }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.post-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
}

.post-image {
  width: 100%;
  height: 200px;
  overflow: hidden;

  :deep(.el-image) {
    width: 100%;
    height: 100%;
  }

  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: var(--el-fill-color-light);
    color: var(--el-text-color-secondary);
    font-size: 24px;
  }
}

.post-content {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 8px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-intro {
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.5;
  margin: 0 0 16px 0;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  font-size: 12px;
  color: var(--el-text-color-secondary);
  gap: 12px;
}

.meta-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;

  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.meta-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;

  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.el-icon {
  font-size: 14px;
}

.author {
  color: var(--el-color-primary);
  font-weight: 500;
}

.animal-name {
  color: var(--el-color-success);
}

.location {
  color: var(--el-color-warning);
}

.view-count {
  color: var(--el-text-color-secondary);
}

.date {
  color: var(--el-text-color-placeholder);
}
</style>