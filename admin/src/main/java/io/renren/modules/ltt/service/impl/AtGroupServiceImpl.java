package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.dto.ImportZipDTO;
import io.renren.modules.ltt.conver.AtGroupConver;
import io.renren.modules.ltt.dao.AtGroupDao;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.service.AtDataTaskService;
import io.renren.modules.ltt.service.AtGroupService;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.vo.AtUserVO;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Override
    public PageUtils<AtGroupVO> queryPage(AtGroupDTO atGroup) {
        IPage<AtGroupVO> page = baseMapper.listPage(
                new Query<AtGroupEntity>(atGroup).getPage(),
                atGroup
        );

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

        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        return byteArray;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reallocateToken(AtGroupDTO atGroup) {
        AtUserDTO atUserDTO = new AtUserDTO();
        atUserDTO.setSysUserId(atGroup.getSysUserId());
        String regions = EnumUtil.queryValueByKey(atGroup.getCountryCode(), CountryCode.values());
        atUserDTO.setNation(regions.toUpperCase());
        atUserDTO.setUserGroupId(atGroup.getUserGroupId());
        atUserDTO.setLimit(atGroup.getIds().size());
        atUserDTO.setStatus(UserStatus.UserStatus4.getKey());
        atUserDTO.setUserSource(AtUserSourceEnum.AtUserSource1.getKey());
        //获取符合账号的号码
        PageUtils pageUtils = atUserService.queryPage(atUserDTO);

        //获取所有群
        List<AtGroupEntity> atGroupEntities = this.listByIds(atGroup.getIds());
        Assert.isTrue(atGroupEntities.size()>pageUtils.getList().size(),"拉群号不足，请增加拉群号");
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

        for (AtGroupEntity atGroupEntity : atGroupEntities) {
            AtDataTaskEntity atDataTask = integerAtDataTaskEntityMap.get(atGroupEntity.getId());
            if (ObjectUtil.isNull(atDataTask)) {
                continue;
            }
            //加粉子任务
            List<AtDataSubtaskEntity> dataSubtaskEntities = integerListMap.get(atGroupEntity.getId());
            if (CollUtil.isEmpty(dataSubtaskEntities)) {
                continue;
            }
            AtUserVO poll = atUserVOQueue.poll();
            AtUserEntity atUserEntity = new AtUserEntity();
            atUserEntity.setId(poll.getId());
            atUserEntity.setStatus(UserStatus.UserStatus6.getKey());
            atUserEntityUpdates.add(atUserEntity);

            atGroupEntity.setGroupStatus(GroupStatus.GroupStatus1.getKey());
            atGroupEntity.setUserId(poll.getId());
            atGroupEntityListUpdate.add(atGroupEntity);

            atDataTask.setTaskStatus(TaskStatus.TaskStatus1.getKey());
            dataTaskEntitiesUpdate.add(atDataTask);

            for (AtDataSubtaskEntity save : dataSubtaskEntities) {
                save.setTaskStatus(TaskStatus.TaskStatus1.getKey());
                save.setUserId(poll.getId());
                atDataSubtaskEntitiesUpdate.add(save);
            }



        }

        if (CollUtil.isNotEmpty(atUserEntityUpdates)) {
            atUserService.updateBatchById(atUserEntityUpdates);
        }

        if (CollUtil.isNotEmpty(atDataSubtaskEntitiesUpdate)) {
            atDataSubtaskService.updateBatchById(atDataSubtaskEntitiesUpdate);
        }

        if (CollUtil.isNotEmpty(dataTaskEntitiesUpdate)) {
            atDataTaskService.updateBatchById(dataTaskEntitiesUpdate);
        }

        if (CollUtil.isNotEmpty(atGroupEntityListUpdate)) {
            this.updateBatchById(atGroupEntityListUpdate);
        }

    }

    private static void init() {
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
    }

}
