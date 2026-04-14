package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.entity.AttendanceRecordEntity;
import com.example.lab203.entity.CleaningScheduleEntity;
import com.example.lab203.entity.ResearchTaskRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.AttendanceRecordManager;
import com.example.lab203.manager.CleaningScheduleManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.DashboardService;
import com.example.lab203.vo.AttendanceRecordVO;
import com.example.lab203.vo.CleaningScheduleVO;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.DashboardAdminVO;
import com.example.lab203.vo.DashboardStudentVO;
import com.example.lab203.vo.DeskItemVO;
import com.example.lab203.vo.ResearchTaskVO;
import com.example.lab203.vo.ResearchTimeRecordVO;
import com.example.lab203.vo.StudentVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 首页聚合服务实现。
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final StudentManager studentManager;
    private final AttendanceRecordManager attendanceRecordManager;
    private final CleaningScheduleManager cleaningScheduleManager;
    private final AttendanceRecordServiceImpl attendanceRecordService;
    private final CleaningScheduleServiceImpl cleaningScheduleService;
    private final DeskItemServiceImpl deskItemService;
    private final ResearchTaskRecordServiceImpl researchTaskRecordService;
    private final ResearchTimeRecordServiceImpl researchTimeRecordService;

    public DashboardServiceImpl(StudentManager studentManager,
                                AttendanceRecordManager attendanceRecordManager,
                                CleaningScheduleManager cleaningScheduleManager,
                                AttendanceRecordServiceImpl attendanceRecordService,
                                CleaningScheduleServiceImpl cleaningScheduleService,
                                DeskItemServiceImpl deskItemService,
                                ResearchTaskRecordServiceImpl researchTaskRecordService,
                                ResearchTimeRecordServiceImpl researchTimeRecordService) {
        this.studentManager = studentManager;
        this.attendanceRecordManager = attendanceRecordManager;
        this.cleaningScheduleManager = cleaningScheduleManager;
        this.attendanceRecordService = attendanceRecordService;
        this.cleaningScheduleService = cleaningScheduleService;
        this.deskItemService = deskItemService;
        this.researchTaskRecordService = researchTaskRecordService;
        this.researchTimeRecordService = researchTimeRecordService;
    }

    /**
     * 获取管理员首页数据。
     *
     * @return 管理员首页
     */
    @Override
    public DashboardAdminVO adminDashboard() {
        AuthAssert.requireAdmin();
        LocalDate today = LocalDate.now();
        DashboardAdminVO vo = new DashboardAdminVO();
        List<AttendanceRecordVO> todayAttendance = attendanceRecordService.list(new com.example.lab203.dto.AttendanceRecordQueryDTO()).stream()
                .filter(item -> today.equals(item.getAttendanceDate()))
                .toList();
        List<CleaningScheduleVO> todayCleaning = cleaningScheduleService.list(new com.example.lab203.dto.CleaningScheduleQueryDTO()).stream()
                .filter(item -> today.equals(item.getScheduleDate()))
                .toList();
        List<ResearchTaskVO> todayTasks = researchTaskRecordService.todayList();
        vo.setStudentCount(studentManager.listAll().size());
        vo.setSignedInCount((int) todayAttendance.stream().filter(item -> item.getSignInTime() != null).count());
        vo.setTaskFilledCount(todayTasks.size());
        vo.setCleaningDoneCount((int) todayCleaning.stream().filter(item -> "DONE".equals(item.getStatus())).count());
        vo.setTodayAttendance(todayAttendance);
        vo.setTodayTasks(todayTasks);
        vo.setTodayCleaning(todayCleaning);
        return vo;
    }

    /**
     * 获取学生首页数据。
     *
     * @return 学生首页
     */
    @Override
    public DashboardStudentVO studentDashboard() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        StudentEntity studentEntity = studentManager.getById(currentUser.getStudentId());
        StudentVO studentVO = new StudentVO();
        studentVO.setId(studentEntity.getId());
        studentVO.setStudentNo(studentEntity.getStudentNo());
        studentVO.setName(studentEntity.getName());
        studentVO.setGrade(studentEntity.getGrade());
        studentVO.setMajor(studentEntity.getMajor());
        studentVO.setResearchDirection(studentEntity.getResearchDirection());
        studentVO.setPhone(studentEntity.getPhone());
        studentVO.setEmail(studentEntity.getEmail());
        studentVO.setAvatarPath(studentEntity.getAvatarPath());
        studentVO.setLatestResearchHours(studentEntity.getLatestResearchHours());
        studentVO.setTotalResearchHours(studentEntity.getTotalResearchHours());
        AttendanceRecordVO todayAttendance = attendanceRecordService.myHistory().stream().findFirst().orElse(null);
        CleaningScheduleVO todayCleaning = cleaningScheduleService.myToday();
        ResearchTaskVO todayTask = researchTaskRecordService.myToday();
        ResearchTimeRecordVO todayResearchTime = researchTimeRecordService.myHistory().stream()
                .filter(item -> LocalDate.now().equals(item.getRecordDate()))
                .findFirst()
                .orElse(null);
        List<DeskItemVO> deskItems = deskItemService.myItems();
        DashboardStudentVO vo = new DashboardStudentVO();
        vo.setStudent(studentVO);
        vo.setTodayAttendance(todayAttendance);
        vo.setTodayCleaning(todayCleaning);
        vo.setTodayTask(todayTask);
        vo.setTodayResearchTime(todayResearchTime);
        vo.setDeskItems(deskItems);
        return vo;
    }
}
