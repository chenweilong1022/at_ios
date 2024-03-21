package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtMessageRecordDTO;
import io.renren.modules.ltt.entity.AtMessageRecordEntity;
import io.renren.modules.ltt.vo.AtMessageRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtMessageRecordConver {

    AtMessageRecordConver MAPPER =  Mappers.getMapper(AtMessageRecordConver.class);

    AtMessageRecordEntity converDTO(AtMessageRecordDTO atMessageRecordDTO);

    List<AtMessageRecordEntity> converDTO(List<AtMessageRecordDTO> atMessageRecordDTOs);

    AtMessageRecordVO conver(AtMessageRecordEntity atMessageRecordEntities);

    List<AtMessageRecordVO> conver(List<AtMessageRecordEntity> atMessageRecordEntities);

}
