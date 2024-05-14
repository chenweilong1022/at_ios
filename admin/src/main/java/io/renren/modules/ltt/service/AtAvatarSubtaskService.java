package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtAvatarSubtaskDTO;
import io.renren.modules.ltt.vo.AtAvatarSubtaskVO;
import io.renren.modules.ltt.entity.AtAvatarSubtaskEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 头像任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-11 14:32:25
 */
public interface AtAvatarSubtaskService extends IService<AtAvatarSubtaskEntity> {

    /**
     * 分页查询
     * @param atAvatarSubtask
     * @return
     */
    PageUtils queryPage(AtAvatarSubtaskDTO atAvatarSubtask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtAvatarSubtaskVO getById(Integer id);
    /**
     * 保存
     * @param atAvatarSubtask
     * @return
     */
    boolean save(AtAvatarSubtaskDTO atAvatarSubtask);
    /**
     * 根据id修改
     * @param atAvatarSubtask
     * @return
     */
    boolean updateById(AtAvatarSubtaskDTO atAvatarSubtask);
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

