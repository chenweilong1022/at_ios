package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Mapper
public interface CdLineRegisterDao extends BaseMapper<CdLineRegisterEntity> {
    /**
     * 获取数量
     * @param registerSubtasksIds
     * @return
     */
    List<GetCountBySubTaskIdVO> getCountBySubTaskId(@Param("registerSubtasksIds") List<Integer> registerSubtasksIds);
}
