package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Mapper
public interface CdRegisterSubtasksDao extends BaseMapper<CdRegisterSubtasksEntity> {

    List<CdRegisterSubtasksVO> groupByTaskId();
}
