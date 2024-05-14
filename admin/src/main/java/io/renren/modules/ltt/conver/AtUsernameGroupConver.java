package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUsernameGroupDTO;
import io.renren.modules.ltt.entity.AtUsernameGroupEntity;
import io.renren.modules.ltt.vo.AtUsernameGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUsernameGroupConver {

    AtUsernameGroupConver MAPPER =  Mappers.getMapper(AtUsernameGroupConver.class);

    AtUsernameGroupEntity converDTO(AtUsernameGroupDTO atUsernameGroupDTO);

    List<AtUsernameGroupEntity> converDTO(List<AtUsernameGroupDTO> atUsernameGroupDTOs);

    AtUsernameGroupVO conver(AtUsernameGroupEntity atUsernameGroupEntities);

    List<AtUsernameGroupVO> conver(List<AtUsernameGroupEntity> atUsernameGroupEntities);

}
