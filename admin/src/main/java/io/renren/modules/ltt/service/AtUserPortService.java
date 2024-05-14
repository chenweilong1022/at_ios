package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUserPortDTO;
import io.renren.modules.ltt.dto.PortDataSummaryResultDto;
import io.renren.modules.ltt.vo.AtUserPortVO;
import io.renren.modules.ltt.entity.AtUserPortEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 账户端口管理
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 20:55:14
 */
public interface AtUserPortService extends IService<AtUserPortEntity> {

    /**
     * 分页查询
     * @param atUserPort
     * @return
     */
    PageUtils queryPage(AtUserPortDTO atUserPort);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserPortVO getById(Integer id);
    /**
     * 保存
     * @param atUserPort
     * @return
     */
    boolean save(AtUserPortDTO atUserPort);
    /**
     * 根据id修改
     * @param atUserPort
     * @return
     */
    boolean updateById(AtUserPortDTO atUserPort);
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
     * 查询用户未过期端口
     *
     * @return
     */
    List<AtUserPortEntity> queryValidPort(Long sysUserId);

    /**
     * 端口数据汇总
     * @param sysUserId
     * @return
     */
    PortDataSummaryResultDto portDataSummary(Long sysUserId);
}

