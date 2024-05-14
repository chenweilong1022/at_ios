package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.LttUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2022-08-02 14:05:38
 */
@Mapper
public interface LttUserDao extends BaseMapper<LttUserEntity> {
	
}
