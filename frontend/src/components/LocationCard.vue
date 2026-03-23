<script setup>
import { computed } from 'vue'
import { getImageUrl } from '@/utils/system'
import { useRouter } from 'vue-router'

const props = defineProps({
  location: {
    type: Object,
    required: true
  }
})
const router = useRouter()

// 处理图片URL
const imageUrl = computed(() => {
  return props.location.imgUrl ? getImageUrl(props.location.imgUrl) : '/default-location.jpg'
})

// 处理点击事件
const handleClick = () => {
  router.push({
    path: "/front/location-detail",
    query: {
      id: props.location.id
    }
  })
}

// 格式化坐标显示
const formatCoordinate = (lat, lon) => {
  if (!lat || !lon) return '暂无坐标信息'
  return `${parseFloat(lat).toFixed(4)}, ${parseFloat(lon).toFixed(4)}`
}

// 处理常见动物显示
const formatAnimals = (animals) => {
  if (!animals) return '暂无信息'
  const animalList = animals.split(',').slice(0, 3)
  return animalList.join('、') + (animals.split(',').length > 3 ? '...' : '')
}
</script>

<template>
  <div class="location-card" @click="handleClick">
    <div class="card-image">
      <el-image
        :src="imageUrl"
        fit="cover"
        :lazy="true"
        class="image"
      >
        <template #error>
          <div class="image-error">
            <el-icon size="40">
              <Picture />
            </el-icon>
            <p>暂无图片</p>
          </div>
        </template>
      </el-image>
      <div class="image-overlay">
        <div class="season-tag" v-if="location.bestSeason">
          <el-icon><Calendar /></el-icon>
          {{ location.bestSeason }}
        </div>
      </div>
    </div>
    
    <div class="card-content">
      <h3 class="location-name">{{ location.name }}</h3>
      
      <div class="location-info">
        <div class="info-item" v-if="location.description">
          <el-icon><InfoFilled /></el-icon>
          <span class="info-text">{{ location.description }}</span>
        </div>
        
        <div class="info-item">
          <el-icon><Location /></el-icon>
          <span class="info-text">{{ formatCoordinate(location.lat, location.lon) }}</span>
        </div>
        
        <div class="info-item" v-if="location.commonAnimals">
          <el-icon><Sunny /></el-icon>
          <span class="info-text">{{ formatAnimals(location.commonAnimals) }}</span>
        </div>
        
        <div class="info-item" v-if="location.facilities">
          <el-icon><House /></el-icon>
          <span class="info-text">{{ location.facilities }}</span>
        </div>
      </div>
    </div>
    
    <div class="card-footer">
      <el-button type="primary" size="small" class="view-btn">
        <el-icon><View /></el-icon>
        查看详情
      </el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.location-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  }

  .card-image {
    position: relative;
    height: 200px;
    overflow: hidden;

    .image {
      width: 100%;
      height: 100%;
      transition: transform 0.3s ease;
    }

    &:hover .image {
      transform: scale(1.05);
    }

    .image-error {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100%;
      background: #f5f7fa;
      color: #909399;

      p {
        margin: 8px 0 0 0;
        font-size: 14px;
      }
    }

    .image-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(to bottom, rgba(0, 0, 0, 0.1), transparent);
      display: flex;
      justify-content: flex-end;
      align-items: flex-start;
      padding: 12px;

      .season-tag {
        background: rgba(255, 255, 255, 0.9);
        backdrop-filter: blur(10px);
        padding: 6px 12px;
        border-radius: 20px;
        font-size: 12px;
        color: #409eff;
        display: flex;
        align-items: center;
        gap: 4px;
        font-weight: 500;
      }
    }
  }

  .card-content {
    padding: 20px;
    flex: 1;

    .location-name {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 16px 0;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .location-info {
      .info-item {
        display: flex;
        align-items: flex-start;
        margin-bottom: 12px;
        gap: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .el-icon {
          color: #409eff;
          margin-top: 2px;
          flex-shrink: 0;
        }

        .info-text {
          font-size: 14px;
          color: #606266;
          line-height: 1.5;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
      }
    }
  }

  .card-footer {
    padding: 0 20px 20px 20px;

    .view-btn {
      width: 100%;
      border-radius: 8px;
      height: 36px;
      font-weight: 500;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .location-card {
    .card-image {
      height: 160px;
    }

    .card-content {
      padding: 16px;

      .location-name {
        font-size: 16px;
      }

      .location-info .info-item .info-text {
        font-size: 13px;
      }
    }

    .card-footer {
      padding: 0 16px 16px 16px;
    }
  }
}
</style>