import dayjs from 'dayjs'

// 格式化日期为 YYYY-MM-DD
export const formatDate = (input?: string | Date) => {
  return dayjs(input ?? new Date()).format('YYYY-MM-DD')
}

// 格式化时间为 YYYY-MM-DD HH:mm:ss
export const formatDateTime = (input?: string | Date) => {
  return dayjs(input ?? new Date()).format('YYYY-MM-DD HH:mm:ss')
}

// 获取今天的日期字符串
export const getTodayKey = () => {
  return formatDate(new Date())
}
