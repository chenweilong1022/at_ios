package io.renren.modules.ltt.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.ltt.dto.CdPhoneFilterDTO;
import io.renren.modules.ltt.dto.CdPhoneFilterStatusDto;
import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.CdPhoneFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 手机号筛选
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-03 22:09:23
 */
@Mapper
public interface CdPhoneFilterDao extends BaseMapper<CdPhoneFilterEntity> {

    CdPhoneFilterStatusDto queryByTaskStatus(@Param("recordId") Integer recordId);

    /**
     * 查询listPage
     * @param page
     * @param dto
     * @return
     */
    IPage<CdPhoneFilterVO> listPage(Page<CdPhoneFilterEntity> page, CdPhoneFilterDTO dto);
}
