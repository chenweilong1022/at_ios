package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdIpConfigDTO;
import io.renren.modules.ltt.entity.CdIpConfigEntity;
import io.renren.modules.ltt.vo.CdIpConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdIpConfigConver {

    CdIpConfigConver MAPPER =  Mappers.getMapper(CdIpConfigConver.class);

    CdIpConfigEntity converDTO(CdIpConfigDTO cdIpConfigDTO);

    List<CdIpConfigEntity> converDTO(List<CdIpConfigDTO> cdIpConfigDTOs);

    CdIpConfigVO conver(CdIpConfigEntity cdIpConfigEntities);

    List<CdIpConfigVO> conver(List<CdIpConfigEntity> cdIpConfigEntities);

}
