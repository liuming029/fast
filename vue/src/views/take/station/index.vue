<template>
  <div>
    <!-- 顶部搜索 -->
    <el-form :model="queryParms" ref="queryRef" :inline="true" label-width="70px">
      <el-form-item label="站点名称" prop="name">
        <el-input v-model="queryParms.name"
                  placeholder="请输入站点名称"
                  clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table ref="tableRef" highlight-current-row border v-loading="loading" :data="stationList"
              @selection-change="handleSelectionChange" @row-click="clickRow">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
      <el-table-column label="站点名称" align="center" prop="name"/>
      <el-table-column label="排序" align="center" prop="sort"/>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination :total="total" v-show="total > 0"
                v-model:page="queryParms.pageNum"
                v-model:limit="queryParms.pageSize"
                @pagination="getList"
    />

  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {listStation} from "@/api/take/station.js";
import Pagination from "@/components/Pagination/index.vue";

//顶部搜索表单实例
const queryRef = ref()

//表格实例
const tableRef = ref()

//加载状态
const loading = ref(false)

//当前选中的行的id数组
const ids = ref([])

//当前是否未选中单行
const single = ref(true)

//当前是否未选中多行
const miltiple = ref(true)

//查询参数
const queryParms = ref({
  pageNum: 1,
  pageSize: 10,
  name: null
})

//快递站点列表数据
const stationList = ref([])

//数据总数
const total = ref(0)

//当前选中的行
const selectedRow = ref({})

//点击行 获取行
const clickRow = (row) => {
  selectedRow.value = row
  const table = tableRef.value;
  //清除所有已选中的行
  table.clearSelection()
  //选中当前点击的行
  table.toggleRowSelection(row, true)
}

//自定义序号
const indexMethod = (index) => {
  let pageNum = queryParms.value.pageNum - 1
  if ((pageNum !== -1 && pageNum !== 0)) {
    return (index + 1) + (pageNum * queryParms.value.pageSize)
  } else {
    return (index + 1)
  }
}

//多选框选中数据
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.stationId)
  single.value = selection.length != 1
  miltiple.value = !selection.length
}

//搜索按钮
const handleQuery = () => {
  queryParms.value.pageNum = 1
  getList()
}

//重置按钮
const resetQuery = () => {
  queryRef.value.resetFields()
  handleQuery()
}

//查询数据
const getList = () => {
  loading.value = true
  listStation(queryParms.value).then(res => {
    stationList.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

//组件挂载完成后执行
onMounted(() => {
  getList()
})
</script>

<style scoped>

</style>
