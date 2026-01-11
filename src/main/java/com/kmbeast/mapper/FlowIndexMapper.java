package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.pojo.vo.FlowIndexScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流量指标持久化接口
 */
@Mapper
public interface FlowIndexMapper extends BaseMapper<FlowIndex> {

    void batchSave(@Param(value = "flowIndexList") List<FlowIndex> flowIndexList);

    List<FlowIndex> list(FlowIndexQueryDto flowIndexQueryDto);

    Integer listPageCount(FlowIndexQueryDto flowIndexQueryDto);

    List<FlowIndexScoreVO> listScores(@Param(value = "contentModule") String contentModule);

}
