package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtDataGroupDTO;
import io.renren.modules.ltt.entity.AtDataGroupEntity;
import io.renren.modules.ltt.vo.AtDataGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtDataGroupConver {

    AtDataGroupConver MAPPER =  Mappers.getMapper(AtDataGroupConver.class);

    AtDataGroupEntity converDTO(AtDataGroupDTO atDataGroupDTO);

    List<AtDataGroupEntity> converDTO(List<AtDataGroupDTO> atDataGroupDTOs);

    AtDataGroupVO conver(AtDataGroupEntity atDataGroupEntities);

    List<AtDataGroupVO> conver(List<AtDataGroupEntity> atDataGroupEntities);

}
