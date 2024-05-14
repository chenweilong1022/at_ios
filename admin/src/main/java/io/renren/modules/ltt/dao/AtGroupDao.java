package io.renren.modules.ltt.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.entity.AtGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.vo.AtGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
@Mapper
public interface AtGroupDao extends BaseMapper<AtGroupEntity> {

    IPage<AtGroupVO> listPage(Page<AtGroupEntity> page,@Param("dto") AtGroupDTO atGroup);

    /**
     * 数据汇总
     * @param groupTaskIdList
     * @return
     */
    List<AtGroupTaskVO> groupDataSummary(@Param("groupTaskIdList") List<Integer> groupTaskIdList);
}
