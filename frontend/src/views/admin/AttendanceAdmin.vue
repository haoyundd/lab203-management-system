<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  createAttendance,
  listAttendance,
  listStudents,
} from '@/services/endpoints'
import type { AttendanceRecord, Student } from '@/types/models'
import { formatDate } from '@/utils/date'

const records = ref<AttendanceRecord[]>([])
const students = ref<Student[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = reactive({
  studentId: 0,
  date: formatDate(),
  signInTime: '',
  signOutTime: '',
  status: '正常',
  remark: '',
})

// 加载出勤记录
const loadRecords = async () => {
  loading.value = true
  try {
    records.value = await listAttendance()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载出勤失败')
  } finally {
    loading.value = false
  }
}

// 加载学生列表
const loadStudents = async () => {
  try {
    students.value = await listStudents()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载学生失败')
  }
}

// 打开补录窗口
const openDialog = () => {
  form.studentId = students.value[0]?.id ?? 0
  form.date = formatDate()
  form.signInTime = ''
  form.signOutTime = ''
  form.status = '正常'
  form.remark = ''
  dialogVisible.value = true
}

// 保存补录记录
const saveRecord = async () => {
  const student = students.value.find((item) => item.id === form.studentId)
  if (!student) {
    ElMessage.warning('请选择学生')
    return
  }
  try {
    await createAttendance({
      id: 0,
      studentId: student.id,
      studentName: student.name,
      date: form.date,
      signInTime: form.signInTime,
      signOutTime: form.signOutTime,
      status: form.status,
      remark: form.remark,
    })
    ElMessage.success('补录成功')
    dialogVisible.value = false
    await loadRecords()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '补录失败')
  }
}

onMounted(() => {
  loadRecords()
  loadStudents()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="topbar" style="margin-bottom: 12px">
        <div class="section-title">出勤管理</div>
        <el-button type="primary" @click="openDialog">补录出勤</el-button>
      </div>
      <el-table class="data-table" :data="records" v-loading="loading">
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="signInTime" label="签到时间" width="160" />
        <el-table-column prop="signOutTime" label="签退时间" width="160" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="补录出勤">
      <div class="form-row">
        <el-form-item label="学生">
          <el-select v-model="form.studentId" placeholder="请选择">
            <el-option
              v-for="student in students"
              :key="student.id"
              :label="`${student.name} (${student.studentNo})`"
              :value="student.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-input v-model="form.date" placeholder="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="签到时间">
          <el-input v-model="form.signInTime" placeholder="09:00" />
        </el-form-item>
        <el-form-item label="签退时间">
          <el-input v-model="form.signOutTime" placeholder="18:00" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="正常" value="正常" />
            <el-option label="迟到" value="迟到" />
            <el-option label="缺勤" value="缺勤" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" />
        </el-form-item>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRecord">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
