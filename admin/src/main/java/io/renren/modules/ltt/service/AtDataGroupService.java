package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtDataGroupDTO;
import io.renren.modules.ltt.vo.AtDataGroupVO;
import io.renren.modules.ltt.entity.AtDataGroupEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 数据分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 00:27:43
 */
public interface AtDataGroupService extends IService<AtDataGroupEntity> {

    /**
     * 分页查询
     * @param atDataGroup
     * @return
     */
    PageUtils queryPage(AtDataGroupDTO atDataGroup);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtDataGroupVO getById(Integer id);
    /**
     * 保存
     * @param atDataGroup
     * @return
     */
    boolean save(AtDataGroupDTO atDataGroup);
    /**
     * 根据id修改
     * @param atDataGroup
     * @return
     */
    boolean updateById(AtDataGroupDTO atDataGroup);

    /**
     * 批量修改昵称
      * @param atDataGroup
     * @return
     */
    boolean updateBatchGroup(AtDataGroupDTO atDataGroup);

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
}

