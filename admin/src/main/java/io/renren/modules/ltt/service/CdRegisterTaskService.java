package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdRegisterTaskDTO;
import io.renren.modules.ltt.vo.CdRegisterTaskVO;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
public interface CdRegisterTaskService extends IService<CdRegisterTaskEntity> {

    /**
     * 分页查询
     * @param cdRegisterTask
     * @return
     */
    PageUtils queryPage(CdRegisterTaskDTO cdRegisterTask);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdRegisterTaskVO getById(Integer id);
    /**
     * 保存
     * @param cdRegisterTask
     * @return
     */
    boolean save(CdRegisterTaskDTO cdRegisterTask);

    /**
     * 创建注册任务
     * @param registerCount 注册数量
     * @param countryCode 国家code
     */
    void createRegisterTask(Integer registerCount, Integer countryCode);

    boolean stopRegisterTask(Integer taskId);

    /**
     * 删除注册任务
     * @param taskId
     * @return
     */
    boolean deleteRegisterTask(Integer taskId);

    /**
     * 根据id修改
     * @param cdRegisterTask
     * @return
     */
    boolean updateById(CdRegisterTaskDTO cdRegisterTask);
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
     * 统计根据任务id
     * @param id
     * @return
     */
    Integer sumByTaskId(Integer id);

    /**
     * 查询真机注册数据
     * @return
     */
    CdRegisterTaskEntity queryRealMachineRegister();
}

