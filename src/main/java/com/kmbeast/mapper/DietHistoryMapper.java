package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.DietHistoryQueryDto;
import com.kmbeast.pojo.entity.DietHistory;
import com.kmbeast.pojo.vo.DietHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 饮食记录持久化接口
 */
@Mapper
public interface DietHistoryMapper extends BaseMapper<DietHistory> {

    List<DietHistoryVO> list(DietHistoryQueryDto dietHistoryQueryDto);

    Integer listPageCount(DietHistoryQueryDto dietHistoryQueryDto);

}
