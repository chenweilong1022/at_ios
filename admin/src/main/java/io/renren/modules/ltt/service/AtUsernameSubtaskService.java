package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUsernameSubtaskDTO;
import io.renren.modules.ltt.vo.AtUsernameSubtaskVO;
import io.renren.modules.ltt.entity.AtUsernameSubtaskEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 昵称任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-20 21:46:41
 */
public interface AtUsernameSubtaskService extends IService<AtUsernameSubtaskEntity> {

    /**
     * 分页查询
     * @param atUsernameSubtask
     * @return
     */
    PageUtils queryPage(AtUsernameSubtaskDTO atUsernameSubtask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUsernameSubtaskVO getById(Integer id);
    /**
     * 保存
     * @param atUsernameSubtask
     * @return
     */
    boolean save(AtUsernameSubtaskDTO atUsernameSubtask);
    /**
     * 根据id修改
     * @param atUsernameSubtask
     * @return
     */
    boolean updateById(AtUsernameSubtaskDTO atUsernameSubtask);
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

