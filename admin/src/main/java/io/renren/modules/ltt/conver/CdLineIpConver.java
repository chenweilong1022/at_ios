package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdLineIpDTO;
import io.renren.modules.ltt.entity.CdLineIpEntity;
import io.renren.modules.ltt.vo.CdLineIpVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdLineIpConver {

    CdLineIpConver MAPPER =  Mappers.getMapper(CdLineIpConver.class);

    CdLineIpEntity converDTO(CdLineIpDTO cdLineIpDTO);

    List<CdLineIpEntity> converDTO(List<CdLineIpDTO> cdLineIpDTOs);

    CdLineIpVO conver(CdLineIpEntity cdLineIpEntities);

    List<CdLineIpVO> conver(List<CdLineIpEntity> cdLineIpEntities);

}
