package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtGroupTaskDTO;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.entity.AtGroupTaskEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 拉群任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
public interface AtGroupTaskService extends IService<AtGroupTaskEntity> {

    /**
     * 分页查询
     * @param atGroupTask
     * @return
     */
    PageUtils queryPage(AtGroupTaskDTO atGroupTask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtGroupTaskVO getById(Integer id);
    /**
     * 保存
     * @param atGroupTask
     * @return
     */
    boolean save(AtGroupTaskDTO atGroupTask);
    /**
     * 根据id修改
     * @param atGroupTask
     * @return
     */
    boolean updateById(AtGroupTaskDTO atGroupTask);
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
     * 获取群名称列表
     * @param atGroupTask
     * @return
     */
    List<String> getGroupNameList(AtGroupTaskDTO atGroupTask);

    /**
     * 群预览
     * @param atGroupTask
     */
    void onGroupPre(AtGroupTaskDTO atGroupTask);
}

