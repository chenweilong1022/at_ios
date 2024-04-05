package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdPhoneFilterRecordDTO;
import io.renren.modules.ltt.entity.CdPhoneFilterRecordEntity;
import io.renren.modules.ltt.vo.CdPhoneFilterRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdPhoneFilterRecordConver {

    CdPhoneFilterRecordConver MAPPER =  Mappers.getMapper(CdPhoneFilterRecordConver.class);

    CdPhoneFilterRecordEntity converDTO(CdPhoneFilterRecordDTO cdPhoneFilterRecordDTO);

    List<CdPhoneFilterRecordEntity> converDTO(List<CdPhoneFilterRecordDTO> cdPhoneFilterRecordDTOs);

    CdPhoneFilterRecordVO conver(CdPhoneFilterRecordEntity cdPhoneFilterRecordEntities);

    List<CdPhoneFilterRecordVO> conver(List<CdPhoneFilterRecordEntity> cdPhoneFilterRecordEntities);

}
