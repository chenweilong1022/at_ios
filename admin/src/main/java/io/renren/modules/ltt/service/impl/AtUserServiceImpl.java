package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.collect.Lists;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.app.service.FileService;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.ltt.conver.AtUserConver;
import io.renren.modules.ltt.dao.AtUserDao;
import io.renren.modules.ltt.dao.UpdateAtUserCustomerParamDto;
import io.renren.modules.ltt.dao.UpdateUserGroupParamDto;
import io.renren.modules.ltt.dao.ValidateAtUserStatusParamDto;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.dto.UserSummaryResultDto;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.renren.modules.ltt.enums.OpenStatus.OpenStatus3;
import static io.renren.modules.ltt.enums.UserStatus.*;


@Service("atUserService")
@Game
@Slf4j
public class AtUserServiceImpl extends ServiceImpl<AtUserDao, AtUserEntity> implements AtUserService {

    @Resource
    private AtUserTokenService atUserTokenService;

    @Resource
    private FileService fileService;

    @Resource
    private CdLineIpProxyService lineIpProxyService;

    @Resource(name = "caffeineCacheListString")
    private Cache<String, Queue<String>> caffeineCacheListString;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private CdGetPhoneService cdGetPhoneService;

    @Resource
    private CdLineRegisterService cdLineRegisterService;


    @Override
    public PageUtils<AtUserVO> queryPage1(AtUserDTO atUser) {
        atUser.setPageStart((atUser.getPage() - 1) * atUser.getLimit());
        Integer count = baseMapper.queryPageCount(atUser);
        List<AtUserVO> resultList = Collections.emptyList();
        if (count > 0) {
            resultList = baseMapper.queryPage(atUser);
        }
        for (AtUserVO atUserVO : resultList) {
            //查询手机号可用状态
            Boolean phoneRegisterState = cdGetPhoneService.getPhoneUseState(atUserVO.getTelephone());
            if (Boolean.FALSE.equals(phoneRegisterState)) {
                atUserVO.setPhoneExpire(redisTemplate
                        .getExpire(RedisKeys.RedisKeys12.getValue(atUserVO.getTelephone()), TimeUnit.MINUTES));
            }
            atUserVO.setPhoneState(phoneRegisterState);
            //代理类型
            Object proxyId = redisTemplate.opsForHash().get(RedisKeys.RedisKeys5.getValue(),
                    String.valueOf(atUserVO.getTelephone()));
            if (ObjectUtil.isNotNull(proxyId)) {
                atUserVO.setProxyId(Integer.valueOf(String.valueOf(proxyId)));
            }
        }
        return new PageUtils(resultList, count, atUser.getLimit(), atUser.getPage());
    }

    @Override
    public PageUtils queryPageOld(AtUserDTO atUser) {
        IPage<AtUserEntity> page = baseMapper.selectPage(
                new Query<AtUserEntity>(atUser).getPage(),
                new QueryWrapper<AtUserEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atUser.getSysUserId()), AtUserEntity::getSysUserId, atUser.getSysUserId())
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
                        .eq(atUser.getUserSource() != null, AtUserEntity::getUserSource, atUser.getUserSource())
                        .eq(atUser.getId() != null, AtUserEntity::getId, atUser.getId())
                        .orderByDesc(AtUserEntity::getId)
        );
        return PageUtils.<AtUserVO>page(page).setList(AtUserConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public AtUserVO getById(Integer id) {
        return AtUserConver.MAPPER.conver(baseMapper.selectById(id));
    }

//    @Override
//    public Map<Integer, AtUserEntity> getIds(List<Integer> ids) {
//        Map<Integer, AtUserEntity> allPresent = stringListAtUserEntitys.getAllPresent(ids);
//        if (ids.size() == allPresent.keySet().size()) {
//            return allPresent;
//        }
//        List<AtUserEntity> atUserEntities = this.listByIds(ids);
//        Map<Integer, AtUserEntity> collect = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> item, (a, b) -> a));
//        if (ids.size() == collect.keySet().size()) {
//            stringListAtUserEntitys.putAll(collect);
//            return allPresent;
//        }
//        return null;
//    }

    @Override
    public boolean unlock(Integer id, UserStatus userStatus) {
        AtUserEntity atUserEntity = new AtUserEntity();
        atUserEntity.setId(id);
        atUserEntity.setStatus(userStatus.getKey());
        return this.updateById(atUserEntity);
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
        Assert.isTrue(paramDto.getFilterRed() == null, "是否过滤不能为空");

        List<AtUserEntity> userEntityList = baseMapper.selectList(new QueryWrapper<AtUserEntity>().lambda()
                .in(AtUserEntity::getId, ids));

        if (Boolean.TRUE.equals(paramDto.getFilterRed())) {
            //过滤掉红灯的手机号
            ids = userEntityList.stream()
                    .filter(i-> Boolean.TRUE.equals(cdGetPhoneService.getPhoneUseState(i.getTelephone())))
                    .map(AtUserEntity::getId).collect(Collectors.toList());
        }

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
    public boolean maintainUser(ValidateAtUserStatusParamDto paramDto) {
        Assert.isTrue(ObjectUtils.isEmpty(paramDto), "数据不能为空");
        Assert.isTrue(paramDto.getValidateFlag() == null || Boolean.TRUE.equals(paramDto.getValidateFlag()),
                "养号类型不能为空，且只能养护勾选账号");
        Assert.isTrue(CollectionUtils.isEmpty(paramDto.getIds()), "选择的数据不能为空");

        List<Integer> ids = paramDto.getIds();
        List<AtUserEntity> userList = baseMapper.selectBatchIds(ids);
        Assert.isTrue(CollectionUtils.isEmpty(userList), "数据不能为空");

        //清空ip
        List<String> phoneList = userList.stream().map(AtUserEntity::getTelephone).collect(Collectors.toList());
        for (String phone : phoneList) {
            Queue<String> getflowip = caffeineCacheListString.getIfPresent(phone);
            if (CollUtil.isNotEmpty(getflowip)) {
                caffeineCacheListString.put(phone, new LinkedList<>());
            }
        }
        lineIpProxyService.deleteByTokenPhone(phoneList);

        //置为：未验证
        this.validateUserStatus(paramDto);
        return true;
    }

    @Override
    public String downloadUserTokenTxt(List<Integer> ids) {
//        Assert.isTrue(CollectionUtils.isEmpty(ids), "选择的数据不能为空");
//
//        //查询对应的token
//        List<AtUserEntity> userList = baseMapper.selectBatchIds(ids);
//        Assert.isTrue(CollectionUtils.isEmpty(userList), "数据为空，请刷新重试");
//
//        List<Integer> userTokenIdList = userList.stream()
//                .filter(i -> i.getUserTokenId() != null)
//                .map(AtUserEntity::getUserTokenId).distinct()
//                .collect(Collectors.toList());
//        Assert.isTrue(CollectionUtils.isEmpty(userTokenIdList), "下载账户数据为空");
//
//        //查询token数据
//        List<AtUserTokenEntity> tokenList = atUserTokenService.selectBatchIds(userTokenIdList);
//        Assert.isTrue(CollectionUtils.isEmpty(tokenList), "下载账户数据为空");
//
//        //处理下载数据
//        List<String> tokenTextList = tokenList.stream().filter(i -> StringUtils.isNotEmpty(i.getToken()))
//                .map(AtUserTokenEntity::getToken).collect(Collectors.toList());
//        Assert.isTrue(CollectionUtils.isEmpty(tokenTextList), "下载账户数据为空");
//        try {
//            return fileService.writeTxtFile(String.valueOf(System.currentTimeMillis()), tokenTextList);
//        } catch (IOException e) {
//            Assert.isTrue(true, "下载异常，请稍后再试");
//        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public byte[] importToken(List<Integer> ids) {
        Assert.isTrue(CollectionUtils.isEmpty(ids), "选择的数据不能为空");

        //查询对应的token
        List<AtUserEntity> userList = baseMapper.selectBatchIds(ids);
        Assert.isTrue(CollectionUtils.isEmpty(userList), "数据为空，请刷新重试");

        List<Integer> userTokenIdList = userList.stream().filter(i -> i.getUserTokenId() != null)
                .map(AtUserEntity::getUserTokenId).distinct().collect(Collectors.toList());
        Assert.isTrue(CollectionUtils.isEmpty(userTokenIdList), "下载账户数据为空");

        //查询token数据
        List<AtUserTokenEntity> tokenList = atUserTokenService.selectBatchIds(userTokenIdList);
        Assert.isTrue(CollectionUtils.isEmpty(tokenList), "下载账户数据为空");
        Map<Integer, String> tokenMap = tokenList.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, AtUserTokenEntity::getToken));

        init();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        List<String> tokens = new ArrayList<>();
        List<String> tokenss = new ArrayList<>();
        List<String> tokens7 = new ArrayList<>();

        String token;
        for (AtUserEntity atUserEntity : userList) {
            token = tokenMap.get(atUserEntity.getUserTokenId());
            //封装模板数据
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            VelocityContext context = new VelocityContext(map);
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate("template/token.txt.vm", "UTF-8" );
            tpl.merge(context, sw);

            //国家
            Integer countryCode = CountryCode.getKeyByValue(atUserEntity.getNation());
            try {
                LineTokenJson lineTokenJson = JSON.parseObject(token, LineTokenJson.class);
                String format = String.format("%s----%s----%s----%s----%s----%s\n",
                        countryCode, lineTokenJson.getPhone().replaceFirst(countryCode+"",""),
                        lineTokenJson.getPassword(), lineTokenJson.getMid(), lineTokenJson.getAccessToken(),
                        lineTokenJson.getRefreshToken());
                tokens.add(format);

                tokenss.add(token);

                String format7 = String.format("%s----%s----%s----%s----%s----%s----%s\n",
                        countryCode, lineTokenJson.getPhone().replaceFirst(countryCode+"",""),
                        lineTokenJson.getPassword(), lineTokenJson.getMid(), lineTokenJson.getAccessToken(),
                        lineTokenJson.getRefreshToken(),lineTokenJson.getAuthToken());
                tokens7.add(format7);

                String packagePath = String.format("token/%s.txt",atUserEntity.getTelephone());
                zip.putNextEntry(new ZipEntry(packagePath));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
            }
        }

        try{
            //封装模板数据
            Map<String, Object> map = new HashMap<>();
            map.put("columns", tokens);
            VelocityContext context = new VelocityContext(map);
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate("template/84data.vm", "UTF-8" );
            tpl.merge(context, sw);

            String packagePath = String.format("token/%s.txt","data");
            zip.putNextEntry(new ZipEntry(packagePath));
            IOUtils.write(sw.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(sw);
            zip.closeEntry();

            //封装模板数据
            Map<String, Object> map7 = new HashMap<>();
            map7.put("columns", tokens7);
            VelocityContext context7 = new VelocityContext(map7);
            //渲染模板
            StringWriter sw7 = new StringWriter();
            Template tpl7 = Velocity.getTemplate("template/84data.vm", "UTF-8" );
            tpl7.merge(context7, sw7);

            String packagePath7 = String.format("token/%s.txt","data7");
            zip.putNextEntry(new ZipEntry(packagePath7));
            IOUtils.write(sw7.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(sw7);
            zip.closeEntry();



            //封装模板数据
            Map<String, Object> mapTokenss = new HashMap<>();
            mapTokenss.put("columns", tokenss);
            VelocityContext contextTokens = new VelocityContext(mapTokenss);
            //渲染模板
            StringWriter swTokens = new StringWriter();
            Template tplTokens = Velocity.getTemplate("template/tokens.txt.vm", "UTF-8" );
            tplTokens.merge(contextTokens, swTokens);

            String packagePathTokens = String.format("token/%s.txt","tokenss");
            zip.putNextEntry(new ZipEntry(packagePathTokens));
            IOUtils.write(swTokens.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(swTokens);
            zip.closeEntry();
        }catch (IOException e) {
        }
        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        return byteArray;
    }

    private static void init() {
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
    }

    @Override
    public Map<Integer, String> queryTelephoneByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<AtUserEntity> userList = baseMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyMap();
        }
        return userList.stream().filter(i -> StringUtils.isNotEmpty(i.getTelephone()))
                .collect(Collectors.toMap(AtUserEntity::getId, AtUserEntity::getTelephone));
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cleanBlockData(Long sysUserId, Integer[] removeIds) {
        //清理封号数据
        List<AtUserEntity> userList = baseMapper
                .selectList(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getStatus, UserStatus.UserStatus2.getKey())
                .eq(ObjectUtil.isNotNull(sysUserId), AtUserEntity::getSysUserId, sysUserId)
                .in(CollUtil.isNotEmpty(Arrays.asList(removeIds)), AtUserEntity::getId, Arrays.asList(removeIds))
                );
        if (CollectionUtils.isNotEmpty(userList)) {
            List<Integer> ids = userList.stream().map(AtUserEntity::getId).distinct().collect(Collectors.toList());
            baseMapper.deleteBatchIds(ids);

            List<Integer> userTokenIdList = userList.stream().filter(i -> i.getUserTokenId() != null)
                    .map(AtUserEntity::getUserTokenId).distinct().collect(Collectors.toList());
            //清理token
            if (CollectionUtils.isNotEmpty(userTokenIdList)) {
                List<List<Integer>> partition = Lists.partition(userTokenIdList, 50);
                for (List<Integer> integers : partition) {
                    atUserTokenService.removeByIds(integers);
                }
            }
        }

        //清理token中过期的数据
        List<AtUserTokenEntity> tokenList = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                .eq(ObjectUtil.isNotNull(sysUserId), AtUserTokenEntity::getSysUserId, sysUserId)
                .in(AtUserTokenEntity::getOpenStatus, Arrays.asList(OpenStatus3.getKey())));
        if (CollectionUtils.isNotEmpty(tokenList)) {
            List<Integer> tokenIdList = tokenList.stream().map(AtUserTokenEntity::getId).distinct().collect(Collectors.toList());
            atUserTokenService.removeByIds(tokenIdList);

            //清理账号数据
            baseMapper.delete(new QueryWrapper<AtUserEntity>().lambda().in(AtUserEntity::getUserTokenId, tokenIdList));

        }
    }


    @Override
    public Map<String, Integer> queryUsedUserSummary(LocalDate searchTime) {

        LocalTime localTime = LocalTime.of(8, 0, 0);

        LocalDateTime searchStartTime = searchTime.atTime(localTime);
        LocalDateTime searchEndTime = searchTime.plusDays(1).atTime(localTime);
        //今日已使用数量
        Map<String, Integer> usedUserMap = baseMapper.queryUsedUserSummary(searchStartTime, searchEndTime).stream()
                .collect(Collectors.toMap(UserSummaryResultDto::getCountryCode, UserSummaryResultDto::getUsedUserStock));
        return usedUserMap;
    }

    @Override
    public Map<String, Integer> queryOnlineUserSummary() {
        //当前在线数量
        Map<String, Integer> onlineUserMap = baseMapper.queryOnlineUserSummary().stream()
                .collect(Collectors.toMap(UserSummaryResultDto::getCountryCode, UserSummaryResultDto::getOnlineUserNum));
        return onlineUserMap;
    }

    @Override
    @Async
    public void syncRegisterCountTest(String phone1) {
//        List<AtUserVO> atuserList = baseMapper.queryMaxRegisterCount("jp");
//        for (AtUserVO atUserVO : atuserList) {
//            threadPoolTaskExecutor.execute(() -> {
//                redisTemplate.opsForHash().put(RedisKeys.RedisKeys10.getValue(), atUserVO.getTelephone(), atUserVO.getRegisterCount().toString());
//            });
//        }
//        Set<String> keys1 = redisTemplate.keys(RedisKeys.RedisKeys12.getValue("*"));
//        for (String key : keys1) {
//            threadPoolTaskExecutor.execute(() -> {
//                redisTemplate.delete(key);
//            });
//        }

        Set<Object> keys = redisTemplate.opsForHash().keys(RedisKeys.RedisKeys10.getValue());
        for (Object key : keys) {
            threadPoolTaskExecutor.execute(() -> {
                String phone = String.valueOf(key);
                        Integer registerCount = cdGetPhoneService.getPhoneRegisterCount(phone);
                        //大于等于3次的卡，与前两次的做对比，超过24小时，才为可用状态
                        if (registerCount >= 3) {
                            Map<Integer, Date> userMap = baseMapper.selectList(new QueryWrapper<AtUserEntity>().lambda()
                                            .eq(AtUserEntity::getTelephone, phone)).stream()
                                    .filter(i -> i.getRegisterCount() != null)
                                    .collect(Collectors.toMap(AtUserEntity::getRegisterCount,
                                            i -> i.getRegisterTime() != null ? i.getRegisterTime() : i.getCreateTime(), (a, b) -> b));
                            Integer judgeFrequency = registerCount - 2;//与前两次的对比

                            Date time = null;
                            if (ObjectUtil.isNotNull(userMap.get(judgeFrequency))) {
                                time = userMap.get(judgeFrequency);
                            } else if (ObjectUtil.isNotNull(userMap.get(registerCount))) {
                                time = userMap.get(registerCount);
                            } else {
                                time = userMap.get(0);
                                log.error("注册时间异常  {}", phone);
                            }

                            //在此时间上加24小时
                            Date expireDate = DateUtils.addDateMinutes(time, 24 * 60);

                            Long expireMinutes = DateUtils.betweenMinutes(new Date(), expireDate);

                            if (expireMinutes > 5) {
                                redisTemplate.opsForValue().set(RedisKeys.RedisKeys12.getValue(phone), String.valueOf(registerCount), expireMinutes, TimeUnit.MINUTES);
                            }
                        }
                    });
}


//        if (StringUtils.isNotEmpty(phone)) {
//            redisTemplate.opsForHash().delete(RedisKeys.RedisKeys10.getValue(), phone);
//
//        } else {
//            redisTemplate.delete(RedisKeys.RedisKeys10.getValue());
//        }
//        AtUserDTO atUser = new AtUserDTO();
//        atUser.setPage(1);
//        atUser.setLimit(500);
//        boolean hasNextPage = true;
//
//        while (hasNextPage) {
//            IPage<AtUserEntity> page = baseMapper.selectPage(new Query<AtUserEntity>(atUser).getPage(),
//                    new QueryWrapper<AtUserEntity>().lambda()
//                            .eq(StringUtils.isNotEmpty(phone), AtUserEntity::getTelephone, phone)
//                            .eq(AtUserEntity::getNation, "JP")
//                            .orderByAsc(AtUserEntity::getCreateTime)
//            );
//            List<AtUserEntity> currentPageData = page.getRecords();
//            hasNextPage = currentPageData.size() >= atUser.getLimit();
//            log.info("同步注册次数，第{}页，数量{}", atUser.getPage(), currentPageData.size());
//            if (hasNextPage) {
//                atUser.setPage(atUser.getPage() + 1);
//            }
//
//            List<AtUserEntity> updateAtUserList = new ArrayList<>();
//            for (AtUserEntity atUserEntity : currentPageData) {
//                //更新次数
//                Integer registerCount = cdGetPhoneService.getPhoneRegisterCount(atUserEntity.getTelephone());
//
//
//                AtUserEntity updateAtUserEntity = new AtUserEntity();
//                updateAtUserEntity.setId(atUserEntity.getId());
//                updateAtUserEntity.setRegisterCount(registerCount);
//                updateAtUserList.add(updateAtUserEntity);
//                redisTemplate.opsForHash().put(RedisKeys.RedisKeys10.getValue(), atUserEntity.getTelephone(), registerCount.toString());
//            }
//        }
//        log.info("同步注册次数结束，总页码:{}", atUser.getPage());

    }

//    @Override
//    public void syncPhoneRegisterTest() {
//        AtUserDTO atUser = new AtUserDTO();
//        atUser.setPage(1);
//        atUser.setLimit(500);
//        boolean hasNextPage = true;
//        while (hasNextPage) {
//            IPage<AtUserEntity> page = baseMapper.selectPage(new Query<AtUserEntity>(atUser).getPage(),
//                    new QueryWrapper<AtUserEntity>().lambda()
//                            .eq(AtUserEntity::getNation, "JP")
//                            .orderByDesc(AtUserEntity::getCreateTime)
//            );
//            List<AtUserEntity> currentPageData = page.getRecords();
//            hasNextPage = currentPageData.size() >= atUser.getLimit();
//            if (hasNextPage) {
//                atUser.setPage(atUser.getPage() + 1);
//            }
//
//            List<AtUserEntity> updateAtUserList = new ArrayList<>();
//            for (AtUserEntity atUserEntity : currentPageData) {
//                threadPoolTaskExecutor.execute(() -> {
//                    AtUserEntity updateUser = new AtUserEntity();
//                    CdLineRegisterEntity cdLineRegisterEntity = cdLineRegisterService.queryLineRegisterByPhone(atUserEntity.getTelephone());
//                    if (ObjectUtil.isNotNull(cdLineRegisterEntity)) {
//                        log.info(atUserEntity.getCreateTime()  + "---" + cdLineRegisterEntity.getCreateTime());
//                        if ( atUserEntity.getCreateTime().after(cdLineRegisterEntity.getCreateTime())) {
//                            updateUser.setId(atUserEntity.getId());
//                            updateUser.setRegisterTime(cdLineRegisterEntity.getCreateTime());
//                            updateAtUserList.add(updateUser);
//                            baseMapper.updateById(updateUser);
//                        }
//                    }
//                });
//            }
//        }
//
//    }

    public static void main(String[] args) {

    }

    @Override
    @Async
    public void syncPhoneInvalidTest(String phoneParam) {
        Set<String> keys = redisTemplate.keys(RedisKeys.RedisKeys12.getValue("*"));
        for (String key : keys) {
            try {

            String phoneKey = key.replace(RedisKeys.RedisKeys12.getValue(), "");
            if (StringUtils.isNotEmpty(phoneParam)) {
                if (!phoneParam.equals(phoneKey)) {
                    continue;
                }
            }
            log.info("同步红灯开始 {}", phoneKey);

            Integer registerCount = cdGetPhoneService.getPhoneRegisterCount(phoneKey);
            if (registerCount < 3) {
                log.info("同步红灯时间异常 {}, {}", phoneKey, registerCount);
                continue;
            }



            Map<Integer, Date> userMap = baseMapper.selectList(new QueryWrapper<AtUserEntity>()
                            .lambda().eq(AtUserEntity::getTelephone, phoneKey)).stream()
                    .filter(i -> i.getRegisterCount() != null)
                    .collect(Collectors.toMap(AtUserEntity::getRegisterCount,
                            i -> i.getRegisterTime() != null ? i.getRegisterTime() : i.getCreateTime(),(a,b)->b));

            Integer judgeFrequency = registerCount - 2;//与前两次的对比

            Date time = null;
            if (ObjectUtil.isNotNull(userMap.get(judgeFrequency))) {
                time = userMap.get(judgeFrequency);
            } else {
                Map.Entry<Integer, Date> integerDateEntry = userMap.entrySet().stream().max(Map.Entry.comparingByValue(Comparator.comparing(Date::getTime))).orElse(null);
                if (integerDateEntry != null) {
                    time = integerDateEntry.getValue();
                } else {
                    log.info("错误数据 {}", key);
                }
            }

            //在此时间上加24小时+30分钟
            Date expireDate = DateUtils.addDateMinutes(time, (24 * 60) + 30);//过期时间

            Long expireMinutes = DateUtils.betweenMinutes(new Date(), expireDate);

            log.info("同步红灯时间 {}, {}", phoneKey, expireMinutes);
            if (expireMinutes <= 5) {
                log.info("删除红绿灯时间 {}, {}", phoneKey, expireMinutes);
                redisTemplate.delete(RedisKeys.RedisKeys12.getValue(phoneKey));
                continue;
            }

            redisTemplate.opsForValue().set(RedisKeys.RedisKeys12.getValue(phoneKey),
                    String.valueOf(registerCount), expireMinutes, TimeUnit.MINUTES);
        }catch (Exception e){
                log.info("同步红灯异常 {}, {}", key, e);

            }

        }

    }

    @Override
    public void setProxy(AtUserDTO atUser) {
        List<Integer> ids = atUser.getIds();
        List<AtUserEntity> atUserEntities = this.listByIds(ids);
        //给用户设置代理类型
        for (AtUserEntity atUserEntity : atUserEntities) {
            redisTemplate.opsForHash().put(RedisKeys.RedisKeys5.getValue(), String.valueOf(atUserEntity.getTelephone()), String.valueOf(atUser.getProxyIp()));
        }
    }

    @Override
    public Boolean changeNameRegisterAgains(List<Integer> ids) {
        Assert.isTrue(CollectionUtils.isEmpty(ids), "选择的数据不能为空");
        Assert.isTrue(ids.size() > 100, "选择数据不能大于100");

        List<AtUserEntity> userList = baseMapper.selectBatchIds(ids);
        Assert.isTrue(CollectionUtils.isEmpty(userList), "数据为空，请刷新重试");

        //重新发起注册
        List<String> telphoneList = userList.stream().map(AtUserEntity::getTelephone).distinct().collect(Collectors.toList());
        return cdLineRegisterService.registerAgains(telphoneList);
    }
}
