package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUsernameTaskDTO;
import io.renren.modules.ltt.vo.AtUsernameTaskVO;
import io.renren.modules.ltt.entity.AtUsernameTaskEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 昵称任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-20 21:46:41
 */
public interface AtUsernameTaskService extends IService<AtUsernameTaskEntity> {

    /**
     * 分页查询
     * @param atUsernameTask
     * @return
     */
    PageUtils queryPage(AtUsernameTaskDTO atUsernameTask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUsernameTaskVO getById(Integer id);
    /**
     * 保存
     * @param atUsernameTask
     * @return
     */
    boolean save(AtUsernameTaskDTO atUsernameTask);
    /**
     * 根据id修改
     * @param atUsernameTask
     * @return
     */
    boolean updateById(AtUsernameTaskDTO atUsernameTask);
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

