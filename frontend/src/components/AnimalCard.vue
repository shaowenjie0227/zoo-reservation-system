<script setup>
import { computed } from 'vue'
import { getImageUrl } from '@/utils/system'
import { useRouter } from 'vue-router'

const props = defineProps({
  animal: {
    type: Object,
    required: true
  }
})
const router = useRouter()

const handleClick = () => {
  router.push({
    path: "/front/animal-detail",
    query: {
      id: props.animal.id
    }
  })
}

const displayTags = computed(() => {
  if (!props.animal.tagsList) return []
  try {
    return props.animal.tagsList
  } catch {
    return props.animal.tagsList
  }
})

const getTagType = (index) => {
  const types = ['success', 'warning', 'danger', 'info']
  return types[index % types.length]
}

const getTagColor = (index) => {
  const colors = ['#67c23a', '#e6a23c', '#f56c6c', '#909399']
  return colors[index % colors.length]
}
</script>

<template>
  <div class="animal-card" @click="handleClick">
    <div class="animal-image">
      <el-image
        :src="getImageUrl(animal.coverUrl)"
        :alt="animal.name"
        fit="cover"
        :lazy="true"
      >
        <template #error>
          <div class="image-slot">
            <el-icon>
              <Picture />
            </el-icon>
            <span>暂无图片</span>
          </div>
        </template>
        <template #placeholder>
          <div class="image-loading">
            <el-icon class="is-loading">
              <Loading />
            </el-icon>
          </div>
        </template>
      </el-image>
      <div class="image-overlay"></div>
    </div>
    <div class="animal-content">
      <h3 class="animal-name">{{ animal.name }}</h3>
      <p class="animal-intro">{{ animal.intro || animal.description }}</p>
      <div class="animal-tags" v-if="displayTags.length > 0">
        <el-tag
          v-for="(tag, index) in displayTags.slice(0, 3)"
          :key="tag"
          size="small"
          :type="getTagType(index)"
          :color="getTagColor(index)"
          effect="light"
          round
          style="color: white;"
        >
          {{ tag }}
        </el-tag>
        <el-tag
          v-if="displayTags.length > 3"
          size="small"
          type="info"
          effect="plain"
          round
        >
          +{{ displayTags.length - 3 }}
        </el-tag>
      </div>
      <div class="animal-meta">
        <span class="season" v-if="animal.season">
          <el-icon><Calendar /></el-icon>
          {{ animal.season }}
        </span>
        <span class="view-count" v-if="animal.viewCount">
          <el-icon><View /></el-icon>
          {{ animal.viewCount }}次观看
        </span>
      </div>
    </div>
    <div class="card-shine"></div>
  </div>
</template>

<style lang="scss" scoped>
.animal-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  border-radius: 16px;
  box-shadow: 
    0 4px 20px rgba(0, 0, 0, 0.08),
    0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  border: 1px solid rgba(255, 255, 255, 0.8);

  &:hover {
    transform: translateY(-8px) scale(1.02);
    box-shadow: 
      0 20px 40px rgba(0, 0, 0, 0.12),
      0 8px 16px rgba(0, 0, 0, 0.08);
    
    .animal-image {
      .image-overlay {
        opacity: 0.3;
      }
      
      :deep(.el-image img) {
        transform: scale(1.1);
      }
    }
    
    .card-shine {
      opacity: 1;
      transform: translateX(100%);
    }
  }
}

.card-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.4),
    transparent
  );
  opacity: 0;
  transition: all 0.6s ease;
  pointer-events: none;
  z-index: 1;
}

.animal-image {
  width: 100%;
  height: 220px;
  overflow: hidden;
  position: relative;
  border-radius: 16px 16px 0 0;

  :deep(.el-image) {
    width: 100%;
    height: 100%;
    
    img {
      transition: transform 0.4s ease;
    }
  }

  .image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      180deg,
      transparent 0%,
      rgba(0, 0, 0, 0.1) 100%
    );
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: 1;
  }

  .image-slot {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
    color: var(--el-text-color-secondary);
    font-size: 24px;
    gap: 8px;
    
    span {
      font-size: 14px;
      color: var(--el-text-color-placeholder);
    }
  }

  .image-loading {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    color: var(--el-color-primary);
    font-size: 24px;
  }
}

.animal-content {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 2;
}

.animal-name {
  font-size: 20px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  margin: 0 0 12px 0;
  line-height: 1.3;
  background: linear-gradient(135deg, #1f2937 0%, #374151 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.animal-intro {
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 16px 0;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  opacity: 0.8;
}

.animal-tags {
  margin-bottom: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  
  :deep(.el-tag) {
    border: none;
    font-weight: 500;
    font-size: 12px;
    padding: 4px 12px;
    backdrop-filter: blur(10px);
    
    &.el-tag--success {
      background: rgba(103, 194, 58, 0.15);
      color: #67c23a;
    }
    
    &.el-tag--warning {
      background: rgba(230, 162, 60, 0.15);
      color: #e6a23c;
    }
    
    &.el-tag--danger {
      background: rgba(245, 108, 108, 0.15);
      color: #f56c6c;
    }
    
    &.el-tag--info {
      background: rgba(144, 147, 153, 0.15);
      color: #909399;
    }
  }
}

.animal-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: var(--el-text-color-secondary);
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);

  span {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    background: rgba(0, 0, 0, 0.03);
    border-radius: 20px;
    font-weight: 500;
    transition: all 0.2s ease;
    
    &:hover {
      background: rgba(0, 0, 0, 0.06);
      transform: translateY(-1px);
    }
  }

  .el-icon {
    font-size: 16px;
    opacity: 0.7;
  }
  
  .season {
    color: #67c23a;
    
    .el-icon {
      color: #67c23a;
    }
  }
  
  .view-count {
    color: #409eff;
    
    .el-icon {
      color: #409eff;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .animal-card {
    border-radius: 12px;
    
    &:hover {
      transform: translateY(-4px) scale(1.01);
    }
  }
  
  .animal-image {
    height: 180px;
    border-radius: 12px 12px 0 0;
  }
  
  .animal-content {
    padding: 16px;
  }
  
  .animal-name {
    font-size: 18px;
  }
  
  .animal-meta {
    span {
      padding: 4px 8px;
      font-size: 12px;
    }
  }
}
</style>