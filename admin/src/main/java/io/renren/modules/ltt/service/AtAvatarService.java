package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtAvatarDTO;
import io.renren.modules.ltt.vo.AtAvatarVO;
import io.renren.modules.ltt.entity.AtAvatarEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 头像
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-29 17:21:50
 */
public interface AtAvatarService extends IService<AtAvatarEntity> {

    /**
     * 分页查询
     * @param atAvatar
     * @return
     */
    PageUtils queryPage(AtAvatarDTO atAvatar);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtAvatarVO getById(Integer id);
    /**
     * 保存
     * @param atAvatar
     * @return
     */
    boolean save(AtAvatarDTO atAvatar);
    /**
     * 根据id修改
     * @param atAvatar
     * @return
     */
    boolean updateById(AtAvatarDTO atAvatar);
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

