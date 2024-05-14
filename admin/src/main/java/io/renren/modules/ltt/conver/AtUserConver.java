package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.vo.AtUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUserConver {

    AtUserConver MAPPER =  Mappers.getMapper(AtUserConver.class);

    AtUserEntity converDTO(AtUserDTO atUserDTO);

    List<AtUserEntity> converDTO(List<AtUserDTO> atUserDTOs);

    AtUserVO conver(AtUserEntity atUserEntities);

    List<AtUserVO> conver(List<AtUserEntity> atUserEntities);

}
