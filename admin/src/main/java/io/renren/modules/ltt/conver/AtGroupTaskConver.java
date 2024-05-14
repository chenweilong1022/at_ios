package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtGroupTaskDTO;
import io.renren.modules.ltt.entity.AtGroupTaskEntity;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtGroupTaskConver {

    AtGroupTaskConver MAPPER =  Mappers.getMapper(AtGroupTaskConver.class);

    AtGroupTaskEntity converDTO(AtGroupTaskDTO atGroupTaskDTO);

    List<AtGroupTaskEntity> converDTO(List<AtGroupTaskDTO> atGroupTaskDTOs);

    AtGroupTaskVO conver(AtGroupTaskEntity atGroupTaskEntities);

    List<AtGroupTaskVO> conver(List<AtGroupTaskEntity> atGroupTaskEntities);

}
