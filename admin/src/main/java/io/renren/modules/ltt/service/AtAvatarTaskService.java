package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtAvatarTaskDTO;
import io.renren.modules.ltt.vo.AtAvatarTaskVO;
import io.renren.modules.ltt.entity.AtAvatarTaskEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 头像任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-11 14:32:25
 */
public interface AtAvatarTaskService extends IService<AtAvatarTaskEntity> {

    /**
     * 分页查询
     * @param atAvatarTask
     * @return
     */
    PageUtils queryPage(AtAvatarTaskDTO atAvatarTask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtAvatarTaskVO getById(Integer id);
    /**
     * 保存
     * @param atAvatarTask
     * @return
     */
    boolean save(AtAvatarTaskDTO atAvatarTask);
    /**
     * 根据id修改
     * @param atAvatarTask
     * @return
     */
    boolean updateById(AtAvatarTaskDTO atAvatarTask);
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
     * 对错误进行重试
     * @param list
     */
    void errRetry(List<Integer> list);
}

