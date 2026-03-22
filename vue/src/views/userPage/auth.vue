<template>

<div style="padding: 25px 0 ; display: flex ; justify-content: center; align-items: flex-start;">
<div v-if="authStatus ==='无'"
     style="background:#ffffff;border-radius: 10px;padding: 10px;width: 500px ; box-shadow: 0 4px 10px rgba(0,0,0,3)">
  <div style="text-align: center; margin-bottom: 30px">
    <h2 STYLE="font-size: 24px; color: #333">配送员认证</h2>
    <p style="margin: 0; color: #666;font-size: 14px">成为校园配送员，开启兼职之旅</p>
  </div>

  <el-steps :active="activeStep" finish-status="success" simple style="margin-bottom: 30px">
    <el-step title="身份信息"/>
    <el-step title="学生证明"/>
  </el-steps>
    <div style="min-height: 300px">
          <!-- 第一步：身份信息     -->
      <div v-if="activeStep  ===0 "style="padding: 20px 0">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名"/>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号"/>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码"/>
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary"
                     style="width: 120px;height: 40px"
                     @click="nextStep"
          >下一步
          </el-button>
        </el-form-item>
      </el-form>
    </div>
      <!-- 第二步：学生认证    -->

      <div v-if="activeStep===1 "style="padding: 20px 0">
        <el-form :model="form" rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="学生证" prop="studentCardImage">
            <image-upload v-model="form.studentCardImage"/>
          </el-form-item>
          <el-form-item label="身份证" prop="idCardImage">
            <image-upload v-model="form.idCardImage"/>
          </el-form-item>
          <el-form-item>
            <el-button
                       style="width: 120px;height: 40px"
                       @click="prevStep"
            >上一步
            </el-button>
            <el-button type="primary"
                       @click = "submitForm"
                       style="width: 120px;height: 40px"
            >
              确认提交
            </el-button>
          </el-form-item>
        </el-form>

      </div>
    </div>
    </div>
<div style="padding: 20px 0" v-if="authStatus === '审核中'">
  <div style="text-align: center;padding: 40px 0">
    <el-icon style="color:#3AAD6D;font-size: 60px; margin-bottom: 20px">
      <CircleCheck/>
    </el-icon>
    <h3>认证信息已提交</h3>
    <p style="color:#666;">我们将在1-3个工作日内完成审核</p>
  </div>
</div>
  <div style="padding: 20px 0" v-if="authStatus === '已通过'">
    <div style="text-align: center;padding: 40px 0">
      <el-icon style="color:#3AAD6D;font-size: 60px; margin-bottom: 20px">
        <CircleCheck/>
      </el-icon>
      <h3>认证已通过</h3>
      <p style="color:#666;">恭喜你，已成为校园配送员！可以开始接单了</p>
    </div>
  </div>

  <div style="padding: 20px 0" v-if="authStatus === '已拒绝'">
    <div style="text-align: center;padding: 40px 0">
      <el-icon style="color: #e54040;font-size: 60px;margin-bottom: 20px">
        <CircleClose/>
      </el-icon>
      <h3>认证未通过</h3>
      <p style="color: #666">很抱歉, 您的认证申请未通过审核, 请检查提交的资料并重新申请</p>
      <el-button type="primary" @click="resetAuth">重新申请认证</el-button>
    </div>
  </div>

</div>


  </template>

<script setup>

import {addRider, deleteOldAuth, selectIsAuthToStatus} from "../../api/take/rider.js";
import {CircleCheck, CircleClose} from "@element-plus/icons-vue";

//当前的步骤
const activeStep = ref(0)

//认证状态
const authStatus = ref('')

//表单实例

const formRef = ref()

//表单参数
  const form = ref({
    realName: null,
    idCard: null,
    phone: null,
    studentId: null,
    studentCardImage: null,
    idCardImage: null,
  })

//表单校验
const rules = ref({
  realName: [
    {required: true, message: "真实姓名不能为空", trigger: "blur"}
  ],
  idCard: [
    {required: true, message: "身份证号不能为空", trigger: "blur"}
  ],
  phone: [
    {required: true, message: "手机号码不能为空", trigger: "blur"}
  ],
  studentId: [
    {required: true, message: "学号不能为空", trigger: "blur"}
  ],
  studentCardImage: [
    {required: true, message: "学生证不能为空", trigger: "blur"}
  ],
  idCardImage: [
    {required: true, message: "身份证不能为空", trigger: "blur"}
  ],
})

//下一步
const nextStep = () => {
  if (activeStep.value === 0) {
    formRef.value.validate(valid => {
      if (valid) {
        activeStep.value = 1
      }
    })
  } else if (activeStep.value === 1) {
    formRef.value.validate(valid => {
      if (valid) {
        activeStep.value = 2
      }
    })
  }
}

//上一步
const prevStep = () =>{
  if (activeStep.value > 0){
    activeStep.value--
  }
}


//重新申请认证
const resetAuth = () =>{
  //删除之前的认证信息
  deleteOldAuth().then(res =>{
    getAuthStatus
  })
}

//认证状态
const getAuthStatus =() =>{
  //查询用户有没有提交过审核，如果有返回认证状态，没有返回无
  selectIsAuthToStatus().then(res=>{
   authStatus.value = res.msg
  })
 }

onMounted(()=>{
  //查询认证状态
   getAuthStatus()

})

//提交认证
const submitForm = () =>{
  addRider(form.value).then(res=>{
    //查询认证状态
    getAuthStatus()

  })
}

</script>

<style scoped>

</style>