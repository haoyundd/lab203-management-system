package com.example.lab203.controller;

import com.example.lab203.common.result.Result;
import com.example.lab203.dto.DeskItemSaveDTO;
import com.example.lab203.service.DeskItemService;
import com.example.lab203.vo.DeskItemVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 桌面物品控制器。
 */
@RestController
@RequestMapping("/api/desk-items")
public class DeskItemController {

    private final DeskItemService deskItemService;

    public DeskItemController(DeskItemService deskItemService) {
        this.deskItemService = deskItemService;
    }

    /**
     * 查询全部桌面物品。
     *
     * @return 物品列表
     */
    @GetMapping
    public Result<List<DeskItemVO>> listAll() {
        return Result.success(deskItemService.listAll());
    }

    /**
     * 查询我的桌面物品。
     *
     * @return 物品列表
     */
    @GetMapping("/my-items")
    public Result<List<DeskItemVO>> myItems() {
        return Result.success(deskItemService.myItems());
    }

    /**
     * 新增桌面物品。
     *
     * @param saveDTO 保存对象
     * @return 物品详情
     */
    @PostMapping
    public Result<DeskItemVO> create(@Valid @RequestBody DeskItemSaveDTO saveDTO) {
        return Result.success(deskItemService.save(saveDTO));
    }

    /**
     * 修改桌面物品。
     *
     * @param id 主键
     * @param saveDTO 保存对象
     * @return 物品详情
     */
    @PutMapping("/{id}")
    public Result<DeskItemVO> update(@PathVariable Long id, @Valid @RequestBody DeskItemSaveDTO saveDTO) {
        saveDTO.setId(id);
        return Result.success(deskItemService.save(saveDTO));
    }

    /**
     * 删除桌面物品。
     *
     * @param id 主键
     * @return 空结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deskItemService.delete(id);
        return Result.success();
    }
}
