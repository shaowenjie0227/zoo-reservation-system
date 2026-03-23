<script setup lang="ts">
import { ticketApi } from "@/api/ticket";
import { Plus, Search, Refresh, Edit, Delete, Picture as IconPicture } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { computed, onMounted, ref } from "vue";
import { hasPermission } from "@/utils/system";
import { getImageUrl } from "@/utils/system";
import Config from "@/config/index";
import http from "@/utils/http";

const initParams = {
  pageNum: 1,
  pageSize: 10,
  name: "",
  travelId: "",
  status: ""
};

// 搜索条件
const params = ref({ ...initParams });

// 初始表单数据
const initForm = {
  name: "",
  oriPrice: 199,
  price: 100,
  description: "",
  travelId: "",
  status: 1,
  imgUrl: "",
  validStartTime: "",
  validEndTime: "",
  inventory: 99,
  validityDays: 30
};

// 表单数据
const form = ref({ ...initForm });

// 文件上传地址
const uploadUrl = ref(`${Config.baseUrl}/file/upload`);
// 文件上传携带的数据
const uploadData = {
  authorization: `${localStorage.getItem("token")}`,
};
// 图片上传后的回调地址
const imageUrl = ref("");

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入门票名称', trigger: 'blur' }],
  oriPrice: [
    { required: true, message: '请输入门票原价', trigger: 'blur' },
    { type: 'number', min: 0, message: '原价不能小于0', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入门票价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格不能小于0', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (form.value.oriPrice > 0 && value > form.value.oriPrice) {
          callback(new Error('原价不能小于现价'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  travelId: [{ required: true, message: '请选择关联景点', trigger: 'change' }],
  status: [{ required: true, message: '请选择门票状态', trigger: 'change' }],
  inventory: [
    { required: true, message: '请输入库存数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存不能小于0', trigger: 'blur' }
  ],
  validityDays: [
    { required: true, message: '请输入有效期天数', trigger: 'blur' },
    { type: 'number', min: 1, message: '有效期至少为1天', trigger: 'blur' }
  ]
};

// 表格数据
const tableData = ref([]);
const formRef = ref();
// 表格数据总数
const total = ref(0);

// 控制弹窗显示
const visible = ref(false);

// 景点列表
const travelList = ref([]);

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
  return isEdit.value ? "编辑门票" : "新增门票";
});

// 获取景点列表
const getTravelList = async () => {
  const res = await http.get('/travel/list');
  if (res.code === 200) {
    travelList.value = res.data;
  }
};

// 获取表格数据
const getTableData = async () => {
  const res = await ticketApi.getTicketPage(params.value);
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

// 新增
const handleAdd = () => {
  form.value = { ...initForm };
  imageUrl.value = "";
  visible.value = true;
};

// 编辑
const handleEdit = (row) => {
  form.value = { ...row };
  imageUrl.value = row.imgUrl ? getImageUrl(row.imgUrl) : "";
  visible.value = true;
};

// 删除
const delRow = async (id) => {
  const res = await ticketApi.deleteTicket(id);
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
  const res = await ticketApi.batchDeleteTickets(selectedIds.value);
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
  await formRef.value?.validate();

  if (isEdit.value) {
    const res = await ticketApi.editTicket(form.value);
    if (res.code === 200) {
      ElMessage.success("编辑成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("编辑失败");
    }
  } else {
    const res = await ticketApi.addTicket(form.value);
    if (res.code === 200) {
      ElMessage.success("新增成功");
      visible.value = false;
      await getTableData();
    } else {
      ElMessage.error("新增失败");
    }
  }
};

/** 文件上传前的钩子函数 */
const beforeAvatarUpload = (rawFile) => {
  if (!rawFile.type.includes("image")) {
    ElMessage.error("文件类型错误，只允许上传图片文件");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("图片超过2MB限制");
    return false;
  }
  return true;
};

/** 文件上传成功回调 */
const handleAvatarSuccess = (response) => {
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
  await getTravelList();
  await getTableData();
});
</script>

<template>
  <div>
    <header class="flex">
      <div class="flex form-item">
        <p class="label-text">门票名称：</p>
        <el-input v-model="params.name" placeholder="请输入门票名称" />
      </div>
      <div class="flex form-item">
        <p class="label-text">关联景点：</p>
        <el-select v-model="params.travelId" placeholder="请选择景点" clearable style="width: 200px;">
          <el-option
            v-for="item in travelList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>
      <div class="flex form-item">
        <p class="label-text">状态：</p>
        <el-select v-model="params.status" placeholder="请选择状态" clearable style="width: 200px;">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </div>
      <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
      <el-button type="warning" :icon="Refresh" @click="handleReset">重置</el-button>
    </header>
    <main>
      <div class="flex op-box">
        <el-button
          v-if="hasPermission('ticket', '新增')"
          :icon="Plus"
          type="primary"
          @click="handleAdd"
        >新增</el-button>
        <el-button
          v-if="hasPermission('ticket', '删除')"
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
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="门票名称" width="150" />
        <el-table-column prop="oriPrice" label="原价" width="100">
          <template #default="scope">
            <span v-if="scope.row.oriPrice > 0">¥{{ scope.row.oriPrice }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="现价" width="100">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="travelName" label="关联景点" width="150" />
        <el-table-column prop="inventory" label="库存" width="80" />
        <el-table-column prop="validityDays" label="有效期" width="100">
          <template #default="scope">
            {{ scope.row.validityDays }}天
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button
              v-if="hasPermission('ticket', '编辑')"
              type="primary"
              size="small"
              :icon="Edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-popconfirm
              title="您确定要删除吗?"
              @confirm="delRow(scope.row.id)"
            >
              <template #reference>
                <el-button
                  v-if="hasPermission('ticket', '删除')"
                  type="danger"
                  size="small"
                  :icon="Delete"
                >删除</el-button>
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
    <el-dialog v-model="visible" :title="dialogTitle" width="600" top="5%">
      <el-form :model="form" :rules="rules" label-width="120" ref="formRef">
        <el-form-item label="门票名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入门票名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="门票原价" prop="oriPrice">
              <el-input-number v-model="form.oriPrice" :min="0" :precision="2" style="width: 100%" placeholder="0表示无原价" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="门票现价" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="关联景点" prop="travelId">
          <el-select v-model="form.travelId" placeholder="请选择景点" style="width: 100%">
            <el-option
              v-for="item in travelList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="库存数量" prop="inventory">
              <el-input-number v-model="form.inventory" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效期天数" prop="validityDays">
              <el-input-number v-model="form.validityDays" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="门票描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入门票描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="visible = false">取消</el-button>
          <el-button type="primary" @click="submit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
  align-items: center;
  gap: 10px;
}

.form-item {
  margin-right: 20px;
}

.label-text {
  white-space: nowrap;
  margin-right: 8px;
  font-weight: 500;
}

.op-box {
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}
</style>