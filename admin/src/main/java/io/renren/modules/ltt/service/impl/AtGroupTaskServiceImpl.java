package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.conver.AtDataTaskConver;
import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.vo.OnGroupPreVO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtGroupTaskDao;
import io.renren.modules.ltt.dto.AtGroupTaskDTO;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.conver.AtGroupTaskConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service("atGroupTaskService")
@Game
@Slf4j
public class AtGroupTaskServiceImpl extends ServiceImpl<AtGroupTaskDao, AtGroupTaskEntity> implements AtGroupTaskService {

    @Override
    public PageUtils<AtGroupTaskVO> queryPage(AtGroupTaskDTO atGroupTask) {
        IPage<AtGroupTaskEntity> page = baseMapper.selectPage(
                new Query<AtGroupTaskEntity>(atGroupTask).getPage(),
                new QueryWrapper<AtGroupTaskEntity>()
        );

        return PageUtils.<AtGroupTaskVO>page(page).setList(AtGroupTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtGroupTaskVO getById(Integer id) {
        return AtGroupTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtGroupTaskDTO atGroupTask) {
        atGroupTask.setDeleteFlag(DeleteFlag.NO.getKey());
        atGroupTask.setCreateTime(DateUtil.date());
        AtGroupTaskEntity atGroupTaskEntity = AtGroupTaskConver.MAPPER.converDTO(atGroupTask);
        return this.save(atGroupTaskEntity);
    }

    @Override
    public boolean updateById(AtGroupTaskDTO atGroupTask) {
        AtGroupTaskEntity atGroupTaskEntity = AtGroupTaskConver.MAPPER.converDTO(atGroupTask);
        return this.updateById(atGroupTaskEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    int groupCountTotal = 99;
    @Override
    public List<String> getGroupNameList(AtGroupTaskDTO atGroupTask) {
        Assert.isNull(atGroupTask.getGroupCount(),"请输入拉群数量");
        Assert.isBlank(atGroupTask.getGroupName(),"请输入群名称");
        Integer groupCount = atGroupTask.getGroupCount();
        List<String> groupNames = new ArrayList<>();
        for (Integer i = 0; i < groupCount; i++) {
            String groupName = String.format("%s-%s", atGroupTask.getGroupName(), (i + 1));
            groupNames.add(groupName);
        }
        return groupNames;
    }

    @Override
    public List<OnGroupPreVO> onGroupPre(AtGroupTaskDTO atGroupTask) {
        Integer groupCount = atGroupTask.getGroupCount();
        Assert.isNull(groupCount,"请输入拉群数量");
        Assert.isBlank(atGroupTask.getGroupName(),"请输入群名称");
        List<String> navyUrlList = atGroupTask.getNavyUrlList();
        List<String> materialUrlList = atGroupTask.getMaterialUrlList();
        Assert.isTrue(CollUtil.isEmpty(navyUrlList),"水军不能为空");
        Assert.isTrue(CollUtil.isEmpty(materialUrlList),"料子不能为空");

        List<List<String>> navyTextListsList = new ArrayList<>();
        //获取所有的水军列表
        getNavyTextLists(navyUrlList, navyTextListsList);
        //料子
        Queue<String> materialUrlsQueue = new LinkedList<>();
        pushMaterialUrlsQueue(materialUrlList, materialUrlsQueue);
        int materialUrlsQueueSize = materialUrlsQueue.size();

        //2个群的水军
        ArrayList<List<String>> resultNavyTextListsList = new ArrayList<>(navyTextListsList);
        while (resultNavyTextListsList.size() < groupCount) {
            for (List<String> element : navyTextListsList) {
                if (resultNavyTextListsList.size() < groupCount) {
                    resultNavyTextListsList.add(element);
                } else {
                    break;
                }
            }
        }



        List<String> groupNameList = getGroupNameList(atGroupTask);
        List<OnGroupPreVO> onGroupPreVOS = new ArrayList<>();
        int useCount = 0;
        for (int i = 0; i < groupNameList.size(); i++) {
            List<String> strings = resultNavyTextListsList.get(i);
            String groupName = groupNameList.get(i);
            OnGroupPreVO onGroupPreVO = new OnGroupPreVO();
            onGroupPreVO.setNavyTextLists(strings);
            onGroupPreVO.setGroupName(groupName);
            List<String> materialUrls = new ArrayList<>();
            for (int i1 = 0; i1 < groupCountTotal - strings.size(); i1++) {
                if (!materialUrlsQueue.isEmpty()) {
                    String poll = materialUrlsQueue.poll();
                    materialUrls.add(poll);
                } else {
                    break;
                }
            }
            useCount = useCount + materialUrls.size();
            onGroupPreVO.setMaterialUrls(materialUrls);
            onGroupPreVOS.add(onGroupPreVO);
        }
        String remaining = String.format("共有群（%s）个，上传料子（%s）个，使用料子（%s）个，剩余料子（%s）个",groupNameList.size(),materialUrlsQueueSize,useCount,materialUrlsQueue.size());
        atGroupTask.setRemaining(remaining);
        return onGroupPreVOS;
    }

    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Autowired
    private AtGroupService atGroupService;
    @Autowired
    private AtUserService atUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onGroupStart(AtGroupTaskDTO atGroupTask) {
        //获取所有群信息
        List<OnGroupPreVO> onGroupPreVOS = onGroupPre(atGroupTask);


        AtUserDTO atUserDTO = new AtUserDTO();
        atUserDTO.setSysUserId(atGroupTask.getSysUserId());
        String regions = EnumUtil.queryValueByKey(atGroupTask.getCountryCode(), CountryCode.values());
        atUserDTO.setNation(regions.toUpperCase());
        atUserDTO.setUserGroupId(atGroupTask.getUserGroupId());
        atUserDTO.setLimit(onGroupPreVOS.size());
        atUserDTO.setStatus(UserStatus.UserStatus4.getKey());
        atUserDTO.setUserSource(AtUserSourceEnum.AtUserSource1.getKey());
        //获取符合账号的号码
        PageUtils pageUtils = atUserService.queryPage(atUserDTO);
        List<AtUserVO> atUserVOS = pageUtils.getList();
        Assert.isTrue(onGroupPreVOS.size()>atUserVOS.size(),"拉群号不足，请增加拉群号");
        Queue<AtUserVO> atUserVOQueue = new LinkedList<>(atUserVOS);
        for (OnGroupPreVO onGroupPreVO : onGroupPreVOS) {
            AtUserVO poll = atUserVOQueue.poll();
            //料子
            List<String> materialUrls = onGroupPreVO.getMaterialUrls();
            //保存群分组
            AtGroupEntity atGroupTaskEntity = new AtGroupEntity();
            atGroupTaskEntity.setGroupTaskId(atGroupTask.getId());
            atGroupTaskEntity.setGroupName(onGroupPreVO.getGroupName());
            atGroupTaskEntity.setUploadGroupNumber(materialUrls.size());
            atGroupTaskEntity.setCurrentExecutionsNumber(0);
            atGroupTaskEntity.setSuccessfullyAttractGroupsNumber(0);
            atGroupTaskEntity.setGroupStatus(GroupStatus.GroupStatus1.getKey());
            atGroupTaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            atGroupTaskEntity.setCreateTime(DateUtil.date());
            atGroupTaskEntity.setUserId(poll.getId());
            atGroupService.save(atGroupTaskEntity);
            //水军
            List<String> navyTextLists = onGroupPreVO.getNavyTextLists();
            //群类型
            Integer groupType = atGroupTask.getGroupType();
            GroupType groupType4 = GroupType.getGroupTypeByKey(groupType);
            AtDataTaskEntity atDataTask = new AtDataTaskEntity();
            atDataTask.setUserGroupId(atGroupTask.getUserGroupId());
            atDataTask.setTaskName(String.format("%s加粉-%s",onGroupPreVO.getGroupName(),groupType4.getValue()));
            atDataTask.setGroupType(groupType4.getKey());
            atDataTask.setCreateTime(DateUtil.date());
            atDataTask.setDeleteFlag(DeleteFlag.NO.getKey());
            atDataTask.setAddTotalQuantity(navyTextLists.size() + materialUrls.size());
            atDataTask.setSuccessfulQuantity(0);
            atDataTask.setFailuresQuantity(0);
            atDataTask.setUpdateTime(DateUtil.date());
            atDataTask.setTaskStatus(TaskStatus.TaskStatus1.getKey());
            atDataTask.setSysUserId(atGroupTask.getSysUserId());
            atDataTask.setGroupId(atGroupTaskEntity.getId());
            atDataTaskService.save(atDataTask);

            List<AtDataSubtaskEntity> atDataSubtaskEntities = new ArrayList<>();
            for (String navyTextList : navyTextLists) {
                String[] parts = navyTextList.split("\\s+");
                AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                save.setGroupId(atGroupTaskEntity.getId());
                save.setGroupType(groupType4.getKey());
                save.setDataTaskId(atDataTask.getId());
                save.setSysUserId(atGroupTask.getSysUserId());
                save.setDataType(DataType.DataType2.getKey());
                save.setTaskStatus(TaskStatus.TaskStatus1.getKey());
                save.setUserId(poll.getId());
                if (parts.length > 2) {
                    save.setContactKey(parts[0].trim());
                    save.setMid(parts[1].trim());
                    save.setDisplayName(parts[2].trim());
                }else if (parts.length > 1) {
                    save.setContactKey(parts[0].trim());
                    save.setMid(parts[1].trim());
                }else {
                    save.setContactKey(parts[0].trim());
                }
                //校验通讯录模式的国家
                if (GroupType.GroupType5.getKey().equals(groupType4.getKey())) {
                    Assert.isTrue(StrUtil.isEmpty(save.getContactKey()),"手机号不能为空");
                }else {
                    Assert.isTrue(StrUtil.isEmpty(save.getMid()),"uid不能为空");
                }
                save.setDeleteFlag(DeleteFlag.NO.getKey());
                save.setCreateTime(DateUtil.date());
                atDataSubtaskEntities.add(save);
            }

            for (String materialUrl : materialUrls) {
                String[] parts = materialUrl.split("\\s+");
                AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                save.setGroupId(atGroupTaskEntity.getId());
                save.setGroupType(groupType4.getKey());
                save.setTaskStatus(TaskStatus.TaskStatus1.getKey());
                save.setDataTaskId(atDataTask.getId());
                save.setSysUserId(atGroupTask.getSysUserId());
                save.setDataType(DataType.DataType1.getKey());
                save.setUserId(poll.getId());
                if (parts.length > 2) {
                    save.setContactKey(parts[0].trim());
                    save.setMid(parts[1].trim());
                    save.setDisplayName(parts[2].trim());
                }else if (parts.length > 1) {
                    save.setContactKey(parts[0].trim());
                    save.setMid(parts[1].trim());
                }else {
                    save.setContactKey(parts[0].trim());
                }
                //校验通讯录模式的国家
                if (GroupType.GroupType5.getKey().equals(groupType4.getKey())) {
                    Assert.isTrue(StrUtil.isEmpty(save.getContactKey()),"手机号不能为空");
                }else {
                    Assert.isTrue(StrUtil.isEmpty(save.getMid()),"uid不能为空");
                }
                save.setDeleteFlag(DeleteFlag.NO.getKey());
                save.setCreateTime(DateUtil.date());
                atDataSubtaskEntities.add(save);
            }

            //校验通讯录模式的国家
            if (GroupType.GroupType5.getKey().equals(groupType4.getKey())) {
                checkCountry(poll, atDataSubtaskEntities);
            }else {

            }
            atDataSubtaskService.saveBatch(atDataSubtaskEntities);
        }
    }

    private static void checkCountry(AtUserVO poll, List<AtDataSubtaskEntity> atDataSubtaskEntities) {
        Set<Long> set = new HashSet<>();
        try {
            PhoneCountryVO user = PhoneUtil.getPhoneNumberInfo(poll.getTelephone());
            set.add(user.getCountryCode());
            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntities) {
                PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(atDataSubtaskEntity.getContactKey());
                set.add(phoneNumberInfo.getCountryCode());
            }
        }catch (Exception e) {

        }
        Assert.isTrue(set.size() > 1,"通讯录模式，协议号和料子国家必须相同");
    }

    private void getNavyTextLists(List<String> navyUrlList, List<List<String>> navyTextListsList) {
        for (String navyUrl : navyUrlList) {
            String navyText = HttpUtil.downloadString(navyUrl, "UTF-8");
            String[] navyTextLines = navyText.split("\n");

            List<String> navyTextLists = new ArrayList<>();
            for (String navyTextLine : navyTextLines) {
                String phone = containsInternationalPhoneNumber(navyTextLine);
                try {
                    PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(phone);
                    navyTextLists.add(navyTextLine);
                } catch (Exception e) {
                    if (CollUtil.isNotEmpty(navyTextLists)) {
                        navyTextListsList.add(navyTextLists);
                    }
                    navyTextLists = new ArrayList<>();
                }
            }
            if (CollUtil.isNotEmpty(navyTextLists)) {
                navyTextListsList.add(navyTextLists);
            }
        }
    }

    private void pushMaterialUrlsQueue(List<String> materialUrlList, Queue<String> materialUrlsQueue) {
        for (String materialUrl : materialUrlList) {
            String materialText = HttpUtil.downloadString(materialUrl, "UTF-8");
            String[] materialTextLines = materialText.split("\n");
            for (String materialTextLine : materialTextLines) {
                String phone = containsInternationalPhoneNumber(materialTextLine);
                try {
                    PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(phone);
                    materialUrlsQueue.offer(materialTextLine);
                } catch (Exception e) {

                }
            }
        }
    }


    private String containsInternationalPhoneNumber(String text) {
        // 正则表达式匹配一个或多个连续的数字
        String regex = "\\d+";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        // 获取matcher对象
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            // 打印出找到的每一个数字
            return matcher.group();
        }
        return "1";
    }

    private static void checkCountry(AtDataEntity atDataEntity, AtUserEntity atUserEntity) {
        boolean flag = false;
        try {
            String data = atDataEntity.getData();
            String telephone = atUserEntity.getTelephone();
            long countryCodeData = PhoneUtil.getPhoneNumberInfo("+" + data.trim()).getCountryCode();
            PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo("+" + telephone.trim());
            long countryCodeTelephone = phoneNumberInfo.getCountryCode();
            flag = countryCodeData != countryCodeTelephone;
            log.info("flag = {}",flag);
        } catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        Assert.isTrue(flag,"通讯录模式，协议号和料子国家必须相同");
    }

}
