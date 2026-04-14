<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listMyDeskItems } from '@/services/endpoints'
import type { DeskItem } from '@/types/models'

const items = ref<DeskItem[]>([])
const loading = ref(false)

// 加载个人桌面物品
const loadItems = async () => {
  loading.value = true
  try {
    items.value = await listMyDeskItems()
  } catch (error: any) {
    ElMessage.error(error?.message ?? '加载桌面物品失败')
  } finally {
    loading.value = false
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
        <el-table-column prop="name" label="物品" width="160" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>
