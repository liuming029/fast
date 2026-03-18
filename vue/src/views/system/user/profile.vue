<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <template v-slot:header>
            <span>个人信息</span>
          </template>
          <div>
            <div style="text-align: center">
              <!-- 用户头像组件 -->
              <div class="user-info-head">
                <el-upload
                    class="avatar-uploader"
                    :action="uploadUrl"
                    :show-file-list="false"
                    :before-upload="beforeUpload"
                    :on-success="handleAvatarSuccess"
                    :on-error="handleAvatarError"
                    :headers="headers"
                >
                  <img :src="userStore.avatar" class="avatar" alt=""/>
                </el-upload>
              </div>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                用户名称
                <div class="pull-right">{{ state.user.userName }}</div>
              </li>
              <li class="list-group-item">
                用户昵称
                <div class="pull-right">{{ state.user.nickName }}</div>
              </li>
              <li class="list-group-item">
                性别
                <div class="pull-right" v-if="state.user.sex == 0">男</div>
                <div class="pull-right" v-else>女</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <template v-slot:header>
            <span>基本资料</span>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <!-- 用户信息表单 -->
              <el-form ref="userRef" :model="form" :rules="infoRules" label-width="80px">
                <el-form-item label="用户昵称" prop="nickName">
                  <el-input v-model="form.nickName" maxlength="30"/>
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="form.sex">
                    <el-radio value="0">男</el-radio>
                    <el-radio value="1">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitUserInfo">保存</el-button>
                  <el-button type="danger" @click="close">关闭</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <!-- 修改密码表单 -->
              <el-form ref="pwdRef" :model="pwdForm" :rules="pwdRules" label-width="80px">
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input v-model="pwdForm.oldPassword" placeholder="请输入旧密码" type="password" show-password/>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="pwdForm.newPassword" placeholder="请输入新密码" type="password" show-password/>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="pwdForm.confirmPassword" placeholder="请确认新密码" type="password" show-password/>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitPassword">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {getUserProfile, updateUserProfile, updateUserPwd} from "@/api/system/user.js"
import useUserStore from "@/store/modules/userStore.js"
import {ElMessage} from "element-plus"
import {getToken} from "@/utils/auth.js"
import {useRouter} from "vue-router"

// 路由和引用
const router = useRouter()
const userRef = ref()
const pwdRef = ref()
const userStore = useUserStore()

// 数据状态
const activeTab = ref("userinfo")
const state = reactive({
  user: {},
})

const form = ref({})
const pwdForm = reactive({
  oldPassword: null,
  newPassword: null,
  confirmPassword: null
})

// 上传相关
const uploadUrl = import.meta.env.VITE_APP_BASE_API + "/system/user/profile/avatar"
const headers = {Authorization: "Bearer " + getToken()}

// 表单验证规则
const infoRules = ref({
  nickName: [{required: true, message: "用户昵称不能为空", trigger: "blur"}],
})

const equalToPassword = (rule, value, callback) => {
  if (pwdForm.newPassword !== value) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const pwdRules = ref({
  oldPassword: [{required: true, message: "旧密码不能为空", trigger: "blur"}],
  newPassword: [{required: true, message: "新密码不能为空", trigger: "blur"},
    {min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur"},
    {pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur"}],
  confirmPassword: [{required: true, message: "确认密码不能为空", trigger: "blur"},
    {required: true, validator: equalToPassword, trigger: "blur"}]
})

// 获取用户信息
const getUser = () => {
  getUserProfile().then(response => {
    state.user = response.data
    // 初始化表单数据
    form.value = {
      nickName: state.user.nickName,
      sex: state.user.sex
    }
  })
}

// 上传前处理
const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// 处理上传成功
const handleAvatarSuccess = (response) => {
  if (response.code === 200) {
    userStore.avatar = import.meta.env.VITE_APP_BASE_API + response.imgUrl
    ElMessage.success("修改成功")
  } else {
    ElMessage.error(response.msg || "修改失败")
  }
}

// 处理上传失败
const handleAvatarError = () => {
  ElMessage.error("上传失败")
}

// 提交用户信息
const submitUserInfo = () => {
  userRef.value.validate(valid => {
    if (valid) {
      updateUserProfile(form.value).then(response => {
        ElMessage.success("修改成功")
        getUser()
      })
    }
  })
}

// 提交密码修改
const submitPassword = () => {
  pwdRef.value.validate(valid => {
    if (valid) {
      updateUserPwd(pwdForm.oldPassword, pwdForm.newPassword).then(response => {
        ElMessage.success("修改成功")
      })
    }
  })
}

// 关闭页面
const close = () => {
  router.go(-1)
}

// 组件挂载时获取用户信息
onMounted(() => {
  getUser()
})

// 监听用户信息变化并更新表单
watch(() => state.user, user => {
  if (user) {
    form.value = {nickName: user.nickName, sex: user.sex}
  }
}, {immediate: true})
</script>

<style scoped>
.list-group {
  padding-left: 0px;
  list-style: none;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0px;
  font-size: 13px;
}

.pull-right {
  float: right !important;
}

.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>