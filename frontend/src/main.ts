import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import './styles/theme.css'
import './styles/element-plus.css'
import './styles/app.css'

// 初始化应用实例并挂载插件
const app = createApp(App)
// 初始化全局状态管理
app.use(createPinia())
// 初始化路由
app.use(router)
// 初始化 UI 组件库
app.use(ElementPlus)
// 挂载到页面节点
app.mount('#app')
