package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.CdLineIpProxyEntity;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdLineIpProxyConver {

    CdLineIpProxyConver MAPPER =  Mappers.getMapper(CdLineIpProxyConver.class);

    CdLineIpProxyEntity converDTO(CdLineIpProxyDTO cdLineIpProxyDTO);

    List<CdLineIpProxyEntity> converDTO(List<CdLineIpProxyDTO> cdLineIpProxyDTOs);

    CdLineIpProxyVO conver(CdLineIpProxyEntity cdLineIpProxyEntities);

    List<CdLineIpProxyVO> conver(List<CdLineIpProxyEntity> cdLineIpProxyEntities);

}
