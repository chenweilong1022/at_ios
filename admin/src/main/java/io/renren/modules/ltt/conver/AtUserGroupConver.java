package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUserGroupDTO;
import io.renren.modules.ltt.entity.AtUserGroupEntity;
import io.renren.modules.ltt.vo.AtUserGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUserGroupConver {

    AtUserGroupConver MAPPER =  Mappers.getMapper(AtUserGroupConver.class);

    AtUserGroupEntity converDTO(AtUserGroupDTO atUserGroupDTO);

    List<AtUserGroupEntity> converDTO(List<AtUserGroupDTO> atUserGroupDTOs);

    AtUserGroupVO conver(AtUserGroupEntity atUserGroupEntities);

    List<AtUserGroupVO> conver(List<AtUserGroupEntity> atUserGroupEntities);

}
