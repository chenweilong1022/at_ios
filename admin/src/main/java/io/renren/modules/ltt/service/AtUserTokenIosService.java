package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUserTokenIosDTO;
import io.renren.modules.ltt.dto.IosTokenDTO;
import io.renren.modules.ltt.vo.AtUserTokenIosVO;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 用户ios token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 00:16:44
 */
public interface AtUserTokenIosService extends IService<AtUserTokenIosEntity> {

    /**
     * 分页查询
     * @param atUserTokenIos
     * @return
     */
    PageUtils queryPage(AtUserTokenIosDTO atUserTokenIos);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserTokenIosVO getById(Integer id);
    /**
     * 保存
     * @param atUserTokenIos
     * @return
     */
    boolean save(AtUserTokenIosDTO atUserTokenIos);
    /**
     * 根据id修改
     * @param atUserTokenIos
     * @return
     */
    boolean updateById(AtUserTokenIosDTO atUserTokenIos);
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
     * 同步token
     * @param map
     */
    void syncAppToken(IosTokenDTO map);

    /**
     * 找手机
     * @param atUserTokenIos
     */
    void taskIosFind(Integer[] ids);
}

