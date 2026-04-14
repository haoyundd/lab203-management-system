import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AdminLayout from '@/layouts/AdminLayout.vue'
import StudentLayout from '@/layouts/StudentLayout.vue'

// 路由定义
const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true },
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, role: 'admin' },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard',
      },
      {
        path: 'dashboard',
        component: () => import('@/views/admin/AdminDashboard.vue'),
      },
      {
        path: 'students',
        component: () => import('@/views/admin/StudentManagement.vue'),
      },
      {
        path: 'attendance',
        component: () => import('@/views/admin/AttendanceAdmin.vue'),
      },
      {
        path: 'cleaning',
        component: () => import('@/views/admin/CleaningAdmin.vue'),
      },
      {
        path: 'desk-items',
        component: () => import('@/views/admin/DeskItemsAdmin.vue'),
      },
      {
        path: 'research-time',
        component: () => import('@/views/admin/ResearchTimeAdmin.vue'),
      },
      {
        path: 'research-tasks',
        component: () => import('@/views/admin/ResearchTasksAdmin.vue'),
      },
    ],
  },
  {
    path: '/student',
    component: StudentLayout,
    meta: { requiresAuth: true, role: 'student' },
    children: [
      {
        path: '',
        redirect: '/student/home',
      },
      {
        path: 'home',
        component: () => import('@/views/student/StudentDashboard.vue'),
      },
      {
        path: 'attendance',
        component: () => import('@/views/student/AttendanceStudent.vue'),
      },
      {
        path: 'cleaning',
        component: () => import('@/views/student/CleaningStudent.vue'),
      },
      {
        path: 'desk-items',
        component: () => import('@/views/student/DeskItemsStudent.vue'),
      },
      {
        path: 'research-time',
        component: () => import('@/views/student/ResearchTimeStudent.vue'),
      },
      {
        path: 'research-tasks',
        component: () => import('@/views/student/ResearchTasksStudent.vue'),
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫：控制登录与角色访问
router.beforeEach(async (to) => {
  const authStore = useAuthStore()
  if (to.meta.public) return true
  if (!authStore.isAuthed) {
    return '/login'
  }
  const requiredRole = to.meta.role as string | undefined
  if (requiredRole && authStore.role !== requiredRole) {
    return authStore.role === 'admin' ? '/admin' : '/student'
  }
  return true
})

export default router
