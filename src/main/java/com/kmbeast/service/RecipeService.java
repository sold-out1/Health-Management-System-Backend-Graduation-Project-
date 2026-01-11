package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.RecipeQueryDto;
import com.kmbeast.pojo.entity.Recipe;
import com.kmbeast.pojo.vo.OptionsVO;
import com.kmbeast.pojo.vo.RecipeListItemVO;
import com.kmbeast.pojo.vo.RecipeVO;

import java.util.List;

/**
 * 食谱业务逻辑接口
 */
public interface RecipeService extends IService<Recipe> {

    Result<String> saveEntity(Recipe recipe);

    Result<String> updateEntity(Recipe recipe);

    Result<String> delete(Integer id);

    Result<List<RecipeListItemVO>> listUser(RecipeQueryDto recipeQueryDto);

    Result<List<RecipeListItemVO>> listItem(RecipeQueryDto recipeQueryDto);

    Result<RecipeVO> selectById(Integer id);

    Result<List<OptionsVO>> fetchRecipeTypeList();

    Result<String> audit(Integer id);

    Result<List<RecipeListItemVO>> listRecipe(RecipeQueryDto recipeQueryDto);

    Result<List<RecipeListItemVO>> collectionList(RecipeQueryDto recipeQueryDto);

    Result<List<RecipeListItemVO>> recommend(Integer count);
    List<String> listPublicNames();
}