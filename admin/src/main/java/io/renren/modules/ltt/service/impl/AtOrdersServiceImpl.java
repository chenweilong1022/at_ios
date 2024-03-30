package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.conver.AccountBalanceConver;
import io.renren.modules.ltt.dto.AccountBalanceDTO;
import io.renren.modules.ltt.dto.AtOrdersTokenParamDTO;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AccountBalanceVO;
import io.renren.modules.ltt.vo.ProductInfoVO;
import io.renren.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtOrdersDao;
import io.renren.modules.ltt.entity.AtOrdersEntity;
import io.renren.modules.ltt.dto.AtOrdersDTO;
import io.renren.modules.ltt.vo.AtOrdersVO;
import io.renren.modules.ltt.conver.AtOrdersConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("atOrdersService")
@Game
public class AtOrdersServiceImpl extends ServiceImpl<AtOrdersDao, AtOrdersEntity> implements AtOrdersService {

    @Resource
    private AccountBalanceService accountBalanceService;

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private CdLineRegisterService cdLineRegisterService;

    @Autowired
    private CdRegisterTaskService cdRegisterTaskService;

    @Resource
    private AtUserTokenService atUserTokenService;

    @Resource
    private SysUserService sysUserService;

    @Override
    public PageUtils<AtOrdersVO> queryPage(AtOrdersDTO atOrders) {
        IPage<AtOrdersEntity> page = baseMapper.selectPage(
                new Query<AtOrdersEntity>(atOrders).getPage(),
                new QueryWrapper<AtOrdersEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atOrders.getSysUserId()),AtOrdersEntity::getSysUserId,atOrders.getSysUserId() )
                        .eq(ObjectUtil.isNotNull(atOrders.getOrderStatus()),AtOrdersEntity::getOrderStatus,atOrders.getOrderStatus() )
                        .eq(ObjectUtil.isNotNull(atOrders.getProductType()),AtOrdersEntity::getProductType,atOrders.getProductType() )
                        .eq(ObjectUtil.isNotNull(atOrders.getCountryCode()),AtOrdersEntity::getCountryCode,atOrders.getCountryCode() )
                        .orderByDesc(AtOrdersEntity::getOrderTime)
        );

        List<AtOrdersVO> resultList = AtOrdersConver.MAPPER.conver(page.getRecords());
        if (CollectionUtil.isNotEmpty(resultList)) {
            List<Long> sysUserIdList = resultList.stream().map(AtOrdersVO::getSysUserId).collect(Collectors.toList());
            //查询username
            Map<Long, String> usernameMap = sysUserService.queryUserNameByUserIdList(sysUserIdList);
            resultList.forEach(i -> i.setSysUsername(usernameMap.get(i.getSysUserId())));
        }

        return PageUtils.<AtOrdersVO>page(page).setList(resultList);
    }

    @Override
    public AtOrdersVO getById(Long orderId) {
        return AtOrdersConver.MAPPER.conver(baseMapper.selectById(orderId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrderToken(AtOrdersTokenParamDTO atOrders) {
        //校验入参
        this.checkAtOrdersTokenParam(atOrders);

        //查询商品信息
        ProductInfoVO productInfoVO = productInfoService.getById(atOrders.getProductId());
        Assert.isTrue(ObjectUtil.isNull(productInfoVO), "商品不能为空");

        //订单金额
        BigDecimal totalAmount = productInfoVO.getPrice().multiply(BigDecimal.valueOf(atOrders.getOrderNumber()));

        //校验用户账户余额
        boolean accountBalanceFlag = accountBalanceService.judgeAccountBalance(atOrders.getSysUserId(), totalAmount);
        Assert.isTrue(Boolean.FALSE.equals(accountBalanceFlag), "账户余额不足，请先支付！");

        //校验注册手机号数量是否足够
        Integer registerCount = cdLineRegisterService
                .getCountByRegisterStatus(RegisterStatus.RegisterStatus4.getKey(), productInfoVO.getCountryCode());
        if (atOrders.getOrderNumber() > registerCount) {
            //数量不够，则创建注册任务
            cdRegisterTaskService.createRegisterTask(
                    atOrders.getOrderNumber() - registerCount,
                    CountryCode.getKeyByValue(productInfoVO.getCountryCode()));
        }


        //生成订单
        AtOrdersEntity atOrdersEntity = new AtOrdersEntity();
        atOrdersEntity.setSysUserId(atOrders.getSysUserId());
        atOrdersEntity.setOrderStatus(OrderStatus.OrderStatus1.getKey());
        atOrdersEntity.setTotalAmount(totalAmount);
        atOrdersEntity.setOrderTime(new Date());
        atOrdersEntity.setUpdateTime(new Date());
        atOrdersEntity.setProductId(productInfoVO.getProductId());
        atOrdersEntity.setProductType(productInfoVO.getProductType());
        atOrdersEntity.setCountryCode(productInfoVO.getCountryCode());
        atOrdersEntity.setProductName(productInfoVO.getProductName());
        atOrdersEntity.setOrderNumber(atOrders.getOrderNumber());
        atOrdersEntity.setSuccessNumber(0);
        boolean flag = this.save(atOrdersEntity);

        //生成账号流水
        this.changeAccountBalance(atOrders.getSysUserId(), totalAmount);

        return flag;
    }

    /**
     * 生成账户流水
     *
     * @param sysUserId
     * @param amount
     */
    private void changeAccountBalance(Long sysUserId, BigDecimal amount) {
        //生成账户流水
        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
        accountBalanceDTO.setSysUserId(sysUserId);
        accountBalanceDTO.setTransactionType(AccountTransactionTypeEnum.ACCOUNT_TOKEN.getKey());
        accountBalanceDTO.setAmount(amount);
        accountBalanceDTO.setDescription("购买账号");
        accountBalanceDTO.setOperationUserId(sysUserId);
        accountBalanceDTO.setSysUserId(sysUserId);
        accountBalanceService.changeAccount(accountBalanceDTO);
    }


    private void checkAtOrdersTokenParam(AtOrdersTokenParamDTO paramDTO) {
        Assert.isTrue(ObjectUtil.isNull(paramDTO.getSysUserId()), "用户ID不能为空");
        Assert.isTrue(ObjectUtil.isNull(paramDTO.getProductId()), "商品不能为空");
        Assert.isTrue(ObjectUtil.isNull(paramDTO.getOrderNumber()), "购买数量不能为空");
    }

    @Override
    public boolean updateById(AtOrdersDTO atOrders) {
        AtOrdersEntity atOrdersEntity = AtOrdersConver.MAPPER.converDTO(atOrders);
        return this.updateById(atOrdersEntity);
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
