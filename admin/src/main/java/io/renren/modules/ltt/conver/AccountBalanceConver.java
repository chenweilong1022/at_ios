package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.AccountBalanceDTO;
import io.renren.modules.ltt.entity.AccountBalanceEntity;
import io.renren.modules.ltt.vo.AccountBalanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface AccountBalanceConver {

    AccountBalanceConver MAPPER =  Mappers.getMapper(AccountBalanceConver.class);

    AccountBalanceEntity converDTO(AccountBalanceDTO accountBalanceDTO);

    List<AccountBalanceEntity> converDTO(List<AccountBalanceDTO> accountBalanceDTOs);

    AccountBalanceVO conver(AccountBalanceEntity accountBalanceEntities);

    List<AccountBalanceVO> conver(List<AccountBalanceEntity> accountBalanceEntities);

}
