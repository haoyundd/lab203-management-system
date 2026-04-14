package com.example.lab203.service;

import com.example.lab203.vo.DashboardAdminVO;
import com.example.lab203.vo.DashboardStudentVO;

/**
 * ?????????
 */
public interface DashboardService {

    /**
     * ??????????
     *
     * @return ?????
     */
    DashboardAdminVO adminDashboard();

    /**
     * ?????????
     *
     * @return ????
     */
    DashboardStudentVO studentDashboard();
}
