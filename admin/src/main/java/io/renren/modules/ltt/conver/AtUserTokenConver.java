package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUserTokenDTO;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUserTokenConver {

    AtUserTokenConver MAPPER =  Mappers.getMapper(AtUserTokenConver.class);

    AtUserTokenEntity converDTO(AtUserTokenDTO atUserTokenDTO);

    List<AtUserTokenEntity> converDTO(List<AtUserTokenDTO> atUserTokenDTOs);

    AtUserTokenVO conver(AtUserTokenEntity atUserTokenEntities);

    List<AtUserTokenVO> conver(List<AtUserTokenEntity> atUserTokenEntities);

}
