import axios from 'axios'
import type { AxiosRequestConfig } from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'
const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'
const ALLOW_MOCK_FALLBACK = import.meta.env.VITE_ALLOW_MOCK_FALLBACK !== 'false'

// 创建 Axios 实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
})

// 请求拦截器：注入本地 token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('lab203_token')
  if (token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 统一处理后端 Result 结构
const unwrapResult = <T>(payload: any) => {
  if (payload && typeof payload === 'object' && 'code' in payload) {
    if (payload.code !== 0 && payload.code !== 200) {
      throw new Error(payload.message || '接口调用失败')
    }
    return payload.data as T
  }
  return (payload?.data ?? payload) as T
}

// 执行真实接口调用
const callRealApi = async <T>(config: AxiosRequestConfig) => {
  const response = await apiClient.request(config)
  return unwrapResult<T>(response.data)
}

// 统一请求入口，支持 Mock 降级
export const request = async <T>(
  config: AxiosRequestConfig,
  mockFn?: () => Promise<T>,
) => {
  if (USE_MOCK && mockFn) {
    return mockFn()
  }
  try {
    return await callRealApi<T>(config)
  } catch (error) {
    if (ALLOW_MOCK_FALLBACK && mockFn) {
      console.warn('接口调用失败，已切换到 Mock 数据', error)
      return mockFn()
    }
    throw error
  }
}
