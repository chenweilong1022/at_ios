package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.AtDataGroupVODataCountGroupIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:14:06
 */
@Mapper
public interface AtDataDao extends BaseMapper<AtDataEntity> {

    List<AtDataGroupVODataCountGroupIdVO> dataCountGroupId(@Param("dataGroupIdList")List<Integer> dataGroupIdList);
}
