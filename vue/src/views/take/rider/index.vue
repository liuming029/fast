<template>
  <div class="app-container">
    <!-- 顶部搜索 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="真实姓名" prop="realName">
        <el-input
            v-model="queryParams.realName"
            placeholder="请输入真实姓名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学号" prop="studentId">
        <el-input
            v-model="queryParams.studentId"
            placeholder="请输入学号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="认证状态" prop="status">

        <el-select style="width: 200px" v-model="queryParams.status" placeholder="请选择认证状态">
          <el-option label="审核中" value="审核中"/>
          <el-option label="已拒绝" value="已拒绝"/>
          <el-option label="已通过" value="已通过"/>

        </el-select>
      </el-form-item>
      <el-form-item label="认证用户" prop="userName">
        <el-input
            v-model="queryParams.userName"
            placeholder="请输入认证用户"
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
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--            type="success"-->
<!--            plain-->
<!--            icon="Edit"-->
<!--            :disabled="single"-->
<!--            @click="handleUpdate"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
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
              border v-loading="loading" :data="riderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" type="index" :index="indexMethod" />
      <el-table-column label="真实姓名" align="center" prop="realName" />
      <el-table-column label="身份证号" align="center" prop="idCard" />
      <el-table-column label="手机号码" align="center" prop="phone" />
      <el-table-column label="学号" align="center" prop="studentId" />
      <el-table-column label="学生证" align="center" prop="studentCardImage" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.studentCardImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证" align="center" prop="idCardImage" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.idCardImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="认证状态" align="center" prop="status" >
        <template #default="scope">
          <el-tag type="primary" v-if="scope.row.status ==='审核中'">{{scope.row.status}}</el-tag>
          <el-tag type="danger" v-if="scope.row.status ==='已拒绝'">{{scope.row.status}}</el-tag>
          <el-tag type="success" v-if="scope.row.status ==='已通过'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="认证用户" align="center" prop="userName" />
      <el-table-column label="认证提交时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"width="200">
        <template #default="scope">
          <el-button v-if="scope.row.status=== '审核中'"  type="success"  @click="handleApproved(scope.row)">
            审核通过
          </el-button>
          <el-button v-if="scope.row.status=== '审核中'" type="danger"  @click="handleFail(scope.row)">
          不通过
          </el-button>
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

    <!-- 添加或修改配送员对话框 -->
    <vxe-modal :title="title" v-model="open" width="400px" height="500px" show-maximize showFooter resize>
      <el-form ref="riderRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="学生证" prop="studentCardImage">
          <image-upload v-model="form.studentCardImage"/>
        </el-form-item>
        <el-form-item label="身份证" prop="idCardImage">
          <image-upload v-model="form.idCardImage"/>
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
import { listRider, getRider, delRider, addRider, updateRider } from "@/api/take/rider"
import {getToken} from "@/utils/auth.js";
import {ElMessage, ElMessageBox} from "element-plus";
const baseURL = import.meta.env.VITE_APP_BASE_API

const queryRef = ref()
const riderRef = ref()
const riderList = ref([])
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

//审核通过
const handleApproved = (row) => {
  const item = {
    riderId: row.riderId,
    userId: row.userId,
    status: '已通过'
  }
  ElMessageBox.confirm('是否确认审核通过？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    updateRider(item).then(res => {
      ElMessage.success("审核完成")
      getList()
    })
  }).catch(() => {})
}

//审核不通过
const handleFail = (row) => {
  const item = {
    riderId: row.riderId,
    status: '已拒绝'
  }
  ElMessageBox.confirm('是否确认审核不通过？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    updateRider(item).then(res => {
      ElMessage.success("审核完成")
      getList()
    })
  }).catch(() => {})
}


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    realName: null,
    studentId: null,
    status: null,
    userId: null,
    userName: null
  },
  rules: {
    riderId: [
      { required: true, message: "配送员ID不能为空", trigger: "blur" }
    ],
    realName: [
      { required: true, message: "真实姓名不能为空", trigger: "blur" }
    ],
    idCard: [
      { required: true, message: "身份证号不能为空", trigger: "blur" }
    ],
    phone: [
      { required: true, message: "手机号码不能为空", trigger: "blur" }
    ],
    studentId: [
      { required: true, message: "学号不能为空", trigger: "blur" }
    ],
    studentCardImage: [
      { required: true, message: "学生证不能为空", trigger: "blur" }
    ],
    idCardImage: [
      { required: true, message: "身份证不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "认证状态不能为空", trigger: "blur" }
    ],
    userId: [
      { required: true, message: "用户ID不能为空", trigger: "blur" }
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

/** 查询配送员列表 */
const getList = () => {
  loading.value = true
  listRider(queryParams.value).then(response => {
    riderList.value = response.rows
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
    riderId: null,
    realName: null,
    idCard: null,
    phone: null,
    studentId: null,
    studentCardImage: null,
    idCardImage: null,
    status: null,
    userId: null,
    createTime: null
  }
  if (riderRef.value) {
    riderRef.value.resetFields()
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
  ids.value = selection.map(item => item.riderId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "添加配送员"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
  reset()
  const _riderId = row.riderId || ids.value
  getRider(_riderId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改配送员"
  })
}

/** 提交按钮 */
const submitForm = () => {
  riderRef.value.validate(valid => {
    if (valid) {
      if (form.value.riderId != null) {
        updateRider(form.value).then(response => {
          ElMessage.success("修改成功")
          open.value = false
          getList()
        })
      } else {
        addRider(form.value).then(response => {
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
  const _riderIds = row.riderId || ids.value
  ElMessageBox.confirm('是否确认删除该项数据？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(function() {
    return delRider(_riderIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {})
}

onMounted(() => {
  getList()
})
</script>
