<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listDeskItems, updateDeskItem } from '@/services/endpoints'
import type { DeskItem } from '@/types/models'

const items = ref<DeskItem[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editing = ref<DeskItem | null>(null)
const form = reactive({
  status: '',
  remark: '',
  quantity: 1,
})

// 加载桌面物品
const loadItems = async () => {
  loading.value = true
  try {
    items.value = await listDeskItems()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载桌面物品失败')
  } finally {
    loading.value = false
  }
}

// 打开编辑窗口
const openEdit = (record: DeskItem) => {
  editing.value = record
  form.status = record.status
  form.remark = record.remark ?? ''
  form.quantity = record.quantity
  dialogVisible.value = true
}

// 保存桌面物品
const saveItem = async () => {
  if (!editing.value) return
  try {
    await updateDeskItem({
      ...editing.value,
      status: form.status,
      remark: form.remark,
      quantity: form.quantity,
    })
    ElMessage.success('更新成功')
    dialogVisible.value = false
    await loadItems()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '更新失败')
  }
}

onMounted(() => {
  loadItems()
})
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="section-title">桌面物品清单</div>
      <el-table class="data-table" :data="items" v-loading="loading">
        <el-table-column prop="studentName" label="学生" width="120" />
        <el-table-column prop="name" label="物品名称" width="140" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">更新</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="更新桌面物品">
      <div class="form-row">
        <el-form-item label="数量">
          <el-input-number v-model="form.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="在用" value="在用" />
            <el-option label="闲置" value="闲置" />
            <el-option label="维护" value="维护" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" />
        </el-form-item>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveItem">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
