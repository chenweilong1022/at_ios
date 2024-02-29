package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtUserTokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户token数据
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 02:03:03
 */
@Mapper
public interface AtUserTokenDao extends BaseMapper<AtUserTokenEntity> {
	
}
