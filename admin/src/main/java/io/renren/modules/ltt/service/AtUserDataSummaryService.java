package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUserDataSummaryDTO;
import io.renren.modules.ltt.vo.AtUserDataSummaryVO;
import io.renren.modules.ltt.entity.AtUserDataSummaryEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;


/**
 * 数据汇总-定时更新
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-05-06 13:15:52
 */
public interface AtUserDataSummaryService extends IService<AtUserDataSummaryEntity> {

    /**
     * 分页查询
     * @param atUserDataSummary
     * @return
     */
    PageUtils queryPage(AtUserDataSummaryDTO atUserDataSummary);

    void saveAtUserDataSummary(LocalDate searchTime);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserDataSummaryVO getById(Integer id);
    /**
     * 保存
     * @param atUserDataSummary
     * @return
     */
    boolean save(AtUserDataSummaryDTO atUserDataSummary);
    /**
     * 根据id修改
     * @param atUserDataSummary
     * @return
     */
    boolean updateById(AtUserDataSummaryDTO atUserDataSummary);
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

