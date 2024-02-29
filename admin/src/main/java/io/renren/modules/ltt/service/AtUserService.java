package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.entity.AtUserEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 账号数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:44:22
 */
public interface AtUserService extends IService<AtUserEntity> {

    /**
     * 分页查询
     * @param atUser
     * @return
     */
    PageUtils queryPage(AtUserDTO atUser);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserVO getById(Integer id);
    /**
     * 保存
     * @param atUser
     * @return
     */
    boolean save(AtUserDTO atUser);
    /**
     * 根据id修改
     * @param atUser
     * @return
     */
    boolean updateById(AtUserDTO atUser);
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

