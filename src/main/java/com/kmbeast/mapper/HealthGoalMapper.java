package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.entity.HealthGoal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HealthGoalMapper extends BaseMapper<HealthGoal> {

    List<HealthGoal> selectExpiringGoals(@Param("userId") Integer userId);
}