import type {
  AttendanceRecord,
  CleaningSchedule,
  DeskItem,
  ResearchTask,
  ResearchTimeRecord,
  Student,
  User,
} from '@/types/models'
import { formatDateTime, getTodayKey } from '@/utils/date'
import { randomInt, randomPick, randomResearchMinutes } from '@/utils/random'

const STORAGE_KEY = 'lab203_mock_db'
const USER_KEY = 'lab203_user'

interface MockDb {
  users: User[]
  students: Student[]
  attendanceRecords: AttendanceRecord[]
  cleaningSchedules: CleaningSchedule[]
  deskItems: DeskItem[]
  researchTimeRecords: ResearchTimeRecord[]
  researchTasks: ResearchTask[]
}

// 生成自增 ID
const createIdGenerator = () => {
  let current = 0
  return () => {
    current += 1
    return current
  }
}

const nextId = createIdGenerator()

const studentNames = [
  '李昊',
  '张雨泽',
  '王思涵',
  '赵一诺',
  '陈浩宇',
  '刘佳宁',
  '孙泽宇',
  '周沐阳',
  '吴语彤',
  '郑思远',
  '马欣怡',
  '黄子航',
  '林梓涵',
  '何嘉宸',
  '高子辰',
  '梁晨曦',
  '许清扬',
  '邓舒然',
  '谢语安',
  '范思雨',
]

const directions = ['机器视觉', '智能感知', '边缘计算', '信息安全', '数据挖掘']
const deskItemsPool = ['示波器', '电子书', '项目板卡', '测试夹具', 'PCB 样板']
const cleaningTasks = ['地面清洁', '桌面整理', '垃圾清运', '设备除尘', '白板擦拭']

// 生成默认头像链接
const buildAvatar = (seed: string) => {
  return `https://api.dicebear.com/8.x/identicon/svg?seed=${encodeURIComponent(
    seed,
  )}`
}

// 生成学生数据
const seedStudents = () => {
  return studentNames.map((name, index) => {
    const id = index + 1
    return {
      id,
      name,
      studentNo: `2024${String(id).padStart(3, '0')}`,
      grade: '研二',
      direction: randomPick(directions),
      phone: `188${randomInt(1000, 9999)}${randomInt(1000, 9999)}`,
      status: '在读',
      avatar: buildAvatar(name),
      lastResearchMinutes: randomInt(90, 280),
      totalResearchMinutes: randomInt(3000, 8200),
    }
  })
}

// 生成管理员与学生用户
const seedUsers = (students: Student[]): User[] => {
  const admin: User = {
    id: 1000,
    name: '管理员',
    role: 'admin',
    avatar: buildAvatar('admin'),
  }
  const studentUsers = students.map((student) => ({
    id: student.id,
    name: student.name,
    role: 'student' as const,
    avatar: student.avatar,
    studentNo: student.studentNo,
  }))
  return [admin, ...studentUsers]
}

// 生成初始出勤记录
const seedAttendance = (students: Student[]): AttendanceRecord[] => {
  const today = getTodayKey()
  return students.map((student) => ({
    id: nextId(),
    studentId: student.id,
    studentName: student.name,
    date: today,
    signInTime: randomPick(['08:50', '09:10', '09:25', '09:40']),
    signOutTime: randomPick(['18:30', '19:10', '20:00', '21:10']),
    status: randomPick(['正常', '迟到', '缺勤']),
    remark: '实验室自习',
  }))
}

// 生成初始值日安排
const seedCleaningSchedules = (students: Student[]): CleaningSchedule[] => {
  const today = getTodayKey()
  return students.slice(0, 6).map((student) => ({
    id: nextId(),
    date: today,
    studentId: student.id,
    studentName: student.name,
    task: randomPick(cleaningTasks),
    status: randomPick(['待完成', '进行中', '已完成']),
    completedAt: formatDateTime(new Date()),
    remark: '重点清洁设备区域',
  }))
}

// 生成桌面物品清单
const seedDeskItems = (students: Student[]): DeskItem[] => {
  return students.flatMap((student) => {
    return deskItemsPool.slice(0, 3).map((item) => ({
      id: nextId(),
      studentId: student.id,
      studentName: student.name,
      name: item,
      quantity: randomInt(1, 3),
      status: randomPick(['在用', '闲置', '维护']),
      remark: '保持桌面整洁',
    }))
  })
}

// 生成科研时长记录
const seedResearchTime = (students: Student[]): ResearchTimeRecord[] => {
  const today = getTodayKey()
  return students.map((student) => ({
    id: nextId(),
    studentId: student.id,
    studentName: student.name,
    date: today,
    minutes: randomResearchMinutes(),
    source: '系统生成',
  }))
}

// 生成科研任务记录
const seedResearchTasks = (students: Student[]): ResearchTask[] => {
  const today = getTodayKey()
  return students.map((student) => ({
    id: nextId(),
    studentId: student.id,
    studentName: student.name,
    date: today,
    content: randomPick([
      '完成模型训练并整理实验对比表',
      '撰写论文相关工作部分',
      '优化传感器数据预处理脚本',
      '完成基准实验复现实验',
      '整理数据集标注规范',
    ]),
    updatedAt: formatDateTime(new Date()),
  }))
}

// 初始化数据库
const createInitialDb = (): MockDb => {
  const students = seedStudents()
  return {
    users: seedUsers(students),
    students,
    attendanceRecords: seedAttendance(students),
    cleaningSchedules: seedCleaningSchedules(students),
    deskItems: seedDeskItems(students),
    researchTimeRecords: seedResearchTime(students),
    researchTasks: seedResearchTasks(students),
  }
}

// 读取或初始化本地数据库
const loadDb = (): MockDb => {
  const raw = localStorage.getItem(STORAGE_KEY)
  if (!raw) {
    const initDb = createInitialDb()
    localStorage.setItem(STORAGE_KEY, JSON.stringify(initDb))
    return initDb
  }
  return JSON.parse(raw) as MockDb
}

// 持久化数据库
const saveDb = (db: MockDb) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(db))
}

// 读取当前用户
const getCurrentUser = (): User | null => {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null
  return JSON.parse(raw) as User
}

// 保存当前用户
const setCurrentUser = (user: User | null) => {
  if (!user) {
    localStorage.removeItem(USER_KEY)
    return
  }
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

// 模拟登录接口
export const mockLogin = async (payload: {
  account: string
  password: string
}) => {
  const db = loadDb()
  const isPasswordValid = payload.password === '123456'
  if (!isPasswordValid) {
    throw new Error('账号或密码错误')
  }
  if (payload.account === 'admin') {
    const admin = db.users.find((user) => user.role === 'admin')
    if (!admin) throw new Error('管理员不存在')
    setCurrentUser(admin)
    return admin
  }
  const student = db.users.find(
    (user) => user.studentNo === payload.account,
  )
  if (!student) {
    throw new Error('学生账号不存在')
  }
  setCurrentUser(student)
  return student
}

// 模拟登出接口
export const mockLogout = async () => {
  setCurrentUser(null)
  return true
}

// 模拟获取当前用户
export const mockGetCurrentUser = async () => {
  const user = getCurrentUser()
  if (!user) {
    throw new Error('未登录')
  }
  return user
}

// 模拟学生列表查询
export const mockListStudents = async () => {
  const db = loadDb()
  return db.students
}

// 模拟新增学生
export const mockCreateStudent = async (student: Partial<Student>) => {
  const db = loadDb()
  const created: Student = {
    id: nextId(),
    name: student.name ?? '新同学',
    studentNo: student.studentNo ?? `2024${randomInt(100, 999)}`,
    grade: student.grade ?? '研一',
    direction: student.direction ?? randomPick(directions),
    phone: student.phone ?? `188${randomInt(1000, 9999)}${randomInt(1000, 9999)}`,
    status: student.status ?? '在读',
    avatar: student.avatar ?? buildAvatar(student.name ?? 'student'),
    lastResearchMinutes: 0,
    totalResearchMinutes: 0,
  }
  db.students.unshift(created)
  db.users.push({
    id: created.id,
    name: created.name,
    role: 'student',
    avatar: created.avatar,
    studentNo: created.studentNo,
  })
  saveDb(db)
  return created
}

// 模拟更新学生
export const mockUpdateStudent = async (student: Student) => {
  const db = loadDb()
  const index = db.students.findIndex((item) => item.id === student.id)
  if (index === -1) throw new Error('学生不存在')
  db.students[index] = { ...db.students[index], ...student }
  saveDb(db)
  return db.students[index]
}

// 模拟删除学生
export const mockDeleteStudent = async (id: number) => {
  const db = loadDb()
  db.students = db.students.filter((item) => item.id !== id)
  db.users = db.users.filter((item) => item.id !== id)
  saveDb(db)
  return true
}

// 模拟出勤列表
export const mockListAttendance = async () => {
  const db = loadDb()
  return db.attendanceRecords
}

// 模拟个人出勤列表
export const mockListMyAttendance = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  return db.attendanceRecords.filter(
    (record) => record.studentId === currentUser.id,
  )
}

// 模拟签到
export const mockSignIn = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  const existing = db.attendanceRecords.find(
    (record) => record.studentId === currentUser.id && record.date === today,
  )
  if (existing) {
    existing.signInTime = formatDateTime(new Date())
    existing.status = '正常'
    saveDb(db)
    return existing
  }
  const created: AttendanceRecord = {
    id: nextId(),
    studentId: currentUser.id,
    studentName: currentUser.name,
    date: today,
    signInTime: formatDateTime(new Date()),
    status: '正常',
  }
  db.attendanceRecords.unshift(created)
  saveDb(db)
  return created
}

// 模拟签退
export const mockSignOut = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  const existing = db.attendanceRecords.find(
    (record) => record.studentId === currentUser.id && record.date === today,
  )
  if (!existing) {
    throw new Error('请先签到')
  }
  existing.signOutTime = formatDateTime(new Date())
  existing.status = '正常'
  saveDb(db)
  return existing
}

// 模拟管理员补录出勤
export const mockCreateAttendance = async (record: AttendanceRecord) => {
  const db = loadDb()
  const created = { ...record, id: nextId() }
  db.attendanceRecords.unshift(created)
  saveDb(db)
  return created
}

// 模拟值日列表
export const mockListCleaning = async () => {
  const db = loadDb()
  return db.cleaningSchedules
}

// 模拟个人值日
export const mockListMyCleaning = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  return db.cleaningSchedules.filter(
    (item) => item.studentId === currentUser.id,
  )
}

// 模拟更新值日安排
export const mockUpdateCleaning = async (payload: CleaningSchedule) => {
  const db = loadDb()
  const index = db.cleaningSchedules.findIndex((item) => item.id === payload.id)
  if (index === -1) throw new Error('值日记录不存在')
  db.cleaningSchedules[index] = { ...db.cleaningSchedules[index], ...payload }
  saveDb(db)
  return db.cleaningSchedules[index]
}

// 模拟桌面物品列表
export const mockListDeskItems = async () => {
  const db = loadDb()
  return db.deskItems
}

// 模拟个人桌面物品
export const mockListMyDeskItems = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  return db.deskItems.filter((item) => item.studentId === currentUser.id)
}

// 模拟更新桌面物品
export const mockUpdateDeskItem = async (payload: DeskItem) => {
  const db = loadDb()
  const index = db.deskItems.findIndex((item) => item.id === payload.id)
  if (index === -1) throw new Error('桌面物品不存在')
  db.deskItems[index] = { ...db.deskItems[index], ...payload }
  saveDb(db)
  return db.deskItems[index]
}

// 模拟科研时长列表
export const mockListResearchTime = async () => {
  const db = loadDb()
  return db.researchTimeRecords
}

// 模拟个人科研时长列表
export const mockListMyResearchTime = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  return db.researchTimeRecords.filter(
    (record) => record.studentId === currentUser.id,
  )
}

// 模拟生成今日科研时长
export const mockGenerateResearchTime = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  const existing = db.researchTimeRecords.find(
    (record) => record.studentId === currentUser.id && record.date === today,
  )
  if (existing) {
    return existing
  }
  const created: ResearchTimeRecord = {
    id: nextId(),
    studentId: currentUser.id,
    studentName: currentUser.name,
    date: today,
    minutes: randomResearchMinutes(),
    source: '系统生成',
  }
  db.researchTimeRecords.unshift(created)
  const student = db.students.find((item) => item.id === currentUser.id)
  if (student) {
    student.lastResearchMinutes = created.minutes
    student.totalResearchMinutes += created.minutes
  }
  saveDb(db)
  return created
}

// 模拟获取今日科研时长
export const mockGetTodayResearchTime = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  return (
    db.researchTimeRecords.find(
      (record) => record.studentId === currentUser.id && record.date === today,
    ) ?? null
  )
}

// 模拟科研任务列表
export const mockListResearchTasks = async () => {
  const db = loadDb()
  return db.researchTasks
}

// 模拟个人科研任务历史
export const mockListMyResearchTasks = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  return db.researchTasks.filter((task) => task.studentId === currentUser.id)
}

// 模拟获取今日科研任务
export const mockGetTodayResearchTask = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  return (
    db.researchTasks.find(
      (task) => task.studentId === currentUser.id && task.date === today,
    ) ?? null
  )
}

// 模拟保存今日科研任务
export const mockSaveTodayResearchTask = async (content: string) => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  const existing = db.researchTasks.find(
    (task) => task.studentId === currentUser.id && task.date === today,
  )
  if (existing) {
    existing.content = content
    existing.updatedAt = formatDateTime(new Date())
    saveDb(db)
    return existing
  }
  const created: ResearchTask = {
    id: nextId(),
    studentId: currentUser.id,
    studentName: currentUser.name,
    date: today,
    content,
    updatedAt: formatDateTime(new Date()),
  }
  db.researchTasks.unshift(created)
  saveDb(db)
  return created
}

// 模拟管理员仪表盘汇总
export const mockAdminDashboard = async () => {
  const db = loadDb()
  const today = getTodayKey()
  const attendanceToday = db.attendanceRecords.filter(
    (record) => record.date === today,
  ).length
  const tasksSubmitted = db.researchTasks.filter(
    (task) => task.date === today && task.content,
  ).length
  const cleaningDone = db.cleaningSchedules.filter(
    (item) => item.date === today && item.status === '已完成',
  ).length
  const researchMinutesToday = db.researchTimeRecords
    .filter((record) => record.date === today)
    .reduce((sum, record) => sum + record.minutes, 0)
  return {
    studentCount: db.students.length,
    attendanceToday,
    tasksSubmitted,
    cleaningDone,
    researchMinutesToday,
  }
}

// 模拟学生首页数据
export const mockStudentDashboard = async () => {
  const db = loadDb()
  const currentUser = getCurrentUser()
  if (!currentUser) throw new Error('未登录')
  const today = getTodayKey()
  const student = db.students.find((item) => item.id === currentUser.id)
  if (!student) throw new Error('学生不存在')
  return {
    student,
    todayAttendance: db.attendanceRecords.find(
      (record) => record.studentId === student.id && record.date === today,
    ),
    todayCleaning: db.cleaningSchedules.find(
      (item) => item.studentId === student.id && item.date === today,
    ),
    todayResearchTime: db.researchTimeRecords.find(
      (record) => record.studentId === student.id && record.date === today,
    ),
    todayResearchTask: db.researchTasks.find(
      (task) => task.studentId === student.id && task.date === today,
    ),
    deskItems: db.deskItems.filter((item) => item.studentId === student.id),
  }
}
