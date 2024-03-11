package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtAvatarSubtaskDTO;
import io.renren.modules.ltt.entity.AtAvatarSubtaskEntity;
import io.renren.modules.ltt.vo.AtAvatarSubtaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtAvatarSubtaskConver {

    AtAvatarSubtaskConver MAPPER =  Mappers.getMapper(AtAvatarSubtaskConver.class);

    AtAvatarSubtaskEntity converDTO(AtAvatarSubtaskDTO atAvatarSubtaskDTO);

    List<AtAvatarSubtaskEntity> converDTO(List<AtAvatarSubtaskDTO> atAvatarSubtaskDTOs);

    AtAvatarSubtaskVO conver(AtAvatarSubtaskEntity atAvatarSubtaskEntities);

    List<AtAvatarSubtaskVO> conver(List<AtAvatarSubtaskEntity> atAvatarSubtaskEntities);

}
