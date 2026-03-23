<script setup>
import { menuList } from "@/utils/menu";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import { useRoute } from "vue-router";
import useUserStore from "@/stores/userStore";
import { ref, watch } from "vue";

const route = useRoute();
const userStore = useUserStore();

// 当前打开的子菜单索引
const openedSubMenu = ref('');

// 获取图标组件
const getIcon = (iconName) => {
  return ElementPlusIcons[iconName];
};

// 获取菜单列表
const getMenuList = () => {
  return menuList
    .filter((menu) => menu.roleFlag === userStore.roleFlag)
    .flatMap((menu) => menu.backMenu);
};

// 处理子菜单打开事件
const handleSubMenuOpen = (index) => {
  openedSubMenu.value = index;
};

// 处理子菜单关闭事件
const handleSubMenuClose = (index) => {
  if (openedSubMenu.value === index) {
    openedSubMenu.value = '';
  }
};

// 监听路由变化，设置默认打开的子菜单
watch(
  () => route.path,
  (newPath) => {
    const menuList = getMenuList();
    for (const menu of menuList) {
      if (menu.children) {
        const hasActiveChild = menu.children.some(child => child.path === newPath);
        if (hasActiveChild) {
          openedSubMenu.value = menu.path;
          break;
        }
      }
    }
  },
  { immediate: true }
);
</script>

<template>
  <el-menu
    :default-active="route.path"
    :default-openeds="[openedSubMenu]"
    class="el-menu-custom"
    router
    @sub-menu-open="handleSubMenuOpen"
    @sub-menu-close="handleSubMenuClose"
    unique-opened
  >
    <template v-for="menu in getMenuList()" :key="menu.path">
      <!-- 有子菜单的情况 -->
      <el-sub-menu v-if="menu.children" :index="menu.path">
        <template #title>
          <el-icon v-if="menu.icon">
            <component :is="getIcon(menu.icon)" />
          </el-icon>
          <span>{{ menu.name }}</span>
        </template>
        <el-menu-item
          v-for="subMenu in menu.children"
          :key="subMenu.path"
          :index="subMenu.path"
        >
          {{ subMenu.name }}
        </el-menu-item>
      </el-sub-menu>
      <!-- 没有子菜单的情况 -->
      <el-menu-item v-else :index="menu.path">
        <el-icon v-if="menu.icon">
          <component :is="getIcon(menu.icon)" />
        </el-icon>
        <template #title>{{ menu.name }}</template>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<style lang="scss" scoped>
.el-menu-custom {
  height: 100%;
  border-right: none;
  overflow-y: auto;
  background-color: #1f2937;
  
  // 自定义滚动条样式
  &::-webkit-scrollbar {
    width: 6px;
  }
  
  &::-webkit-scrollbar-track {
    background: #374151;
  }
  
  &::-webkit-scrollbar-thumb {
    background: #6b7280;
    border-radius: 3px;
    
    &:hover {
      background: #9ca3af;
    }
  }
}

:deep(.el-menu) {
  border-right: none;
  background-color: #1f2937;
}

// 所有菜单项的基础样式
:deep(.el-menu-item),
:deep(.el-sub-menu .el-sub-menu__title) {
  margin: 4px 12px;
  width: calc(100% - 24px);
  border-radius: 8px;
  padding-left: 20px !important;
  color: #d1d5db;
  background-color: transparent;
  transition: all 0.3s ease;

  .el-icon {
    margin-right: 12px;
    width: 20px;
    text-align: center;
    color: #9ca3af;
  }

  &:hover {
    background-color: #374151;
    color: #f3f4f6;
    
    .el-icon {
      color: #60a5fa;
    }
  }
}

:deep(.el-menu-item) {
  &.is-active {
    background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
    color: #ffffff;
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
    
    .el-icon {
      color: #ffffff;
    }

    &::before {
      position: absolute;
      left: -12px;
      top: 50%;
      transform: translateY(-50%);
      width: 4px;
      height: 20px;
      background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
      border-radius: 0 2px 2px 0;
      content: "";
      box-shadow: 0 2px 8px rgba(59, 130, 246, 0.4);
    }
  }
}

// 子菜单标题样式
:deep(.el-sub-menu .el-sub-menu__title) {
  &:hover {
    background-color: #374151;
    
    .el-sub-menu__icon-arrow {
      color: #60a5fa;
    }
  }
}

// 子菜单展开时的样式
:deep(.el-sub-menu.is-opened .el-sub-menu__title) {
  background-color: #374151;
  color: #f3f4f6;
  
  .el-icon {
    color: #60a5fa;
  }
  
  .el-sub-menu__icon-arrow {
    color: #60a5fa;
  }
}

// 子菜单内容样式
:deep(.el-sub-menu) {
  .el-menu--inline {
    background-color: #111827;
    
    .el-menu-item {
      padding-left: 52px !important;
      margin-left: 12px;
      width: calc(100% - 36px);
      background-color: transparent;
      color: #9ca3af;
      font-size: 14px;

      &:hover {
        background-color: #1f2937;
        color: #d1d5db;
      }

      &.is-active {
        background: linear-gradient(135deg, #059669 0%, #047857 100%);
        color: #ffffff;
        box-shadow: 0 2px 8px rgba(5, 150, 105, 0.3);
        
        &::before {
          left: 0px;
          background: linear-gradient(135deg, #10b981 0%, #059669 100%);
          width: 3px;
          height: 16px;
        }
      }
    }
  }
}

// 子菜单箭头样式
:deep(.el-sub-menu__icon-arrow) {
  color: #6b7280;
  transition: all 0.3s ease;
}

// 折叠时的样式
:deep(.el-menu--collapse) {
  .el-menu-item,
  .el-sub-menu .el-sub-menu__title {
    margin: 4px 8px;
    width: calc(100% - 16px);
    padding-left: 20px !important;
    justify-content: center;
  }

  .el-sub-menu__title {
    .el-sub-menu__icon-arrow {
      display: none;
    }
  }
  
  .el-menu-item {
    &.is-active::before {
      left: -8px;
    }
  }
}

// 菜单项文字样式
:deep(.el-menu-item span),
:deep(.el-sub-menu__title span) {
  font-weight: 500;
  font-size: 14px;
}

// 激活状态的菜单项文字
:deep(.el-menu-item.is-active span) {
  font-weight: 600;
}
</style>
