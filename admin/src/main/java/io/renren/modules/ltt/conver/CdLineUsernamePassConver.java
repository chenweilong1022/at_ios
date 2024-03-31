package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.CdLineUsernamePassDTO;
import io.renren.modules.ltt.entity.CdLineUsernamePassEntity;
import io.renren.modules.ltt.vo.CdLineUsernamePassVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface CdLineUsernamePassConver {

    CdLineUsernamePassConver MAPPER =  Mappers.getMapper(CdLineUsernamePassConver.class);

    CdLineUsernamePassEntity converDTO(CdLineUsernamePassDTO cdLineUsernamePassDTO);

    List<CdLineUsernamePassEntity> converDTO(List<CdLineUsernamePassDTO> cdLineUsernamePassDTOs);

    CdLineUsernamePassVO conver(CdLineUsernamePassEntity cdLineUsernamePassEntities);

    List<CdLineUsernamePassVO> conver(List<CdLineUsernamePassEntity> cdLineUsernamePassEntities);

}
