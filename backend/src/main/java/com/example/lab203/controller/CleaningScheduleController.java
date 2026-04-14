package com.example.lab203.controller;

import com.example.lab203.common.result.Result;
import com.example.lab203.dto.CleaningScheduleQueryDTO;
import com.example.lab203.dto.CleaningScheduleSaveDTO;
import com.example.lab203.service.CleaningScheduleService;
import com.example.lab203.vo.CleaningScheduleVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 卫生排班控制器。
 */
@RestController
@RequestMapping("/api/cleaning-schedules")
public class CleaningScheduleController {

    private final CleaningScheduleService cleaningScheduleService;

    public CleaningScheduleController(CleaningScheduleService cleaningScheduleService) {
        this.cleaningScheduleService = cleaningScheduleService;
    }

    /**
     * 新增排班。
     *
     * @param saveDTO 保存对象
     * @return 排班详情
     */
    @PostMapping
    public Result<CleaningScheduleVO> create(@Valid @RequestBody CleaningScheduleSaveDTO saveDTO) {
        return Result.success(cleaningScheduleService.save(saveDTO));
    }

    /**
     * 修改排班。
     *
     * @param id 主键
     * @param saveDTO 保存对象
     * @return 排班详情
     */
    @PutMapping("/{id}")
    public Result<CleaningScheduleVO> update(@PathVariable Long id, @Valid @RequestBody CleaningScheduleSaveDTO saveDTO) {
        saveDTO.setId(id);
        return Result.success(cleaningScheduleService.save(saveDTO));
    }

    /**
     * 完成排班。
     *
     * @param id 主键
     * @return 排班详情
     */
    @PostMapping("/{id}/finish")
    public Result<CleaningScheduleVO> finish(@PathVariable Long id) {
        return Result.success(cleaningScheduleService.finish(id));
    }

    /**
     * 查询排班列表。
     *
     * @param queryDTO 查询对象
     * @return 排班列表
     */
    @GetMapping
    public Result<List<CleaningScheduleVO>> list(CleaningScheduleQueryDTO queryDTO) {
        return Result.success(cleaningScheduleService.list(queryDTO));
    }

    /**
     * 查询我的今日排班。
     *
     * @return 今日排班
     */
    @GetMapping("/my-today")
    public Result<CleaningScheduleVO> myToday() {
        return Result.success(cleaningScheduleService.myToday());
    }
}
