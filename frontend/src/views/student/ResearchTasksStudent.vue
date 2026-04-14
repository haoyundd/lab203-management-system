<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getTodayResearchTask,
  listMyResearchTasks,
  saveTodayResearchTask,
} from '@/services/endpoints'
import type { ResearchTask } from '@/types/models'
import { getTodayKey } from '@/utils/date'

const taskContent = ref('')
const tasks = ref<ResearchTask[]>([])
const loading = ref(false)

// 加载科研任务
const loadTasks = async () => {
  loading.value = true
  try {
    const todayTask = await getTodayResearchTask()
    taskContent.value = todayTask?.content ?? ''
    tasks.value = await listMyResearchTasks()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载科研任务失败')
  } finally {
    loading.value = false
  }
}

// 保存今日科研任务
const handleSave = async () => {
  if (!taskContent.value) {
    ElMessage.warning('请填写今日科研任务')
    return
  }
  try {
    await saveTodayResearchTask(taskContent.value)
    ElMessage.success('保存成功')
    await loadTasks()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '保存失败')
  }
}

onMounted(() => {
  loadTasks()
})
</script>

<template>
  <div class="page" v-loading="loading">
    <div class="card">
      <div class="card-title">今日科研任务</div>
      <div class="card-meta">日期：{{ getTodayKey() }}</div>
      <el-input
        v-model="taskContent"
        type="textarea"
        rows="6"
        placeholder="填写今日科研任务"
        style="margin-top: 12px"
      />
      <div class="action-row" style="margin-top: 12px">
        <el-button type="primary" @click="handleSave">保存任务</el-button>
      </div>
    </div>

    <div class="card">
      <div class="card-title">历史任务</div>
      <el-table class="data-table" :data="tasks">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="content" label="任务内容" />
        <el-table-column prop="updatedAt" label="更新时间" width="170" />
      </el-table>
    </div>
  </div>
</template>
