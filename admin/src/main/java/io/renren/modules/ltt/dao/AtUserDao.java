package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账号数据
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:55:49
 */
@Mapper
public interface AtUserDao extends BaseMapper<AtUserEntity> {

    Integer updateCustomerByIds(UpdateAtUserCustomerParamDto paramDto);
}
