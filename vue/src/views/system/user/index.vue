<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col>
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="用户名称" prop="userName">
            <el-input v-model="queryParams.userName" placeholder="请输入用户名称" clearable
                      style="width: 240px" @keyup.enter="handleQuery"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">
              修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">
              删除
            </el-button>
          </el-col>
        </el-row>

        <el-table border v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="用户编号" align="center" key="userId" prop="userId"/>
          <el-table-column label="用户名称" align="center" key="userName" prop="userName"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createTime" width="200"/>
          <el-table-column label="操作" align="center" width="150"
                           class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top" v-if="scope.row.userId !== 1">
                <el-button link type="primary" icon="Edit"
                           @click="handleUpdate(scope.row)"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top" v-if="scope.row.userId !== 1">
                <el-button link type="primary" icon="Delete"
                           @click="handleDelete(scope.row)"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                    v-model:limit="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <vxe-modal :title="title" v-model="open" width="600px" show-maximize showFooter resize>
      <el-form :model="form" :rules="rules" ref="userRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleId" placeholder="请选择角色">
                <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName"
                           :value="item.roleId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == null" label="用户名称" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == null" label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password"
                        maxlength="20" show-password/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option value="0" label="男"/>
                <el-option value="1" label="女"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
import {addUser, delUser, getUser, listUser, updateUser} from "@/api/system/user"
import {ElMessage, ElMessageBox} from "element-plus";
import {selectAllRole} from "@/api/system/role.js";

const userRef = ref()
const queryRef = ref()

const userList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const roleList = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: null,
  },
  rules: {
    userName: [{required: true, message: "用户名称不能为空", trigger: "blur"},
      {min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur"}],
    nickName: [{required: true, message: "用户昵称不能为空", trigger: "blur"}],
    password: [{required: true, message: "用户密码不能为空", trigger: "blur"},
      {min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur"}],
  }
})

const {queryParams, form, rules} = toRefs(data)

/** 查询用户列表 */
const getList = () => {
  loading.value = true
  listUser(queryParams.value).then(res => {
    loading.value = false
    userList.value = res.rows
    total.value = res.total
  })
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  if (queryRef.value) {
    queryRef.value.resetFields()
  }
  handleQuery()
}

/** 删除按钮操作 */
const handleDelete = (row) => {
  const userIds = row.userId || ids.value
  ElMessageBox.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: "warning",
  }).then(() => {
    delUser(userIds).then(res => {
      getList()
      ElMessage.success("删除成功")
    })
  }).catch(() => {
  })
}

/** 选择条数  */
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.userId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 重置操作表单 */
const reset = () => {
  form.value = {
    userId: null,
    userName: null,
    nickName: null,
    password: null,
    sex: null,
    roleId: null
  }
  if (userRef.value) {
    userRef.value.resetFields()
  }

}

/** 取消按钮 */
const cancel = () => {
  open.value = false
  reset()
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "添加用户"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
  reset()
  const userId = row.userId || ids.value
  getUser(userId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改用户"
    form.password = ""
  })
}

/** 提交按钮 */
const submitForm = () => {
  userRef.value.validate(valid => {
    if (valid) {
      if (form.value.userId != null) {
        updateUser(form.value).then(response => {
          ElMessage.success("修改成功")
          open.value = false
          getList()
        })
      } else {
        addUser(form.value).then(response => {
          ElMessage.success("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

onMounted(() => {
  getList()

  selectAllRole().then(res => {
    roleList.value = res.data
  })
})
</script>
