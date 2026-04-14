<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const form = reactive({
  account: '',
  password: '',
})
const submitting = ref(false)

// 执行登录
const handleLogin = async () => {
  if (!form.account || !form.password) {
    ElMessage.warning('请填写账号与密码')
    return
  }
  submitting.value = true
  try {
    const user = await authStore.login(form.account, form.password)
    ElMessage.success('登录成功')
    router.push(user.role === 'admin' ? '/admin' : '/student')
  } catch (error: any) {
    ElMessage.error(error?.message ?? '登录失败')
  } finally {
    submitting.value = false
  }
}

// 填充管理员账号
const fillAdmin = () => {
  form.account = 'admin'
  form.password = '123456'
}

// 填充学生账号
const fillStudent = () => {
  form.account = '2024001'
  form.password = '123456'
}
</script>

<template>
  <div class="login-shell">
    <section class="login-hero">
      <h1>副203实验室 · 科研仪表盘</h1>
      <p style="font-size: 18px; max-width: 420px">
        汇总科研、出勤、值日与桌面物品的日常数据，让每一天的实验进度都清晰可见。
      </p>
      <div class="card">
        <div class="card-title">今日关注</div>
        <ul style="margin: 0; padding-left: 18px; line-height: 1.8">
          <li>科研任务：明确目标与落地进度</li>
          <li>科研时长：随机生成并归档</li>
          <li>出勤与值日：对齐实验室节奏</li>
        </ul>
      </div>
    </section>
    <section class="login-card">
      <div class="login-panel">
        <div class="card-title">登录实验室系统</div>
        <div class="card-meta">默认密码：123456</div>
        <el-form style="margin-top: 20px" @submit.prevent="handleLogin">
          <el-form-item label="账号">
            <el-input v-model="form.account" placeholder="管理员或学号" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input
              v-model="form.password"
              placeholder="请输入密码"
              type="password"
              show-password
            />
          </el-form-item>
          <div class="action-row">
            <el-button
              type="primary"
              :loading="submitting"
              @click="handleLogin"
            >
              进入系统
            </el-button>
            <el-button type="default" @click="fillAdmin">填充管理员</el-button>
            <el-button type="default" @click="fillStudent">填充学生</el-button>
          </div>
        </el-form>
      </div>
    </section>
  </div>
</template>
