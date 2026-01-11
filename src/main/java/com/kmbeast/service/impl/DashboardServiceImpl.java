package com.kmbeast.service.impl;

import com.kmbeast.mapper.DashboardMapper;
import com.kmbeast.mapper.HealthModelMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HealthModelQueryDto;
import com.kmbeast.pojo.dto.QueryDto;
import com.kmbeast.pojo.em.TypeEnum;
import com.kmbeast.pojo.entity.HealthModel;
import com.kmbeast.pojo.vo.ChartVO;
import com.kmbeast.pojo.vo.ContentTypeVO;
import com.kmbeast.pojo.vo.OptionsVO;
import com.kmbeast.pojo.vo.StaticCountVO;
import com.kmbeast.service.DashboardService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仪表盘业务逻辑接口实现类
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private DashboardMapper dashboardMapper;
    @Resource
    private HealthModelMapper healthModelMapper;


    /**
     * 静态数据统计
     *
     * @return Result<StaticCountVO> 响应结果
     */
    @Override
    public Result<StaticCountVO> staticCount() {
        return ApiResult.success(dashboardMapper.staticCount());
    }

    /**
     * 收录模型统计（折线图）
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> modelInfo(Integer days) {
        AssertUtils.notNull(days, "查询日期不能为空");
        QueryDto queryDto = DateUtil.startAndEndTime(days);
        HealthModelQueryDto healthModelQueryDto = new HealthModelQueryDto();
        healthModelQueryDto.setStartTime(queryDto.getStartTime());
        healthModelQueryDto.setEndTime(queryDto.getEndTime());
        List<HealthModel> healthModelList = healthModelMapper.list(healthModelQueryDto);
        List<LocalDateTime> dateTimeList = healthModelList.stream()
                .map(HealthModel::getCreateTime)
                .collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesRange(days, dateTimeList);
        return ApiResult.success(chartVOS);
    }

    /**
     * 统计健康资讯模块下的内容类型（饼状图）
     *
     * @return Result<List < ContentTypeVO>> 响应结果
     */
    @Override
    public Result<List<ContentTypeVO>> newsContentType() {
        List<Integer> typeIds = getTypeIds("NEWS");
        List<ContentTypeVO> contentTypeVOS = dashboardMapper.newsContentType(typeIds);
        setContentTypeName("NEWS", contentTypeVOS);
        return ApiResult.success(contentTypeVOS);
    }

    private void setContentTypeName(String module, List<ContentTypeVO> contentTypeVOS) {
        for (ContentTypeVO contentTypeVO : contentTypeVOS) {
            String typeName = TypeEnum.getNameByModule(module, contentTypeVO.getId());
            contentTypeVO.setTypeName(typeName);
        }
    }

    private List<Integer> getTypeIds(String contentType) {
        List<OptionsVO> optionsVOS = TypeEnum.getOptionsByModule(contentType);
        AssertUtils.isTrue(!optionsVOS.isEmpty(), "暂无类型信息！");
        return optionsVOS.stream().map(OptionsVO::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 统计食谱模块下的内容类型（饼状图）
     *
     * @return Result<List < ContentTypeVO>> 响应结果
     */
    @Override
    public Result<List<ContentTypeVO>> recipeContentType() {
        List<Integer> typeIds = getTypeIds("RECIPE");
        List<ContentTypeVO> contentTypeVOS = dashboardMapper.recipeContentType(typeIds);
        setContentTypeName("RECIPE", contentTypeVOS);
        return ApiResult.success(contentTypeVOS);
    }
}
