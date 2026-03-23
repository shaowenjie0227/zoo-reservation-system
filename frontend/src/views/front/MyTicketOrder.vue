<script setup>
import http from "@/utils/http";
import { Search, Refresh, Delete, Check } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, onMounted, ref } from "vue";
import { hasPermission } from "@/utils/system";
import useUserStore from "../../stores/userStore";

const userStore = useUserStore();

const initParams = {
  pageNum: 1,
  pageSize: 10,
  orderNo: "",
  status: "",
  userId: userStore.userInfo.id,
};

// 搜索条件
const params = ref({ ...initParams });

// 表格数据
const tableData = ref([]);

// 表格数据总数
const total = ref(0);

// 订单状态选项
const statusOptions = [
  { label: "已付款", value: 1 },
  { label: "已完成", value: 2 },
  { label: "已取消", value: 3 }
];

// 批量删除相关
const multipleSelection = ref([]);
const selectedIds = computed(() => {
  return multipleSelection.value.map((item) => item.id);
});
const hasSelected = computed(() => {
  return multipleSelection.value.length > 0;
});

// 获取表格数据
const getTableData = async () => {
  const res = await http.get(
    `/ticketOrder/page`, {
      params: params.value
    }
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
const handleReset = () => {
  params.value = { ...initParams };
  getTableData();
};

// 删除
const delRow = async (id) => {
  const res = await http.get(`/ticketOrder/del?id=${id}`);
  if (res.code === 200) {
    ElMessage.success("删除成功");
    await getTableData();
  } else {
    ElMessage.error("删除失败");
  }
};

// 批量删除
const batchDel = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning("请先选择要删除的数据");
    return;
  }
  const res = await http.post("/ticketOrder/batchDel", selectedIds.value);
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

// 分页大小变化
const handleSizeChange = (val) => {
  params.value.pageSize = val;
  getTableData();
};

// 当前页变化
const handleCurrentChange = (val) => {
  params.value.pageNum = val;
  getTableData();
};

// 格式化订单状态
const formatStatus = (status) => {
  const statusMap = {
    1: "已付款",
    2: "已完成", 
    3: "已取消"
  };
  return statusMap[status] || status;
};

// 格式化金额
const formatAmount = (amount) => {
  return `¥${amount}`;
};

// 取消订单
const cancelOrder = async (id) => {
  await ElMessageBox.confirm("确定取消订单吗？", "取消订单", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  });

  const res = await http.get(`/ticketOrder/cancel?id=${id}`);
  if (res.code === 200) {
    ElMessage.success("取消成功");
    await getTableData();
  } else {
    ElMessage.error("取消失败");
  }
};

// 完成订单
const completeOrder = async (id) => {
  await ElMessageBox.confirm("确定完成订单吗？", "完成订单", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  });

  const res = await http.post(`/ticketOrder/edit`, {
    id,
    status: 2
  });
  if (res.code === 200) {
    ElMessage.success("完成成功");
    await getTableData();
  } else {
    ElMessage.error("完成失败");
  }
};

onMounted(async () => {
  await getTableData();
});
</script>

<template>
  <div style="width: 80%; margin: 0 auto;">
    <header class="flex">
      <div class="flex form-item">
        <p class="label-text">订单号：</p>
        <el-input v-model="params.orderNo" placeholder="请输入订单号" clearable style="width: 200px;" />
      </div>
      <div class="flex form-item">
        <p class="label-text">状态：</p>
        <el-select v-model="params.status" placeholder="请选择订单状态" clearable style="width: 200px;">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
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
        show-overflow-tooltip
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="userName" label="用户名" />
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="scope">
            {{ formatAmount(scope.row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 2 ? 'info' : 'danger'">
              {{ formatStatus(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系人" />
        <el-table-column prop="contactPhone" label="联系电话" />
        <el-table-column prop="cardNo" label="身份证号" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-popconfirm
            v-if="scope.row.status != 1"
              title="您确定要删除这条订单记录吗?"
              @confirm="delRow(scope.row.id)"
            >
              <template #reference>
                <el-button
                
                  type="danger"
                  :icon="Delete"
                  size="small"
                  >删除</el-button
                >
              </template>
            </el-popconfirm>
            <el-button 
            v-if="scope.row.status == 1"
            type="danger"
            :icon="Delete"
            size="small"
            @click="cancelOrder(scope.row.id)"
            >取消</el-button>
            <el-button 
            v-if="scope.row.status == 1"
            type="primary"
            :icon="Check"
            size="small"
            @click="completeOrder(scope.row.id)"
            >完成</el-button>
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
  width: 80px;
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