package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.HealthRecordMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HealthRecordQueryDto;
import com.kmbeast.pojo.dto.QueryDto;
import com.kmbeast.pojo.em.RoleEnum;
import com.kmbeast.pojo.entity.HealthRecord;
import com.kmbeast.pojo.vo.HealthRecordLineChartVO;
import com.kmbeast.pojo.vo.HealthRecordVO;
import com.kmbeast.service.HealthRecordService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 健康记录业务逻辑接口实现类
 */
@Service
public class HealthRecordServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthRecordService {

    /**
     * 批量新增
     *
     * @param healthRecordList 实体数据
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> batchSave(List<HealthRecord> healthRecordList) {
        AssertUtils.notNull(healthRecordList, "请填写数据");
        LocalDateTime nowDateTime = LocalDateTime.now(); // 这一批数据，统一用同一个时间
        Integer userId = LocalThreadHolder.getUserId(); // 当前操作者用户ID
        for (HealthRecord healthRecord : healthRecordList) {
            // 这一批里面，如果存在模型没有选定，或者说没有记录具体的值，这一批不作数，退回前端重新填
            AssertUtils.notNull(healthRecord.getHealthModelId(), "存在异常模型，请重试");
            AssertUtils.notNull(healthRecord.getValue(), "记录值不能为空");
            healthRecord.setUserId(userId); // 设置当前操作者用户ID
            healthRecord.setCreateTime(nowDateTime);
        }
        saveBatch(healthRecordList);
        return ApiResult.success("健康数据记录成功");
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> delete(Integer id) {
        removeById(id);
        return ApiResult.success("删除成功");
    }

    /**
     * 列表查询
     *
     * @param healthRecordQueryDto 查询参数
     * @return Result<List < HealthRecordVO>> 响应结果
     */
    @Override
    public Result<List<HealthRecordVO>> list(HealthRecordQueryDto healthRecordQueryDto) {
        AssertUtils.notNull(healthRecordQueryDto.getCurrent(), "当前页参数不能为空");
        AssertUtils.notNull(healthRecordQueryDto.getSize(), "页面大小参数不能为空");
        // 要求调用者必须是管理员
        AssertUtils.isTrue(Objects.equals(LocalThreadHolder.getRoleId(), RoleEnum.ADMIN.getRole()), "无操作权限");
        List<HealthRecordVO> healthRecordVOS = this.baseMapper.list(healthRecordQueryDto);
        Integer count = this.baseMapper.listPageCount(healthRecordQueryDto);
        return ApiResult.success(healthRecordVOS, count);
    }

    /**
     * 用户查询自己的健康记录列表
     *
     * @param healthRecordQueryDto 查询参数
     * @return Result<List < HealthRecordVO>> 响应结果
     */
    @Override
    public Result<List<HealthRecordVO>> listUser(HealthRecordQueryDto healthRecordQueryDto) {
        healthRecordQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置上用户ID，此处是数据隔离
        List<HealthRecordVO> healthRecordVOS = this.baseMapper.list(healthRecordQueryDto);
        Integer count = this.baseMapper.listPageCount(healthRecordQueryDto);
        return ApiResult.success(healthRecordVOS, count);
    }

    /**
     * 健康数据可视化（折线图）
     *
     * @param healthRecordQueryDto 查询参数
     * @return Result<List < HealthRecordLineChartVO>> 响应结果
     */
    @Override
    public Result<List<HealthRecordLineChartVO>> listLineChart(HealthRecordQueryDto healthRecordQueryDto) {
        // 参数校验
        AssertUtils.notNull(healthRecordQueryDto,"查询参数对象不能为空");
        AssertUtils.notNull(healthRecordQueryDto.getDays(),"查询天数不能为空");
        AssertUtils.notNull(healthRecordQueryDto.getHealthModelId(),"模型ID不能为空");
        healthRecordQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置上用户ID
        // 处理查询时间
        QueryDto queryDto = DateUtil.startAndEndTime(healthRecordQueryDto.getDays());
        LocalDateTime startTime = queryDto.getStartTime();
        LocalDateTime endTime = queryDto.getEndTime();
        healthRecordQueryDto.setStartTime(startTime);
        healthRecordQueryDto.setEndTime(endTime);
        List<HealthRecordLineChartVO> healthRecordLineChartVOS = this.baseMapper.listLineChart(healthRecordQueryDto);
        return ApiResult.success(healthRecordLineChartVOS);
    }
}