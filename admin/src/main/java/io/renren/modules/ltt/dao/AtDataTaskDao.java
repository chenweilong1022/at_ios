package io.renren.modules.ltt.dao;

import io.renren.modules.client.vo.GroupCountByDataTaskIdVO;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加粉任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 18:52:43
 */
@Mapper
public interface AtDataTaskDao extends BaseMapper<AtDataTaskEntity> {

    List<GroupCountByDataTaskIdVO> groupCountByDataTaskId(@Param("mod") Integer mod);
}
