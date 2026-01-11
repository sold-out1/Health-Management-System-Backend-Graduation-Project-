package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.FlowIndexMapper;
import com.kmbeast.mapper.HealthNewsMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.dto.HealthNewsQueryDto;
import com.kmbeast.pojo.em.FlowIndexEnum;
import com.kmbeast.pojo.em.RoleEnum;
import com.kmbeast.pojo.em.TypeEnum;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.pojo.entity.HealthNews;
import com.kmbeast.pojo.vo.FlowIndexScoreVO;
import com.kmbeast.pojo.vo.HealthNewsListVO;
import com.kmbeast.pojo.vo.HealthNewsVO;
import com.kmbeast.pojo.vo.OptionsVO;
import com.kmbeast.service.HealthNewsService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.UserBasedCFUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 健康资讯业务逻辑接口实现类
 */
@Service
public class HealthNewsServiceImpl extends ServiceImpl<HealthNewsMapper, HealthNews> implements HealthNewsService {

    @Resource
    private FlowIndexMapper flowIndexMapper;

    /**
     * 新增
     *
     * @param healthNews 实体数据
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> saveEntity(HealthNews healthNews) {
        // 参数校验
        validParam(healthNews);
        healthNews.setCreateTime(LocalDateTime.now()); // 设置上当前新增的时间
        // 数据新增
        save(healthNews);
        return ApiResult.success("健康资讯新增成功");
    }

    /**
     * 参数校验
     *
     * @param healthNews 实体数据
     */
    private void validParam(HealthNews healthNews) {
        AssertUtils.notNull(healthNews, "实体不能为空");
        AssertUtils.hasText(healthNews.getTitle(), "标题不能为空");
        AssertUtils.hasText(healthNews.getCover(), "封面必须上传");
        AssertUtils.hasText(healthNews.getSummary(), "摘要不能为空");
        AssertUtils.notNull(healthNews.getTypeId(), "请选定类型");
        AssertUtils.isTrue(healthNews.getTitle().length() < 50, "标题请控制在50字以内");
        AssertUtils.isTrue(healthNews.getSummary().length() < 200, "摘要请控制在200字以内");
    }

    /**
     * 修改
     *
     * @param healthNews 实体数据
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> updateEntity(HealthNews healthNews) {
        // 参数校验
        validParam(healthNews);
        // 数据修改
        updateById(healthNews);
        return ApiResult.success("健康资讯修改成功");
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return Result<String>
     */
    @Override
    public Result<String> delete(Integer id) {
        removeById(id);
        return ApiResult.success("删除成功");
    }

    /**
     * 通过ID查询
     *
     * @param id 主键ID
     * @return Result<HealthNewsVO> 响应结果
     */
    @Override
    public Result<HealthNewsVO> selectById(Integer id) {
        AssertUtils.notNull(id, "ID不能为空");
        HealthNewsVO healthNewsVO = this.baseMapper.getVOById(id);
        healthNewsVO.setTypeName(TypeEnum.getNameByModule("NEWS", healthNewsVO.getTypeId()));
        return ApiResult.success(healthNewsVO);
    }

    /**
     * 列表查询
     *
     * @param healthNewsQueryDto 查询参数
     * @return Result<List < HealthNewsListVO>> 响应结果
     */
    @Override
    public Result<List<HealthNewsListVO>> list(HealthNewsQueryDto healthNewsQueryDto) {
        AssertUtils.notNull(healthNewsQueryDto.getCurrent(), "当前页参数不能为空");
        AssertUtils.notNull(healthNewsQueryDto.getSize(), "页面大小参数不能为空");
        List<HealthNewsListVO> healthNewsListVOS = this.baseMapper.list(healthNewsQueryDto);// 查列表
        Integer count = this.baseMapper.listPageCount(healthNewsQueryDto);// 查总页数
        dealClickRate(healthNewsListVOS);
        dealShowOperation(healthNewsListVOS);
        return ApiResult.success(healthNewsListVOS, count);
    }

    /**
     * 处理展现量
     *
     * @param healthNewsListVOS 健康资讯列表
     */
    private void dealShowOperation(List<HealthNewsListVO> healthNewsListVOS) {
        // 如果是管理员查询的数据，不需要记录展现，只有用户查询该接口，才需要设置
        if (!healthNewsListVOS.isEmpty() && !Objects.equals(LocalThreadHolder.getRoleId(), RoleEnum.ADMIN.getRole())) {
            List<Integer> healthNewsIds = healthNewsListVOS.stream().map(HealthNewsListVO::getId).collect(Collectors.toList());
            List<FlowIndex> flowIndexList = new ArrayList<>();
            for (Integer healthNewsId : healthNewsIds) {
                // 构造展现的流量指标
                FlowIndex flowIndex = new FlowIndex();
                flowIndex.setType(FlowIndexEnum.FLOW_INDEX_1.getId()); // 标识此种流量类型为展现
                flowIndex.setContentModule("HEALTH_NEWS"); // 标识为健康资讯模块
                flowIndex.setContentId(healthNewsId); // 标识ID
                flowIndex.setCreateTime(LocalDateTime.now()); // 操作时间
                flowIndex.setUserId(LocalThreadHolder.getUserId()); // 标识查询的数据
                flowIndexList.add(flowIndex);
            }
            flowIndexMapper.batchSave(flowIndexList);
        }
    }

    /**
     * 处理点击率
     * 点击率 = 阅读量 / 展现量
     *
     * @param healthNewsListVOS 健康资讯列表数据
     */
    private void dealClickRate(List<HealthNewsListVO> healthNewsListVOS) {
        for (HealthNewsListVO healthNewsListVO : healthNewsListVOS) {
            if (healthNewsListVO.getShowCount() != 0) {
                healthNewsListVO.setClickRate((double) (healthNewsListVO.getViewCount() / healthNewsListVO.getShowCount()));
            }
        }
    }

    /**
     * 查询健康资讯类别
     *
     * @return Result<List < OptionsVO>> 响应结果
     */
    @Override
    public Result<List<OptionsVO>> fetchHealthNewsTypes() {
        return ApiResult.success(TypeEnum.getOptionsByModule("NEWS"));
    }

    /**
     * 查询用户的收藏列表
     *
     * @return Result<List < HealthNewsListVO>> 响应结果
     */
    @Override
    public Result<List<HealthNewsListVO>> collectionList(HealthNewsQueryDto healthNewsQueryDto) {
        AssertUtils.notNull(healthNewsQueryDto, "查询条件不能为空");
        AssertUtils.notNull(healthNewsQueryDto.getSize(), "页面大小参数不能为空");
        AssertUtils.notNull(healthNewsQueryDto.getCurrent(), "当前页参数不能为空");
        // 1. 用户收藏了什么东西？
        FlowIndexQueryDto flowIndexQueryDto = new FlowIndexQueryDto();
        flowIndexQueryDto.setUserId(LocalThreadHolder.getUserId()); // 做好用户的数据隔离
        flowIndexQueryDto.setType(FlowIndexEnum.FLOW_INDEX_4.getId()); // 声明是收藏类型的互动数据
        flowIndexQueryDto.setContentModule("HEALTH_NEWS"); // 声明是健康资讯模块
        List<FlowIndex> flowIndexList = flowIndexMapper.list(flowIndexQueryDto);
        if (flowIndexList.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        // 获取用户收藏的所有健康资讯的ID列表
        List<Integer> healthNewsId = flowIndexList.stream()
                .map(FlowIndex::getContentId)
                .collect(Collectors.toList());
        // 2. 凭借着用户收藏的这些资讯ID列表，去查询健康资讯的数据
        healthNewsQueryDto.setIds(healthNewsId);
        List<HealthNewsListVO> healthNewsListVOS = this.baseMapper.list(healthNewsQueryDto);
        Integer count = this.baseMapper.listPageCount(healthNewsQueryDto);
        return ApiResult.success(healthNewsListVOS, count);
    }

    /**
     * 查询推荐的健康资讯数据
     *
     * @return Result<List < HealthNewsListVO>> 响应结果
     */
    @Override
    public Result<List<HealthNewsListVO>> recommend(Integer count) {
        // 查询系统下面的所有的健康资讯的ID集合
        List<Integer> healthNewsIds = this.baseMapper.listIds();
        if (healthNewsIds.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        // 查询用户对应内容的评分矩阵
        List<FlowIndexScoreVO> flowIndexScoreVOS = flowIndexMapper.listScores("HEALTH_NEWS");
        // 算法工具类所期望的评分矩阵
        List<UserBasedCFUtil.Score> scoreList = flowIndexScoreVOS.stream().map(flowIndexScoreVO -> new UserBasedCFUtil.Score(
                flowIndexScoreVO.getUserId(),
                flowIndexScoreVO.getContentId(),
                flowIndexScoreVO.getScore()
        )).collect(Collectors.toList());
        Map<Integer, Map<Integer, Double>> integerMapMap = UserBasedCFUtil.buildUserItemMatrix(healthNewsIds, scoreList);
        UserBasedCFUtil userBasedCFUtil = new UserBasedCFUtil(integerMapMap);
        // 此处就是向用户推荐的健康资讯的ID列表
        List<Integer> recommendIds = userBasedCFUtil.recommendItems(LocalThreadHolder.getUserId(), count);
        // 冷启动 - 推荐热度最多的数据即可
        if (recommendIds.isEmpty()) {
            HealthNewsQueryDto healthNewsQueryDto = new HealthNewsQueryDto();
            healthNewsQueryDto.setCurrent(0);
            healthNewsQueryDto.setSize(count);
            List<HealthNewsListVO> healthNewsListVOS = this.baseMapper.list(healthNewsQueryDto);
            return ApiResult.success(healthNewsListVOS);
        }
        HealthNewsQueryDto healthNewsQueryDto = new HealthNewsQueryDto();
        healthNewsQueryDto.setIds(recommendIds);
        List<HealthNewsListVO> healthNewsListVOS = this.baseMapper.list(healthNewsQueryDto);
        return ApiResult.success(healthNewsListVOS);
    }
}
