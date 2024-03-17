package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.vo.AtDataTaskVO;
import io.renren.modules.ltt.entity.AtDataTaskEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 加粉任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 18:52:43
 */
public interface AtDataTaskService extends IService<AtDataTaskEntity> {

    /**
     * 分页查询
     * @param atDataTask
     * @return
     */
    PageUtils queryPage(AtDataTaskDTO atDataTask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtDataTaskVO getById(Integer id);
    /**
     * 保存
     * @param atDataTask
     * @return
     */
    boolean save(AtDataTaskDTO atDataTask);
    /**
     * 根据id修改
     * @param atDataTask
     * @return
     */
    boolean updateById(AtDataTaskDTO atDataTask);
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
     * 启动任务
     * @param ids
     */
    void startUp(Collection<? extends Serializable> ids);
}

