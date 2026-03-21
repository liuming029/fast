<template>
  <div>
    <!-- 顶部搜索 -->
    <el-form :model="queryParms" ref="queryRef" :inline="true" label-width="70px">
      <el-form-item label="站点名称" prop="name">
        <el-input v-model="queryParms.name" placeholder="请输入站点名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 顶部按钮 -->
    <el-row :gutter="10" style="padding: 10px 0">
      <!-- 新增按钮 -->
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <!-- 修改按钮 -->
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleBatchUpdate">修改</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table ref="tableRef" highlight-current-row border v-loading="loading" :data="stationList"
              @selection-change="handleSelectionChange" @row-click="clickRow">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
      <el-table-column label="站点名称" align="center" prop="name"/>
      <el-table-column label="排序" align="center" prop="sort"/>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination :total="total" v-show="total > 0"
                v-model:page="queryParms.pageNum"
                v-model:limit="queryParms.pageSize"
                @pagination="getList"/>

    <!-- 弹窗 -->
    <vxe-modal :title="title" v-model="open" width="600px" show-maximize showFooter resize>
      <el-form ref="stationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number style="width:100%" v-model="form.sort" placeholder="请输入排序"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm()">确定</el-button>
        <el-button @click="open=false">取消</el-button>
      </template>
    </vxe-modal>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listStation, addStation, getStation, updateStation } from "@/api/take/station.js";
import Pagination from "@/components/Pagination/index.vue";
import { ElMessage } from "element-plus";

// 顶部搜索表单实例
const queryRef = ref()
// 表格实例
const tableRef = ref()
// 对话框title名称
const title = ref('')
// 对话框是否打开
const open = ref(false)
// 对话框表单实例
const stationRef = ref()

// ✅ 修正：统一用 form（不是 from）
const form = ref({
  stationId: null,
  name: null,
  sort: null
})

// 表单校验规则
const rules = ref({
  name: [{ required: true, message: "站点名称不能为空", trigger: "blur" }],
  sort: [{ required: true, message: "排序不能为空", trigger: "blur" }]
})

// 新增按钮
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "新增快递站点"
}

// ✅ 修正：行内修改按钮（传row）
const handleUpdate = (row) => {
  reset()
  const stationId = row.stationId
  getStation(stationId).then(res => {
    form.value = res.data // ✅ 赋值给正确的 form
    open.value = true
    title.value = "修改快递站点"
  })
}

// 顶部批量修改按钮（兼容多选）
const handleBatchUpdate = () => {
  if (ids.value.length !== 1) {
    ElMessage.warning('请选择一条数据进行修改')
    return
  }
  getStation(ids.value[0]).then(res => {
    form.value = res.data
    open.value = true
    title.value = "修改快递站点"
  })
}

// ✅ 修正：表单重置函数
const reset = () => {
  form.value = {
    stationId: null,
    name: null,
    sort: null
  }
  // 重置表单校验
  if (stationRef.value) {
    stationRef.value.resetFields()
  }
}

// ✅ 修正：提交按钮逻辑
const submitForm = () => {
  stationRef.value.validate((valid) => {
    if (valid) {
      // 根据 stationId 判断新增/修改
      if (form.value.stationId) {
        // 修改逻辑
        updateStation(form.value).then(() => {
          ElMessage.success('修改成功~')
          open.value = false
          getList()
        })
      } else {
        // 新增逻辑
        addStation(form.value).then(() => {
          ElMessage.success('新增成功~')
          open.value = false
          getList()
        })
      }
    }
  })
}

// 加载状态
const loading = ref(false)
// 当前选中的行的id数组
const ids = ref([])
// 当前是否未选中单行
const single = ref(true)
// 当前是否未选中多行
const miltiple = ref(true)
// 查询参数
const queryParms = ref({
  pageNum: 1,
  pageSize: 10,
  name: null
})
// 快递站点列表数据
const stationList = ref([])
// 数据总数
const total = ref(0)
// 当前选中的行
const selectedRow = ref({})

// 点击行 获取行
const clickRow = (row) => {
  selectedRow.value = row
  const table = tableRef.value;
  table.clearSelection()
  table.toggleRowSelection(row, true)
}

// 自定义序号
const indexMethod = (index) => {
  let pageNum = queryParms.value.pageNum - 1
  return (index + 1) + (pageNum * queryParms.value.pageSize)
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.stationId)
  single.value = selection.length !== 1
  miltiple.value = !selection.length
}

// 搜索按钮
const handleQuery = () => {
  queryParms.value.pageNum = 1
  getList()
}

// 重置按钮
const resetQuery = () => {
  queryRef.value.resetFields()
  handleQuery()
}

// 查询数据
const getList = () => {
  loading.value = true
  listStation(queryParms.value).then(res => {
    stationList.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

// 组件挂载完成后执行
onMounted(() => {
  getList()
})
</script>

<style scoped>
</style>