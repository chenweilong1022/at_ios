package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUserGroupDTO;
import io.renren.modules.ltt.vo.AtUserGroupVO;
import io.renren.modules.ltt.entity.AtUserGroupEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 用户分组表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-27 17:18:34
 */
public interface AtUserGroupService extends IService<AtUserGroupEntity> {

    /**
     * 分页查询
     * @param atUserGroup
     * @return
     */
    PageUtils queryPage(AtUserGroupDTO atUserGroup);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserGroupVO getById(Integer id);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    List<AtUserGroupVO> getByIds(List<Integer> id);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Map<Integer, String> getMapByIds(List<Integer> id);
    /**
     * 保存
     * @param atUserGroup
     * @return
     */
    boolean save(AtUserGroupDTO atUserGroup);
    /**
     * 根据id修改
     * @param atUserGroup
     * @return
     */
    boolean updateById(AtUserGroupDTO atUserGroup);
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

