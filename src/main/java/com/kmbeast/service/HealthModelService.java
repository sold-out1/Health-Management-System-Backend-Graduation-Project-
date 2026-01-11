package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.BMIDto;
import com.kmbeast.pojo.dto.HealthModelQueryDto;
import com.kmbeast.pojo.entity.HealthModel;
import com.kmbeast.pojo.vo.*;

import java.util.List;

/**
 * 健康模型业务逻辑接口
 */
public interface HealthModelService extends IService<HealthModel> {

    Result<String> saveEntity(HealthModel healthModel);

    Result<String> updateEntity(HealthModel healthModel);

    Result<String> delete(Integer id);

    Result<List<HealthModel>> list(HealthModelQueryDto healthModelQueryDto);

    Result<List<HealthModelToolTipVO>> options();

    Result<List<HealthModelToolTipVO>> optionsUser();

    Result<BMIResultVO> computeBMI(BMIDto bmiDto);

    Result<HealthModelCountVO> modelCount();


}