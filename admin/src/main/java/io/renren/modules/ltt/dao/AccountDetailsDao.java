package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AccountDetailsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流水明细表
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
@Mapper
public interface AccountDetailsDao extends BaseMapper<AccountDetailsEntity> {
	
}
