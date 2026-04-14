<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const menuItems = [
  { label: '我的首页', path: '/student/home' },
  { label: '我的出勤', path: '/student/attendance' },
  { label: '我的值日', path: '/student/cleaning' },
  { label: '桌面物品', path: '/student/desk-items' },
  { label: '科研时长', path: '/student/research-time' },
  { label: '科研任务', path: '/student/research-tasks' },
]

// 判断导航是否处于激活状态
const isActive = (path: string) => {
  return route.path.startsWith(path)
}

// 计算当前用户展示信息
const userProfile = computed(() => ({
  name: authStore.user?.name ?? '研究生',
  avatar: authStore.user?.avatar ?? '',
}))

// 执行退出登录
const handleLogout = async () => {
  await authStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="sidebar-brand">
        <div class="sidebar-title">副203实验室</div>
        <div class="sidebar-subtitle">个人科研面板</div>
      </div>
      <div class="card" style="margin-bottom: 20px">
        <div style="display: flex; gap: 12px; align-items: center">
          <el-avatar :size="48" :src="userProfile.avatar" />
          <div>
            <div style="font-weight: 600">{{ userProfile.name }}</div>
            <div class="card-meta">研究生 · 副203</div>
          </div>
        </div>
      </div>
      <nav class="nav-group">
        <RouterLink
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="nav-link"
          :class="{ 'is-active': isActive(item.path) }"
        >
          {{ item.label }}
        </RouterLink>
      </nav>
    </aside>
    <section class="shell-main">
      <header class="topbar">
        <div class="topbar-title">学生工作台</div>
        <div class="action-row">
          <span class="tag-pill">科研专注中</span>
          <el-button type="primary" @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <router-view />
    </section>
  </div>
</template>
