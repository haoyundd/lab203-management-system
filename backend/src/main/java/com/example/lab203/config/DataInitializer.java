package com.example.lab203.config;

import com.example.lab203.common.util.AvatarSvgGenerator;
import com.example.lab203.common.util.RandomResearchTimeGenerator;
import com.example.lab203.entity.AccountEntity;
import com.example.lab203.entity.AttendanceRecordEntity;
import com.example.lab203.entity.CleaningScheduleEntity;
import com.example.lab203.entity.DeskItemEntity;
import com.example.lab203.entity.ResearchTaskRecordEntity;
import com.example.lab203.entity.ResearchTimeRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.AccountManager;
import com.example.lab203.manager.AttendanceRecordManager;
import com.example.lab203.manager.CleaningScheduleManager;
import com.example.lab203.manager.DeskItemManager;
import com.example.lab203.manager.ResearchTaskRecordManager;
import com.example.lab203.manager.ResearchTimeRecordManager;
import com.example.lab203.manager.StudentManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 初始化演示数据。
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private static final List<String> NAMES = List.of(
            "陈远航", "李知秋", "王嘉禾", "赵书宁", "刘昊川",
            "周星野", "徐安澜", "孙景明", "黄若溪", "吴泽辰",
            "郑承恩", "谢可欣", "冯宇凡", "蒋慕白", "韩以沫",
            "唐修远", "宋知意", "邓语桐", "何景尧", "许青禾"
    );
    private static final List<String> DIRECTIONS = List.of(
            "多模态大模型", "知识图谱", "推荐系统", "边缘计算", "时序预测",
            "联邦学习", "计算机视觉", "工业互联网", "自然语言处理", "强化学习"
    );
    private static final List<String> COLORS = List.of(
            "#0F7CFF", "#19A974", "#F59E0B", "#EF4444", "#7C3AED",
            "#0EA5E9", "#14B8A6", "#E11D48", "#84CC16", "#F97316"
    );

    private final StudentManager studentManager;
    private final AccountManager accountManager;
    private final AttendanceRecordManager attendanceRecordManager;
    private final CleaningScheduleManager cleaningScheduleManager;
    private final DeskItemManager deskItemManager;
    private final ResearchTimeRecordManager researchTimeRecordManager;
    private final ResearchTaskRecordManager researchTaskRecordManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.avatar.storage-dir}")
    private String avatarStorageDir;

    public DataInitializer(StudentManager studentManager,
                           AccountManager accountManager,
                           AttendanceRecordManager attendanceRecordManager,
                           CleaningScheduleManager cleaningScheduleManager,
                           DeskItemManager deskItemManager,
                           ResearchTimeRecordManager researchTimeRecordManager,
                           ResearchTaskRecordManager researchTaskRecordManager,
                           PasswordEncoder passwordEncoder) {
        this.studentManager = studentManager;
        this.accountManager = accountManager;
        this.attendanceRecordManager = attendanceRecordManager;
        this.cleaningScheduleManager = cleaningScheduleManager;
        this.deskItemManager = deskItemManager;
        this.researchTimeRecordManager = researchTimeRecordManager;
        this.researchTaskRecordManager = researchTaskRecordManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 启动时写入演示数据。
     *
     * @param args 启动参数
     */
    @Override
    public void run(String... args) throws Exception {
        if (!studentManager.listAll().isEmpty()) {
            log.info("[DataInitializer] 已存在种子数据，跳过初始化");
            return;
        }
        initAdmin();
        initStudents();
        log.info("[DataInitializer] 种子数据初始化完成");
    }

    /**
     * 初始化管理员账号。
     */
    private void initAdmin() {
        if (accountManager.getByUsername("admin") != null) {
            return;
        }
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername("admin");
        accountEntity.setPasswordHash(passwordEncoder.encode("Admin@123"));
        accountEntity.setRole("ADMIN");
        accountEntity.setDisplayName("实验室管理员");
        accountEntity.setIsDeleted(0);
        accountEntity.setCreateTime(LocalDateTime.now());
        accountEntity.setUpdateTime(LocalDateTime.now());
        accountManager.save(accountEntity);
    }

    /**
     * 初始化 20 名研究生及相关业务数据。
     *
     * @throws IOException 头像生成异常
     */
    private void initStudents() throws IOException {
        LocalDate today = LocalDate.now();
        for (int index = 0; index < 20; index++) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setStudentNo("2024" + String.format("%04d", index + 1));
            studentEntity.setName(NAMES.get(index));
            studentEntity.setGrade(index < 10 ? "研一" : "研二");
            studentEntity.setMajor("信息工程");
            studentEntity.setResearchDirection(DIRECTIONS.get(index % DIRECTIONS.size()));
            studentEntity.setPhone("1380000" + String.format("%04d", index + 1));
            studentEntity.setEmail("student" + (index + 1) + "@lab203.edu.cn");
            studentEntity.setLatestResearchHours(BigDecimal.ZERO.setScale(1));
            studentEntity.setTotalResearchHours(BigDecimal.ZERO.setScale(1));
            studentEntity.setIsDeleted(0);
            studentEntity.setCreateTime(LocalDateTime.now());
            studentEntity.setUpdateTime(LocalDateTime.now());
            studentEntity.setAvatarPath(generateAvatar(index, studentEntity.getName()));
            studentManager.save(studentEntity);
            initStudentAccount(studentEntity);
            initDeskItems(studentEntity, index);
            initAttendance(studentEntity, today, index);
            initCleaning(studentEntity, today, index);
            initResearchTime(studentEntity, today);
            initResearchTask(studentEntity, today, index);
        }
    }

    /**
     * 初始化学生账号。
     *
     * @param studentEntity 学生实体
     */
    private void initStudentAccount(StudentEntity studentEntity) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(studentEntity.getStudentNo());
        accountEntity.setPasswordHash(passwordEncoder.encode("Lab203@123"));
        accountEntity.setRole("STUDENT");
        accountEntity.setStudentId(studentEntity.getId());
        accountEntity.setDisplayName(studentEntity.getName());
        accountEntity.setIsDeleted(0);
        accountEntity.setCreateTime(LocalDateTime.now());
        accountEntity.setUpdateTime(LocalDateTime.now());
        accountManager.save(accountEntity);
    }

    /**
     * 初始化桌面物品。
     *
     * @param studentEntity 学生实体
     * @param index 序号
     */
    private void initDeskItems(StudentEntity studentEntity, int index) {
        String[] items = {"笔记本电脑", "实验记录本", "开发板"};
        for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
            DeskItemEntity entity = new DeskItemEntity();
            entity.setStudentId(studentEntity.getId());
            entity.setItemName(items[itemIndex]);
            entity.setQuantity(itemIndex == 2 ? 1 : 2);
            entity.setItemStatus(itemIndex == 1 ? "整齐" : "在位");
            entity.setRemark("工位 A-" + (index + 1));
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            deskItemManager.save(entity);
        }
    }

    /**
     * 初始化近三天出勤数据。
     *
     * @param studentEntity 学生实体
     * @param today 今天
     * @param index 序号
     */
    private void initAttendance(StudentEntity studentEntity, LocalDate today, int index) {
        for (int dayOffset = 0; dayOffset < 3; dayOffset++) {
            LocalDate date = today.minusDays(dayOffset);
            AttendanceRecordEntity entity = new AttendanceRecordEntity();
            entity.setStudentId(studentEntity.getId());
            entity.setAttendanceDate(date);
            entity.setSignInTime(date.atTime(8 + index % 3, 10 + (index * 3) % 40));
            entity.setSignOutTime(dayOffset == 0 && index % 5 == 0 ? null : date.atTime(21 - index % 2, 30));
            entity.setStatus(entity.getSignOutTime() == null ? "SIGNED_IN" : "COMPLETED");
            entity.setRemark(dayOffset == 0 ? "实验推进中" : "正常出勤");
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            attendanceRecordManager.save(entity);
        }
    }

    /**
     * 初始化值日安排。
     *
     * @param studentEntity 学生实体
     * @param today 今天
     * @param index 序号
     */
    private void initCleaning(StudentEntity studentEntity, LocalDate today, int index) {
        CleaningScheduleEntity entity = new CleaningScheduleEntity();
        entity.setScheduleDate(today.plusDays(index % 4));
        entity.setStudentId(studentEntity.getId());
        entity.setTaskContent(index % 2 == 0 ? "擦拭桌面与设备" : "清理垃圾并整理公共区域");
        entity.setStatus(index % 3 == 0 ? "DONE" : "PENDING");
        entity.setFinishedTime(index % 3 == 0 ? today.atTime(18, 0) : null);
        entity.setRemark("副203实验室例行值日");
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        cleaningScheduleManager.save(entity);
    }

    /**
     * 初始化科研时长历史。
     *
     * @param studentEntity 学生实体
     * @param today 今天
     */
    private void initResearchTime(StudentEntity studentEntity, LocalDate today) {
        BigDecimal totalHours = BigDecimal.ZERO.setScale(1);
        for (int dayOffset = 1; dayOffset <= 2; dayOffset++) {
            LocalDate date = today.minusDays(dayOffset);
            BigDecimal hours = RandomResearchTimeGenerator.generate(studentEntity.getId(), date);
            ResearchTimeRecordEntity entity = new ResearchTimeRecordEntity();
            entity.setStudentId(studentEntity.getId());
            entity.setRecordDate(date);
            entity.setHours(hours);
            entity.setSourceType("SYSTEM_RANDOM");
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            researchTimeRecordManager.save(entity);
            totalHours = totalHours.add(hours);
        }
        studentEntity.setLatestResearchHours(totalHours.divide(BigDecimal.valueOf(2), 1, java.math.RoundingMode.HALF_UP));
        studentEntity.setTotalResearchHours(totalHours);
        studentEntity.setUpdateTime(LocalDateTime.now());
        studentManager.save(studentEntity);
    }

    /**
     * 初始化科研任务历史。
     *
     * @param studentEntity 学生实体
     * @param today 今天
     * @param index 序号
     */
    private void initResearchTask(StudentEntity studentEntity, LocalDate today, int index) {
        ResearchTaskRecordEntity yesterdayTask = new ResearchTaskRecordEntity();
        yesterdayTask.setStudentId(studentEntity.getId());
        yesterdayTask.setTaskDate(today.minusDays(1));
        yesterdayTask.setTaskContent("整理文献并完善周报 PPT");
        yesterdayTask.setLastModifiedTime(LocalDateTime.now().minusDays(1));
        yesterdayTask.setIsDeleted(0);
        yesterdayTask.setCreateTime(LocalDateTime.now());
        yesterdayTask.setUpdateTime(LocalDateTime.now());
        researchTaskRecordManager.save(yesterdayTask);

        ResearchTaskRecordEntity todayTask = new ResearchTaskRecordEntity();
        todayTask.setStudentId(studentEntity.getId());
        todayTask.setTaskDate(today);
        todayTask.setTaskContent(index % 2 == 0 ? "完成模型训练并记录实验结果" : "阅读 2 篇论文并补充实验设计");
        todayTask.setLastModifiedTime(LocalDateTime.now());
        todayTask.setIsDeleted(0);
        todayTask.setCreateTime(LocalDateTime.now());
        todayTask.setUpdateTime(LocalDateTime.now());
        researchTaskRecordManager.save(todayTask);
    }

    /**
     * 生成头像并返回访问路径。
     *
     * @param index 序号
     * @param name 姓名
     * @return 头像访问路径
     * @throws IOException 头像写入异常
     */
    private String generateAvatar(int index, String name) throws IOException {
        Path outputPath = Path.of(avatarStorageDir, "student-" + (index + 1) + ".svg");
        String primaryColor = COLORS.get(index % COLORS.size());
        String secondaryColor = COLORS.get((index + 3) % COLORS.size());
        AvatarSvgGenerator.generate(outputPath, name.substring(0, 1) + (index + 1), primaryColor, secondaryColor);
        return "/uploads/avatars/student-" + (index + 1) + ".svg";
    }
}
