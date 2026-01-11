package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HealthRecordQueryDto;
import com.kmbeast.pojo.entity.HealthRecord;
import com.kmbeast.pojo.vo.HealthRecordLineChartVO;
import com.kmbeast.pojo.vo.HealthRecordVO;
import com.kmbeast.pojo.vo.OptionsVO;

import java.util.List;

/**
 * 健康记录业务逻辑接口
 */
public interface HealthRecordService extends IService<HealthRecord> {

    Result<String> batchSave(List<HealthRecord> healthRecordList);

    Result<String> delete(Integer id);

    Result<List<HealthRecordVO>> list(HealthRecordQueryDto healthRecordQueryDto);

    Result<List<HealthRecordVO>> listUser(HealthRecordQueryDto healthRecordQueryDto);

    Result<List<HealthRecordLineChartVO>> listLineChart(HealthRecordQueryDto healthRecordQueryDto);

}