package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUsernameTaskDTO;
import io.renren.modules.ltt.entity.AtUsernameTaskEntity;
import io.renren.modules.ltt.vo.AtUsernameTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUsernameTaskConver {

    AtUsernameTaskConver MAPPER =  Mappers.getMapper(AtUsernameTaskConver.class);

    AtUsernameTaskEntity converDTO(AtUsernameTaskDTO atUsernameTaskDTO);

    List<AtUsernameTaskEntity> converDTO(List<AtUsernameTaskDTO> atUsernameTaskDTOs);

    AtUsernameTaskVO conver(AtUsernameTaskEntity atUsernameTaskEntities);

    List<AtUsernameTaskVO> conver(List<AtUsernameTaskEntity> atUsernameTaskEntities);

}
