- [x] 初始化工作区目录与执行基线
- [x] 搭建 backend Spring Boot + MyBatis 项目骨架
- [x] 设计并实现数据库表结构、Mapper、Manager、Service、Controller
- [x] 编写 SQL 初始化脚本与 20 名学生种子数据、头像资源
- [x] 搭建 frontend Vue 3 项目骨架与科研仪表盘界面
- [x] 对接前后端接口并完成核心业务页面
- [x] 进行构建验证与结果记录
- [x] 验证真实 MySQL 连接与后端启动
- [x] 启动前端联调环境并完成学生业务链路冒烟

- [x] 补充发布前收尾项（忽略规则、仓库初始化、最终验证）
- [x] 初始化本地 Git 仓库并整理首个提交
- [ ] 尝试连接并上传到 GitHub

## review
- 后端：已补齐 Spring Boot + MyBatis 四层结构、统一 Result、全局异常、鉴权拦截、种子数据初始化、SVG 头像生成与核心业务接口。
- 前端：已完成 Vue 3 + Pinia + Router + Element Plus 管理端/学生端页面，并通过构建验证。
- 验证：`backend` 使用 JBR 21 + Maven Wrapper 执行 `mvn test` 通过；`frontend` 执行 `npm run build` 通过；真实 MySQL 已连接成功，后端已启动，并完成管理员登录、学生登录、仪表盘接口冒烟验证。
- 联调：前端开发服务已启动在 `http://127.0.0.1:5174/`；学生账号已成功修改今日科研任务并生成今日科研时长，管理员端可看到更新后的今日任务。
- 修复：真实启动时发现 `AvatarSvgGenerator` 的 `%` 格式化问题，已修复后重新启动成功。
- 风险：当前终端/PowerShell 对部分中文输出存在编码显示异常，但接口实际返回与数据库写入不受影响。
- 二轮复测：2026-04-14 再次使用真实接口完成“管理员新增学生 -> 管理员新增值日/桌面物品 -> 学生登录签到/签退 -> 填写今日科研任务 -> 生成今日科研时长 -> 完成值日 -> 管理员总览复核”完整闭环，新增测试学生 `20270414203014` 全流程通过。
- 回归修复：复测发现管理员新建学生未传 `avatarPath` 时头像会留空，现已在学生保存逻辑中补齐自动 SVG 头像生成并落库；复测新增学生头像路径已成功写入 `/uploads/avatars/student-20270414203014.svg`。


- 发布：已新增根目录 .gitignore、完成本地 Git 初始化，当前首个提交为 dd92134 feat: finalize lab203 management system。
- 阻塞：当前工作区原本不是 Git 仓库，且本机未安装 GitHub CLI / browser automation 运行环境，也没有现成的 GitHub 远程地址，因此上传 GitHub 还需补充仓库目标信息。
