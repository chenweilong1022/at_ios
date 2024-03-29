package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.vo.AccountBalanceVO;
import io.renren.modules.sys.service.SysUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AccountDetailsDao;
import io.renren.modules.ltt.entity.AccountDetailsEntity;
import io.renren.modules.ltt.dto.AccountDetailsDTO;
import io.renren.modules.ltt.vo.AccountDetailsVO;
import io.renren.modules.ltt.service.AccountDetailsService;
import io.renren.modules.ltt.conver.AccountDetailsConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("accountDetailsService")
@Game
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsDao, AccountDetailsEntity> implements AccountDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Override
    public PageUtils<AccountDetailsVO> queryPage(AccountDetailsDTO param) {
        IPage<AccountDetailsEntity> page = baseMapper.selectPage(
                new Query<AccountDetailsEntity>(param).getPage(),
                new QueryWrapper<AccountDetailsEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(param.getSysUserId()),  AccountDetailsEntity::getSysUserId, param.getSysUserId())
                        .eq(ObjectUtil.isNotNull(param.getAccountId()),  AccountDetailsEntity::getAccountId, param.getAccountId())
                        .eq(ObjectUtil.isNotNull(param.getTransactionType()),  AccountDetailsEntity::getTransactionType, param.getTransactionType())
                        .eq(ObjectUtil.isNotNull(param.getStatus()),  AccountDetailsEntity::getStatus, param.getStatus())
                        .orderByDesc(AccountDetailsEntity::getTransactionDate)
        );

        List<AccountDetailsVO> resultList = AccountDetailsConver.MAPPER.conver(page.getRecords());

        if (CollectionUtil.isNotEmpty(resultList)) {
            List<Long> sysUserIdList = resultList.stream().map(AccountDetailsVO::getSysUserId).collect(Collectors.toList());
            //查询username
            Map<Long, String> usernameMap = sysUserService.queryUserNameByUserIdList(sysUserIdList);
            resultList.forEach(i -> i.setSysUsername(usernameMap.get(i.getSysUserId())));
        }

        return PageUtils.<AccountDetailsVO>page(page).setList(resultList);
    }
    @Override
    public AccountDetailsVO getById(Integer transactionId) {
        return AccountDetailsConver.MAPPER.conver(baseMapper.selectById(transactionId));
    }

    @Override
    public boolean save(AccountDetailsDTO accountDetails) {
        AccountDetailsEntity accountDetailsEntity = AccountDetailsConver.MAPPER.converDTO(accountDetails);
        return this.save(accountDetailsEntity);
    }

    @Override
    public boolean updateById(AccountDetailsDTO accountDetails) {
        AccountDetailsEntity accountDetailsEntity = AccountDetailsConver.MAPPER.converDTO(accountDetails);
        return this.updateById(accountDetailsEntity);
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
