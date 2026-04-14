package com.example.lab203.controller;

import com.example.lab203.common.result.PageResult;
import com.example.lab203.common.result.Result;
import com.example.lab203.dto.StudentQueryDTO;
import com.example.lab203.dto.StudentSaveDTO;
import com.example.lab203.service.StudentService;
import com.example.lab203.vo.StudentVO;
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
 * 学生控制器。
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 分页查询学生。
     *
     * @param queryDTO 查询对象
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<StudentVO>> page(StudentQueryDTO queryDTO) {
        return Result.success(studentService.page(queryDTO));
    }

    /**
     * 查询全部学生。
     *
     * @return 学生列表
     */
    @GetMapping("/all")
    public Result<List<StudentVO>> listAll() {
        return Result.success(studentService.listAll());
    }

    /**
     * 查询学生详情。
     *
     * @param id 主键
     * @return 学生详情
     */
    @GetMapping("/{id}")
    public Result<StudentVO> detail(@PathVariable Long id) {
        return Result.success(studentService.detail(id));
    }

    /**
     * 新增学生。
     *
     * @param saveDTO 保存对象
     * @return 学生详情
     */
    @PostMapping
    public Result<StudentVO> create(@Valid @RequestBody StudentSaveDTO saveDTO) {
        return Result.success(studentService.save(saveDTO));
    }

    /**
     * 修改学生。
     *
     * @param id 主键
     * @param saveDTO 保存对象
     * @return 学生详情
     */
    @PutMapping("/{id}")
    public Result<StudentVO> update(@PathVariable Long id, @Valid @RequestBody StudentSaveDTO saveDTO) {
        saveDTO.setId(id);
        return Result.success(studentService.save(saveDTO));
    }

    /**
     * 删除学生。
     *
     * @param id 主键
     * @return 空结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Result.success();
    }
}
