package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HealthNewsQueryDto;
import com.kmbeast.pojo.entity.HealthNews;
import com.kmbeast.pojo.vo.HealthNewsListVO;
import com.kmbeast.pojo.vo.HealthNewsVO;
import com.kmbeast.pojo.vo.OptionsVO;

import java.util.List;

/**
 * 健康资讯业务逻辑接口
 */
public interface HealthNewsService extends IService<HealthNews> {

    Result<String> saveEntity(HealthNews healthNews);

    Result<String> updateEntity(HealthNews healthNews);

    Result<String> delete(Integer id);

    Result<HealthNewsVO> selectById(Integer id);

    Result<List<HealthNewsListVO>> list(HealthNewsQueryDto healthNewsQueryDto);

    Result<List<OptionsVO>> fetchHealthNewsTypes();

    Result<List<HealthNewsListVO>> collectionList(HealthNewsQueryDto healthNewsQueryDto);

    Result<List<HealthNewsListVO>> recommend(Integer count);

}
