export type UserRole = 'admin' | 'student'

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

export interface User {
  id: number
  name: string
  role: UserRole
  avatar: string
  studentNo?: string
}

export interface Student {
  id: number
  name: string
  studentNo: string
  grade: string
  direction: string
  phone: string
  status: string
  avatar: string
  lastResearchMinutes: number
  totalResearchMinutes: number
}

export interface AttendanceRecord {
  id: number
  studentId: number
  studentName: string
  date: string
  signInTime?: string
  signOutTime?: string
  status: string
  remark?: string
}

export interface CleaningSchedule {
  id: number
  date: string
  studentId: number
  studentName: string
  task: string
  status: string
  completedAt?: string
  remark?: string
}

export interface DeskItem {
  id: number
  studentId: number
  studentName: string
  name: string
  quantity: number
  status: string
  remark?: string
}

export interface ResearchTimeRecord {
  id: number
  studentId: number
  studentName: string
  date: string
  minutes: number
  source: string
}

export interface ResearchTask {
  id: number
  studentId: number
  studentName: string
  date: string
  content: string
  updatedAt: string
}

export interface AdminDashboard {
  studentCount: number
  attendanceToday: number
  tasksSubmitted: number
  cleaningDone: number
  researchMinutesToday: number
}

export interface StudentDashboard {
  student: Student
  todayAttendance?: AttendanceRecord
  todayCleaning?: CleaningSchedule
  todayResearchTime?: ResearchTimeRecord
  todayResearchTask?: ResearchTask
  deskItems: DeskItem[]
}
