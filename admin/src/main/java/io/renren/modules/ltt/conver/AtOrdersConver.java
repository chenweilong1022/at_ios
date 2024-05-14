package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtOrdersDTO;
import io.renren.modules.ltt.entity.AtOrdersEntity;
import io.renren.modules.ltt.vo.AtOrdersVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtOrdersConver {

    AtOrdersConver MAPPER =  Mappers.getMapper(AtOrdersConver.class);

    AtOrdersEntity converDTO(AtOrdersDTO atOrdersDTO);

    List<AtOrdersEntity> converDTO(List<AtOrdersDTO> atOrdersDTOs);

    AtOrdersVO conver(AtOrdersEntity atOrdersEntities);

    List<AtOrdersVO> conver(List<AtOrdersEntity> atOrdersEntities);

}
