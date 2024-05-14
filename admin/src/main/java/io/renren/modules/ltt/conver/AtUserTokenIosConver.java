package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUserTokenIosDTO;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import io.renren.modules.ltt.vo.AtUserTokenIosVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUserTokenIosConver {

    AtUserTokenIosConver MAPPER =  Mappers.getMapper(AtUserTokenIosConver.class);

    AtUserTokenIosEntity converDTO(AtUserTokenIosDTO atUserTokenIosDTO);

    List<AtUserTokenIosEntity> converDTO(List<AtUserTokenIosDTO> atUserTokenIosDTOs);

    AtUserTokenIosVO conver(AtUserTokenIosEntity atUserTokenIosEntities);

    List<AtUserTokenIosVO> conver(List<AtUserTokenIosEntity> atUserTokenIosEntities);

}
