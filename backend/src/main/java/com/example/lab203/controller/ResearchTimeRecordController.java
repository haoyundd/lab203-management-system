package com.example.lab203.controller;

import com.example.lab203.common.result.Result;
import com.example.lab203.service.ResearchTimeRecordService;
import com.example.lab203.vo.ResearchTimeRecordVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 科研时长控制器。
 */
@RestController
@RequestMapping("/api/research-time-records")
public class ResearchTimeRecordController {

    private final ResearchTimeRecordService researchTimeRecordService;

    public ResearchTimeRecordController(ResearchTimeRecordService researchTimeRecordService) {
        this.researchTimeRecordService = researchTimeRecordService;
    }

    /**
     * 生成今日科研时长。
     *
     * @return 时长详情
     */
    @PostMapping("/generate-today")
    public Result<ResearchTimeRecordVO> generateToday() {
        return Result.success(researchTimeRecordService.generateToday());
    }

    /**
     * 查询我的科研时长历史。
     *
     * @return 历史列表
     */
    @GetMapping("/my-history")
    public Result<List<ResearchTimeRecordVO>> myHistory() {
        return Result.success(researchTimeRecordService.myHistory());
    }

    /**
     * 查询全部科研时长记录。
     *
     * @return 记录列表
     */
    @GetMapping
    public Result<List<ResearchTimeRecordVO>> listAll() {
        return Result.success(researchTimeRecordService.listAll());
    }
}
