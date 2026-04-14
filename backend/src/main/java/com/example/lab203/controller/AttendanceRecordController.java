package com.example.lab203.controller;

import com.example.lab203.common.result.Result;
import com.example.lab203.dto.AttendanceRecordQueryDTO;
import com.example.lab203.dto.AttendanceRecordSaveDTO;
import com.example.lab203.service.AttendanceRecordService;
import com.example.lab203.vo.AttendanceRecordVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 出勤控制器。
 */
@RestController
@RequestMapping("/api/attendance-records")
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService) {
        this.attendanceRecordService = attendanceRecordService;
    }

    /**
     * 学生签到。
     *
     * @return 今日记录
     */
    @PostMapping("/sign-in")
    public Result<AttendanceRecordVO> signIn() {
        return Result.success(attendanceRecordService.signIn());
    }

    /**
     * 学生签退。
     *
     * @return 今日记录
     */
    @PostMapping("/sign-out")
    public Result<AttendanceRecordVO> signOut() {
        return Result.success(attendanceRecordService.signOut());
    }

    /**
     * 管理员保存出勤。
     *
     * @param saveDTO 保存对象
     * @return 出勤详情
     */
    @PostMapping
    public Result<AttendanceRecordVO> create(@Valid @RequestBody AttendanceRecordSaveDTO saveDTO) {
        return Result.success(attendanceRecordService.saveByAdmin(saveDTO));
    }

    /**
     * 管理员修改出勤。
     *
     * @param saveDTO 保存对象
     * @return 出勤详情
     */
    @PutMapping
    public Result<AttendanceRecordVO> update(@Valid @RequestBody AttendanceRecordSaveDTO saveDTO) {
        return Result.success(attendanceRecordService.saveByAdmin(saveDTO));
    }

    /**
     * 查询出勤列表。
     *
     * @param queryDTO 查询对象
     * @return 出勤列表
     */
    @GetMapping
    public Result<List<AttendanceRecordVO>> list(AttendanceRecordQueryDTO queryDTO) {
        return Result.success(attendanceRecordService.list(queryDTO));
    }

    /**
     * 查询我的历史记录。
     *
     * @return 历史列表
     */
    @GetMapping("/my-history")
    public Result<List<AttendanceRecordVO>> myHistory() {
        return Result.success(attendanceRecordService.myHistory());
    }
}
