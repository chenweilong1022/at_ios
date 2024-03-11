package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtAvatarTaskDTO;
import io.renren.modules.ltt.entity.AtAvatarTaskEntity;
import io.renren.modules.ltt.vo.AtAvatarTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtAvatarTaskConver {

    AtAvatarTaskConver MAPPER =  Mappers.getMapper(AtAvatarTaskConver.class);

    AtAvatarTaskEntity converDTO(AtAvatarTaskDTO atAvatarTaskDTO);

    List<AtAvatarTaskEntity> converDTO(List<AtAvatarTaskDTO> atAvatarTaskDTOs);

    AtAvatarTaskVO conver(AtAvatarTaskEntity atAvatarTaskEntities);

    List<AtAvatarTaskVO> conver(List<AtAvatarTaskEntity> atAvatarTaskEntities);

}
