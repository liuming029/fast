import router from './router'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/userStore.js'
import useRouteStore from '@/store/modules/routeStore.js'

const whiteList = ['/login', '/register']

const isWhiteList = (path) => {
  return whiteList.includes(path)
}

router.beforeEach((to, from, next) => {
  if (getToken()) {
    to.meta.title
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
    } else if (isWhiteList(to.path)) {
      next()
    } else {
      if (useUserStore().name === '') {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        useUserStore().getInfo().then(res => {
          isRelogin.show = false
          useRouteStore().generateRoutes().then(accessRoutes => {
            // 根据角色权限生成可访问的路由表
            accessRoutes.forEach(route => {
                router.addRoute(route) // 动态添加可访问路由表
            })

            // 根据角色确定跳转路径
            const userRoleName = res.data.roleName
            let redirectPath = to.path

            // 如果是根路径，则根据角色决定跳转页面
            if (to.path === '/' || to.path === '/index') {
              if (userRoleName === 'admin') {
                // admin角色跳转到默认路径
                redirectPath = "/index"
              }
              else if (userRoleName === 'user' || userRoleName === 'rider') {
                // 用户角色跳转到/dashboard路径
                redirectPath = "/user/home"
              }
            }

            // 如果需要跳转到特定页面
            if (redirectPath !== to.path) {
              next({ path: redirectPath, replace: true })
            } else {
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
            }
          })
        }).catch(err => {
          useUserStore().logOut().then(() => {
            ElMessage.error(err)
            next({ path: '/' })
          })
        })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next('/login') // 否则全部重定向到登录页
    }
  }
})
