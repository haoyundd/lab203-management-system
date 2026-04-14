<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  generateResearchTime,
  listMyResearchTime,
} from '@/services/endpoints'
import type { ResearchTimeRecord } from '@/types/models'
import { getTodayKey } from '@/utils/date'

const records = ref<ResearchTimeRecord[]>([])
const loading = ref(false)

// 加载个人科研时长
const loadRecords = async () => {
  loading.value = true
  try {
    records.value = await listMyResearchTime()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载科研时长失败')
  } finally {
    loading.value = false
  }
}

// 生成科研时长
const handleGenerate = async () => {
  try {
    await generateResearchTime()
    ElMessage.success('生成成功')
    await loadRecords()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '生成失败')
  }
}

// 获取今日记录
const todayRecord = computed(() => {
  const today = getTodayKey()
  return records.value.find((record) => record.date === today)
})

onMounted(() => {
  loadRecords()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="topbar" style="margin-bottom: 12px">
        <div>
          <div class="section-title">科研时长记录</div>
          <div class="card-meta">
            今日时长：{{ todayRecord?.minutes ?? 0 }} 分钟
          </div>
        </div>
        <el-button
          type="primary"
          :disabled="Boolean(todayRecord)"
          @click="handleGenerate"
        >
          生成今日时长
        </el-button>
      </div>
      <el-table class="data-table" :data="records" v-loading="loading">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="minutes" label="分钟" width="120" />
        <el-table-column prop="source" label="来源" width="120" />
      </el-table>
    </div>
  </div>
</template>
