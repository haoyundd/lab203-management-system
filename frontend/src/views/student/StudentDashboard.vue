<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  generateResearchTime,
  getStudentDashboard,
  saveTodayResearchTask,
  signIn,
  signOut,
} from '@/services/endpoints'
import type { StudentDashboard } from '@/types/models'
import { getTodayKey } from '@/utils/date'

const dashboard = ref<StudentDashboard | null>(null)
const loading = ref(false)
const taskContent = ref('')

// 加载学生首页数据
const loadDashboard = async () => {
  loading.value = true
  try {
    const data = await getStudentDashboard()
    dashboard.value = data
    taskContent.value = data.todayResearchTask?.content ?? ''
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载首页失败')
  } finally {
    loading.value = false
  }
}

// 执行签到
const handleSignIn = async () => {
  try {
    await signIn()
    ElMessage.success('签到成功')
    await loadDashboard()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '签到失败')
  }
}

// 执行签退
const handleSignOut = async () => {
  try {
    await signOut()
    ElMessage.success('签退成功')
    await loadDashboard()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '签退失败')
  }
}

// 生成今日科研时长
const handleGenerateTime = async () => {
  try {
    await generateResearchTime()
    ElMessage.success('科研时长已生成')
    await loadDashboard()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '生成失败')
  }
}

// 保存今日科研任务
const handleSaveTask = async () => {
  if (!taskContent.value) {
    ElMessage.warning('请填写今日科研任务')
    return
  }
  try {
    await saveTodayResearchTask(taskContent.value)
    ElMessage.success('保存成功')
    await loadDashboard()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '保存失败')
  }
}

onMounted(() => {
  loadDashboard()
})
</script>

<template>
  <div class="page" v-loading="loading">
    <div class="grid-3">
      <div class="card">
        <div class="card-title">今日出勤</div>
        <div class="stat-number">
          {{ dashboard?.todayAttendance?.status ?? '未签到' }}
        </div>
        <div class="action-row">
          <el-button type="primary" @click="handleSignIn">签到</el-button>
          <el-button type="default" @click="handleSignOut">签退</el-button>
        </div>
      </div>
      <div class="card">
        <div class="card-title">今日科研时长</div>
        <div class="stat-number">
          {{ dashboard?.todayResearchTime?.minutes ?? 0 }} 分钟
        </div>
        <div class="card-meta">日期：{{ getTodayKey() }}</div>
        <el-button
          type="primary"
          style="margin-top: 12px"
          :disabled="Boolean(dashboard?.todayResearchTime)"
          @click="handleGenerateTime"
        >
          随机生成时长
        </el-button>
      </div>
      <div class="card">
        <div class="card-title">今日值日</div>
        <div class="stat-number">
          {{ dashboard?.todayCleaning?.task ?? '暂无安排' }}
        </div>
        <div class="card-meta">
          状态：{{ dashboard?.todayCleaning?.status ?? '未分配' }}
        </div>
      </div>
    </div>

    <div class="grid-2">
      <div class="card">
        <div class="card-title">今日科研任务</div>
        <el-input
          v-model="taskContent"
          type="textarea"
          rows="6"
          placeholder="填写今日科研任务"
        />
        <div class="action-row" style="margin-top: 12px">
          <el-button type="primary" @click="handleSaveTask">保存任务</el-button>
        </div>
      </div>
      <div class="card">
        <div class="card-title">桌面物品一览</div>
        <el-table class="data-table" :data="dashboard?.deskItems ?? []">
          <el-table-column prop="name" label="物品" width="140" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="status" label="状态" />
        </el-table>
      </div>
    </div>
  </div>
</template>
