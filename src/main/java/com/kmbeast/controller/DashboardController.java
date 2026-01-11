package com.kmbeast.controller;

import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.vo.ChartVO;
import com.kmbeast.pojo.vo.ContentTypeVO;
import com.kmbeast.pojo.vo.StaticCountVO;
import com.kmbeast.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 静态数据统计
     *
     * @return Result<StaticCountVO> 响应结果
     */
    @GetMapping(value = "/staticCount")
    @ResponseBody
    public Result<StaticCountVO> staticCount() {
        return dashboardService.staticCount();
    }

    /**
     * 收录模型统计（折线图）
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/modelInfo/{days}")
    @ResponseBody
    public Result<List<ChartVO>> modelInfo(@PathVariable Integer days) {
        return dashboardService.modelInfo(days);
    }

    /**
     * 统计健康资讯模块下的内容类型（饼状图）
     *
     * @return Result<List < ContentTypeVO>> 响应结果
     */
    @GetMapping(value = "/newsContentType")
    @ResponseBody
    public Result<List<ContentTypeVO>> newsContentType() {
        return dashboardService.newsContentType();
    }

    /**
     * 统计食谱模块下的内容类型（饼状图）
     *
     * @return Result<List < ContentTypeVO>> 响应结果
     */
    @GetMapping(value = "/recipeContentType")
    @ResponseBody
    public Result<List<ContentTypeVO>> recipeContentType() {
        return dashboardService.recipeContentType();
    }

}