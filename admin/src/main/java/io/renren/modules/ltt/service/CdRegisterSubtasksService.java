package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdRegisterSubtasksDTO;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
public interface CdRegisterSubtasksService extends IService<CdRegisterSubtasksEntity> {

    /**
     * 分页查询
     * @param cdRegisterSubtasks
     * @return
     */
    PageUtils queryPage(CdRegisterSubtasksDTO cdRegisterSubtasks);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdRegisterSubtasksVO getById(Integer id);
    /**
     * 保存
     * @param cdRegisterSubtasks
     * @return
     */
    boolean save(CdRegisterSubtasksDTO cdRegisterSubtasks);
    /**
     * 根据id修改
     * @param cdRegisterSubtasks
     * @return
     */
    boolean updateById(CdRegisterSubtasksDTO cdRegisterSubtasks);
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

