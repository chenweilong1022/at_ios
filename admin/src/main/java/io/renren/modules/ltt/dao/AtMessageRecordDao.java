package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtMessageRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息记录
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-21 14:41:06
 */
@Mapper
public interface AtMessageRecordDao extends BaseMapper<AtMessageRecordEntity> {
	
}
