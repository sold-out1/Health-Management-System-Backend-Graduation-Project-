package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.DietHistoryQueryDto;
import com.kmbeast.pojo.entity.DietHistory;
import com.kmbeast.pojo.vo.DietHistoryVO;
import com.kmbeast.service.DietHistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 饮食记录控制器
 */
@RestController
@RequestMapping("/diet-history")
public class DietHistoryController {

    @Resource
    private DietHistoryService dietHistoryService;

    /**
     * 新增
     *
     * @param dietHistories 实体数据集合
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody List<DietHistory> dietHistories) {
        return dietHistoryService.saveEntity(dietHistories);
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
        return dietHistoryService.delete(id);
    }

    /**
     * 用户查询自己的饮食记录列表
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/listUser")
    @ResponseBody
    public Result<List<DietHistoryVO>> listUser(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.listUser(dietHistoryQueryDto);
    }

    /**
     * 列表查询
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<DietHistoryVO>> list(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.listItem(dietHistoryQueryDto);
    }

}