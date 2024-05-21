package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:12
 */
@Mapper
public interface CdGetPhoneDao extends BaseMapper<CdGetPhoneEntity> {

    List<LineRegisterSummaryResultDto> queryLineRegisterSummary(@Param("searchStartTime") LocalDateTime searchStartTime,
                                                                @Param("searchEndTime") LocalDateTime searchEndTime);


}
