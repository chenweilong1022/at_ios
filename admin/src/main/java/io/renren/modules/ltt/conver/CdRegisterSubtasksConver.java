package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdRegisterSubtasksDTO;
import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdRegisterSubtasksConver {

    CdRegisterSubtasksConver MAPPER =  Mappers.getMapper(CdRegisterSubtasksConver.class);

    CdRegisterSubtasksEntity converDTO(CdRegisterSubtasksDTO cdRegisterSubtasksDTO);

    List<CdRegisterSubtasksEntity> converDTO(List<CdRegisterSubtasksDTO> cdRegisterSubtasksDTOs);

    CdRegisterSubtasksVO conver(CdRegisterSubtasksEntity cdRegisterSubtasksEntities);

    List<CdRegisterSubtasksVO> conver(List<CdRegisterSubtasksEntity> cdRegisterSubtasksEntities);

}
