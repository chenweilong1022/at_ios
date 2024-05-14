package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtAvatarGroupDTO;
import io.renren.modules.ltt.entity.AtAvatarGroupEntity;
import io.renren.modules.ltt.vo.AtAvatarGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtAvatarGroupConver {

    AtAvatarGroupConver MAPPER =  Mappers.getMapper(AtAvatarGroupConver.class);

    AtAvatarGroupEntity converDTO(AtAvatarGroupDTO atAvatarGroupDTO);

    List<AtAvatarGroupEntity> converDTO(List<AtAvatarGroupDTO> atAvatarGroupDTOs);

    AtAvatarGroupVO conver(AtAvatarGroupEntity atAvatarGroupEntities);

    List<AtAvatarGroupVO> conver(List<AtAvatarGroupEntity> atAvatarGroupEntities);

}
