import { request } from '@/services/api'
import {
  mockAdminDashboard,
  mockCreateAttendance,
  mockCreateStudent,
  mockDeleteStudent,
  mockGenerateResearchTime,
  mockGetCurrentUser,
  mockGetTodayResearchTask,
  mockGetTodayResearchTime,
  mockListAttendance,
  mockListCleaning,
  mockListDeskItems,
  mockListMyAttendance,
  mockListMyCleaning,
  mockListMyDeskItems,
  mockListMyResearchTasks,
  mockListMyResearchTime,
  mockListResearchTasks,
  mockListResearchTime,
  mockListStudents,
  mockLogin,
  mockLogout,
  mockSaveTodayResearchTask,
  mockSignIn,
  mockSignOut,
  mockStudentDashboard,
  mockUpdateCleaning,
  mockUpdateDeskItem,
  mockUpdateStudent,
} from '@/services/mock-db'
import type {
  AttendanceRecord,
  CleaningSchedule,
  DeskItem,
  ResearchTask,
  ResearchTimeRecord,
  Student,
  User,
} from '@/types/models'

// 登录接口
export const login = async (payload: { account: string; password: string }) => {
  return request<User>(
    {
      url: '/auth/login',
      method: 'post',
      data: payload,
    },
    () => mockLogin(payload),
  )
}

// 登出接口
export const logout = async () => {
  return request<boolean>(
    {
      url: '/auth/logout',
      method: 'post',
    },
    () => mockLogout(),
  )
}

// 获取当前用户
export const getCurrentUser = async () => {
  return request<User>(
    {
      url: '/auth/me',
      method: 'get',
    },
    () => mockGetCurrentUser(),
  )
}

// 获取学生列表
export const listStudents = async () => {
  return request<Student[]>(
    {
      url: '/students',
      method: 'get',
    },
    () => mockListStudents(),
  )
}

// 新增学生
export const createStudent = async (student: Partial<Student>) => {
  return request<Student>(
    {
      url: '/students',
      method: 'post',
      data: student,
    },
    () => mockCreateStudent(student),
  )
}

// 更新学生
export const updateStudent = async (student: Student) => {
  return request<Student>(
    {
      url: `/students/${student.id}`,
      method: 'put',
      data: student,
    },
    () => mockUpdateStudent(student),
  )
}

// 删除学生
export const deleteStudent = async (id: number) => {
  return request<boolean>(
    {
      url: `/students/${id}`,
      method: 'delete',
    },
    () => mockDeleteStudent(id),
  )
}

// 获取出勤列表
export const listAttendance = async () => {
  return request<AttendanceRecord[]>(
    {
      url: '/attendance-records',
      method: 'get',
    },
    () => mockListAttendance(),
  )
}

// 获取个人出勤
export const listMyAttendance = async () => {
  return request<AttendanceRecord[]>(
    {
      url: '/attendance-records/me',
      method: 'get',
    },
    () => mockListMyAttendance(),
  )
}

// 签到
export const signIn = async () => {
  return request<AttendanceRecord>(
    {
      url: '/attendance-records/sign-in',
      method: 'post',
    },
    () => mockSignIn(),
  )
}

// 签退
export const signOut = async () => {
  return request<AttendanceRecord>(
    {
      url: '/attendance-records/sign-out',
      method: 'post',
    },
    () => mockSignOut(),
  )
}

// 管理员补录出勤
export const createAttendance = async (record: AttendanceRecord) => {
  return request<AttendanceRecord>(
    {
      url: '/attendance-records',
      method: 'post',
      data: record,
    },
    () => mockCreateAttendance(record),
  )
}

// 获取值日列表
export const listCleaningSchedules = async () => {
  return request<CleaningSchedule[]>(
    {
      url: '/cleaning-schedules',
      method: 'get',
    },
    () => mockListCleaning(),
  )
}

// 获取个人值日
export const listMyCleaningSchedules = async () => {
  return request<CleaningSchedule[]>(
    {
      url: '/cleaning-schedules/me',
      method: 'get',
    },
    () => mockListMyCleaning(),
  )
}

// 更新值日安排
export const updateCleaningSchedule = async (payload: CleaningSchedule) => {
  return request<CleaningSchedule>(
    {
      url: `/cleaning-schedules/${payload.id}`,
      method: 'put',
      data: payload,
    },
    () => mockUpdateCleaning(payload),
  )
}

// 获取桌面物品
export const listDeskItems = async () => {
  return request<DeskItem[]>(
    {
      url: '/desk-items',
      method: 'get',
    },
    () => mockListDeskItems(),
  )
}

// 获取个人桌面物品
export const listMyDeskItems = async () => {
  return request<DeskItem[]>(
    {
      url: '/desk-items/me',
      method: 'get',
    },
    () => mockListMyDeskItems(),
  )
}

// 更新桌面物品
export const updateDeskItem = async (payload: DeskItem) => {
  return request<DeskItem>(
    {
      url: `/desk-items/${payload.id}`,
      method: 'put',
      data: payload,
    },
    () => mockUpdateDeskItem(payload),
  )
}

// 获取科研时长列表
export const listResearchTime = async () => {
  return request<ResearchTimeRecord[]>(
    {
      url: '/research-time-records',
      method: 'get',
    },
    () => mockListResearchTime(),
  )
}

// 获取个人科研时长
export const listMyResearchTime = async () => {
  return request<ResearchTimeRecord[]>(
    {
      url: '/research-time-records/me',
      method: 'get',
    },
    () => mockListMyResearchTime(),
  )
}

// 生成科研时长
export const generateResearchTime = async () => {
  return request<ResearchTimeRecord>(
    {
      url: '/research-time-records/generate',
      method: 'post',
    },
    () => mockGenerateResearchTime(),
  )
}

// 获取今日科研时长
export const getTodayResearchTime = async () => {
  return request<ResearchTimeRecord | null>(
    {
      url: '/research-time-records/today',
      method: 'get',
    },
    () => mockGetTodayResearchTime(),
  )
}

// 获取科研任务列表
export const listResearchTasks = async () => {
  return request<ResearchTask[]>(
    {
      url: '/research-tasks',
      method: 'get',
    },
    () => mockListResearchTasks(),
  )
}

// 获取个人科研任务历史
export const listMyResearchTasks = async () => {
  return request<ResearchTask[]>(
    {
      url: '/research-tasks/me',
      method: 'get',
    },
    () => mockListMyResearchTasks(),
  )
}

// 获取今日科研任务
export const getTodayResearchTask = async () => {
  return request<ResearchTask | null>(
    {
      url: '/research-tasks/today',
      method: 'get',
    },
    () => mockGetTodayResearchTask(),
  )
}

// 保存今日科研任务
export const saveTodayResearchTask = async (content: string) => {
  return request<ResearchTask>(
    {
      url: '/research-tasks/today',
      method: 'post',
      data: { content },
    },
    () => mockSaveTodayResearchTask(content),
  )
}

// 获取管理员仪表盘
export const getAdminDashboard = async () => {
  return request(
    {
      url: '/dashboard',
      method: 'get',
    },
    () => mockAdminDashboard(),
  )
}

// 获取学生仪表盘
export const getStudentDashboard = async () => {
  return request(
    {
      url: '/dashboard/me',
      method: 'get',
    },
    () => mockStudentDashboard(),
  )
}
