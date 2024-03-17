package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
import io.renren.modules.ltt.vo.AtDataTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtDataTaskConver {

    AtDataTaskConver MAPPER =  Mappers.getMapper(AtDataTaskConver.class);

    AtDataTaskEntity converDTO(AtDataTaskDTO atDataTaskDTO);

    List<AtDataTaskEntity> converDTO(List<AtDataTaskDTO> atDataTaskDTOs);

    AtDataTaskVO conver(AtDataTaskEntity atDataTaskEntities);

    List<AtDataTaskVO> conver(List<AtDataTaskEntity> atDataTaskEntities);

}
