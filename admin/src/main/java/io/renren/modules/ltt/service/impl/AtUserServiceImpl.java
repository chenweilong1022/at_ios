package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
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
import io.renren.modules.ltt.dto.CustomerUserResultDto;
import io.renren.modules.ltt.dto.UserSummaryResultDto;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UserStatus;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.service.AtUserTokenService;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.AtUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.renren.modules.ltt.enums.OpenStatus.OpenStatus3;
import static io.renren.modules.ltt.enums.OpenStatus.OpenStatus4;
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
    @Override
    public PageUtils<AtUserVO> queryPage1(AtUserDTO atUser) {
        atUser.setPageStart((atUser.getPage() - 1) * atUser.getLimit());
        Integer count = baseMapper.queryPageCount(atUser);
        List<AtUserVO> resultList = Collections.emptyList();
        if (count > 0) {
            resultList = baseMapper.queryPage(atUser);
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
    public void cleanBlockData(Long sysUserId) {
        //清理封号数据
        List<AtUserEntity> userList = baseMapper
                .selectList(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getStatus, UserStatus.UserStatus2.getKey())
                .eq(ObjectUtil.isNotNull(sysUserId), AtUserEntity::getSysUserId, sysUserId)
                );
        if (CollectionUtils.isNotEmpty(userList)) {
            List<Integer> ids = userList.stream().map(AtUserEntity::getId).distinct().collect(Collectors.toList());
            baseMapper.deleteBatchIds(ids);

            List<Integer> userTokenIdList = userList.stream().filter(i -> i.getUserTokenId() != null)
                    .map(AtUserEntity::getUserTokenId).distinct().collect(Collectors.toList());
            //清理token
            if (CollectionUtils.isNotEmpty(userTokenIdList)) {
                atUserTokenService.removeByIds(userTokenIdList);
            }
        }

        //清理token中过期的数据
        List<AtUserTokenEntity> tokenList = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                .eq(ObjectUtil.isNotNull(sysUserId), AtUserTokenEntity::getSysUserId, sysUserId)
                .in(AtUserTokenEntity::getOpenStatus, Arrays.asList(OpenStatus3.getKey(), OpenStatus4.getKey())));
        if (CollectionUtils.isNotEmpty(tokenList)) {
            List<Integer> tokenIdList = tokenList.stream().map(AtUserTokenEntity::getId).distinct().collect(Collectors.toList());
            atUserTokenService.removeByIds(tokenIdList);

            //清理账号数据
            baseMapper.delete(new QueryWrapper<AtUserEntity>().lambda().in(AtUserEntity::getUserTokenId, tokenIdList));

        }
    }


    @Override
    public UserSummaryResultDto queryUserSummary() {
        //今日已使用数量
        Integer usedUserStock = baseMapper.selectCount(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getStatus, UserStatus6.getKey())
                .between(AtUserEntity::getCreateTime, DateUtils.getTodayStart(), DateUtils.getTodayEnd()));
        //当前在线数量
        Integer onlineUserNum = baseMapper.selectCount(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getStatus, UserStatus4.getKey()));

        UserSummaryResultDto resultDto = new UserSummaryResultDto();
        resultDto.setUsedUserStock(usedUserStock);
        resultDto.setOnlineUserNum(onlineUserNum);
        return resultDto;
    }
}
