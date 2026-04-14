DROP TABLE IF EXISTS lab_research_task_record;
DROP TABLE IF EXISTS lab_research_time_record;
DROP TABLE IF EXISTS lab_desk_item;
DROP TABLE IF EXISTS lab_cleaning_schedule;
DROP TABLE IF EXISTS lab_attendance_record;
DROP TABLE IF EXISTS lab_account;
DROP TABLE IF EXISTS lab_student;

CREATE TABLE IF NOT EXISTS lab_student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_no VARCHAR(32) NOT NULL COMMENT '学号',
    name VARCHAR(64) NOT NULL COMMENT '姓名',
    grade VARCHAR(32) NOT NULL COMMENT '年级',
    major VARCHAR(64) NOT NULL COMMENT '专业',
    research_direction VARCHAR(128) NOT NULL COMMENT '研究方向',
    phone VARCHAR(32) NOT NULL COMMENT '手机号',
    email VARCHAR(128) NOT NULL COMMENT '邮箱',
    avatar_path VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
    latest_research_hours DECIMAL(5,1) NOT NULL DEFAULT 0.0 COMMENT '最近一次科研时长',
    total_research_hours DECIMAL(7,1) NOT NULL DEFAULT 0.0 COMMENT '累计科研时长',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_student_no (student_no),
    KEY idx_student_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研究生档案表';

CREATE TABLE IF NOT EXISTS lab_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    role VARCHAR(32) NOT NULL COMMENT '角色',
    student_id BIGINT DEFAULT NULL COMMENT '关联学生主键',
    display_name VARCHAR(64) NOT NULL COMMENT '显示名称',
    token VARCHAR(128) DEFAULT NULL COMMENT '登录 token',
    token_expire_time DATETIME DEFAULT NULL COMMENT 'token 过期时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统账号表';

CREATE TABLE IF NOT EXISTS lab_attendance_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_id BIGINT NOT NULL COMMENT '学生主键',
    attendance_date DATE NOT NULL COMMENT '出勤日期',
    sign_in_time DATETIME DEFAULT NULL COMMENT '签到时间',
    sign_out_time DATETIME DEFAULT NULL COMMENT '签退时间',
    status VARCHAR(32) NOT NULL COMMENT '出勤状态',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_attendance_student_date (student_id, attendance_date),
    KEY idx_attendance_date (attendance_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出勤记录表';

CREATE TABLE IF NOT EXISTS lab_cleaning_schedule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    schedule_date DATE NOT NULL COMMENT '值日日期',
    student_id BIGINT NOT NULL COMMENT '责任学生',
    task_content VARCHAR(255) NOT NULL COMMENT '任务内容',
    status VARCHAR(32) NOT NULL COMMENT '状态',
    finished_time DATETIME DEFAULT NULL COMMENT '完成时间',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    KEY idx_cleaning_date (schedule_date),
    KEY idx_cleaning_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卫生排班表';

CREATE TABLE IF NOT EXISTS lab_desk_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_id BIGINT NOT NULL COMMENT '学生主键',
    item_name VARCHAR(128) NOT NULL COMMENT '物品名称',
    quantity INT NOT NULL COMMENT '数量',
    item_status VARCHAR(64) NOT NULL COMMENT '物品状态',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    KEY idx_desk_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='桌面物品表';

CREATE TABLE IF NOT EXISTS lab_research_time_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_id BIGINT NOT NULL COMMENT '学生主键',
    record_date DATE NOT NULL COMMENT '记录日期',
    hours DECIMAL(5,1) NOT NULL COMMENT '科研时长',
    source_type VARCHAR(64) NOT NULL COMMENT '来源类型',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_research_time_student_date (student_id, record_date),
    KEY idx_research_time_date (record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研时长记录表';

CREATE TABLE IF NOT EXISTS lab_research_task_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_id BIGINT NOT NULL COMMENT '学生主键',
    task_date DATE NOT NULL COMMENT '任务日期',
    task_content TEXT NOT NULL COMMENT '任务内容',
    last_modified_time DATETIME NOT NULL COMMENT '最后修改时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_research_task_student_date (student_id, task_date),
    KEY idx_research_task_date (task_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研任务记录表';
