package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.HealthRecordQueryDto;
import com.kmbeast.pojo.entity.HealthRecord;
import com.kmbeast.pojo.vo.HealthRecordLineChartVO;
import com.kmbeast.pojo.vo.HealthRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 健康记录持久化接口
 */
@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {

    List<HealthRecordVO> list(HealthRecordQueryDto healthRecordQueryDto);

    Integer listPageCount(HealthRecordQueryDto healthRecordQueryDto);

    List<HealthRecordLineChartVO> listLineChart(HealthRecordQueryDto healthRecordQueryDto);

}
