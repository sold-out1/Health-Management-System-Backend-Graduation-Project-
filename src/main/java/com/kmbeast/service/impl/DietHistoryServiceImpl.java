package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.DietHistoryMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.DietHistoryQueryDto;
import com.kmbeast.pojo.em.RoleEnum;
import com.kmbeast.pojo.entity.DietHistory;
import com.kmbeast.pojo.vo.DietHistoryVO;
import com.kmbeast.service.DietHistoryService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 饮食记录业务逻辑接口实现类
 */
@Service
public class DietHistoryServiceImpl extends ServiceImpl<DietHistoryMapper, DietHistory> implements DietHistoryService {


    private void createAndValidList(List<DietHistory> dietHistories) {
        AssertUtils.notNull(dietHistories, "请完善饮食记录");
        LocalDateTime dateTime = LocalDateTime.now();
        Integer userId = LocalThreadHolder.getUserId();
        for (DietHistory dietHistory : dietHistories) {
            AssertUtils.notNull(dietHistory.getRecipeId(), "食谱ID不能为空");
            dietHistory.setUserId(userId); // 设置记录者用户ID
            dietHistory.setCreateTime(dateTime); // 设置记录的时间
        }
    }

    /**
     * 饮食记录新增
     *
     * @param dietHistories 饮食记录列表
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(List<DietHistory> dietHistories) {
        // 组装数据以及参数校验
        createAndValidList(dietHistories);
        saveBatch(dietHistories);
        return ApiResult.success("记录成功");
    }

    @Override
    public Result<String> delete(Integer id) {
        removeById(id);
        return ApiResult.success("饮食删除成功");
    }

    @Override
    public Result<List<DietHistoryVO>> listUser(DietHistoryQueryDto dietHistoryQueryDto) {
        dietHistoryQueryDto.setUserId(LocalThreadHolder.getUserId());
        return getDietHistoryVOS(dietHistoryQueryDto);
    }

    @Override
    public Result<List<DietHistoryVO>> listItem(DietHistoryQueryDto dietHistoryQueryDto) {
        // 此接口要求必须是管理员才能调用
        AssertUtils.isTrue(Objects.equals(
                LocalThreadHolder.getRoleId(),
                RoleEnum.ADMIN.getRole()
        ), "无操作权限");
        return getDietHistoryVOS(dietHistoryQueryDto);
    }

    private Result<List<DietHistoryVO>> getDietHistoryVOS(DietHistoryQueryDto dietHistoryQueryDto) {
        AssertUtils.notNull(dietHistoryQueryDto.getCurrent(), "当前页不能为空");
        AssertUtils.notNull(dietHistoryQueryDto.getSize(), "页面大小不能为空");
        List<DietHistoryVO> dietHistoryVOS = this.baseMapper.list(dietHistoryQueryDto);
        Integer count = this.baseMapper.listPageCount(dietHistoryQueryDto);
        return ApiResult.success(dietHistoryVOS, count);
    }
}