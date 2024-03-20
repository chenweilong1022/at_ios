package io.renren.modules.ltt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.datasources.annotation.Admin;
import io.renren.modules.ltt.dto.CustomerUserParamDto;
import io.renren.modules.ltt.dto.CustomerUserResultDto;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
@Mapper
@Admin
public interface CustomerUserDao extends BaseMapper<SysUserEntity> {

    Integer queryPageCount(CustomerUserParamDto paramDto);

    List<CustomerUserResultDto> queryPage(CustomerUserParamDto paramDto);

    List<CustomerUserResultDto> queryByFuzzyName(CustomerUserParamDto paramDto);

}
