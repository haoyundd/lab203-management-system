package com.example.lab203.controller;

import com.example.lab203.common.result.Result;
import com.example.lab203.dto.ResearchTaskSaveDTO;
import com.example.lab203.service.ResearchTaskRecordService;
import com.example.lab203.vo.ResearchTaskVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 科研任务控制器。
 */
@RestController
@RequestMapping("/api/research-tasks")
public class ResearchTaskController {

    private final ResearchTaskRecordService researchTaskRecordService;

    public ResearchTaskController(ResearchTaskRecordService researchTaskRecordService) {
        this.researchTaskRecordService = researchTaskRecordService;
    }

    /**
     * 查询我今天的科研任务。
     *
     * @return 任务详情
     */
    @GetMapping("/my-today")
    public Result<ResearchTaskVO> myToday() {
        return Result.success(researchTaskRecordService.myToday());
    }

    /**
     * 保存我今天的科研任务。
     *
     * @param saveDTO 保存对象
     * @return 任务详情
     */
    @PutMapping("/my-today")
    public Result<ResearchTaskVO> saveToday(@Valid @RequestBody ResearchTaskSaveDTO saveDTO) {
        return Result.success(researchTaskRecordService.saveToday(saveDTO));
    }

    /**
     * 查询我的科研任务历史。
     *
     * @return 历史列表
     */
    @GetMapping("/my-history")
    public Result<List<ResearchTaskVO>> myHistory() {
        return Result.success(researchTaskRecordService.myHistory());
    }

    /**
     * 查询全体学生今日任务。
     *
     * @return 今日任务列表
     */
    @GetMapping("/today")
    public Result<List<ResearchTaskVO>> todayList() {
        return Result.success(researchTaskRecordService.todayList());
    }
}
