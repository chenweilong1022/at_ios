package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUsernameDTO;
import io.renren.modules.ltt.vo.AtUsernameGroupUsernameCountGroupIdVO;
import io.renren.modules.ltt.vo.AtUsernameVO;
import io.renren.modules.ltt.entity.AtUsernameEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 昵称
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:56:04
 */
public interface AtUsernameService extends IService<AtUsernameEntity> {

    /**
     * 分页查询
     * @param atUsername
     * @return
     */
    PageUtils queryPage(AtUsernameDTO atUsername);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUsernameVO getById(Integer id);
    /**
     * 保存
     * @param atUsername
     * @return
     */
    boolean save(AtUsernameDTO atUsername);
    /**
     * 根据id修改
     * @param atUsername
     * @return
     */
    boolean updateById(AtUsernameDTO atUsername);
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
     * 根据id分组用户名称
     * @return
     */
    List<AtUsernameGroupUsernameCountGroupIdVO> usernameCountGroupId(List<Integer> groupIdList);


}

