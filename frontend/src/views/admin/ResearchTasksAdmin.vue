<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listResearchTasks } from '@/services/endpoints'
import type { ResearchTask } from '@/types/models'
import { getTodayKey } from '@/utils/date'

const tasks = ref<ResearchTask[]>([])
const loading = ref(false)
const today = getTodayKey()

// 加载科研任务
const loadTasks = async () => {
  loading.value = true
  try {
    const all = await listResearchTasks()
    tasks.value = all.filter((task) => task.date === today)
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载科研任务失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadTasks()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="section-title">今日科研任务总览</div>
      <div class="card-meta">日期：{{ today }}</div>
      <el-table
        class="data-table"
        :data="tasks"
        v-loading="loading"
        style="margin-top: 12px"
      >
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="content" label="任务内容" />
        <el-table-column prop="updatedAt" label="最后修改" width="170" />
      </el-table>
    </div>
  </div>
</template>
