package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.entity.FlowIndex;

import java.util.List;

/**
 * 流量指标业务逻辑接口
 */
public interface FlowIndexService extends IService<FlowIndex> {

    Result<String> saveEntity(FlowIndex flowIndex);

    Result<String> delete(Integer id);

    Result<List<FlowIndex>> listItem(FlowIndexQueryDto flowIndexQueryDto);

    Result<String> operation(FlowIndex flowIndex);

}