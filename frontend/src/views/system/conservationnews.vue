<script setup>
import http from "@/utils/http";
import { Plus, Search, Refresh, Edit, Delete } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { getImageUrl } from "@/utils/system";
import Config from "@/config/index";
import { Picture as IconPicture } from "@element-plus/icons-vue";
import { computed, onMounted, ref } from "vue";
import { hasPermission } from "@/utils/system";

const initParams = {
  pageNum: 1,
  pageSize: 10,
  title: "",
  type: "",
  status: "",
};
// 搜索条件
const params = ref({ ...initParams });
// 初始参数
const initForm = {
  title: "",
  intro: "",
  content: "",
  type: "",
  source: "",
  imgUrl: "",
  status: 1,
};
// 表单数据
const form = ref({ ...initForm });
// 表格数据
const tableData = ref([]);
// 表格数据总数
const total = ref(0);
// 控制弹窗显示
const visible = ref(false);

// 批量删除相关
const multipleSelection = ref([]);
const selectedIds = computed(() => {
  return multipleSelection.value.map((item) => item.id);
});
const hasSelected = computed(() => {
  return multipleSelection.value.length > 0;
});

const isEdit = computed(() => {
  return form.value.id > 0;
});
const dialogTitle = computed(() => {
  return isEdit.value ? "编辑" : "新增";
});
// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref(
  form.value?.imgUrl ? `${Config.baseUrl}${form.value?.imgUrl}` : ""
);

// 新闻类型选项
const typeOptions = [
  { label: "生态保护", value: "生态保护" },
  { label: "环境治理", value: "环境治理" },
  { label: "野生动物保护", value: "野生动物保护" },
  { label: "湿地保护", value: "湿地保护" },
  { label: "科研成果", value: "科研成果" },
  { label: "政策法规", value: "政策法规" },
];

// 状态选项
const statusOptions = [
  { label: "启用", value: 1 },
  { label: "禁用", value: 0 },
];

// 获取表格数据
const getTableData = async () => {
  const res = await http.get(
    `/conservationNews/page?pageNum=${params.value.pageNum}&pageSize=${params.value.pageSize}&title=${params.value.title}&type=${params.value.type}&status=${params.value.status}`
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

// 新增
const handleAdd = () => {
  form.value = { ...initForm };
  imageUrl.value = "";
  visible.value = true;
};

// 编辑
const handleEdit = (row) => {
  imageUrl.value = row.imgUrl ? getImageUrl(row.imgUrl) : "";
  form.value = { ...row };
  visible.value = true;
};

// 删除
const delRow = async (id) => {
  const res = await http.get(`/conservationNews/del?id=${id}`);
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
    ElMessage.warning("请先选择要删除的新闻");
    return;
  }
  const res = await http.post("/conservationNews/batchDel", selectedIds.value);
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

// 确认按钮
const submit = async () => {
  if (isEdit.value) {
    const res = await http.post(`/conservationNews/edit`, form.value);
    if (res.code === 200) {
      ElMessage.success("编辑成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("编辑失败");
    }
  } else {
    const res = await http.post(`/conservationNews/add`, form.value);
    if (res.code === 200) {
      ElMessage.success("新增成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("新增失败");
    }
  }
};

// 处理关闭弹窗
const handleClose = () => {
  visible.value = false;
};

/** 文件上传前的钩子函数 */
const beforeImageUpload = (rawFile) => {
  if (!rawFile.type.includes("image")) {
    ElMessage.error("文件类型错误，只允许上传图片文件");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("图片超过2MB限制");
    return false;
  }
  return true;
};

/** 文件 上传成功回调 */
const handleImageSuccess = (response) => {
  form.value.imgUrl = response.data;
  imageUrl.value = `${Config.baseUrl}${response.data}`;
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
        <p class="label-text">类型：</p>
        <el-select v-model="params.type" placeholder="请选择类型" clearable style="width: 200px;">
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="flex form-item">
        <p class="label-text">状态：</p>
        <el-select v-model="params.status" placeholder="请选择状态" clearable style="width: 200px;">
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
          v-if="hasPermission('conservationnews', '新增')"
          :icon="Plus"
          type="primary"
          @click="handleAdd"
          >新增</el-button
        >
        <el-button
          v-if="hasPermission('conservationnews', '删除')"
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
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="intro" label="简介" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="source" label="来源" width="120" />
        <el-table-column prop="image" label="图片" width="100">
          <template #default="scope">
            <el-image
              v-if="scope.row.imgUrl"
              :src="getImageUrl(scope.row.imgUrl)"
              :preview-src-list="[getImageUrl(scope.row.imgUrl)]"
              :preview-teleported="true"
              :hide-on-click-modal="true"
              :lazy="true"
              :initial-index="0"
              fit="cover"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon>
                    <icon-picture />
                  </el-icon>
                </div>
              </template>
            </el-image>
            <span v-else>无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览数" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button
              v-if="hasPermission('conservationnews', '编辑')"
              type="primary"
              :icon="Edit"
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-popconfirm
              v-if="hasPermission('conservationnews', '删除')"
              title="您确定要删除吗?"
              @confirm="delRow(scope.row.id)"
            >
              <template #reference>
                <el-button type="danger" :icon="Delete">删除</el-button>
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
    <el-dialog
      v-model="visible"
      :title="dialogTitle"
      width="800"
      :before-close="handleClose"
    >
      <el-form :model="form" label-width="120">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            v-model="form.intro"
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 8 }"
            v-model="form.content"
          />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="来源" prop="source">
          <el-input v-model="form.source" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <el-upload
            class="image-uploader"
            :action="uploadUrl"
            :headers="uploadData"
            :show-file-list="false"
            :on-success="handleImageSuccess"
            :before-upload="beforeImageUpload"
          >
            <el-image
              v-if="imageUrl"
              :src="imageUrl"
              style="width: 100%; height: 100%"
            />
            <el-icon v-else class="image-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit()"> 确认 </el-button>
          <el-button @click="visible = false">取消</el-button>
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

.image-uploader {
  width: 178px;
  height: 178px;
  overflow: hidden;
  border: 1px dashed var(--el-border-color);

  &:hover {
    border-color: var(--el-color-primary);
  }
}

.image {
  width: 100%;
  height: 100%;
}

.el-icon.image-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
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
</style>