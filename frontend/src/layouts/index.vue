<script setup>
import Header from "./components/header.vue";
import Aside from "./components/aside.vue";
import TagsView from "./components/tags-view.vue";
</script>

<template>
  <div class="layout-container">
    <Header />
    <div class="main-content">
      <div class="aside-container">
        <Aside />
      </div>
      <div class="content-area">
        <TagsView />
        <div class="page-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.layout-container {
  display: flex;
  flex-direction: column;
  width: 100vw;
  height: 100vh;
  overflow: hidden;

  .main-content {
    flex: 1;
    display: flex;
    overflow: hidden;

    .aside-container {
      width: 200px;
      height: 100%;
      overflow: hidden;
      background-color: #1f2937;
      border-right: 1px solid #374151;
      box-shadow: 2px 0 8px 0 rgba(0, 0, 0, 0.15);
    }

    .content-area {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;
      background-color: #f0f2f5;

      .page-container {
        flex: 1;
        padding: 16px;
        box-sizing: border-box;
        overflow-y: auto;
      }
    }
  }
}
</style>
