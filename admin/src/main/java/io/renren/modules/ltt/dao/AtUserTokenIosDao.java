package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.dto.AtUserTokenIosDeviceParamDTO;
import io.renren.modules.ltt.dto.AtUserTokenIosDeviceResultDTO;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户ios token数据
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 00:16:44
 */
@Mapper
public interface AtUserTokenIosDao extends BaseMapper<AtUserTokenIosEntity> {

    Integer queryDevicePageCount(AtUserTokenIosDeviceParamDTO paramDTO);
    List<AtUserTokenIosDeviceResultDTO> queryDevicePage(AtUserTokenIosDeviceParamDTO paramDTO);
}
