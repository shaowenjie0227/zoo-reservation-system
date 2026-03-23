<script setup>
import http from "@/utils/http";
import { Search, Refresh, Delete } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { computed, onMounted, ref } from "vue";
import { hasPermission } from "@/utils/system";

const initParams = {
  pageNum: 1,
  pageSize: 10,
  type: "",
  userId: "",
  targetId: "",
};

// 搜索条件
const params = ref({ ...initParams });

// 表格数据
const tableData = ref([]);

// 表格数据总数
const total = ref(0);

// 批量删除相关
const multipleSelection = ref([]);
const selectedIds = computed(() => {
  return multipleSelection.value.map((item) => item.id);
});
const hasSelected = computed(() => {
  return multipleSelection.value.length > 0;
});

// 类型选项
  const typeOptions = [
    { label: "动物", value: "animal" },
    { label: "动物地点", value: "location" },
    { label: "论坛帖子", value: "post" },
    { label: "旅游景点", value: "travel" },
  ];

// 获取表格数据
const getTableData = async () => {
  const res = await http.get(
    `/like/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&type=${params.value.type}&userId=${params.value.userId}&targetId=${params.value.targetId}`
  );
  if (res.code === 200) {
    tableData.value = res.data.records;
    total.value = res.data.total;
  }
};

// 搜索
const handleSearch = async () => {
  params.value.pageNum = 1;
  await getTableData();
};

// 重置
const handleReset = async () => {
  params.value = { ...initParams };
  await getTableData();
};

// 删除单行
const delRow = async (id) => {
  const res = await http.get(`/like/del?id=${id}`);
  if (res.code === 200) {
    ElMessage.success("删除成功");
    await getTableData();
  } else {
    ElMessage.error("删除失败");
  }
};

// 批量删除
const batchDel = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning("请选择要删除的数据");
    return;
  }
  const res = await http.post(`/like/batchDel`, selectedIds.value);
  if (res.code === 200) {
    ElMessage.success("批量删除成功");
    await getTableData();
  } else {
    ElMessage.error("批量删除失败");
  }
};

// 表格选择变化
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

const handleSizeChange = (val) => {
  params.value.pageSize = val;
  getTableData();
};

const handleCurrentChange = (val) => {
  params.value.pageNum = val;
  getTableData();
};

onMounted(async () => {
  await getTableData();
});
</script>

<template>
  <div>
    <header class="flex">
      <div class="flex form-item">
        <p class="label-text">类型：</p>
        <el-select v-model="params.type" placeholder="请选择点赞类型" clearable style="width: 200px;">
          <el-option
            v-for="option in typeOptions"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </el-select>
      </div>
      <div class="flex form-item">
        <p class="label-text">用户ID：</p>
        <el-input v-model="params.userId" placeholder="请输入用户ID" />
      </div>
      <div class="flex form-item">
        <p class="label-text">目标ID：</p>
        <el-input v-model="params.targetId" placeholder="请输入目标ID" />
      </div>
      <el-button type="primary" :icon="Search" @click="handleSearch"
        >搜索</el-button
      >
      <el-button type="warning" :icon="Refresh" @click="handleReset"
        >重置</el-button
      >
    </header>
    <main>
      <div class="flex op-box">
        <el-button
          v-if="hasPermission('like', '删除')"
          :icon="Delete"
          type="danger"
          :disabled="!hasSelected"
          @click="batchDel"
          >批量删除</el-button
        >
      </div>
      <!-- 表格 -->
      <el-table
        :data="tableData"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userNickName" label="用户昵称" width="120" />
        <el-table-column prop="typeName" label="类型" width="100" />
        <el-table-column prop="targetName" label="目标名称" show-overflow-tooltip />
        <el-table-column prop="createTime" label="点赞时间" width="180" />
        <el-table-column label="操作" fixed="right" width="120">
          <template #default="scope">
            <el-popconfirm
              title="您确定要删除吗?"
              @confirm="delRow(scope.row.id)"
            >
              <template #reference>
                <el-button
                  v-if="hasPermission('like', '删除')"
                  type="danger"
                  :icon="Delete"
                  >删除</el-button
                >
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="params.pageNum"
          v-model:page-size="params.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </main>
  </div>
</template>

<style lang="scss" scoped>
.label-text {
  color: #606266;
  width: 120px;
}

.form-item {
  margin-right: 20px;
}

.op-box {
  margin: 10px 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--el-fill-color-light);
}
</style>