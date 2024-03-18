package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtDataSubtaskDTO;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 加粉任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 10:53:33
 */
public interface AtDataSubtaskService extends IService<AtDataSubtaskEntity> {

    /**
     * 分页查询
     * @param atDataSubtask
     * @return
     */
    PageUtils queryPage(AtDataSubtaskDTO atDataSubtask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtDataSubtaskVO getById(Integer id);
    /**
     * 保存
     * @param atDataSubtask
     * @return
     */
    boolean save(AtDataSubtaskDTO atDataSubtask);
    /**
     * 根据id修改
     * @param atDataSubtask
     * @return
     */
    boolean updateById(AtDataSubtaskDTO atDataSubtask);
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

