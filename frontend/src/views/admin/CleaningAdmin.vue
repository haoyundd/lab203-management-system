<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listCleaningSchedules, updateCleaningSchedule } from '@/services/endpoints'
import type { CleaningSchedule } from '@/types/models'

const schedules = ref<CleaningSchedule[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editing = ref<CleaningSchedule | null>(null)
const form = reactive({
  status: '',
  remark: '',
})

// 加载值日安排
const loadSchedules = async () => {
  loading.value = true
  try {
    schedules.value = await listCleaningSchedules()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载值日失败')
  } finally {
    loading.value = false
  }
}

// 打开编辑窗口
const openEdit = (record: CleaningSchedule) => {
  editing.value = record
  form.status = record.status
  form.remark = record.remark ?? ''
  dialogVisible.value = true
}

// 保存值日状态
const saveSchedule = async () => {
  if (!editing.value) return
  try {
    await updateCleaningSchedule({
      ...editing.value,
      status: form.status,
      remark: form.remark,
    })
    ElMessage.success('更新成功')
    dialogVisible.value = false
    await loadSchedules()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '更新失败')
  }
}

onMounted(() => {
  loadSchedules()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="section-title">值日安排</div>
      <el-table class="data-table" :data="schedules" v-loading="loading">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="studentName" label="负责人" width="120" />
        <el-table-column prop="task" label="任务内容" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">更新</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="更新值日状态">
      <div class="form-row">
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="待完成" value="待完成" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" />
        </el-form-item>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSchedule">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
