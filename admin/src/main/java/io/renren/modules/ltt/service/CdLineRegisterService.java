package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
public interface CdLineRegisterService extends IService<CdLineRegisterEntity> {

    /**
     * 分页查询
     *
     * @param cdLineRegister
     * @return
     */
    PageUtils queryPage(CdLineRegisterDTO cdLineRegister);

    /**
     * 根据taskid查询
     *
     * @param cdLineRegister
     * @return
     */
    PageUtils listByTaskId(CdLineRegisterDTO cdLineRegister);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    CdLineRegisterVO getById(Integer id);

    /**
     * 保存
     *
     * @param cdLineRegister
     * @return
     */
    boolean save(CdLineRegisterDTO cdLineRegister);

    /**
     * 根据id修改
     *
     * @param cdLineRegister
     * @return
     */
    boolean updateById(CdLineRegisterDTO cdLineRegister);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 根据id批量删除
     *
     * @param ids
     * @return
     */
    @Override
    boolean removeByIds(Collection<? extends Serializable> ids);

    /**
     * 获取子任务
     *
     * @param registerSubtasksIds
     * @return
     */
    List<GetCountBySubTaskIdVO> getCountBySubTaskId(List<Integer> registerSubtasksIds);

    /**
     * 根据注册状态查询总数
     *
     * @param registerStatus
     * @return
     */
    Integer getCountByRegisterStatus(Integer registerStatus, String countryCode);

    /**
     * 根据注册状态查询列表
     *
     * @param registerStatus
     * @param limit
     * @return
     */
    List<CdLineRegisterVO> getListByRegisterStatus(Integer registerStatus, String countryCode, Integer limit);

    /**
     * 注册重试
     * @param id
     * @return
     */
    boolean registerRetry(Integer id);

    /**
     * 注册重试
     * @param id
     * @return
     */
    boolean registerRetry(String telephone);

    /**
     * 查询line注册数量
     * @param countryCode
     * @return
     */
    Integer queryLineRegisterCount(String countryCode);

    /**
     * 注册数据汇总
     * @return
     */
    List<LineRegisterSummaryResultDto> queryLineRegisterSummary(LocalDate searchTime);
}

