package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.RecipeQueryDto;
import com.kmbeast.pojo.entity.Recipe;
import com.kmbeast.pojo.vo.RecipeListItemVO;
import com.kmbeast.pojo.vo.RecipeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 食谱持久化接口
 */
@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {

    List<RecipeListItemVO> list(RecipeQueryDto recipeQueryDto);

    Integer listPageCount(RecipeQueryDto recipeQueryDto);

    RecipeVO getRecipeVOById(@Param(value = "id") Integer id);

    List<Integer> listIds();
    // 路径：src/main/java/com/kmbeast/mapper/RecipeMapper.java

    // 增加这两个简单的方法，参数只有 offset(跳过几条) 和 size(取几条)
    List<RecipeListItemVO> selectGlobalPublic(@Param("offset") Integer offset, @Param("size") Integer size);

    Integer selectGlobalPublicCount();
    List<RecipeListItemVO> listGlobalSmart(@Param("dto") RecipeQueryDto dto, @Param("myId") Integer myId);

    Integer listGlobalSmartCount(@Param("dto") RecipeQueryDto dto, @Param("myId") Integer myId);
    // 获取前20个公开且审核通过的食谱名称
    List<String> selectPublicNames();

}
