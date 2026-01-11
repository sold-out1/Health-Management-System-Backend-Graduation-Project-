package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.HealthModelQueryDto;
import com.kmbeast.pojo.dto.HealthNewsQueryDto;
import com.kmbeast.pojo.entity.HealthModel;
import com.kmbeast.pojo.entity.HealthNews;
import com.kmbeast.pojo.vo.HealthModelCountVO;
import com.kmbeast.pojo.vo.HealthModelToolTipVO;
import com.kmbeast.pojo.vo.HealthNewsListVO;
import com.kmbeast.pojo.vo.OptionsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 健康模型持久化接口
 */
@Mapper
public interface HealthModelMapper extends BaseMapper<HealthModel> {

    List<HealthModel> list(HealthModelQueryDto healthModelQueryDto);

    Integer listPageCount(HealthModelQueryDto healthModelQueryDto);

    List<HealthModelToolTipVO> options(@Param(value = "isGlobal")Integer isGlobal,
                                       @Param(value = "userId")Integer userId);

    HealthModelCountVO modelCount(@Param(value = "isGlobal")Integer isGlobal,
                                        @Param(value = "userId")Integer userId);

}
