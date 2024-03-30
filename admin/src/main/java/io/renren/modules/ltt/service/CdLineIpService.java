package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdLineIpDTO;
import io.renren.modules.ltt.vo.CdLineIpVO;
import io.renren.modules.ltt.entity.CdLineIpEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-30 21:39:27
 */
public interface CdLineIpService extends IService<CdLineIpEntity> {

    /**
     * 分页查询
     * @param cdLineIp
     * @return
     */
    PageUtils queryPage(CdLineIpDTO cdLineIp);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdLineIpVO getById(Integer id);
    /**
     * 保存
     * @param cdLineIp
     * @return
     */
    boolean save(CdLineIpDTO cdLineIp);
    /**
     * 根据id修改
     * @param cdLineIp
     * @return
     */
    boolean updateById(CdLineIpDTO cdLineIp);
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

