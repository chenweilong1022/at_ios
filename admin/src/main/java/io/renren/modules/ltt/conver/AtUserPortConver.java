package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUserPortDTO;
import io.renren.modules.ltt.entity.AtUserPortEntity;
import io.renren.modules.ltt.vo.AtUserPortVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUserPortConver {

    AtUserPortConver MAPPER =  Mappers.getMapper(AtUserPortConver.class);

    AtUserPortEntity converDTO(AtUserPortDTO atUserPortDTO);

    List<AtUserPortEntity> converDTO(List<AtUserPortDTO> atUserPortDTOs);

    AtUserPortVO conver(AtUserPortEntity atUserPortEntities);

    List<AtUserPortVO> conver(List<AtUserPortEntity> atUserPortEntities);

}
