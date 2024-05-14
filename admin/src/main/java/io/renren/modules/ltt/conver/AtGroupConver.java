package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.vo.AtGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtGroupConver {

    AtGroupConver MAPPER =  Mappers.getMapper(AtGroupConver.class);

    AtGroupEntity converDTO(AtGroupDTO atGroupDTO);

    List<AtGroupEntity> converDTO(List<AtGroupDTO> atGroupDTOs);

    AtGroupVO conver(AtGroupEntity atGroupEntities);

    List<AtGroupVO> conver(List<AtGroupEntity> atGroupEntities);

    AtGroupEntity conver1(AtGroupEntity atGroupEntities);
    List<AtGroupEntity> conver1(List<AtGroupEntity> atGroupEntities);

}
