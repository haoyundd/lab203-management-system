<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const menuItems = [
  { label: '实验室总览', path: '/admin/dashboard' },
  { label: '学生管理', path: '/admin/students' },
  { label: '出勤管理', path: '/admin/attendance' },
  { label: '值日安排', path: '/admin/cleaning' },
  { label: '桌面物品', path: '/admin/desk-items' },
  { label: '科研时长', path: '/admin/research-time' },
  { label: '科研任务总览', path: '/admin/research-tasks' },
]

// 判断导航是否处于激活状态
const isActive = (path: string) => {
  return route.path.startsWith(path)
}

// 计算当前用户显示名
const userName = computed(() => authStore.user?.name ?? '管理员')

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
        <div class="sidebar-subtitle">科研管理中枢</div>
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
        <div class="topbar-title">管理员控制台</div>
        <div class="action-row">
          <span class="tag-pill">当前：{{ userName }}</span>
          <el-button type="primary" @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <router-view />
    </section>
  </div>
</template>
