package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.modules.ltt.enums.AccountTransactionTypeEnum;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.AccountDetailsEntity;
import io.renren.modules.ltt.service.AccountDetailsService;
import io.renren.modules.sys.service.SysUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AccountBalanceDao;
import io.renren.modules.ltt.entity.AccountBalanceEntity;
import io.renren.modules.ltt.dto.AccountBalanceDTO;
import io.renren.modules.ltt.vo.AccountBalanceVO;
import io.renren.modules.ltt.service.AccountBalanceService;
import io.renren.modules.ltt.conver.AccountBalanceConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.renren.modules.ltt.enums.AccountTransactionTypeEnum.RECHARGE;


@Service("accountBalanceService")
@Game
public class AccountBalanceServiceImpl extends ServiceImpl<AccountBalanceDao, AccountBalanceEntity> implements AccountBalanceService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private AccountDetailsService accountDetailsService;

    @Override
    public PageUtils<AccountBalanceVO> queryPage(AccountBalanceDTO accountBalance) {
        IPage<AccountBalanceEntity> page = baseMapper.selectPage(
                new Query<AccountBalanceEntity>(accountBalance).getPage(),
                new QueryWrapper<AccountBalanceEntity>().lambda()
                        .orderByDesc(AccountBalanceEntity::getAccountId)
        );
        List<AccountBalanceVO> resultList = AccountBalanceConver.MAPPER.conver(page.getRecords());
        if (CollectionUtil.isNotEmpty(resultList)) {
            List<Long> sysUserIdList = resultList.stream().map(AccountBalanceVO::getSysUserId).collect(Collectors.toList());
            //查询username
            Map<Long, String> usernameMap = sysUserService.queryUserNameByUserIdList(sysUserIdList);
            resultList.forEach(i -> i.setSysUsername(usernameMap.get(i.getSysUserId())));
        }
        return PageUtils.<AccountBalanceVO>page(page).setList(resultList);
    }

    @Override
    public AccountBalanceVO getById(Integer accountId) {
        return AccountBalanceConver.MAPPER.conver(baseMapper.selectById(accountId));
    }

    @Override
    public AccountBalanceVO getBySysUserId(Long sysUserId) {
        return AccountBalanceConver.MAPPER.conver(baseMapper
                .selectOne(new QueryWrapper<AccountBalanceEntity>().lambda()
                        .eq(AccountBalanceEntity::getSysUserId, sysUserId)));
    }

    /**
     * 用户账号信息保存
     */
    private AccountBalanceEntity saveAccountBalance(AccountBalanceDTO accountParam,
                                                    AccountBalanceVO accountInfo) {
        AccountBalanceEntity accountBalanceEntity;

        //查询账户信息
        if (accountInfo == null) {
            //创建一个新的账号
            accountBalanceEntity = AccountBalanceConver.MAPPER.converDTO(accountParam);
            accountBalanceEntity.setBalance(accountParam.getAmount());
            accountBalanceEntity.setUpdateTime(new Date());
            accountBalanceEntity.setCreateTime(new Date());
            this.save(accountBalanceEntity);
        } else {
            accountBalanceEntity = new AccountBalanceEntity();
            accountBalanceEntity.setUpdateTime(new Date());
            accountBalanceEntity.setBalance(accountInfo.getBalance().add(accountParam.getAmount()));
            accountBalanceEntity.setAccountId(accountInfo.getAccountId());
            this.updateById(accountBalanceEntity);
        }
        return accountBalanceEntity;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeAccount(AccountBalanceDTO accountParam) {
        Assert.isTrue(ObjectUtil.isNull(accountParam.getSysUserId()), "用户信息不能为空");
        Assert.isTrue(ObjectUtil.isNull(accountParam.getAmount()), "金额不能为空");
        Assert.isTrue(ObjectUtil.isNull(accountParam.getTransactionType()), "类型不能为空");
        AccountTransactionTypeEnum typeEnum = AccountTransactionTypeEnum.checkType(accountParam.getTransactionType());
        Assert.isTrue(ObjectUtil.isNull(typeEnum), "类型不能为空");

        //查询用户账号信息
        AccountBalanceVO accountInfo = getBySysUserId(accountParam.getSysUserId());

        //交易类型若不是充值，且无账号，报错！
        Assert.isTrue(ObjectUtil.isNull(accountInfo)
                && !RECHARGE.getKey().equals(accountParam.getTransactionType()), "请先充值！");

        //修改账户变动
        AccountBalanceEntity accountBalanceEntity = this.saveAccountBalance(accountParam, accountInfo);

        //插入账户流水
        this.saveAccountDetail(accountParam, accountBalanceEntity);

        return true;
    }

    /**
     * 保存账户流水
     */
    private AccountDetailsEntity saveAccountDetail(AccountBalanceDTO accountParam,
                                                                         AccountBalanceEntity accountBalanceEntity) {
        //插入流水表
        AccountDetailsEntity DetailsEntity = new AccountDetailsEntity();
        DetailsEntity.setAccountId(accountBalanceEntity.getAccountId());
        DetailsEntity.setSysUserId(accountBalanceEntity.getSysUserId());
        DetailsEntity.setTransactionType(accountParam.getTransactionType());//交易类型
        DetailsEntity.setAmount(accountParam.getAmount());
        DetailsEntity.setDescription(accountParam.getDescription());
        DetailsEntity.setStatus(1);
        DetailsEntity.setTransactionDate(new Date());
        DetailsEntity.setOperationUserId(accountParam.getOperationUserId());
        accountDetailsService.save(DetailsEntity);
        return DetailsEntity;
    }

    @Override
    public boolean updateById(AccountBalanceDTO accountBalance) {
        AccountBalanceEntity accountBalanceEntity = AccountBalanceConver.MAPPER.converDTO(accountBalance);
        return this.updateById(accountBalanceEntity);
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
