package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtUserTokenDTO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.entity.AtUserTokenEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 用户token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 02:03:03
 */
public interface AtUserTokenService extends IService<AtUserTokenEntity> {

    /**
     * 分页查询
     * @param atUserToken
     * @return
     */
    PageUtils queryPage(AtUserTokenDTO atUserToken);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserTokenVO getById(Integer id);
    /**
     * 保存
     * @param atUserToken
     * @return
     */
    boolean save(AtUserTokenDTO atUserToken);

    boolean saveUserTokenBatch(List<AtUserTokenEntity> userTokenEntityList);

    /**
     * 根据id修改
     * @param atUserToken
     * @return
     */
    boolean updateById(AtUserTokenDTO atUserToken);
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

    List<AtUserTokenEntity> selectBatchIds(List<Integer> ids);

    Map<Integer, AtUserTokenEntity> queryMapBatchIds(List<Integer> ids);
}

