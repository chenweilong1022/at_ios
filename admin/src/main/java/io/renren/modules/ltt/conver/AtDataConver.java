package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtDataDTO;
import io.renren.modules.ltt.entity.AtDataEntity;
import io.renren.modules.ltt.vo.AtDataVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtDataConver {

    AtDataConver MAPPER =  Mappers.getMapper(AtDataConver.class);

    AtDataEntity converDTO(AtDataDTO atDataDTO);

    List<AtDataEntity> converDTO(List<AtDataDTO> atDataDTOs);

    AtDataVO conver(AtDataEntity atDataEntities);

    List<AtDataVO> conver(List<AtDataEntity> atDataEntities);

}
