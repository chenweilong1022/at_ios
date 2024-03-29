package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AccountDetailsDTO;
import io.renren.modules.ltt.entity.AccountDetailsEntity;
import io.renren.modules.ltt.vo.AccountDetailsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AccountDetailsConver {

    AccountDetailsConver MAPPER =  Mappers.getMapper(AccountDetailsConver.class);

    AccountDetailsEntity converDTO(AccountDetailsDTO accountDetailsDTO);

    List<AccountDetailsEntity> converDTO(List<AccountDetailsDTO> accountDetailsDTOs);

    AccountDetailsVO conver(AccountDetailsEntity accountDetailsEntities);

    List<AccountDetailsVO> conver(List<AccountDetailsEntity> accountDetailsEntities);

}
