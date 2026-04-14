<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getAdminDashboard,
  listCleaningSchedules,
  listResearchTasks,
} from '@/services/endpoints'
import type { AdminDashboard, CleaningSchedule, ResearchTask } from '@/types/models'
import { getTodayKey } from '@/utils/date'

const summary = ref<AdminDashboard | null>(null)
const todayTasks = ref<ResearchTask[]>([])
const todayCleaning = ref<CleaningSchedule[]>([])
const loading = ref(false)

// 加载仪表盘数据
const loadDashboard = async () => {
  loading.value = true
  try {
    summary.value = await getAdminDashboard()
    const today = getTodayKey()
    const allTasks = await listResearchTasks()
    todayTasks.value = allTasks.filter((task) => task.date === today)
    const allCleaning = await listCleaningSchedules()
    todayCleaning.value = allCleaning.filter((item) => item.date === today)
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载仪表盘失败')
  } finally {
    loading.value = false
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
        <div class="card-title">实验室成员</div>
        <div class="stat-number">{{ summary?.studentCount ?? 0 }}</div>
        <div class="card-meta">当前在库研究生人数</div>
      </div>
      <div class="card">
        <div class="card-title">今日出勤</div>
        <div class="stat-number">{{ summary?.attendanceToday ?? 0 }}</div>
        <div class="card-meta">今日签到记录条数</div>
      </div>
      <div class="card">
        <div class="card-title">科研时长</div>
        <div class="stat-number">
          {{ summary?.researchMinutesToday ?? 0 }}
        </div>
        <div class="card-meta">今日累计分钟数</div>
      </div>
    </div>

    <div class="grid-2">
      <div class="card">
        <div class="card-title">今日科研任务</div>
        <div class="card-meta">已提交：{{ summary?.tasksSubmitted ?? 0 }}</div>
        <el-table
          class="data-table"
          :data="todayTasks"
          height="240"
          style="margin-top: 12px"
        >
          <el-table-column prop="studentName" label="姓名" width="120" />
          <el-table-column prop="content" label="任务内容" />
          <el-table-column prop="updatedAt" label="更新时间" width="170" />
        </el-table>
      </div>
      <div class="card">
        <div class="card-title">今日值日安排</div>
        <div class="card-meta">
          已完成：{{ summary?.cleaningDone ?? 0 }}
        </div>
        <el-table
          class="data-table"
          :data="todayCleaning"
          height="240"
          style="margin-top: 12px"
        >
          <el-table-column prop="studentName" label="姓名" width="120" />
          <el-table-column prop="task" label="任务内容" />
          <el-table-column prop="status" label="状态" width="100" />
        </el-table>
      </div>
    </div>
  </div>
</template>
