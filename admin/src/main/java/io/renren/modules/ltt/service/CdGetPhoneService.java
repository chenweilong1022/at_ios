package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.dto.CdRegisterRedisDto;
import io.renren.modules.ltt.vo.CdGetPhoneVO;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:12
 */
public interface CdGetPhoneService extends IService<CdGetPhoneEntity> {

    /**
     * 分页查询
     * @param cdGetPhone
     * @return
     */
    PageUtils queryPage(CdGetPhoneDTO cdGetPhone);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CdGetPhoneVO getById(Integer id);

    CdGetPhoneEntity queryById(Integer id);

    List<CdGetPhoneEntity> getByIds(List<Integer> ids);

    /**
     * 保存
     * @param cdGetPhone
     * @return
     */
    boolean save(CdGetPhoneDTO cdGetPhone);
    /**
     * 根据id修改
     * @param cdGetPhone
     * @return
     */
    boolean updateById(CdGetPhoneDTO cdGetPhone);
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

    List<CdGetPhoneEntity> addCount(CdGetPhoneDTO cdGetPhone);

    /**
     * 待注册的手机号存redis
     *
     * @param phoneEntityList
     */
    void saveWaitRegisterPhone(List<CdGetPhoneEntity> phoneEntityList);

    Integer getPhoneRegisterCount(String phone);

    Boolean getPhoneUseState(String phone);

    Integer savePhoneRegisterCount(String phone);
}

