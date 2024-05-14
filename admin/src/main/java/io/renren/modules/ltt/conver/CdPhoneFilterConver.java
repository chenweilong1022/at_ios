package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdPhoneFilterDTO;
import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
import io.renren.modules.ltt.vo.CdPhoneFilterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdPhoneFilterConver {

    CdPhoneFilterConver MAPPER =  Mappers.getMapper(CdPhoneFilterConver.class);

    CdPhoneFilterEntity converDTO(CdPhoneFilterDTO cdPhoneFilterDTO);

    List<CdPhoneFilterEntity> converDTO(List<CdPhoneFilterDTO> cdPhoneFilterDTOs);

    CdPhoneFilterVO conver(CdPhoneFilterEntity cdPhoneFilterEntities);

    List<CdPhoneFilterVO> conver(List<CdPhoneFilterEntity> cdPhoneFilterEntities);

}
