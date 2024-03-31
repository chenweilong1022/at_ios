package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdLineUsernamePassDTO;
import io.renren.modules.ltt.vo.CdLineUsernamePassVO;
import io.renren.modules.ltt.entity.CdLineUsernamePassEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 02:37:15
 */
public interface CdLineUsernamePassService extends IService<CdLineUsernamePassEntity> {

    /**
     * 分页查询
     * @param cdLineUsernamePass
     * @return
     */
    PageUtils queryPage(CdLineUsernamePassDTO cdLineUsernamePass);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdLineUsernamePassVO getById(Integer id);
    /**
     * 保存
     * @param cdLineUsernamePass
     * @return
     */
    boolean save(CdLineUsernamePassDTO cdLineUsernamePass);
    /**
     * 根据id修改
     * @param cdLineUsernamePass
     * @return
     */
    boolean updateById(CdLineUsernamePassDTO cdLineUsernamePass);
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

