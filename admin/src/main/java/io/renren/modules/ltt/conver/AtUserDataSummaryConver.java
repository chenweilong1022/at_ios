package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUserDataSummaryDTO;
import io.renren.modules.ltt.entity.AtUserDataSummaryEntity;
import io.renren.modules.ltt.vo.AtUserDataSummaryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUserDataSummaryConver {

    AtUserDataSummaryConver MAPPER =  Mappers.getMapper(AtUserDataSummaryConver.class);

    AtUserDataSummaryEntity converDTO(AtUserDataSummaryDTO atUserDataSummaryDTO);

    List<AtUserDataSummaryEntity> converDTO(List<AtUserDataSummaryDTO> atUserDataSummaryDTOs);

    AtUserDataSummaryVO conver(AtUserDataSummaryEntity atUserDataSummaryEntities);

    List<AtUserDataSummaryVO> conver(List<AtUserDataSummaryEntity> atUserDataSummaryEntities);

}
