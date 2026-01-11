package com.kmbeast.service;

import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.vo.ChartVO;
import com.kmbeast.pojo.vo.ContentTypeVO;
import com.kmbeast.pojo.vo.StaticCountVO;

import java.util.List;

/**
 * 仪表盘业务逻辑接口
 */
public interface DashboardService {

    Result<StaticCountVO> staticCount();

    Result<List<ChartVO>> modelInfo(Integer days);

    Result<List<ContentTypeVO>> newsContentType();

    Result<List<ContentTypeVO>> recipeContentType();


}