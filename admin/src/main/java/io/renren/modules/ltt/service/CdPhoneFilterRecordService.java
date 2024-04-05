package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdPhoneFilterRecordDTO;
import io.renren.modules.ltt.vo.CdPhoneFilterRecordVO;
import io.renren.modules.ltt.entity.CdPhoneFilterRecordEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 手机号筛选
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-04 16:29:58
 */
public interface CdPhoneFilterRecordService extends IService<CdPhoneFilterRecordEntity> {

    /**
     * 分页查询
     * @param cdPhoneFilterRecord
     * @return
     */
    PageUtils queryPage(CdPhoneFilterRecordDTO cdPhoneFilterRecord);
    /**
     * 根据id查询
     * @param recordId
     * @return
     */
    CdPhoneFilterRecordVO getById(Integer recordId);

    List<CdPhoneFilterRecordVO> getByIds(List<Integer> recordIdList);

    /**
     * 保存
     * @param cdPhoneFilterRecord
     * @return
     */
    boolean save(CdPhoneFilterRecordDTO cdPhoneFilterRecord);
    /**
     * 根据id修改
     * @param cdPhoneFilterRecord
     * @return
     */
    boolean updateById(CdPhoneFilterRecordDTO cdPhoneFilterRecord);
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

