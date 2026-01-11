package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.FlowIndexMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.service.FlowIndexService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 流量指标业务逻辑接口实现类
 */
@Service
public class FlowIndexServiceImpl extends ServiceImpl<FlowIndexMapper, FlowIndex> implements FlowIndexService {

    private void validParams(FlowIndex flowIndex) {
        AssertUtils.notNull(flowIndex, "实体不能为空");
        AssertUtils.notNull(flowIndex.getType(), "类型不能为空");
        AssertUtils.hasText(flowIndex.getContentModule(), "内容模块不能为空");
        AssertUtils.notNull(flowIndex.getContentId(), "内容ID不能为空");
    }

    @Override
    public Result<String> saveEntity(FlowIndex flowIndex) {
        validParams(flowIndex);
        flowIndex.setUserId(LocalThreadHolder.getUserId());
        flowIndex.setCreateTime(LocalDateTime.now());
        save(flowIndex);
        return ApiResult.success();
    }

    @Override
    public Result<String> delete(Integer id) {
        FlowIndex flowIndex = getById(id);
        AssertUtils.notNull(flowIndex, "数据找不到");
        AssertUtils.isTrue(Objects.equals(
                        flowIndex.getUserId(),
                        LocalThreadHolder.getUserId()),
                "删除异常");
        removeById(id);
        return ApiResult.success();
    }

    @Override
    public Result<List<FlowIndex>> listItem(FlowIndexQueryDto flowIndexQueryDto) {
        List<FlowIndex> flowIndexList = this.baseMapper.list(flowIndexQueryDto);
        Integer count = this.baseMapper.listPageCount(flowIndexQueryDto);
        return ApiResult.success(flowIndexList, count);
    }

    /**
     * 点赞与收藏操作
     *
     * @param flowIndex 实体数据
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> operation(FlowIndex flowIndex) {
        // 有没有进行过这种操作 -> 点赞？收藏
        validParams(flowIndex);
        FlowIndexQueryDto flowIndexQueryDto = new FlowIndexQueryDto();
        flowIndexQueryDto.setUserId(LocalThreadHolder.getUserId());
        flowIndexQueryDto.setContentModule(flowIndex.getContentModule());
        flowIndexQueryDto.setContentId(flowIndex.getContentId());
        flowIndexQueryDto.setType(flowIndex.getType());
        List<FlowIndex> flowIndexList = this.baseMapper.list(flowIndexQueryDto);
        System.out.println("查询的数据：" + flowIndexList);
        // 没有进行过这种操作，对应新增
        if (flowIndexList.isEmpty()) {
            flowIndex.setUserId(LocalThreadHolder.getUserId());
            flowIndex.setCreateTime(LocalDateTime.now());
            save(flowIndex);
            return ApiResult.success();
        }
        // 已经存在类似操作记录，删除
        FlowIndex flow = flowIndexList.get(0);
        removeById(flow.getId());
        return ApiResult.success();
    }
}