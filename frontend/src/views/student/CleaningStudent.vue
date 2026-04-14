<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listMyCleaningSchedules } from '@/services/endpoints'
import type { CleaningSchedule } from '@/types/models'

const schedules = ref<CleaningSchedule[]>([])
const loading = ref(false)

// 加载个人值日安排
const loadSchedules = async () => {
  loading.value = true
  try {
    schedules.value = await listMyCleaningSchedules()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载值日失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadSchedules()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="section-title">我的值日安排</div>
      <el-table class="data-table" :data="schedules" v-loading="loading">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="task" label="任务内容" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>
