package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.dto.DeskItemSaveDTO;
import com.example.lab203.entity.DeskItemEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.DeskItemManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.DeskItemService;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.DeskItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 桌面物品服务实现。
 */
@Service
public class DeskItemServiceImpl implements DeskItemService {

    private final DeskItemManager deskItemManager;
    private final StudentManager studentManager;

    public DeskItemServiceImpl(DeskItemManager deskItemManager, StudentManager studentManager) {
        this.deskItemManager = deskItemManager;
        this.studentManager = studentManager;
    }

    /**
     * 保存桌面物品。
     *
     * @param saveDTO 保存对象
     * @return 物品详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeskItemVO save(DeskItemSaveDTO saveDTO) {
        AuthAssert.requireAdmin();
        StudentEntity studentEntity = requireStudent(saveDTO.getStudentId());
        DeskItemEntity entity = saveDTO.getId() == null ? new DeskItemEntity() : requireDeskItem(saveDTO.getId());
        entity.setStudentId(saveDTO.getStudentId());
        entity.setItemName(saveDTO.getItemName());
        entity.setQuantity(saveDTO.getQuantity());
        entity.setItemStatus(saveDTO.getItemStatus());
        entity.setRemark(saveDTO.getRemark());
        if (entity.getId() == null) {
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setUpdateTime(LocalDateTime.now());
        deskItemManager.save(entity);
        return toVO(entity, studentEntity);
    }

    /**
     * 删除桌面物品。
     *
     * @param id 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AuthAssert.requireAdmin();
        requireDeskItem(id);
        deskItemManager.delete(id);
    }

    /**
     * 查询全部物品。
     *
     * @return 物品列表
     */
    @Override
    public List<DeskItemVO> listAll() {
        AuthAssert.requireAdmin();
        Map<Long, StudentEntity> studentMap = studentManager.listAll().stream().collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        return deskItemManager.listAll().stream()
                .sorted(Comparator.comparing(DeskItemEntity::getId).reversed())
                .map(item -> toVO(item, studentMap.get(item.getStudentId())))
                .toList();
    }

    /**
     * 查询我的桌面物品。
     *
     * @return 物品列表
     */
    @Override
    public List<DeskItemVO> myItems() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        return deskItemManager.listAll().stream()
                .filter(item -> currentUser.getStudentId().equals(item.getStudentId()))
                .map(item -> toVO(item, studentEntity))
                .toList();
    }

    /**
     * 校验学生是否存在。
     *
     * @param studentId 学生主键
     * @return 学生实体
     */
    private StudentEntity requireStudent(Long studentId) {
        StudentEntity studentEntity = studentManager.getById(studentId);
        if (studentEntity == null) {
            throw new BizException(404, "学生不存在");
        }
        return studentEntity;
    }

    /**
     * 校验物品是否存在。
     *
     * @param id 主键
     * @return 物品实体
     */
    private DeskItemEntity requireDeskItem(Long id) {
        DeskItemEntity entity = deskItemManager.getById(id);
        if (entity == null) {
            throw new BizException(404, "桌面物品不存在");
        }
        return entity;
    }

    /**
     * 转换桌面物品视图对象。
     *
     * @param entity 物品实体
     * @param studentEntity 学生实体
     * @return 视图对象
     */
    private DeskItemVO toVO(DeskItemEntity entity, StudentEntity studentEntity) {
        DeskItemVO vo = new DeskItemVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setStudentName(studentEntity == null ? "未知学生" : studentEntity.getName());
        vo.setItemName(entity.getItemName());
        vo.setQuantity(entity.getQuantity());
        vo.setItemStatus(entity.getItemStatus());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
