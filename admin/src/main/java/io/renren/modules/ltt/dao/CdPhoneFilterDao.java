package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.dto.CdPhoneFilterStatusDto;
import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 手机号筛选
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-03 22:09:23
 */
@Mapper
public interface CdPhoneFilterDao extends BaseMapper<CdPhoneFilterEntity> {

    CdPhoneFilterStatusDto queryByTaskStatus(@Param("recordId") Integer recordId);
}
