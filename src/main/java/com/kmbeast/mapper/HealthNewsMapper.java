package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.HealthNewsQueryDto;
import com.kmbeast.pojo.entity.HealthNews;
import com.kmbeast.pojo.vo.HealthNewsListVO;
import com.kmbeast.pojo.vo.HealthNewsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 健康资讯持久化接口
 */
@Mapper
public interface HealthNewsMapper extends BaseMapper<HealthNews> {

    List<HealthNewsListVO> list(HealthNewsQueryDto healthNewsQueryDto);

    Integer listPageCount(HealthNewsQueryDto healthNewsQueryDto);

    HealthNewsVO getVOById(@Param(value = "id") Integer id);

    List<Integer> listIds();

}
