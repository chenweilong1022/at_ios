package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdPhoneFilterDTO;
import io.renren.modules.ltt.dto.CdPhoneFilterStatusDto;
import io.renren.modules.ltt.vo.CdPhoneFilterVO;
import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
import java.io.Serializable;
import java.util.Collection;


/**
 * 手机号筛选
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-25 00:55:11
 */
public interface CdPhoneFilterService extends IService<CdPhoneFilterEntity> {

    /**
     * 分页查询
     * @param cdPhoneFilter
     * @return
     */
    PageUtils queryPage(CdPhoneFilterDTO cdPhoneFilter);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdPhoneFilterVO getById(Integer id);
    /**
     * 保存
     * @param cdPhoneFilter
     * @return
     */
    boolean save(CdPhoneFilterDTO cdPhoneFilter);
    /**
     * 根据id修改
     * @param cdPhoneFilter
     * @return
     */
    boolean updateById(CdPhoneFilterDTO cdPhoneFilter);
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

    byte[] exportSJ(Long recordId);

    CdPhoneFilterStatusDto queryByTaskStatus(Integer recordId);

}

