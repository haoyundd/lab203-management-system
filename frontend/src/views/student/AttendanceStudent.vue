<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listMyAttendance, signIn, signOut } from '@/services/endpoints'
import type { AttendanceRecord } from '@/types/models'

const records = ref<AttendanceRecord[]>([])
const loading = ref(false)

// 加载个人出勤记录
const loadRecords = async () => {
  loading.value = true
  try {
    records.value = await listMyAttendance()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载出勤失败')
  } finally {
    loading.value = false
  }
}

// 签到操作
const handleSignIn = async () => {
  try {
    await signIn()
    ElMessage.success('签到成功')
    await loadRecords()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '签到失败')
  }
}

// 签退操作
const handleSignOut = async () => {
  try {
    await signOut()
    ElMessage.success('签退成功')
    await loadRecords()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '签退失败')
  }
}

onMounted(() => {
  loadRecords()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="topbar" style="margin-bottom: 12px">
        <div class="section-title">我的出勤</div>
        <div class="action-row">
          <el-button type="primary" @click="handleSignIn">签到</el-button>
          <el-button @click="handleSignOut">签退</el-button>
        </div>
      </div>
      <el-table class="data-table" :data="records" v-loading="loading">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="signInTime" label="签到时间" width="160" />
        <el-table-column prop="signOutTime" label="签退时间" width="160" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>
