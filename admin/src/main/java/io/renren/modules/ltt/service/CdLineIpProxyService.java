package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import io.renren.modules.ltt.entity.CdLineIpProxyEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 17:49:13
 */
public interface CdLineIpProxyService extends IService<CdLineIpProxyEntity> {

    /**
     * 分页查询
     * @param cdLineIpProxy
     * @return
     */
    PageUtils queryPage(CdLineIpProxyDTO cdLineIpProxy);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdLineIpProxyVO getById(Integer id);
    /**
     * 保存
     * @param cdLineIpProxy
     * @return
     */
    boolean save(CdLineIpProxyDTO cdLineIpProxy);
    /**
     * 根据id修改
     * @param cdLineIpProxy
     * @return
     */
    boolean updateById(CdLineIpProxyDTO cdLineIpProxy);
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

    String getProxyIp(CdLineIpProxyDTO cdLineIpProxyDTO);
}

