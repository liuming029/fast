<template>
  <div class="app-container">
    <!-- 顶部搜索 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="包裹规格" prop="size">
        <el-input
            v-model="queryParams.size"
            placeholder="请输入包裹规格"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="宿舍楼" prop="building">
        <el-input
            v-model="queryParams.building"
            placeholder="请输入宿舍楼"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="快递站点" prop="station">
        <el-input
            v-model="queryParams.station"
            placeholder="请输入快递站点"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px;" placeholder="请选择状态">
          <el-option label="待接单" value="待接单" />
          <el-option label="配送中" value="配送中" />
          <el-option label="已完成" value="已完成" />
          <el-option label="已取消" value="已取消" />
        </el-select>
      </el-form-item>
      <el-form-item label="下单用户" prop="userName">
        <el-input
            v-model="queryParams.userName"
            placeholder="请输入下单用户"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="配送员" prop="rider">
        <el-input
            v-model="queryParams.rider"
            placeholder="请输入配送员"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 顶部按钮 -->
    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--            type="primary"-->
<!--            plain-->
<!--            icon="Plus"-->
<!--            @click="handleAdd"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
        >修改</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--            type="danger"-->
<!--            plain-->
<!--            icon="Delete"-->
<!--            :disabled="multiple"-->
<!--            @click="handleDelete"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
    </el-row>

    <!-- 表格 -->
    <el-table @row-click="clickRow" ref="tableRef" highlight-current-row
              border v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" type="index" :index="indexMethod" />
      <el-table-column label="取件码" align="center" prop="code" />
      <el-table-column label="包裹规格" align="center" prop="size" />
      <el-table-column label="宿舍楼" align="center" prop="building" />
      <el-table-column label="快递站点" align="center" prop="station" />
      <el-table-column label="寝室号/联系人" align="center" prop="room" />
      <el-table-column label="总价" align="center" prop="totalPrice" />
      <el-table-column label="状态" align="center" prop="status" >
        <template #default ="scope">
          <el-tag type="warning" v-if="scope.row.status==='待接单'">{{scope.row.status}}</el-tag>
          <el-tag type="primary" v-if="scope.row.status==='配送中'">{{scope.row.status}}</el-tag>
          <el-tag type="success" v-if="scope.row.status==='已完成'">{{scope.row.status}}</el-tag>
          <el-tag type="danger" v-if="scope.row.status==='已取消'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="下单用户" align="center" prop="userName" />
      <el-table-column label="配送员" align="center" prop="rider" />
      <el-table-column label="提交时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改订单对话框 -->
    <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
      <el-form ref="orderRef" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="取件码" prop="code">
          <el-input v-model="form.code" placeholder="请输入取件码" />
        </el-form-item>
        <el-form-item label="包裹规格ID" prop="sizeId">
          <el-input v-model="form.sizeId" placeholder="请输入包裹规格ID" />
        </el-form-item>
        <el-form-item label="宿舍楼ID" prop="buildingId">
          <el-input v-model="form.buildingId" placeholder="请输入宿舍楼ID" />
        </el-form-item>
        <el-form-item label="快递站点ID" prop="stationId">
          <el-input v-model="form.stationId" placeholder="请输入快递站点ID" />
        </el-form-item>
        <el-form-item label="寝室号/联系人" prop="room">
          <el-input v-model="form.room" placeholder="请输入寝室号/联系人" />
        </el-form-item>
        <el-form-item label="总价" prop="totalPrice">
          <el-input v-model="form.totalPrice" placeholder="请输入总价" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>

        <el-form-item label="配送员ID" prop="riderId">
          <el-input v-model="form.riderId" placeholder="请输入配送员ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </vxe-modal>
  </div>
</template>

<script setup>
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/take/order"
import {getToken} from "@/utils/auth.js";
import {ElMessage, ElMessageBox} from "element-plus";
const baseURL = import.meta.env.VITE_APP_BASE_API

const queryRef = ref()
const orderRef = ref()
const orderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)
const tableRef = ref()

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sizeId: null,
    buildingId: null,
    stationId: null,
    status: null,
    userId: null,
    riderId: null,
    size:null,
    buliding:null,
    station:null,
    rider:null,
    userName: null,
  },
  rules: {
    orderId: [
      { required: true, message: "订单ID不能为空", trigger: "blur" }
    ],
    code: [
      { required: true, message: "取件码不能为空", trigger: "blur" }
    ],
    sizeId: [
      { required: true, message: "包裹规格ID不能为空", trigger: "blur" }
    ],
    buildingId: [
      { required: true, message: "宿舍楼ID不能为空", trigger: "blur" }
    ],
    stationId: [
      { required: true, message: "快递站点ID不能为空", trigger: "blur" }
    ],
    room: [
      { required: true, message: "寝室号/联系人不能为空", trigger: "blur" }
    ],
    totalPrice: [
      { required: true, message: "总价不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

//点击行 获取行
const clickRow = (row) => {
  selectedRow.value = row; // 更新选中的行
  const table = tableRef.value;
  // 清除所有已选中的行
  table.clearSelection();
  // 选中当前点击的行
  table.toggleRowSelection(row, true);
}

/** 自定义序号 */
const indexMethod = (index) => {
  let pageNum = queryParams.value.pageNum - 1;
  if((pageNum!==-1 && pageNum!==0)){
    return (index + 1) + (pageNum  * queryParams.value.pageSize);
  }else{
    return (index + 1)
  }
}

/** 查询订单列表 */
const getList = () => {
  loading.value = true
  listOrder(queryParams.value).then(response => {
    orderList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
const cancel = () => {
  open.value = false
  reset()
}

// 表单重置
const reset = () => {
  form.value = {
    orderId: null,
    code: null,
    sizeId: null,
    buildingId: null,
    stationId: null,
    room: null,
    totalPrice: null,
    status: null,
    remark: null,
    userId: null,
    riderId: null,
    createTime: null
  }
  if (orderRef.value) {
    orderRef.value.resetFields()
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryRef.value.resetFields()
  handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.orderId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "添加订单"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
  reset()
  const _orderId = row.orderId || ids.value
  getOrder(_orderId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改订单"
  })
}

/** 提交按钮 */
const submitForm = () => {
  orderRef.value.validate(valid => {
    if (valid) {
      if (form.value.orderId != null) {
        updateOrder(form.value).then(response => {
          ElMessage.success("修改成功")
          open.value = false
          getList()
        })
      } else {
        addOrder(form.value).then(response => {
          ElMessage.success("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
const handleDelete = (row) => {
  const _orderIds = row.orderId || ids.value
  ElMessageBox.confirm('是否确认删除该项数据？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(function() {
    return delOrder(_orderIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {})
}

onMounted(() => {
  getList()
})
</script>
