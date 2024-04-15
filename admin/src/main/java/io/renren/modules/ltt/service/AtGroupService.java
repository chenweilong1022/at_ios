package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.client.dto.ImportZipDTO;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.entity.AtGroupEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
public interface AtGroupService extends IService<AtGroupEntity> {

    /**
     * 分页查询
     * @param atGroup
     * @return
     */
    PageUtils queryPage(AtGroupDTO atGroup);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtGroupVO getById(Integer id);
    /**
     * 保存
     * @param atGroup
     * @return
     */
    boolean save(AtGroupDTO atGroup);
    /**
     * 根据id修改
     * @param atGroup
     * @return
     */
    boolean updateById(AtGroupDTO atGroup);
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
     * 导出zip
     * @param importZipDTO
     */
    byte[] importZip(ImportZipDTO importZipDTO);

    /**
     * 重新分配token
     * @param atGroup
     */
    void reallocateToken(AtGroupDTO atGroup);

    /**
     * 启动任务
     * @param atGroup
     */
    void startTask(AtGroupDTO atGroup);
}

