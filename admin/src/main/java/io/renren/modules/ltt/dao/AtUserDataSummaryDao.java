package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtUserDataSummaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据汇总-定时更新
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-05-06 13:15:52
 */
@Mapper
public interface AtUserDataSummaryDao extends BaseMapper<AtUserDataSummaryEntity> {
	
}
