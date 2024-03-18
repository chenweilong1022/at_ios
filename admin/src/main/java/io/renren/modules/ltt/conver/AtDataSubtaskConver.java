package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtDataSubtaskDTO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtDataSubtaskConver {

    AtDataSubtaskConver MAPPER =  Mappers.getMapper(AtDataSubtaskConver.class);

    AtDataSubtaskEntity converDTO(AtDataSubtaskDTO atDataSubtaskDTO);

    List<AtDataSubtaskEntity> converDTO(List<AtDataSubtaskDTO> atDataSubtaskDTOs);

    AtDataSubtaskVO conver(AtDataSubtaskEntity atDataSubtaskEntities);

    List<AtDataSubtaskVO> conver(List<AtDataSubtaskEntity> atDataSubtaskEntities);

}
