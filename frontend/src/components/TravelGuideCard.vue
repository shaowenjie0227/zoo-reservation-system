<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { getImageUrl } from '@/utils/system'
import { formatDate } from '@/utils/system'
import { Picture, Calendar, View, User, LocationInformation } from '@element-plus/icons-vue'

const router = useRouter()

const props = defineProps({
  travelGuide: {
    type: Object,
    required: true
  }
})

// 跳转到攻略详情页面
const goToDetail = () => {
  router.push({
    path: '/front/travel-guide-detail',
    query: {
      id: props.travelGuide.id
    }
  })
}
</script>

<template>
  <div class="travel-guide-card" @click="goToDetail">
    <div class="travel-guide-image">
      <el-image
        :src="getImageUrl(travelGuide.imgUrl)"
        :alt="travelGuide.title"
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
    <div class="travel-guide-content">
      <h3 class="travel-guide-title">{{ travelGuide.title }}</h3>
      
      <!-- 作者和景点信息 -->
      <div class="travel-guide-info">
        <span v-if="travelGuide.userNickName">
          作者：
          {{ travelGuide.userNickName }}
        </span>
        <span v-if="travelGuide.travelName">
          景点：
          {{ travelGuide.travelName }}
        </span>
        <span v-if="travelGuide?.rating" class="rating-info">
          <span class="rating-label">评分：</span>
          <el-rate
            v-model="travelGuide.rating"
            :disabled="true"
            :show-score="true"
            :score-template="'{value} 分'"
            size="small"
            class="rating-stars"
          />
        </span>
      </div>
      
      <!-- 统计信息 -->
      <div class="travel-guide-stats">
        <span class="stat-item">
          <el-icon><View /></el-icon>
          {{ travelGuide.viewCount || 0 }}
        </span>
        <span class="stat-item">
          <el-icon><Calendar /></el-icon>
          {{ formatDate(travelGuide.createTime) }}
        </span>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.travel-guide-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  }

  .travel-guide-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
    position: relative;

    .el-image {
      width: 100%;
      height: 100%;
      transition: transform 0.3s ease;
    }

    &:hover .el-image {
      transform: scale(1.05);
    }

    .image-slot {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      background: #f5f7fa;
      color: #909399;
      font-size: 2rem;
    }
  }

  .travel-guide-content {
    padding: 20px;
    flex: 1;
    display: flex;
    flex-direction: column;

    .travel-guide-title {
      font-size: 1.2rem;
      font-weight: 600;
      color: #2c3e50;
      margin-bottom: 12px;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .travel-guide-info {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-bottom: 16px;

      span {
        font-size: 0.9rem;
        color: #606266;
        display: flex;
        align-items: center;
        gap: 4px;
      }

      .rating-info {
        display: flex;
        align-items: center;
        gap: 8px;

        .rating-label {
          font-size: 0.9rem;
          color: #606266;
          min-width: 40px;
        }

        .rating-stars {
          :deep(.el-rate__text) {
            font-size: 0.85rem;
            color: #606266;
          }
        }
      }
    }

    .travel-guide-stats {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 12px;
      border-top: 1px solid #f0f0f0;

      .stat-item {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 0.85rem;
        color: #909399;

        .el-icon {
          font-size: 1rem;
        }
      }
    }
  }
}

.travel-guide-intro {
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.5;
  margin: 0 0 12px 0;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.travel-guide-tags {
  margin-bottom: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
</style>