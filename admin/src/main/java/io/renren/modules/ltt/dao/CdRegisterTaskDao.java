package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Mapper
public interface CdRegisterTaskDao extends BaseMapper<CdRegisterTaskEntity> {

    Integer sumByTaskId(@Param("id") Integer id);
}
