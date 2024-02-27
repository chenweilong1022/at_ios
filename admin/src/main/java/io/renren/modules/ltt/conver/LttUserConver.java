package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.LttUserDTO;
import io.renren.modules.ltt.entity.LttUserEntity;
import io.renren.modules.ltt.vo.LttUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface LttUserConver {

    LttUserConver MAPPER =  Mappers.getMapper(LttUserConver.class);

    LttUserEntity converDTO(LttUserDTO lttUserDTO);

    List<LttUserEntity> converDTO(List<LttUserDTO> lttUserDTOs);

    LttUserVO conver(LttUserEntity lttUserEntities);

    List<LttUserVO> conver(List<LttUserEntity> lttUserEntities);

}
