<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import http from "@/utils/http";
import AnimalCard from "@/components/AnimalCard.vue";
import { ElMessage } from "element-plus";
import { Search, Refresh, Star } from "@element-plus/icons-vue";

// 路由
const router = useRouter();

// 搜索参数
const searchParams = ref({
  pageNum: 1,
  pageSize: 12,
  name: "",
  typeId: "",
});

// 页面数据
const loading = ref(false);
const animalList = ref([]);
const total = ref(0);
// 分类列表
const animalTypeList = ref([]);

// 推荐相关
const recommendDialogVisible = ref(false);
const recommendLoading = ref(false);
const recommendList = ref([]);
const recommendNum = ref(6); // 默认推荐6个动物

// 获取动物列表数据
const getAnimalList = async () => {
  loading.value = true;
  try {
    const res = await http.get(`/animal/page`, {
      params: searchParams.value,
    });
    if (res.code === 200) {
      animalList.value = res.data.records || [];
      total.value = res.data.total || 0;
    } else {
      ElMessage.error("获取动物数据失败");
    }
  } catch (error) {
    console.error("获取动物数据失败:", error);
    ElMessage.error("获取动物数据失败");
  } finally {
    loading.value = false;
  }
};

// 获取分类列表
const getAnimalTypeList = async () => {
  try {
    const res = await http.get("/animalType/list");
    if (res.code === 200) {
      animalTypeList.value = res.data || [];
      animalTypeList.value.unshift({
        id: "",
        name: "全部",
      });
    } else {
      ElMessage.error("获取分类列表失败");
    }
  } catch (error) {
    console.error("获取分类列表失败:", error);
    ElMessage.error("获取分类列表失败");
  }
};

// 搜索
const handleSearch = () => {
  searchParams.value.pageNum = 1;
  getAnimalList();
};

// 重置搜索
const handleReset = () => {
  searchParams.value.name = "";
  searchParams.value.pageNum = 1;
  searchParams.value.typeId = "";
  getAnimalList();
};

// 分页处理
const handleCurrentChange = (page: number) => {
  searchParams.value.pageNum = page;
  getAnimalList();
};

const handleSizeChange = (size: number) => {
  searchParams.value.pageSize = size;
  searchParams.value.pageNum = 1;
  getAnimalList();
};

// 修改查询类型
const changeType = (type) => {
  searchParams.value.typeId = type.id;
  getAnimalList();
};

// 获取推荐动物
const getRecommendList = async () => {
  recommendLoading.value = true;
  try {
    const response = await http.get('/animal/recommend', { 
      params: { num: recommendNum.value } 
    });
    
    if (response.code === 200) {
      recommendList.value = response.data || [];
      if (recommendList.value.length === 0) {
        ElMessage.info('暂无推荐动物');
      }
    } else {
      ElMessage.error(response.message || '获取推荐动物失败');
      recommendList.value = [];
    }
  } catch (error) {
    console.error('获取推荐动物失败:', error);
    ElMessage.error('获取推荐动物失败');
    recommendList.value = [];
  } finally {
    recommendLoading.value = false;
  }
};

// 打开推荐弹窗
const handleRecommend = () => {
  recommendDialogVisible.value = true;
  getRecommendList();
};

// 关闭推荐弹窗
const handleCloseRecommend = () => {
  recommendDialogVisible.value = false;
  recommendList.value = [];
};

// 页面初始化
onMounted(async () => {
  await getAnimalTypeList();
  getAnimalList();
});
</script>

<template>
  <div class="animal-list-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">奇幻动物园动物大全</h1>
        <p class="page-description">
          发现奇幻动物园丰富的动物资源，探索自然生态之美
        </p>
      </div>

      <!-- 搜索区域 -->
      <div class="search-section">
        <div class="search-container">
          <el-input
            v-model="searchParams.name"
            placeholder="搜索动物名称..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" :icon="Search" @click="handleSearch">
            搜索
          </el-button>
          <el-button :icon="Refresh" @click="handleReset"> 重置 </el-button>
          <el-button 
            type="warning" 
            :icon="Star" 
            @click="handleRecommend"
            class="recommend-btn"
          >
            智能推荐
          </el-button>
        </div>

        <!-- 分类选择 -->
        <div class="type-box">
          <div class="type-grid">
            <div
              v-for="type in animalTypeList"
              :key="type.id"
              class="type-card"
              :class="{ active: type.id === searchParams.typeId }"
              @click="changeType(type)"
            >
              {{ type.name }}
            </div>
          </div>
        </div>
      </div>

      <!-- 页面内容 -->
      <div class="page-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="animal-grid">
            <div v-for="n in 12" :key="n" class="animal-skeleton">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item
                    variant="image"
                    style="width: 100%; height: 200px"
                  />
                  <div style="padding: 16px">
                    <el-skeleton-item variant="h3" style="width: 60%" />
                    <el-skeleton-item
                      variant="text"
                      style="width: 100%; margin-top: 8px"
                    />
                    <el-skeleton-item
                      variant="text"
                      style="width: 80%; margin-top: 8px"
                    />
                  </div>
                </template>
              </el-skeleton>
            </div>
          </div>
        </div>

        <!-- 动物列表 -->
        <div v-else-if="animalList.length > 0" class="animal-grid">
          <AnimalCard v-for="animal in animalList" :key="animal.id" :animal="animal" />
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty
            :description="
              searchParams.name ? '未找到相关动物信息' : '暂无动物数据'
            "
            :image-size="120"
          >
            <template #image>
              <div class="empty-icon">🦅</div>
            </template>
            <el-button
              v-if="searchParams.name"
              type="primary"
              @click="handleReset"
            >
              查看全部
            </el-button>
          </el-empty>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination-container">
          <el-pagination
            v-model:current-page="searchParams.pageNum"
            v-model:page-size="searchParams.pageSize"
            :page-sizes="[12, 24, 36, 48]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- 推荐动物弹窗 -->
    <el-dialog
      v-model="recommendDialogVisible"
      title="智能推荐动物"
      width="90%"
      :before-close="handleCloseRecommend"
      class="recommend-dialog"
    >
      <div class="recommend-content">
        <div class="recommend-header">
          <p class="recommend-description">
            <el-icon><Star /></el-icon>
            基于您的浏览偏好，为您推荐以下精选动物
          </p>
        </div>

        <!-- 推荐动物加载状态 -->
        <div v-if="recommendLoading" class="recommend-loading">
          <div class="recommend-grid">
            <div v-for="n in 6" :key="n" class="recommend-skeleton">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item variant="image" style="width: 100%; height: 180px;" />
                  <div style="padding: 12px;">
                    <el-skeleton-item variant="h3" style="width: 60%;" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-top: 6px;" />
                    <el-skeleton-item variant="text" style="width: 80%; margin-top: 6px;" />
                  </div>
                </template>
              </el-skeleton>
            </div>
          </div>
        </div>

        <!-- 推荐动物列表 -->
        <div v-else-if="recommendList.length > 0" class="recommend-grid">
          <AnimalCard
            v-for="animal in recommendList"
            :key="animal.id"
            :animal="animal"
          />
        </div>

        <!-- 推荐空状态 -->
        <div v-else class="recommend-empty">
          <el-empty 
            description="暂无推荐动物"
            :image-size="100"
          >
            <template #image>
              <div class="empty-icon">🎯</div>
            </template>
            <p class="empty-tip">多浏览一些动物，我们会为您提供更精准的推荐</p>
          </el-empty>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseRecommend">关闭</el-button>
          <el-button type="primary" @click="getRecommendList" :loading="recommendLoading">
            刷新推荐
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.animal-list-page {
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

    .page-stats {
      display: flex;
      justify-content: center;
      gap: 32px;
      flex-wrap: wrap;

      .stat-item {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 12px 20px;
        background: rgba(255, 255, 255, 0.9);
        border-radius: 25px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        backdrop-filter: blur(10px);

        .stat-icon {
          font-size: 20px;
        }

        .stat-text {
          font-size: 14px;
          font-weight: 600;
          color: #34495e;
        }
      }
    }
  }

  .search-section {
    margin-bottom: 32px;

    .search-container {
      display: flex;
      justify-content: center;
      gap: 12px;
      align-items: center;
      flex-wrap: wrap;

      .search-input {
        width: 320px;
        max-width: 100%;
      }

      :deep(.el-input__wrapper) {
        border-radius: 25px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      }

      .el-button {
        border-radius: 25px;
        padding: 12px 24px;
        font-weight: 600;
      }
    }

    .type-box {
      width: 100%;
      .type-grid {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        gap: 12px;
        margin-top: 20px;

        span {
          padding: 8px 12px;
          background-color: var(--secondary-dark);
          font-size: 16px;
          border-radius: 10px;
          color: white;
        }

        .type-card {
          padding: 8px 12px;
          border-radius: 10px;
          background-color: white;
          cursor: pointer;
          font-size: 16px;
          transition: all ease-in 0.3s;

          &:hover {
            background-color: var(--primary-color);
            color: white;
          }
        }
        .active {
          background-color: var(--primary-color);
          color: white;
        }
      }
    }
  }

  .page-content {
    .animal-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 24px;
      margin-bottom: 40px;
    }

    .animal-skeleton {
      background: white;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .loading-container {
      .animal-grid {
        animation: pulse 1.5s ease-in-out infinite;
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
        font-size: 64px;
        margin-bottom: 16px;
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
  .animal-list-page {
    .page-content .animal-grid {
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
  }
}

@media (max-width: 768px) {
  .animal-list-page {
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

      .page-stats {
        gap: 16px;

        .stat-item {
          padding: 8px 16px;

          .stat-text {
            font-size: 13px;
          }
        }
      }
    }

    .search-section {
      .search-container {
        flex-direction: column;
        gap: 12px;

        .search-input {
          width: 100%;
        }

        .el-button {
          width: 100%;
        }
      }
    }

    .page-content {
      .animal-grid {
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
  .animal-list-page {
    .page-content .animal-grid {
      grid-template-columns: 1fr;
      gap: 16px;
    }
  }
}

// 动画效果
@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

// 骨架屏动画
:deep(.el-skeleton.is-animated .el-skeleton__item) {
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}

 // 推荐弹窗样式
 .recommend-dialog {
   :deep(.el-dialog__body) {
     padding: 0 20px 20px;
   }
 }

 .recommend-content {
   max-height: 70vh;
   overflow-y: auto;
 }

 .recommend-header {
   margin-bottom: 20px;
   text-align: center;
 }

 .recommend-description {
   display: flex;
   align-items: center;
   justify-content: center;
   gap: 8px;
   font-size: 16px;
   color: #606266;
   margin: 0;
   
   .el-icon {
     color: #f39c12;
     font-size: 18px;
   }
 }

 .recommend-grid {
   display: grid;
   grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
   gap: 16px;
 }

 .recommend-skeleton {
   border: 1px solid #ebeef5;
   border-radius: 8px;
   overflow: hidden;
 }

 .recommend-loading {
   padding: 20px 0;
 }

 .recommend-empty {
   padding: 40px 20px;
   text-align: center;
   
   .empty-icon {
     font-size: 48px;
     margin-bottom: 16px;
   }
   
   .empty-tip {
     color: #909399;
     font-size: 14px;
     margin: 12px 0 0;
   }
 }

 .dialog-footer {
   display: flex;
   justify-content: flex-end;
   gap: 12px;
 }

 // 响应式设计
 @media (max-width: 1200px) {
  .animal-list-page {
    .page-content .animal-grid {
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
  }
}

@media (max-width: 768px) {
  .animal-list-page {
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

      .page-stats {
        gap: 16px;

        .stat-item {
          padding: 8px 16px;

          .stat-text {
            font-size: 13px;
          }
        }
      }
    }

    .search-section {
      .search-container {
        flex-direction: column;
        gap: 12px;

        .search-input {
          width: 100%;
        }

        .el-button {
          width: 100%;
        }
      }
    }

    .page-content {
      .animal-grid {
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
  .animal-list-page {
    .page-content .animal-grid {
      grid-template-columns: 1fr;
      gap: 16px;
    }
  }
}

// 动画效果
@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

// 骨架屏动画
:deep(.el-skeleton.is-animated .el-skeleton__item) {
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>
