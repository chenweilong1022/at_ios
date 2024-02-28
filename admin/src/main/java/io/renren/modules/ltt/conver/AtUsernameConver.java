package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AtUsernameDTO;
import io.renren.modules.ltt.entity.AtUsernameEntity;
import io.renren.modules.ltt.vo.AtUsernameVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AtUsernameConver {

    AtUsernameConver MAPPER =  Mappers.getMapper(AtUsernameConver.class);

    AtUsernameEntity converDTO(AtUsernameDTO atUsernameDTO);

    List<AtUsernameEntity> converDTO(List<AtUsernameDTO> atUsernameDTOs);

    AtUsernameVO conver(AtUsernameEntity atUsernameEntities);

    List<AtUsernameVO> conver(List<AtUsernameEntity> atUsernameEntities);

}
