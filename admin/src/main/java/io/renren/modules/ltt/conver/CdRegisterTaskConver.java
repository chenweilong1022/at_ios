package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdRegisterTaskDTO;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.vo.CdRegisterTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdRegisterTaskConver {

    CdRegisterTaskConver MAPPER =  Mappers.getMapper(CdRegisterTaskConver.class);

    CdRegisterTaskEntity converDTO(CdRegisterTaskDTO cdRegisterTaskDTO);

    List<CdRegisterTaskEntity> converDTO(List<CdRegisterTaskDTO> cdRegisterTaskDTOs);

    CdRegisterTaskVO conver(CdRegisterTaskEntity cdRegisterTaskEntities);

    List<CdRegisterTaskVO> conver(List<CdRegisterTaskEntity> cdRegisterTaskEntities);

}
