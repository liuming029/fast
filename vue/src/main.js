import { createApp } from 'vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/styles/huacai.scss';

import App from './App'
import store from './store'
import router from './router'

import VxeUIBase from 'vxe-pc-ui'
import 'vxe-pc-ui/es/style.css'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'

// Element Plus 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import './permission' // permission control

// 分页组件
import Pagination from '@/components/Pagination'
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"

const app = createApp(App)

// 引入VxeUI
app.use(VxeUIBase)

// 全局组件挂载
app.component('Pagination', Pagination)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
app.component('Editor', Editor)

app.use(router)
app.use(store)

app.component('svg-icon', SvgIcon)
// 注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.component('svg-icon', SvgIcon)

// 使用element-plus 并且设置全局的大小
app.use(ElementPlus, {
  locale: locale,
  // large、default、small
  size: 'default'
})

app.mount('#app')
