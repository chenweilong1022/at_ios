package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtAvatarGroupDTO;
import io.renren.modules.ltt.vo.AtAvatarGroupVO;
import io.renren.modules.ltt.entity.AtAvatarGroupEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 头像分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:45:51
 */
public interface AtAvatarGroupService extends IService<AtAvatarGroupEntity> {

    /**
     * 分页查询
     * @param atAvatarGroup
     * @return
     */
    PageUtils queryPage(AtAvatarGroupDTO atAvatarGroup);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtAvatarGroupVO getById(Integer id);

    List<AtAvatarGroupVO> getByIds(List<Integer> ids);

    Map<Integer, String> getMapByIds(List<Integer> ids);

    /**
     * 保存
     * @param atAvatarGroup
     * @return
     */
    boolean save(AtAvatarGroupDTO atAvatarGroup);
    /**
     * 根据id修改
     * @param atAvatarGroup
     * @return
     */
    boolean updateById(AtAvatarGroupDTO atAvatarGroup);
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
     *
     * 模糊检索根据分组名称
     * @param searchWord
     * @return
     */
    List<AtAvatarGroupEntity> queryByFuzzyName(String searchWord, Long sysUserId);
}

