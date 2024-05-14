package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.vo.CdGetPhoneVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdGetPhoneConver {

    CdGetPhoneConver MAPPER =  Mappers.getMapper(CdGetPhoneConver.class);

    CdGetPhoneEntity converDTO(CdGetPhoneDTO cdGetPhoneDTO);

    List<CdGetPhoneEntity> converDTO(List<CdGetPhoneDTO> cdGetPhoneDTOs);

    CdGetPhoneVO conver(CdGetPhoneEntity cdGetPhoneEntities);

    List<CdGetPhoneVO> conver(List<CdGetPhoneEntity> cdGetPhoneEntities);

}
