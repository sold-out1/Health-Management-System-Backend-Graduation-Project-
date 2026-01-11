package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.DietHistoryQueryDto;
import com.kmbeast.pojo.entity.DietHistory;
import com.kmbeast.pojo.vo.DietHistoryVO;

import java.util.List;

/**
 * 饮食记录业务逻辑接口
 */
public interface DietHistoryService extends IService<DietHistory> {

    Result<String> saveEntity(List<DietHistory> dietHistories);

    Result<String> delete(Integer id);

    Result<List<DietHistoryVO>> listUser(DietHistoryQueryDto dietHistoryQueryDto);

    Result<List<DietHistoryVO>> listItem(DietHistoryQueryDto dietHistoryQueryDto);

}