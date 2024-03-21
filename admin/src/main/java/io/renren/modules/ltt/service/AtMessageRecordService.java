package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtMessageRecordDTO;
import io.renren.modules.ltt.vo.AtMessageRecordVO;
import io.renren.modules.ltt.entity.AtMessageRecordEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 消息记录
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-21 14:41:06
 */
public interface AtMessageRecordService extends IService<AtMessageRecordEntity> {

    /**
     * 分页查询
     * @param atMessageRecord
     * @return
     */
    PageUtils queryPage(AtMessageRecordDTO atMessageRecord);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtMessageRecordVO getById(Integer id);
    /**
     * 保存
     * @param atMessageRecord
     * @return
     */
    boolean save(AtMessageRecordDTO atMessageRecord);
    /**
     * 根据id修改
     * @param atMessageRecord
     * @return
     */
    boolean updateById(AtMessageRecordDTO atMessageRecord);
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

