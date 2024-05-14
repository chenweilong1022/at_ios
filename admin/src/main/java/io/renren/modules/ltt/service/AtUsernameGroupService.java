package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUsernameGroupDTO;
import io.renren.modules.ltt.vo.AtUsernameGroupVO;
import io.renren.modules.ltt.entity.AtUsernameGroupEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;


/**
 * 昵称分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 15:19:44
 */
public interface AtUsernameGroupService extends IService<AtUsernameGroupEntity> {

    /**
     * 分页查询
     * @param atUsernameGroup
     * @return
     */
    PageUtils queryPage(AtUsernameGroupDTO atUsernameGroup);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUsernameGroupVO getById(Integer id);
    /**
     * 保存
     * @param atUsernameGroup
     * @return
     */
    boolean save(AtUsernameGroupDTO atUsernameGroup);
    /**
     * 根据id修改
     * @param atUsernameGroup
     * @return
     */
    boolean updateById(AtUsernameGroupDTO atUsernameGroup);

    /**
     * 批量更改昵称
      * @param atUsernameGroup
     * @return
     */
    boolean updateBatchAtUsername(AtUsernameGroupDTO atUsernameGroup);

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

