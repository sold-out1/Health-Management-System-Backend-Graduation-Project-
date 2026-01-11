package com.kmbeast.mapper;

import com.kmbeast.pojo.vo.ContentTypeVO;
import com.kmbeast.pojo.vo.StaticCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仪表盘持久化接口
 */
@Mapper
public interface DashboardMapper {

    StaticCountVO staticCount();

    List<ContentTypeVO> newsContentType(@Param(value = "typeIds") List<Integer> typeIds);

    List<ContentTypeVO> recipeContentType(@Param(value = "typeIds") List<Integer> typeIds);

}
