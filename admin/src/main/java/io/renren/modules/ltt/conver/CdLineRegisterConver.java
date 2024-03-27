package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdLineRegisterConver {

    CdLineRegisterConver MAPPER =  Mappers.getMapper(CdLineRegisterConver.class);

    CdLineRegisterEntity converDTO(CdLineRegisterDTO cdLineRegisterDTO);

    List<CdLineRegisterEntity> converDTO(List<CdLineRegisterDTO> cdLineRegisterDTOs);

    CdLineRegisterVO conver(CdLineRegisterEntity cdLineRegisterEntities);

    List<CdLineRegisterVO> conver(List<CdLineRegisterEntity> cdLineRegisterEntities);

}
