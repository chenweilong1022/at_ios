package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.LttUserDTO;
import io.renren.modules.ltt.vo.LttUserVO;
import io.renren.modules.ltt.entity.LttUserEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 用户表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2022-08-02 14:05:38
 */
public interface LttUserService extends IService<LttUserEntity> {

    /**
     * 分页查询
     * @param lttUser
     * @return
     */
    PageUtils queryPage(LttUserDTO lttUser);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    LttUserVO getById(Long id);
    /**
     * 保存
     * @param lttUser
     * @return
     */
    boolean save(LttUserDTO lttUser);
    /**
     * 根据id修改
     * @param lttUser
     * @return
     */
    boolean updateById(LttUserDTO lttUser);
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

