package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.dto.ImportZipDTO;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.ltt.conver.AtGroupConver;
import io.renren.modules.ltt.dao.AtGroupDao;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.vo.AtUserVO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service("atGroupService")
@Game
public class AtGroupServiceImpl extends ServiceImpl<AtGroupDao, AtGroupEntity> implements AtGroupService {

    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtUserService atUserService;
    @Resource(name = "caffeineCacheAtGroupEntity")
    private Cache<Integer, AtGroupEntity> caffeineCacheAtGroupEntity;

    @Resource(name = "caffeineCacheDate")
    private Cache<Integer, Date> caffeineCacheDate;

    @Resource
    private CdLineIpProxyService cdLineIpProxyService;

    @Override
    public PageUtils<AtGroupVO> queryPage(AtGroupDTO atGroup) {
        IPage<AtGroupVO> page = baseMapper.listPage(
                new Query<AtGroupEntity>(atGroup).getPage(),
                atGroup
        );

        List<AtGroupVO> resultList = page.getRecords();
        if (CollUtil.isNotEmpty(resultList)) {
            //拉群手机号
            List<Integer> userIdList = resultList.stream().filter(i -> ObjectUtil.isNotNull(i.getUserId()))
                    .map(AtGroupVO::getUserId).collect(Collectors.toList());
            Map<Integer, String> phoneMap = atUserService.queryTelephoneByIds(userIdList);

            for (AtGroupVO atGroupVO : resultList) {
                atGroupVO.setUserTelephone(phoneMap.get(atGroupVO.getUserId()));

                //下次加粉时间
                atGroupVO.setNextTime(caffeineCacheDate.getIfPresent(atGroupVO.getId()));
            }
        }
        page.setRecords(resultList);
        return PageUtils.<AtGroupVO>page(page);
    }
    @Override
    public AtGroupVO getById(Integer id) {
        return AtGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtGroupDTO atGroup) {
        AtGroupEntity atGroupEntity = AtGroupConver.MAPPER.converDTO(atGroup);
        return this.save(atGroupEntity);
    }

    @Override
    public boolean updateById(AtGroupDTO atGroup) {
        AtGroupEntity atGroupEntity = AtGroupConver.MAPPER.converDTO(atGroup);
        return this.updateById(atGroupEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public byte[] importZip(ImportZipDTO importZipDTO) {

        init();


        List<AtGroupEntity> cdGroupTasksEntities = this.list(new QueryWrapper<AtGroupEntity>().lambda()
                .in(AtGroupEntity::getId, importZipDTO.getIds())
                .eq(AtGroupEntity::getGroupStatus, GroupStatus.GroupStatus9.getKey())
        );

        List<AtDataSubtaskEntity> list = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                .in(AtDataSubtaskEntity::getGroupId, importZipDTO.getIds())
                .in(AtDataSubtaskEntity::getTaskStatus, TaskStatus.TaskStatus11.getKey())
        );


        Map<Integer, List<AtDataSubtaskEntity>> integerListMap = list.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getGroupId));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        StringWriter swAll = new StringWriter();

        int totalSuccessfullyAttractGroupsNumber = 0;
        for (AtGroupEntity cdGroupEntity : cdGroupTasksEntities) {
            List<AtDataSubtaskEntity> atDataSubtaskEntities = integerListMap.get(cdGroupEntity.getId());
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                continue;
            }
            Integer successfullyAttractGroupsNumber = cdGroupEntity.getSuccessfullyAttractGroupsNumber();
            totalSuccessfullyAttractGroupsNumber = totalSuccessfullyAttractGroupsNumber + successfullyAttractGroupsNumber;

            //封装模板数据
            Map<String, Object> map = new HashMap<>();
            map.put("columns", atDataSubtaskEntities);
            map.put("name", cdGroupEntity.getGroupName());
            map.put("count", successfullyAttractGroupsNumber);
            map.put("url", cdGroupEntity.getRoomId());
            VelocityContext context = new VelocityContext(map);
            //渲染模板
            StringWriter sw = new StringWriter();

            Template tpl = Velocity.getTemplate("template/url.txt.vm", "UTF-8" );
            tpl.merge(context, sw);

            swAll.append(sw.toString());
            swAll.append("\r\n");
            swAll.append("\r\n");
            swAll.append("\r\n");

            try {
                String packagePath = String.format("【群数%d】/%s-【人数-%d】-【群链-%s】.txt",cdGroupTasksEntities.size(),cdGroupEntity.getGroupName(), successfullyAttractGroupsNumber,cdGroupEntity.getRoomId());
                zip.putNextEntry(new ZipEntry(packagePath));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {

            }
        }

        StringWriter allWrite = new StringWriter();
        String oneText = String.format("共有【群-%d】-【人数-%d】",cdGroupTasksEntities.size(),totalSuccessfullyAttractGroupsNumber);
        importZipDTO.setZipName(oneText);
        allWrite.append(oneText);
        allWrite.append("\r\n");
        allWrite.append("\r\n");
        allWrite.append("\r\n");
        allWrite.append(swAll.toString());
        try {
            String packagePath = String.format("共有【群-%d】-【人数-%d】.txt",cdGroupTasksEntities.size(),totalSuccessfullyAttractGroupsNumber);
            zip.putNextEntry(new ZipEntry(packagePath));
            IOUtils.write(allWrite.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(allWrite);
            zip.closeEntry();
        }catch (IOException e) {

        }

        try {
            String packagePath = String.format("【群数%d】/共有【群-%d】-【人数-%d】.txt",cdGroupTasksEntities.size(),cdGroupTasksEntities.size(),totalSuccessfullyAttractGroupsNumber);
            zip.putNextEntry(new ZipEntry(packagePath));
            IOUtils.write(allWrite.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(allWrite);
            zip.closeEntry();
        }catch (IOException e) {

        }

        try{


            List<AtGroupEntity> atGroupEntities = this.list(new QueryWrapper<AtGroupEntity>().lambda()
                    .in(AtGroupEntity::getId, importZipDTO.getIds())
                    .isNull(AtGroupEntity::getRoomId)
            );

            List<Integer> integers = atGroupEntities.stream().map(AtGroupEntity::getId).collect(Collectors.toList());

            List<AtDataSubtaskEntity> atDataSubtaskEntityList = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .in(AtDataSubtaskEntity::getGroupId, integers)
                    .eq(AtDataSubtaskEntity::getDataType,DataType.DataType1.getKey())
            );

            List<String> tokens7 = atDataSubtaskEntityList.stream().map(item -> String.format("%s  %s  %s", item.getContactKey(), item.getMid(), item.getDisplayName())).collect(Collectors.toList());
            //封装模板数据
            Map<String, Object> map7 = new HashMap<>();
            map7.put("columns", tokens7);
            VelocityContext context7 = new VelocityContext(map7);
            //渲染模板
            StringWriter sw7 = new StringWriter();
            Template tpl7 = Velocity.getTemplate("template/84data.vm", "UTF-8" );
            tpl7.merge(context7, sw7);

            String packagePath7 = String.format("export/%s.txt","export");
            zip.putNextEntry(new ZipEntry(packagePath7));
            IOUtils.write(sw7.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(sw7);
            zip.closeEntry();
        }catch (Exception e) {

        }







        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        return byteArray;
    }

    @Autowired
    private AtUserTokenService atUserTokenService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reallocateToken(AtGroupDTO atGroup) {
        AtUserDTO atUserDTO = new AtUserDTO();
        atUserDTO.setSysUserId(atGroup.getSysUserId());
        String regions = EnumUtil.queryValueByKey(atGroup.getCountryCode(), CountryCode.values());
        atUserDTO.setNation(regions.toUpperCase());
        atUserDTO.setUserGroupId(atGroup.getUserGroupId());
        atUserDTO.setLimit(atGroup.getIds().size() * atGroup.getPullGroupNumber());
        atUserDTO.setStatus(UserStatus.UserStatus4.getKey());
        atUserDTO.setUserSource(AtUserSourceEnum.AtUserSource1.getKey());
        //获取符合账号的号码
        PageUtils pageUtils = atUserService.queryPageOld(atUserDTO);

        //获取所有群
        List<AtGroupEntity> atGroupEntities = this.listByIds(atGroup.getIds());
        Assert.isTrue(atGroupEntities.size() * atGroup.getPullGroupNumber()>pageUtils.getList().size(),"拉群号不足，请增加拉群号");
        //获取加粉任务
        List<AtDataTaskEntity> atDataTaskEntities = atDataTaskService.list(new QueryWrapper<AtDataTaskEntity>().lambda()
                .in(AtDataTaskEntity::getGroupId,atGroup.getIds())
        );
        //获取加粉任务
        Map<Integer, AtDataTaskEntity> integerAtDataTaskEntityMap = atDataTaskEntities.stream().collect(Collectors.toMap(AtDataTaskEntity::getGroupId, item -> item));
        //数据加粉任务子任务
        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                .in(AtDataSubtaskEntity::getGroupId,atGroup.getIds())
        );

        Map<Integer, List<AtDataSubtaskEntity>> integerListMap = atDataSubtaskEntities.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getGroupId));

        List<AtUserVO> list = pageUtils.getList();
        Queue<AtUserVO> atUserVOQueue = new LinkedList<>(list);

        List<AtUserEntity> atUserEntityUpdates = new ArrayList<>();
        List<AtDataSubtaskEntity> atDataSubtaskEntitiesUpdate = new ArrayList<>();


        List<AtDataTaskEntity> dataTaskEntitiesUpdate = new ArrayList<>();
        List<AtGroupEntity> atGroupEntityListUpdate = new ArrayList<>();

        List<AtGroupEntity> atGroupEntities1 = AtGroupConver.MAPPER.conver1(atGroupEntities);

        for (AtGroupEntity atGroupEntity : atGroupEntities) {
            Assert.isTrue(StrUtil.isNotEmpty(atGroupEntity.getRoomId()),"当前群已经有群号，不能去重新分配");
            AtDataTaskEntity atDataTask = integerAtDataTaskEntityMap.get(atGroupEntity.getId());
            if (ObjectUtil.isNull(atDataTask)) {
                continue;
            }
            //加粉子任务
            List<AtDataSubtaskEntity> dataSubtaskEntities = integerListMap.get(atGroupEntity.getId());
            if (CollUtil.isEmpty(dataSubtaskEntities)) {
                continue;
            }


            atDataTask.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            dataTaskEntitiesUpdate.add(atDataTask);


            Map<Integer, List<AtDataSubtaskEntity>> integerListMap1 = dataSubtaskEntities.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getUserId));

            List<AtDataSubtaskEntity> atDataSubtaskEntityListSave = new ArrayList<>();
            for (Integer key : integerListMap1.keySet()) {
                //数据data
                List<AtDataSubtaskEntity> atDataSubtaskEntities1 = integerListMap1.get(key);

                AtUserVO poll = atUserVOQueue.poll();
                AtUserEntity atUserEntity = new AtUserEntity();
                atUserEntity.setId(poll.getId());
                atUserEntity.setStatus(UserStatus.UserStatus6.getKey());
                atUserEntityUpdates.add(atUserEntity);
                //如果拉群的用户和当前id一样
                Integer userId = atDataSubtaskEntities1.get(0).getUserId();
                if (userId.equals(atGroupEntity.getUserId())) {
                    atGroupEntity.setGroupStatus(GroupStatus.GroupStatus1.getKey());
                    atGroupEntity.setUserId(poll.getId());
                    atGroupEntityListUpdate.add(atGroupEntity);
                }else {
                    AtUserVO atUserVO = atUserService.getById(atGroupEntity.getUserId());
                    AtUserTokenVO atUserTokenVO = atUserTokenService.getById(atUserVO.getUserTokenId());
                    LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenVO.getToken(), LineTokenJson.class);
                    AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                    save.setGroupId(atGroupEntity.getId());
                    save.setGroupType(atDataTask.getGroupType());
                    save.setTaskStatus(TaskStatus.TaskStatus1.getKey());
                    save.setDataTaskId(atDataTask.getId());
                    save.setSysUserId(atDataTask.getSysUserId());
                    save.setDataType(DataType.DataType3.getKey());
                    save.setContactKey(lineTokenJson.getPhone());
                    save.setMid(lineTokenJson.getMid());
                    save.setDisplayName(lineTokenJson.getNickName());
                    save.setUserId(poll.getId());
                    atDataSubtaskEntityListSave.add(save);
                }
                for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntities1) {
                    if (DataType.DataType3.getKey().equals(atDataSubtaskEntity.getDataType())) {
                        atDataSubtaskService.removeById(atDataSubtaskEntity);
                        continue;
                    }
                    atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                    atDataSubtaskEntity.setUserId(poll.getId());
                    atDataSubtaskEntitiesUpdate.add(atDataSubtaskEntity);
                }
//                long count = atDataSubtaskEntities1.stream().filter(item -> item.getTaskStatus().equals(TaskStatus.TaskStatus13.getKey()) || item.getTaskStatus().equals(TaskStatus.TaskStatus8.getKey())  || item.getTaskStatus().equals(TaskStatus.TaskStatus5.getKey())).count();
//                if (count > 0) {}
            }

            if (CollUtil.isNotEmpty(atDataSubtaskEntityListSave)) {
                atDataSubtaskService.saveBatch(atDataSubtaskEntityListSave);
            }

        }

        if (CollUtil.isNotEmpty(atUserEntityUpdates)) {
            atUserService.updateBatchById(atUserEntityUpdates);
        }

        if (CollUtil.isNotEmpty(atDataSubtaskEntitiesUpdate)) {
            atDataSubtaskService.updateBatchById(atDataSubtaskEntitiesUpdate,atDataSubtaskEntitiesUpdate.size());
        }

        if (CollUtil.isNotEmpty(dataTaskEntitiesUpdate)) {
            atDataTaskService.updateBatchById(dataTaskEntitiesUpdate);
        }

        if (CollUtil.isNotEmpty(atGroupEntityListUpdate)) {
            this.updateBatchById(atGroupEntityListUpdate);
        }

        //保存一份历史数据，方便统计报表
        if (CollUtil.isNotEmpty(atGroupEntities1)) {
            for (AtGroupEntity atGroupEntity : atGroupEntities1) {
                atGroupEntity.setId(null);
            }
            this.saveBatch(atGroupEntities1);
        }

    }


    @Override
    public void startTask(AtGroupDTO atGroup) {
        List<Integer> ids = atGroup.getIds();
        List<AtDataTaskEntity> atDataTaskEntities = atDataTaskService.list(new QueryWrapper<AtDataTaskEntity>().lambda()
                .in(AtDataTaskEntity::getGroupId,ids)
        );
        List<AtDataTaskEntity> updates = new ArrayList<>();
        for (AtDataTaskEntity atDataTaskEntity : atDataTaskEntities) {
            AtDataTaskEntity update = new AtDataTaskEntity();
            update.setId(atDataTaskEntity.getId());
            update.setTaskStatus(TaskStatus.TaskStatus1.getKey());
            updates.add(update);
        }
        atDataTaskService.updateBatchById(updates);
    }

    @Override
    public AtGroupEntity getByIdCache(Integer groupId) {
        AtGroupEntity atGroupEntity = caffeineCacheAtGroupEntity.getIfPresent(groupId);
        if(ObjectUtil.isNotNull(atGroupEntity)) {
            return atGroupEntity;
        }
        atGroupEntity = this.getById((Serializable) groupId);
        caffeineCacheAtGroupEntity.put(groupId,atGroupEntity);
        return atGroupEntity;
    }

    private static void init() {
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean errRetryGroup(Integer groupId) {
        AtGroupEntity atGroup = baseMapper.selectById(groupId);
        Assert.isNull(atGroup, "数据不存在，请检查");

        if (StringUtils.isNotEmpty(atGroup.getMsg()) && atGroup.getMsg().contains("网络异常")) {
            //网络异常的处理
            AtGroupEntity updateGroup = new AtGroupEntity();
            updateGroup.setId(atGroup.getId());
            updateGroup.setGroupStatus(GroupStatus.GroupStatus7.getKey());
            int count = baseMapper.updateById(updateGroup);

            //查询手机号
            AtUserVO atUser = atUserService.getById(atGroup.getUserId());
            if (atUser != null) {
                //代理清空
                cdLineIpProxyService.clearTokenPhone(atUser.getTelephone());
            }
            return count > 0;
        } else if (StringUtils.isNotEmpty(atGroup.getRoomId())
                && atGroup.getSuccessfullyAttractGroupsNumber() != null
                && atGroup.getSuccessfullyAttractGroupsNumber() == 0) {
            //已有群号，代表已经成功，状态改成
            AtGroupEntity updateGroup = new AtGroupEntity();
            updateGroup.setId(atGroup.getId());
            updateGroup.setGroupStatus(GroupStatus.GroupStatus5.getKey());
            int count = baseMapper.updateById(updateGroup);
            return count > 0;
        }
        return false;
    }

    @Override
    public Map<Integer, AtGroupTaskVO> groupDataSummary(List<Integer> groupTaskIdList) {
        return baseMapper.groupDataSummary(groupTaskIdList).stream()
                .collect(Collectors.toMap(AtGroupTaskVO::getId, i -> i));
    }

    @Override
    public Boolean startGroup(List<Integer> ids) {
        List<AtGroupEntity> atGroupEntities = this.listByIds(ids);
        for (AtGroupEntity atGroupEntity : atGroupEntities) {
            Assert.isTrue(!GroupStatus.GroupStatus14.getKey().equals(atGroupEntity.getGroupStatus()), "启动拉群状态必须为等待拉群");
            atGroupEntity.setGroupStatus(GroupStatus.GroupStatus7.getKey());
        }
        boolean b = this.updateBatchById(atGroupEntities);
        return b;
    }


}
