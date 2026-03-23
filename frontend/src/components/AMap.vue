<script setup>
import { onMounted, onUnmounted, ref, watch } from "vue";
import { useRoute, useRouter } from 'vue-router'
import AMapLoader from "@amap/amap-jsapi-loader";
import config from "@/config"
import http from '@/utils/http'
import { ElMessage } from 'element-plus'
import { getImageUrl } from '@/utils/system'

const route = useRoute()
const router = useRouter()
const location = ref({
    lat: route.query.lat,
    lon: route.query.lon,
    name: route.query.name,
});

let map = ref(null);
const allLocations = ref([]);
const loading = ref(false);

// 获取所有动物地点数据
const getAllLocations = async () => {
  loading.value = true
  try {
    const res = await http.get('/animalLocation/list')
    if (res.code === 200) {
      allLocations.value = res.data || []
    } else {
      ElMessage.error('获取动物地点数据失败')
    }
  } catch (error) {
    console.error('获取动物地点数据失败:', error)
    ElMessage.error('获取动物地点数据失败')
  } finally {
    loading.value = false
  }
}

// 初始化地图
const initMap = async () => {
  window._AMapSecurityConfig = {
    securityJsCode: config.safetyKey,
  };
  const AMap = await AMapLoader.load({
    key: config.mapKey, // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.Scale"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  })
  
  // 设置地图中心点，优先使用传入的地点坐标，否则使用默认坐标
  const centerLon = location.value.lon || 116.2;
  const centerLat = location.value.lat || 29.0;
  
  map.value = new AMap.Map("container", {
    // 设置地图容器id
    viewMode: "3D", // 是否为3D地图模式
    zoom: 11, // 初始化地图级别
    center: [centerLon, centerLat], // 初始化地图中心点位置
  });
  
  // 获取所有动物地点数据并添加标记
  await getAllLocations();
  addAllMarkers();
}

// 添加所有动物地点标记
const addAllMarkers = () => {
  if (!map.value || !allLocations.value.length) return;
  
  allLocations.value.forEach(locationItem => {
    if (locationItem.lon && locationItem.lat) {
      // 判断是否为当前传入的地点
      const isCurrentLocation = location.value.name === locationItem.name;
      
      // 创建标记
      const marker = new AMap.Marker({
        position: [parseFloat(locationItem.lon), parseFloat(locationItem.lat)],
        title: locationItem.name, // 设置悬浮标题
        label: {
          content: locationItem.name,
          direction: "top",
          offset: new AMap.Pixel(0, 0)
        },
        icon: new AMap.Icon({
          size: new AMap.Size(isCurrentLocation ? 40 : 32, isCurrentLocation ? 40 : 32),
          image: isCurrentLocation ? 
            '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png' : 
            '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png',
          imageSize: new AMap.Size(isCurrentLocation ? 40 : 32, isCurrentLocation ? 40 : 32)
        })
      });

      // 创建信息窗体
      const infoWindow = createInfoWindow(locationItem);
      
      // 添加点击事件
      marker.on('click', () => {
        infoWindow.open(map.value, marker.getPosition());
      });

      map.value.add(marker);
    }
  });
}

// 创建信息窗体
const createInfoWindow = (locationItem) => {
  const imageUrl = locationItem.imgUrl ? getImageUrl(locationItem.imgUrl) : '/default-location.jpg';
  
  const content = `
    <div style="
      width: 120px;
      padding: 12px;
      text-align: center;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.15);
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    ">
      <div style="
        width: 60px;
        height: 60px;
        border-radius: 50%;
        overflow: hidden;
        margin: 0 auto 8px;
        border: 2px solid #409eff;
        background: #f5f7fa;
      ">
        <img 
          src="${imageUrl}" 
          style="
            width: 100%;
            height: 100%;
            object-fit: cover;
          "
          onerror="this.src='/default-location.jpg'"
        />
      </div>
      <div style="
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        line-height: 1.4;
        word-break: break-all;
        margin-bottom: 8px;
      ">
        ${locationItem.name}
      </div>
      <button 
        onclick="window.navigateToLocationDetail(${locationItem.id})"
        style="
          width: 100%;
          padding: 6px 12px;
          background: #409eff;
          color: white;
          border: none;
          border-radius: 4px;
          font-size: 12px;
          cursor: pointer;
          transition: background-color 0.3s;
        "
        onmouseover="this.style.backgroundColor='#337ecc'"
        onmouseout="this.style.backgroundColor='#409eff'"
      >
        查看详情
      </button>
    </div>
  `;

  return new AMap.InfoWindow({
    content: content,
    offset: new AMap.Pixel(0, -30),
    closeWhenClickMap: true
  });
}

// 全局导航函数
window.navigateToLocationDetail = (locationId) => {
  router.push({
    path: "/front/location-detail",
    query: {
      id: locationId
    }
  });
}

// 添加地图标记（兼容旧版本调用方式）
const addMarkerLegacy = (lon, lat, name, id, locationData) => {
  // 转换为新的location对象格式
  const locationObj = {
    id: id,
    name: name,
    lat: lat,
    lon: lon,
    description: locationData?.description || '',
    bestSeason: locationData?.bestSeason || '',
    commonAnimals: locationData?.commonAnimals || '',
    facilities: locationData?.facilities || '',
    imgUrl: locationData?.imgUrl || ''
  };
  
  // 调用新的addMarker方法
  addMarker(locationObj);
}

// 修改addMarker方法，为当前地点使用特殊样式（保留兼容性）
const addMarker = (location) => {
  // 这个方法现在主要用于兼容性，实际标记添加在addAllMarkers中处理
  console.log('addMarker called for:', location.name);
}
onMounted(() => {
  initMap();
});

onUnmounted(() => {
  map.value?.destroy();
});
</script>

<template>
  <div class="amap-container">
    <div v-if="loading" class="loading-overlay">
      <div class="loading-content">
        <div class="loading-spinner"></div>
        <p>正在加载动物地点数据...</p>
      </div>
    </div>
    <div id="container"></div>
  </div>
</template>

<style scoped lang="scss">
.amap-container {
  position: relative;
  width: 100%;
  height: 800px;
}

#container {
  width: 100%;
  height: 100%;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-content {
  text-align: center;
  
  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 4px solid #f3f3f3;
    border-top: 4px solid #1890ff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin: 0 auto 16px;
  }
  
  p {
    color: #666;
    font-size: 14px;
    margin: 0;
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
