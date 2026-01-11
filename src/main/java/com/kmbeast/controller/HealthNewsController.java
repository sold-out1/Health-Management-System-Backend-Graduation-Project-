package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HealthNewsQueryDto;
import com.kmbeast.pojo.entity.HealthNews;
import com.kmbeast.pojo.vo.HealthNewsListVO;
import com.kmbeast.pojo.vo.HealthNewsVO;
import com.kmbeast.pojo.vo.OptionsVO;
import com.kmbeast.service.HealthNewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 健康资讯控制器
 */
@RestController
@RequestMapping("/health-news")
public class HealthNewsController {

    @Resource
    private HealthNewsService healthNewsService;

    /**
     * 新增
     *
     * @param healthNews 实体数据
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> saveEntity(@RequestBody HealthNews healthNews) {
        return healthNewsService.saveEntity(healthNews);
    }

    /**
     * 修改
     *
     * @param healthNews 实体数据
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> updateEntity(@RequestBody HealthNews healthNews) {
        return healthNewsService.updateEntity(healthNews);
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return Result<String> 响应结果
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public Result<String> delete(@PathVariable Integer id) {
        return healthNewsService.delete(id);
    }

    /**
     * 通过ID查询
     *
     * @param id 主键ID
     * @return Result<HealthNewsVO> 响应结果
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<HealthNewsVO> getById(@PathVariable Integer id) {
        return healthNewsService.selectById(id);
    }

    /**
     * 列表查询
     *
     * @param healthNewsQueryDto 查询参数
     * @return Result<List < HealthNewsListVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<HealthNewsListVO>> list(@RequestBody HealthNewsQueryDto healthNewsQueryDto) {
        return healthNewsService.list(healthNewsQueryDto);
    }

    /**
     * 查询用户的收藏列表
     *
     * @return Result<List < HealthNewsListVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/collectionList")
    @ResponseBody
    public Result<List<HealthNewsListVO>> collectionList(@RequestBody HealthNewsQueryDto healthNewsQueryDto) {
        return healthNewsService.collectionList(healthNewsQueryDto);
    }

    /**
     * 查询健康资讯类别
     *
     * @return Result<List < OptionsVO>> 响应结果
     */
    @GetMapping(value = "/fetchHealthNewsTypes")
    @ResponseBody
    public Result<List<OptionsVO>> fetchHealthNewsTypes() {
        return healthNewsService.fetchHealthNewsTypes();
    }

    /**
     * 查询推荐的健康资讯数据
     *
     * @return Result<List < HealthNewsListVO>> 响应结果
     */
    @GetMapping(value = "/recommend/{count}")
    @ResponseBody
    public Result<List<HealthNewsListVO>> recommend(@PathVariable Integer count) {
        return healthNewsService.recommend(count);
    }

}

