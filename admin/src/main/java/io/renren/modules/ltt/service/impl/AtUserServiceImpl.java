package io.renren.modules.ltt.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dao.UpdateAtUserCustomerParamDto;
import io.renren.modules.ltt.dao.UpdateUserGroupParamDto;
import io.renren.modules.ltt.dao.ValidateAtUserStatusParamDto;
import io.renren.modules.ltt.enums.DeleteFlag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserDao;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.conver.AtUserConver;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.renren.modules.ltt.enums.UserStatus.UserStatus1;


@Service("atUserService")
@Game
@Slf4j
public class AtUserServiceImpl extends ServiceImpl<AtUserDao, AtUserEntity> implements AtUserService {

    @Override
    public PageUtils<AtUserVO> queryPage(AtUserDTO atUser) {
        //todo bug 没有区分账户userId
        IPage<AtUserEntity> page = baseMapper.selectPage(
                new Query<AtUserEntity>(atUser).getPage(),
                new QueryWrapper<AtUserEntity>().lambda()
                        .eq(StringUtils.isNotEmpty(atUser.getNickName()), AtUserEntity::getNickName, atUser.getNickName())
                        .eq(StringUtils.isNotEmpty(atUser.getNation()), AtUserEntity::getNation, atUser.getNation())
                        .eq(StringUtils.isNotEmpty(atUser.getTelephone()), AtUserEntity::getTelephone, atUser.getTelephone())
                        .eq(atUser.getUserGroupId() != null, AtUserEntity::getUserGroupId, atUser.getUserGroupId())
                        //有客服id，不为0则查询：客服id
                        .eq(atUser.getCustomerServiceId() != null && atUser.getCustomerServiceId() != 0, AtUserEntity::getCustomerServiceId, atUser.getCustomerServiceId())
                        //有客服id，为0则查询：没有客服的用户列表
                        .isNull(atUser.getCustomerServiceId() != null && atUser.getCustomerServiceId() == 0, AtUserEntity::getCustomerServiceId)
                        .eq(atUser.getStatus() != null, AtUserEntity::getStatus, atUser.getStatus())
                        .notIn(atUser.getValidateFlag() != null && Boolean.TRUE.equals(atUser.getValidateFlag()), AtUserEntity::getStatus, UserStatus1.getKey())
                        .eq(atUser.getValidateFlag() != null && Boolean.FALSE.equals(atUser.getValidateFlag()), AtUserEntity::getStatus, UserStatus1.getKey())
                        .orderByDesc(AtUserEntity::getId)
        );

        return PageUtils.<AtUserVO>page(page).setList(AtUserConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public AtUserVO getById(Integer id) {
        return AtUserConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserDTO atUser) {
        AtUserEntity atUserEntity = AtUserConver.MAPPER.converDTO(atUser);
        return this.save(atUserEntity);
    }

    @Override
    public boolean updateById(AtUserDTO atUser) {
        AtUserEntity atUserEntity = AtUserConver.MAPPER.converDTO(atUser);
        return this.updateById(atUserEntity);
    }


    @Override
    public boolean updateUserGroup(UpdateUserGroupParamDto paramDto) {
        Assert.isTrue(ObjectUtils.isEmpty(paramDto), "数据不能为空");

        List<Integer> ids = paramDto.getIds();
        Integer userGroupId = paramDto.getUserGroupId();
        Assert.isTrue(CollectionUtils.isEmpty(ids), "选择的数据不能为空");
        Assert.isTrue(userGroupId == null, "选择的分组不能为空");

        List<AtUserEntity> updateList = new ArrayList<>(ids.size());
        AtUserEntity atUserEntity = null;
        for (Integer id : ids) {
            atUserEntity = new AtUserEntity();
            atUserEntity.setUserGroupId(userGroupId);
            atUserEntity.setUserGroupName(paramDto.getUserGroupName());
            atUserEntity.setId(id);
            updateList.add(atUserEntity);
        }

        return this.updateBatchById(updateList);
    }

    @Override
    public boolean updateUserCustomer(UpdateAtUserCustomerParamDto paramDto) {
        Assert.isTrue(ObjectUtils.isEmpty(paramDto), "数据不能为空");

        Assert.isTrue(CollectionUtils.isEmpty(paramDto.getIds()), "选择的数据不能为空");
        Assert.isTrue(paramDto.getCustomerServiceId() == null, "分配客服不能为空");

        if (paramDto.getCustomerServiceId() == 0) {
            paramDto.setCustomerServiceId(null);
            paramDto.setCustomerService(null);
        }

        Integer count = baseMapper.updateCustomerByIds(paramDto);
        return count > 0;
    }

    @Override
    public boolean validateUserStatus(ValidateAtUserStatusParamDto paramDto) {
        Assert.isTrue(ObjectUtils.isEmpty(paramDto), "数据不能为空");
        Assert.isTrue(paramDto.getValidateFlag() == null, "分配客服不能为空");

        if (Boolean.TRUE.equals(paramDto.getValidateFlag())) {
            //是否验活全部 true：全部
            AtUserEntity atUserEntity = new AtUserEntity();
            atUserEntity.setStatus(UserStatus1.getKey());

            return baseMapper.update(atUserEntity, new UpdateWrapper<AtUserEntity>().lambda()
                    .eq(AtUserEntity::getDeleteFlag, DeleteFlag.NO.getKey())) > 0;
        } else {
            Assert.isTrue(CollectionUtils.isEmpty(paramDto.getIds()), "选择的数据不能为空");
            List<AtUserEntity> updateList = new ArrayList<>(paramDto.getIds().size());
            AtUserEntity atUserEntity = null;
            for (Integer id : paramDto.getIds()) {
                atUserEntity = new AtUserEntity();
                atUserEntity.setStatus(UserStatus1.getKey());
                atUserEntity.setId(id);
                updateList.add(atUserEntity);
            }
            return this.updateBatchById(updateList);
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

}
