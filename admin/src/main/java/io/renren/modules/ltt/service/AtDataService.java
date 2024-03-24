package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtDataDTO;
import io.renren.modules.ltt.vo.AtDataGroupVODataCountGroupIdVO;
import io.renren.modules.ltt.vo.AtDataVO;
import io.renren.modules.ltt.entity.AtDataEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:14:06
 */
public interface AtDataService extends IService<AtDataEntity> {

    /**
     * 分页查询
     * @param atData
     * @return
     */
    PageUtils queryPage(AtDataDTO atData);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtDataVO getById(Integer id);
    /**
     * 保存
     * @param atData
     * @return
     */
    boolean save(AtDataDTO atData);
    /**
     * 根据id修改
     * @param atData
     * @return
     */
    boolean updateById(AtDataDTO atData);
    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @Override
    boolean removeByIds(Collection<? extends Serializable> ids);

    /**
     * 剩余可用数据分组
     * @return
     */
    List<AtDataGroupVODataCountGroupIdVO> dataCountGroupId(List<Integer> dataGroupId);
}

