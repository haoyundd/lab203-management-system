import { defineStore } from 'pinia'
import type { User, UserRole } from '@/types/models'
import { getCurrentUser, login, logout } from '@/services/endpoints'

const TOKEN_KEY = 'lab203_token'
const USER_KEY = 'lab203_user'

// 生成本地 token
const buildToken = () => {
  return `mock-${Date.now()}`
}

// 读取本地用户信息
const readUser = () => {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null
  return JSON.parse(raw) as User
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) ?? '',
    user: readUser() as User | null,
    loading: false,
  }),
  getters: {
    // 判断是否已登录
    isAuthed: (state) => Boolean(state.token && state.user),
    // 获取当前角色
    role: (state) => state.user?.role ?? ('student' as UserRole),
  },
  actions: {
    // 执行登录
    async login(account: string, password: string) {
      this.loading = true
      try {
        const user = await login({ account, password })
        const token = buildToken()
        this.token = token
        this.user = user
        localStorage.setItem(TOKEN_KEY, token)
        localStorage.setItem(USER_KEY, JSON.stringify(user))
        return user
      } finally {
        this.loading = false
      }
    },
    // 拉取当前用户
    async fetchCurrentUser() {
      const user = await getCurrentUser()
      this.user = user
      localStorage.setItem(USER_KEY, JSON.stringify(user))
      return user
    },
    // 执行登出
    async logout() {
      this.loading = true
      try {
        await logout()
      } finally {
        this.loading = false
        this.token = ''
        this.user = null
        localStorage.removeItem(TOKEN_KEY)
        localStorage.removeItem(USER_KEY)
      }
    },
  },
})
