package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.dto.AtDataSubtaskParamPageDTO;
import io.renren.modules.ltt.dto.AtDataSubtaskResultDto;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.dto.UserSummaryResultDto;
import io.renren.modules.ltt.entity.AtUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.AtUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账号数据
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:55:49
 */
@Mapper
public interface AtUserDao extends BaseMapper<AtUserEntity> {

    Integer updateCustomerByIds(UpdateAtUserCustomerParamDto paramDto);

    /**
     * 计算账户账号总数
     * @param sysUserId
     * @return
     */
    Integer queryCountBySysUserId(Long sysUserId);

    Integer queryPageCount(AtUserDTO atUserDTO);

    List<AtUserVO> queryPage(AtUserDTO atUserDTO);

    /**
     * 已使用账户汇总
     * @return
     */
    List<UserSummaryResultDto> queryUsedUserSummary(@Param("searchStartTime") LocalDateTime searchStartTime,
                                                    @Param("searchEndTime") LocalDateTime searchEndTime);


    /**
     * 在线账户汇总
     * @return
     */
    List<UserSummaryResultDto> queryOnlineUserSummary();


}
