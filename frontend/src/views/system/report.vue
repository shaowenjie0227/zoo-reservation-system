<script setup>
import http from "@/utils/http";
import { Search, Refresh, Delete, View } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getImageUrl } from "@/utils/system";
import { computed, onMounted, ref } from "vue";
import { hasPermission } from "@/utils/system";

const initParams = {
  pageNum: 1,
  pageSize: 10,
  title: "",
  status: "",
};

// 搜索条件
const params = ref({ ...initParams });
// 表格数据
const tableData = ref([]);
// 表格数据总数
const total = ref(0);
// 处理结果弹窗显示
const processVisible = ref(false);
// 处理结果表单
const processForm = ref({
  id: null,
  processResult: ""
});

// 批量删除相关
const multipleSelection = ref([]);
const selectedIds = computed(() => {
  return multipleSelection.value.map((item) => item.id);
});
const hasSelected = computed(() => {
  return multipleSelection.value.length > 0;
});

// 状态字典
const statusDict = {
  0: { text: "待处理", type: "warning" },
  1: { text: "处理中", type: "primary" },
  2: { text: "处理完成", type: "success" }
};

// 获取表格数据
const getTableData = async () => {
  const res = await http.get(
    `/report/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&title=${params.value.title}&status=${params.value.status}`
  );
  if (res.code === 200) {
    tableData.value = res.data.records;
    total.value = res.data.total;
  }
};

// 搜索
const handleSearch = async () => {
  await getTableData();
};

// 重置
const handleReset = () => {
  params.value = { ...initParams };
  getTableData();
};

// 开始处理
const handleStartProcess = async (row) => {
  const res = await http.post(`/report/edit`, { ...row, status: 1 });
  if (res.code === 200 && res.data) {
    ElMessage.success("已开始处理");
    await getTableData();
  } else {
    ElMessage.error("操作失败");
  }
};

// 处理完成
const handleCompleteProcess = (row) => {
  processForm.value.id = row.id;
  processForm.value.processResult = "";
  processVisible.value = true;
};

// 提交处理结果
const submitProcessResult = async () => {
  if (!processForm.value.processResult.trim()) {
    ElMessage.warning("请输入处理结果");
    return;
  }
  
  const res = await http.post(`/report/edit`, {
    id: processForm.value.id,
    status: 2,
    processResult: processForm.value.processResult
  });
  
  if (res.code === 200) {
    ElMessage.success("处理完成");
    processVisible.value = false;
    await getTableData();
  } else {
    ElMessage.error("操作失败");
  }
};

// 删除
const delRow = async (id) => {
  const res = await http.get(`/report/del?id=${id}`);
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
    ElMessage.warning("请先选择要删除的举报");
    return;
  }
  const res = await http.post("/report/batchDel", selectedIds.value);
  if (res.code === 200) {
    ElMessage.success("批量删除成功");
    multipleSelection.value = [];
    await getTableData();
  } else {
    ElMessage.error("批量删除失败");
  }
};

// 多选
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection;
};

// 查看详情
const viewDetail = (row) => {
  ElMessageBox.alert(
    `<div style="text-align: left;">
      <p><strong>标题：</strong>${row.title}</p>
      <p><strong>描述：</strong>${row.description}</p>
      <p><strong>地点：</strong>${row.location || '未填写'}</p>
      <p><strong>发生时间：</strong>${row.occurrenceTime || '未填写'}</p>
      <p><strong>联系方式：</strong>${row.contactInfo || '未填写'}</p>
      <p><strong>举报人：</strong>${row.isAnonymous ? '匿名' : (row.reporterName || '未知')}</p>
      <p><strong>状态：</strong>${statusDict[row.status].text}</p>
      ${row.processResult ? `<p><strong>处理结果：</strong>${row.processResult}</p>` : ''}
    </div>`,
    '举报详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确定'
    }
  );
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
        <p class="label-text">标题：</p>
        <el-input v-model="params.title" placeholder="请输入标题" />
      </div>
      <div class="flex form-item">
        <p class="label-text">状态：</p>
        <el-select v-model="params.status" placeholder="请选择状态" clearable style="width: 200px;">
          <el-option label="待处理" :value="0" />
          <el-option label="处理中" :value="1" />
          <el-option label="处理完成" :value="2" />
        </el-select>
      </div>
      <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
      <el-button type="warning" :icon="Refresh" @click="handleReset">重置</el-button>
    </header>
    
    <main>
      <div class="flex op-box">
        <el-button
          v-if="hasPermission('report', '删除')"
          :icon="Delete"
          type="danger"
          :disabled="!hasSelected"
          @click="batchDel"
        >批量删除</el-button>
      </div>
      
      <!-- 表格 -->
      <el-table
        :data="tableData"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="location" label="地点" width="120" />
        <el-table-column prop="occurrenceTime" label="发生时间" width="180" />
        <el-table-column prop="reporterName" label="举报人" width="120">
          <template #default="scope">
            {{ scope.row.isAnonymous ? '匿名' : (scope.row.reporterName || '未知') }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="statusDict[scope.row.status].type">
              {{ statusDict[scope.row.status].text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="imgUrl" label="图片证据" width="100">
          <template #default="scope">
            <el-image
              v-if="scope.row.imgUrl"
              :src="getImageUrl(scope.row.imgUrl)"
              :preview-src-list="[getImageUrl(scope.row.imgUrl)]"
              :preview-teleported="true"
              :hide-on-click-modal="true"
              :lazy="true"
              fit="cover"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="processResult" label="处理结果" width="200" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.processResult">{{ scope.row.processResult }}</span>
            <span v-else class="no-result">暂无处理结果</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="280">
          <template #default="scope">
            <el-button
              type="info"
              :icon="View"
              size="small"
              @click="viewDetail(scope.row)"
            >查看详情</el-button>
            
            <!-- 待处理状态：显示开始处理按钮 -->
            <el-button
              v-if="scope.row.status === 0"
              type="primary"
              size="small"
              @click="handleStartProcess(scope.row)"
            >开始处理</el-button>
            
            <!-- 处理中状态：显示处理完成按钮 -->
            <el-button
              v-if="scope.row.status === 1"
              type="success"
              size="small"
              @click="handleCompleteProcess(scope.row)"
            >处理完成</el-button>
            
            <!-- 处理完成状态：显示删除按钮 -->
            <el-popconfirm
              v-if="scope.row.status === 2 && hasPermission('report', '删除')"
              title="您确定要删除吗?"
              @confirm="delRow(scope.row.id)"
            >
              <template #reference>
                <el-button type="danger" size="small" :icon="Delete">删除</el-button>
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
    
    <!-- 处理结果弹窗 -->
    <el-dialog
      v-model="processVisible"
      title="输入处理结果"
      width="500"
    >
      <el-form :model="processForm" label-width="100">
        <el-form-item label="处理结果" prop="processResult">
          <el-input
            v-model="processForm.processResult"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }"
            placeholder="请输入处理结果"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitProcessResult">确认</el-button>
          <el-button @click="processVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
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

:deep(.el-image) {
  width: 55px;
  height: 55px;
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--el-fill-color-light);
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}

.no-result {
  color: #909399;
  font-style: italic;
}
</style>