package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.RecipeQueryDto;
import com.kmbeast.pojo.entity.Recipe;
import com.kmbeast.pojo.vo.OptionsVO;
import com.kmbeast.pojo.vo.RecipeListItemVO;
import com.kmbeast.pojo.vo.RecipeVO;
import com.kmbeast.service.RecipeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 食谱控制器
 */
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Resource
    private RecipeService recipeService;

    /**
     * 新增
     *
     * @param recipe 实体数据
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Recipe recipe) {
        return recipeService.saveEntity(recipe);
    }

    /**
     * 修改
     *
     * @param recipe 实体数据
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Recipe recipe) {
        return recipeService.updateEntity(recipe);
    }

    /**
     * 审核食谱
     *
     * @param id 食谱ID
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/audit/{id}")
    @ResponseBody
    public Result<String> audit(@PathVariable Integer id) {
        return recipeService.audit(id);
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
        return recipeService.delete(id);
    }

    /**
     * 通过ID查询食谱详情信息
     *
     * @param id 主键ID
     * @return Result<String> 响应结果
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<RecipeVO> selectById(@PathVariable Integer id) {
        return recipeService.selectById(id);
    }

    /**
     * 用户查询自己的食谱列表
     *
     * @param recipeQueryDto 查询参数
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
//    @Pager
    @PostMapping(value = "/listUser")
    @ResponseBody
    public Result<List<RecipeListItemVO>> listUser(@RequestBody RecipeQueryDto recipeQueryDto) {
        return recipeService.listUser(recipeQueryDto);
    }

    /**
     * 食谱类别查询
     *
     * @return Result<List < OptionsVO>> 响应结果
     */
    @GetMapping(value = "/fetchRecipeTypeList")
    @ResponseBody
    public Result<List<OptionsVO>> fetchRecipeTypeList() {
        return recipeService.fetchRecipeTypeList();
    }

    /**
     * 用户食谱列表，只能查询食谱状态是公开状态，并且是已经经过审核的食谱
     *
     * @param recipeQueryDto 查询参数
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
//    @Pager
    @PostMapping(value = "/listRecipe")
    @ResponseBody
    public Result<List<RecipeListItemVO>> listRecipe(@RequestBody RecipeQueryDto recipeQueryDto) {
        return recipeService.listRecipe(recipeQueryDto);
    }

    /**
     * 列表查询
     *
     * @param recipeQueryDto 查询参数
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
//    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<RecipeListItemVO>> list(@RequestBody RecipeQueryDto recipeQueryDto) {
        return recipeService.listItem(recipeQueryDto);
    }

    /**
     * 查询用户的收藏列表
     *
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/collectionList")
    @ResponseBody
    public Result<List<RecipeListItemVO>> collectionList(@RequestBody RecipeQueryDto recipeQueryDto) {
        return recipeService.collectionList(recipeQueryDto);
    }

    /**
     * 查询推荐的食谱
     *
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
    @GetMapping(value = "/recommend/{count}")
    @ResponseBody
    public Result<List<RecipeListItemVO>> recommend(@PathVariable Integer count) {
        return recipeService.recommend(count);
    }

}