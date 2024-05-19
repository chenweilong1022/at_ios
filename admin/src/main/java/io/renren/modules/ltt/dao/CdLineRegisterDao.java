package io.renren.modules.ltt.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.dto.CdLineRegisterSummaryDto;
import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Mapper
public interface CdLineRegisterDao extends BaseMapper<CdLineRegisterEntity> {
    /**
     * 获取数量
     * @param registerSubtasksIds
     * @return
     */
    List<GetCountBySubTaskIdVO> getCountBySubTaskId(@Param("registerSubtasksIds") List<Integer> registerSubtasksIds);

    CdLineRegisterSummaryDto listSummary(@Param("dto")  CdLineRegisterDTO cdLineRegister);

    IPage<CdLineRegisterVO> listPage(Page<CdLineRegisterEntity> page,@Param("dto")  CdLineRegisterDTO cdLineRegister);


    List<LineRegisterSummaryResultDto> queryLineRegisterSummary(@Param("searchStartTime") LocalDateTime searchStartTime,
                                                                @Param("searchEndTime") LocalDateTime searchEndTime);
}
