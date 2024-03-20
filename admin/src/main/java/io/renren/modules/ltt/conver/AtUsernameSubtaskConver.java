package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUsernameSubtaskDTO;
import io.renren.modules.ltt.entity.AtUsernameSubtaskEntity;
import io.renren.modules.ltt.vo.AtUsernameSubtaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUsernameSubtaskConver {

    AtUsernameSubtaskConver MAPPER =  Mappers.getMapper(AtUsernameSubtaskConver.class);

    AtUsernameSubtaskEntity converDTO(AtUsernameSubtaskDTO atUsernameSubtaskDTO);

    List<AtUsernameSubtaskEntity> converDTO(List<AtUsernameSubtaskDTO> atUsernameSubtaskDTOs);

    AtUsernameSubtaskVO conver(AtUsernameSubtaskEntity atUsernameSubtaskEntities);

    List<AtUsernameSubtaskVO> conver(List<AtUsernameSubtaskEntity> atUsernameSubtaskEntities);

}
