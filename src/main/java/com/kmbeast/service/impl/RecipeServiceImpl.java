package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.FlowIndexMapper;
import com.kmbeast.mapper.RecipeMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.dto.RecipeQueryDto;
import com.kmbeast.pojo.em.FlowIndexEnum;
import com.kmbeast.pojo.em.RoleEnum;
import com.kmbeast.pojo.em.TypeEnum;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.pojo.entity.Recipe;
import com.kmbeast.pojo.vo.FlowIndexScoreVO;
import com.kmbeast.pojo.vo.OptionsVO;
import com.kmbeast.pojo.vo.RecipeListItemVO;
import com.kmbeast.pojo.vo.RecipeVO;
import com.kmbeast.service.RecipeService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.UserBasedCFUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 食谱业务逻辑接口实现类
 */
@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

    @Resource
    private FlowIndexMapper flowIndexMapper;

    private final static Boolean NO_AUDIT = false; // 未审核状态
    private final static Boolean AUDIT = true; // 审核状态
    private final static Boolean PUBLIC = true; // 公开状态

    /**
     * 参数校验
     *
     * @param recipe 食谱实体对象
     */
    private void validParams(Recipe recipe) {
        AssertUtils.notNull(recipe, "参数不能为空");
        AssertUtils.hasText(recipe.getName(), "食谱名不能为空");
        AssertUtils.hasText(recipe.getCover(), "封面不能为空");
        AssertUtils.notNull(recipe.getTypeId(), "必须选中分类");
        AssertUtils.notNull(recipe.getIsPublic(), "请设置食谱权限（日人食谱或公开食谱）");
        AssertUtils.isTrue(recipe.getName().length() < 50, "食谱名称字数请控制在50以内");
    }

    @Override
    public Result<String> saveEntity(Recipe recipe) {
        // 参数校验
        validParams(recipe);
        recipe.setUserId(LocalThreadHolder.getUserId()); // 设置用户ID
        recipe.setCreateTime(LocalDateTime.now()); // 设置新增时间
        recipe.setIsAudit(NO_AUDIT); // 食谱新增时，是未审核状态
        save(recipe);
        return ApiResult.success("食谱新增成功");
    }

    @Override
    public Result<String> updateEntity(Recipe recipe) {
        // 参数校验
        validParams(recipe);
        recipe.setIsAudit(null);
        updateById(recipe);
        return ApiResult.success("食谱修改成功");
    }

    @Override
    public Result<String> delete(Integer id) {
        removeById(id);
        return ApiResult.success("食谱删除成功");
    }

    @Override
    public Result<List<RecipeListItemVO>> listUser(RecipeQueryDto recipeQueryDto) {
        recipeQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置用户ID，划分权限
        return listItem(recipeQueryDto);
    }

    @Override
    public Result<List<RecipeListItemVO>> listItem(RecipeQueryDto recipeQueryDto) {
        List<RecipeListItemVO> recipeListItemVOS = this.baseMapper.list(recipeQueryDto);
        Integer count = this.baseMapper.listPageCount(recipeQueryDto);
        return ApiResult.success(recipeListItemVOS, count);
    }

    @Override
    public Result<RecipeVO> selectById(Integer id) {
        AssertUtils.notNull(id, "ID不能为空");
        RecipeVO recipeVO = this.baseMapper.getRecipeVOById(id);
        // 设置食谱所在的分类
        String typeName = TypeEnum.getNameByModule("RECIPE", recipeVO.getTypeId());
        recipeVO.setTypeName(typeName);
        return ApiResult.success(recipeVO);
    }

    /**
     * 食谱类别查询
     *
     * @return Result<List < OptionsVO>> 响应结果
     */
    @Override
    public Result<List<OptionsVO>> fetchRecipeTypeList() {
        return ApiResult.success(TypeEnum.getOptionsByModule("RECIPE"));
    }

    /**
     * 审核食谱
     *
     * @param id 食谱ID
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> audit(Integer id) {
        // 只有管理员才能进行审核
        AssertUtils.isTrue(Objects.equals(
                LocalThreadHolder.getRoleId(),
                RoleEnum.ADMIN.getRole()
        ), "无操作权限");
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setIsAudit(AUDIT);
        updateById(recipe);
        return ApiResult.success("食谱审核通过");
    }

    /**
     * 用户食谱列表，只能查询食谱状态是公开状态，并且是已经经过审核的食谱
     *
     * @param recipeQueryDto 查询参数
     * @return Result<List < RecipeListItemVO>> 响应结果
     */
//    @Override
//    public Result<List<RecipeListItemVO>> listRecipe(RecipeQueryDto recipeQueryDto) {
//        recipeQueryDto.setIsPublic(PUBLIC); // 既是公开
//        recipeQueryDto.setIsAudit(AUDIT); // 也是已审核
//        List<RecipeListItemVO> recipeListItemVOS = this.baseMapper.list(recipeQueryDto);
//        Integer count = this.baseMapper.listPageCount(recipeQueryDto);
//        return ApiResult.success(recipeListItemVOS, count);
//    }
    /**
     * 用户食谱列表（公开广场）：
     * 所有用户都能查询到所有【公开】且【已审核】的食谱，不限制创建者
     */
    @Override
    public Result<List<RecipeListItemVO>> listRecipe(RecipeQueryDto recipeQueryDto) {
        // 1. 获取当前登录人的真实 ID (那个 64)
        Integer myId = LocalThreadHolder.getUserId();
        System.err.println(">>>> [DEBUG] 前端传来的分类ID是: " + recipeQueryDto.getTypeId());

        // 2. 依然要手动计算分页偏移量
        int offset = (recipeQueryDto.getCurrent() - 1) * recipeQueryDto.getSize();
        recipeQueryDto.setCurrent(offset);

        // 3. 打印一下，看看现在前端到底想看什么模式
        // isPublic: null(全部), 0(私人), 1(公开)
        System.out.println(">>> 切换查询模式: " + recipeQueryDto.getIsPublic() + "，当前用户: " + myId);

        // 4. 调用一个更聪明的 Mapper 方法，把 DTO 和 我的 ID 都传进去
        List<RecipeListItemVO> list = this.baseMapper.listGlobalSmart(recipeQueryDto, myId);
        Integer total = this.baseMapper.listGlobalSmartCount(recipeQueryDto, myId);

        return ApiResult.success(list, total);
    }
//     * 查询用户的收藏列表
//     *
//     * @return Result<List < RecipeListItemVO>> 响应结果
//     */
    @Override
    public Result<List<RecipeListItemVO>> collectionList(RecipeQueryDto recipeQueryDto) {
        AssertUtils.notNull(recipeQueryDto, "查询条件不能为空");
        AssertUtils.notNull(recipeQueryDto.getSize(), "页面大小参数不能为空");
        AssertUtils.notNull(recipeQueryDto.getCurrent(), "当前页参数不能为空");
        // 1. 用户收藏了什么东西？
        FlowIndexQueryDto flowIndexQueryDto = new FlowIndexQueryDto();
        flowIndexQueryDto.setUserId(LocalThreadHolder.getUserId()); // 做好用户的数据隔离
        flowIndexQueryDto.setType(FlowIndexEnum.FLOW_INDEX_4.getId()); // 声明是收藏类型的互动数据
        flowIndexQueryDto.setContentModule("RECIPE"); // 声明是健康资讯模块
        List<FlowIndex> flowIndexList = flowIndexMapper.list(flowIndexQueryDto);
        if (flowIndexList.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        // 获取用户收藏的所有食谱的ID列表
        List<Integer> recipeIds = flowIndexList.stream()
                .map(FlowIndex::getContentId)
                .collect(Collectors.toList());
        // 2. 凭借着用户收藏的这些食谱ID列表，去查询食谱的数据
        recipeQueryDto.setIds(recipeIds);
        List<RecipeListItemVO> recipeListItemVOS = this.baseMapper.list(recipeQueryDto);
        Integer count = this.baseMapper.listPageCount(recipeQueryDto);
        return ApiResult.success(recipeListItemVOS, count);
    }

    @Override
    public Result<List<RecipeListItemVO>> recommend(Integer count) {
        // 查询系统下，既公开又审核的食谱ID集合
        List<Integer> recipeIds = this.baseMapper.listIds();
        if (recipeIds.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        // 查询用户对应内容的评分矩阵
        List<FlowIndexScoreVO> flowIndexScoreVOS = flowIndexMapper.listScores("RECIPE");
        // 算法工具类所期望的评分矩阵
        List<UserBasedCFUtil.Score> scoreList = flowIndexScoreVOS.stream().map(flowIndexScoreVO -> new UserBasedCFUtil.Score(
                flowIndexScoreVO.getUserId(),
                flowIndexScoreVO.getContentId(),
                flowIndexScoreVO.getScore()
        )).collect(Collectors.toList());
        Map<Integer, Map<Integer, Double>> integerMapMap = UserBasedCFUtil.buildUserItemMatrix(recipeIds, scoreList);
        UserBasedCFUtil userBasedCFUtil = new UserBasedCFUtil(integerMapMap);
        // 此处就是向用户推荐的食谱的ID列表
        List<Integer> recommendIds = userBasedCFUtil.recommendItems(LocalThreadHolder.getUserId(), count);
        // 冷启动
        if (recommendIds.isEmpty()) {
            RecipeQueryDto recipeQueryDto = new RecipeQueryDto();
            recipeQueryDto.setCurrent(0);
            recipeQueryDto.setSize(count);
            List<RecipeListItemVO> recipeListItemVOS = this.baseMapper.list(recipeQueryDto);
            return ApiResult.success(recipeListItemVOS);
        }
        RecipeQueryDto recipeQueryDto = new RecipeQueryDto();
        recipeQueryDto.setIds(recommendIds);
        List<RecipeListItemVO> recipeListItemVOS = this.baseMapper.list(recipeQueryDto);
        return ApiResult.success(recipeListItemVOS);
    }
    @Override
    public List<String> listPublicNames() {
        return this.baseMapper.selectPublicNames();
    }
}

