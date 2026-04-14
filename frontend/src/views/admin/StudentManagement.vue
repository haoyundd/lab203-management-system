<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createStudent,
  deleteStudent,
  listStudents,
  updateStudent,
} from '@/services/endpoints'
import type { Student } from '@/types/models'

const students = ref<Student[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editing = ref<Student | null>(null)
const form = reactive({
  name: '',
  studentNo: '',
  grade: '',
  direction: '',
  phone: '',
  status: '',
})

// 加载学生列表
const loadStudents = async () => {
  loading.value = true
  try {
    students.value = await listStudents()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载学生失败')
  } finally {
    loading.value = false
  }
}

// 打开新增弹窗
const openCreate = () => {
  editing.value = null
  form.name = ''
  form.studentNo = ''
  form.grade = '研一'
  form.direction = ''
  form.phone = ''
  form.status = '在读'
  dialogVisible.value = true
}

// 打开编辑弹窗
const openEdit = (student: Student) => {
  editing.value = student
  form.name = student.name
  form.studentNo = student.studentNo
  form.grade = student.grade
  form.direction = student.direction
  form.phone = student.phone
  form.status = student.status
  dialogVisible.value = true
}

// 保存学生信息
const saveStudent = async () => {
  if (!form.name || !form.studentNo) {
    ElMessage.warning('请填写姓名与学号')
    return
  }
  try {
    if (editing.value) {
      await updateStudent({
        ...editing.value,
        ...form,
      })
      ElMessage.success('更新成功')
    } else {
      await createStudent({ ...form })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    await loadStudents()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '保存失败')
  }
}

// 删除学生
const removeStudent = async (student: Student) => {
  try {
    await ElMessageBox.confirm(`确认删除 ${student.name} 吗？`, '提示', {
      type: 'warning',
    })
    await deleteStudent(student.id)
    ElMessage.success('删除成功')
    await loadStudents()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadStudents()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="topbar" style="margin-bottom: 12px">
        <div class="section-title">学生管理</div>
        <el-button type="primary" @click="openCreate">新增学生</el-button>
      </div>
      <el-table class="data-table" :data="students" v-loading="loading">
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="studentNo" label="学号" width="140" />
        <el-table-column prop="grade" label="年级" width="80" />
        <el-table-column prop="direction" label="方向" />
        <el-table-column prop="phone" label="联系方式" width="140" />
        <el-table-column prop="status" label="状态" width="90" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="removeStudent(row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="学生信息">
      <div class="form-row">
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.studentNo" />
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="form.grade" />
        </el-form-item>
        <el-form-item label="研究方向">
          <el-input v-model="form.direction" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="form.status" />
        </el-form-item>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveStudent">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
