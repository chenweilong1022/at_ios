package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtAvatarDTO;
import io.renren.modules.ltt.entity.AtAvatarEntity;
import io.renren.modules.ltt.vo.AtAvatarVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtAvatarConver {

    AtAvatarConver MAPPER =  Mappers.getMapper(AtAvatarConver.class);

    AtAvatarEntity converDTO(AtAvatarDTO atAvatarDTO);

    List<AtAvatarEntity> converDTO(List<AtAvatarDTO> atAvatarDTOs);

    AtAvatarVO conver(AtAvatarEntity atAvatarEntities);

    List<AtAvatarVO> conver(List<AtAvatarEntity> atAvatarEntities);

}
