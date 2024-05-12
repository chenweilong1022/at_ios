package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdIpConfigDTO;
import io.renren.modules.ltt.vo.CdIpConfigVO;
import io.renren.modules.ltt.entity.CdIpConfigEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-26 12:10:55
 */
public interface CdIpConfigService extends IService<CdIpConfigEntity> {

    /**
     * 分页查询
     * @param cdIpConfig
     * @return
     */
    PageUtils queryPage(CdIpConfigDTO cdIpConfig);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdIpConfigVO getById(Integer id);
    /**
     * 保存
     * @param cdIpConfig
     * @return
     */
    boolean save(CdIpConfigDTO cdIpConfig);
    /**
     * 根据id修改
     * @param cdIpConfig
     * @return
     */
    boolean updateById(CdIpConfigDTO cdIpConfig);

    /**
     * 累加ip使用次数
     * @param id
     * @return
     */
    boolean updateUsedCountById(Integer id);

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

    CdIpConfigEntity getIpConfig(Integer countryCode);

    /**
     * 清空ip
     * @param cdIpConfig
     */
    boolean clear(CdIpConfigDTO cdIpConfig);
}

