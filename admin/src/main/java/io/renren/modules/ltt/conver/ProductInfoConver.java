package io.renren.modules.ltt.conver;

import io.renren.modules.ltt.dto.ProductInfoDTO;
import io.renren.modules.ltt.entity.ProductInfoEntity;
import io.renren.modules.ltt.vo.ProductInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-02-07 12:36
 */
@Mapper
public interface ProductInfoConver {

    ProductInfoConver MAPPER =  Mappers.getMapper(ProductInfoConver.class);

    ProductInfoEntity converDTO(ProductInfoDTO productInfoDTO);

    List<ProductInfoEntity> converDTO(List<ProductInfoDTO> productInfoDTOs);

    ProductInfoVO conver(ProductInfoEntity productInfoEntities);

    List<ProductInfoVO> conver(List<ProductInfoEntity> productInfoEntities);

}
