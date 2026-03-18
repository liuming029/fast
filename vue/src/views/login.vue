<template>
    <div class="login-container">
        <div class="login-box">

            <div class="login-form">
                <div class="form-header">
                    <h3 class="title">登录</h3>
                    <p class="subtitle">请输入您的登录信息</p>
                </div>

                <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="form-content">
                    <el-form-item prop="username">
                        <el-input
                                v-model="loginForm.username"
                                type="text"
                                size="large"
                                auto-complete="off"
                                placeholder="请输入用户名"
                        >
                            <template #prefix>
                                <svg-icon icon-class="user" class="el-input__icon input-icon"/>
                            </template>
                        </el-input>
                    </el-form-item>

                    <el-form-item prop="password">
                        <el-input
                                v-model="loginForm.password"
                                type="password"
                                size="large"
                                auto-complete="off"
                                placeholder="请输入密码"
                                @keyup.enter="handleLogin"
                        >
                            <template #prefix>
                                <svg-icon icon-class="password" class="el-input__icon input-icon"/>
                            </template>
                        </el-input>
                    </el-form-item>

                    <el-form-item style="width:100%;">
                        <el-button
                                :loading="loading"
                                size="large"
                                type="primary"
                                style="width:100%;"
                                @click.prevent="handleLogin"
                        >
                            <span v-if="!loading">登录</span>
                            <span v-else>登录中...</span>
                        </el-button>
                    </el-form-item>

                    <div class="register-link" v-if="register">
                        还没有账户？
                        <router-link class="link-type" :to="'/register'">立即注册</router-link>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script setup>
import useUserStore from '@/store/modules/userStore.js'
import {useRouter} from "vue-router"

const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const router = useRouter()

const loginForm = ref({
    username: "",
    password: "",
    code: "",
    uuid: ""
})

const loginRules = {
    username: [{required: true, trigger: "blur", message: "请输入您的账号"}],
    password: [{required: true, trigger: "blur", message: "请输入您的密码"}]
}

const loading = ref(false)
// 注册开关
const register = ref(true)

const loginRef = ref()

const handleLogin = () => {
    loginRef.value.validate(valid => {
        if (valid) {
            loading.value = true
            // 调用action的登录方法
            userStore.login(loginForm.value).then(() => {
                // 登录成功后，让路由守卫处理跳转逻辑
                const redirectPath = "/"
                router.push({path: redirectPath})
            }).catch(() => {
                loading.value = false
            })
        }
    })
}

onMounted(() => {
})
</script>

<style scoped>
.login-container {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
    min-height: 600px;
    background: url('@/assets/images/login-background.jpg') no-repeat center center;
    background-size: cover;
    position: relative;
}

.login-box {
    display: flex;
    width: 500px;
    height: 400px;
    background: rgba(255, 255, 255, 0.85);
    border-radius: 10px;
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
    overflow: hidden;
}

.login-form {
    flex: 1;
    padding: 40px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.form-header {
    text-align: center;
    margin-bottom: 30px;
}

.title {
    font-size: 28px;
    font-weight: 600;
    color: #333;
    margin-bottom: 10px;
}

.subtitle {
    font-size: 14px;
    color: #999;
}

:deep(.el-form-item) {
    margin-bottom: 22px;
}

:deep(.el-input__inner) {
    height: 46px;
    line-height: 46px;
    padding-left: 40px;
    border-radius: 4px;
}

:deep(.input-icon) {
    height: 46px;
    width: 16px;
    margin-left: 12px;
}

:deep(.el-checkbox) {
    color: #666;
}

.register-link {
    text-align: center;
    margin-top: 20px;
    color: #666;
}

.link-type {
    color: #409eff;
    text-decoration: none;
    margin-left: 5px;
}

.link-type:hover {
    text-decoration: underline;
}
</style>
