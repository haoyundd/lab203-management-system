<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listResearchTime } from '@/services/endpoints'
import type { ResearchTimeRecord } from '@/types/models'

const records = ref<ResearchTimeRecord[]>([])
const loading = ref(false)

// 加载科研时长记录
const loadRecords = async () => {
  loading.value = true
  try {
    records.value = await listResearchTime()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载科研时长失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRecords()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="section-title">科研时长记录</div>
      <el-table class="data-table" :data="records" v-loading="loading">
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="minutes" label="分钟" width="120" />
        <el-table-column prop="source" label="来源" width="120" />
      </el-table>
    </div>
  </div>
</template>
