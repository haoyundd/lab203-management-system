package com.example.lab203.controller;

import com.example.lab203.common.result.Result;
import com.example.lab203.service.DashboardService;
import com.example.lab203.vo.DashboardAdminVO;
import com.example.lab203.vo.DashboardStudentVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页聚合控制器。
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 获取管理员首页数据。
     *
     * @return 管理员首页
     */
    @GetMapping("/admin")
    public Result<DashboardAdminVO> adminDashboard() {
        return Result.success(dashboardService.adminDashboard());
    }

    /**
     * 获取学生首页数据。
     *
     * @return 学生首页
     */
    @GetMapping("/student")
    public Result<DashboardStudentVO> studentDashboard() {
        return Result.success(dashboardService.studentDashboard());
    }
}
