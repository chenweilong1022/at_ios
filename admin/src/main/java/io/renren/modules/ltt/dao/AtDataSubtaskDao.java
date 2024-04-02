package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.dto.AtDataSubtaskParamPageDTO;
import io.renren.modules.ltt.dto.AtDataSubtaskResultDto;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加粉任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 10:53:33
 */
@Mapper
public interface AtDataSubtaskDao extends BaseMapper<AtDataSubtaskEntity> {

    Integer queryPageCount(AtDataSubtaskParamPageDTO paramPageDto);

    List<AtDataSubtaskResultDto> queryPage(AtDataSubtaskParamPageDTO paramPageDto);

    List<AtDataSubtaskVO> groupByUserId(@Param("dto") AtDataSubtaskEntity dto);
}
