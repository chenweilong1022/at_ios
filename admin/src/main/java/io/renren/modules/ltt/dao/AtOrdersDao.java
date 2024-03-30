package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtOrdersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:15:12
 */
@Mapper
public interface AtOrdersDao extends BaseMapper<AtOrdersEntity> {
	
}
