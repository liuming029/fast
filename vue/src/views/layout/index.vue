<template>
  <div class="layout-wrapper">
    <!-- 顶部导航栏 -->
    <header class="layout-header">
      <div class="header-logo">
        <img :src="logo" class="logo-img" alt="Logo" />
        <span class="logo-title">{{ title }}</span>
      </div>

      <div class="header-navbar">
        <!-- 面包屑导航 -->
        <el-breadcrumb class="breadcrumb" separator="/">
          <el-breadcrumb-item
            v-for="(item, index) in breadcrumbItems"
            :key="item.path"
          >
            <span v-if="index === breadcrumbItems.length - 1">{{ item.meta.title }}</span>
            <a v-else>{{ item.meta.title }}</a>
          </el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 用户信息区 -->
        <div class="user-actions">
          <span class="welcome-text">您好, {{ nickName }}</span>

          <el-button
            type="text"
            @click="logout"
            class="logout-btn"
          >
            <svg-icon icon-class="logout" />
            <span>退出登录</span>
          </el-button>

          <el-dropdown trigger="click" class="user-dropdown">
            <div class="user-info">
              <img :src="userStore.avatar" class="user-avatar" alt="Avatar" />
              <el-icon class="dropdown-icon">
                <CaretBottom />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <router-link to="/user/profile">
                  <el-dropdown-item>个人中心</el-dropdown-item>
                </router-link>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- 侧边栏 -->
    <aside class="layout-sidebar">
      <sidebar />
    </aside>

    <!-- 主内容区 -->
    <main class="layout-main">
      <app-main />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import Sidebar from './components/Sidebar/index.vue'
import { AppMain } from './components'
import logo from '@/assets/logo/logo.png'
import useUserStore from '@/store/modules/userStore.js'
import { getUser } from "@/api/system/user.js"
import { CaretBottom } from "@element-plus/icons-vue"
import useRouteStore from '@/store/modules/routeStore.js'

// 基本数据
const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const nickName = ref(null)
const route = useRoute()
const routeStore = useRouteStore()
const breadcrumbItems = ref([])

// 获取面包屑数据
const getBreadcrumb = () => {
  let matched = []
  const pathNum = findPathNum(route.path)

  if (pathNum > 2) {
    const reg = /\/\w+/gi
    const pathList = route.path.match(reg).map((item, index) => {
      if (index !== 0) item = item.slice(1)
      return item
    })
    getMatched(pathList, routeStore.defaultRoutes, matched)
  } else {
    matched = route.matched.filter((item) => item.meta && item.meta.title)
  }

  // 判断是否为首页
  if (!isDashboard(matched[0])) {
    matched = [{ path: "/index", meta: { title: "首页" } }].concat(matched)
  }

  breadcrumbItems.value = matched.filter(
      item => item.meta && item.meta.title && item.meta.breadcrumb !== false
  )
}

const findPathNum = (str, char = "/") => {
  let index = str.indexOf(char)
  let num = 0
  while (index !== -1) {
    num++
    index = str.indexOf(char, index + 1)
  }
  return num
}

const getMatched = (pathList, routeList, matched) => {
  let data = routeList.find(
      item => item.path == pathList[0] || (item.name += '').toLowerCase() == pathList[0]
  )

  if (data) {
    matched.push(data)
    if (data.children && pathList.length) {
      pathList.shift()
      getMatched(pathList, data.children, matched)
    }
  }
}

const isDashboard = (route) => {
  const name = route && route.name
  if (!name) return false
  return name.trim() === 'Index'
}

// 登出功能
const logout = () => {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logOut().then(() => {
      location.href = '/index'
    })
  }).catch(() => {})
}

// 生命周期钩子
onMounted(() => {
  getUser(userStore.id).then(res => {
    nickName.value = res.data.nickName
  })

  // 初始化面包屑
  getBreadcrumb()
})

// 监听路由变化更新面包屑
watch(() => route.path, () => {
  getBreadcrumb()
})
</script>

<style scoped>
.layout-wrapper {
  min-height: 100vh;
}

/* 头部样式 */
.layout-header {
  height: 40px;
  display: flex;
  padding: 0 8px;
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  background: #fff;
  z-index: 1000;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-logo {
  display: flex;
  align-items: center;
}

.logo-img {
  width: 40px;
  height: 40px;
  margin-right: 12px;
}

.logo-title {
  font-weight: bold;
  font-size: 15px;
}

.header-navbar {
  flex: 1;
  height: 100%;
  display: flex;
  align-items: center;
  margin-left: 20px;
}

.breadcrumb {
  flex: 1;
  display: flex;
  align-items: center;
}

.user-actions {
  display: flex;
  align-items: center;
}

.welcome-text {
  margin-right: 15px;
  font-size: 14px;
}

.logout-btn {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  margin-right: 13px;
}

.logout-btn :deep(span) {
  margin-left: 4px;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.dropdown-icon {
  margin-left: 5px;
  font-size: 12px;
}

/* 侧边栏样式 */
.layout-sidebar {
  width: 200px;
  position: fixed;
  top: 40px;
  left: 0;
  bottom: 0;
  z-index: 1001;
  background: #fff;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
}

/* 主内容区样式 */
.layout-main {
  min-height: calc(100vh - 40px);
  margin-left: 240px;
  position: relative;
  top: 40px;
}
</style>
