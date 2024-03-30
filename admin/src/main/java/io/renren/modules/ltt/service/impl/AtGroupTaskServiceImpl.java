package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.conver.AtDataTaskConver;
import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.AtDataTaskService;
import io.renren.modules.ltt.service.AtGroupService;
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
import io.renren.modules.ltt.service.AtGroupTaskService;
import io.renren.modules.ltt.conver.AtGroupTaskConver;

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
    private AtGroupService atGroupService;
    @Override
    public void onGroupStart(AtGroupTaskDTO atGroupTask) {
        //分组
        List<OnGroupPreVO> onGroupPreVOS = onGroupPre(atGroupTask);
        //
        for (OnGroupPreVO onGroupPreVO : onGroupPreVOS) {
            //料子
            List<String> materialUrls = onGroupPreVO.getMaterialUrls();
            //保存群分组
            AtGroupEntity atGroupTaskEntity = new AtGroupEntity();
            atGroupTaskEntity.setGroupName(onGroupPreVO.getGroupName());
            atGroupTaskEntity.setUploadGroupNumber(materialUrls.size());
            atGroupTaskEntity.setCurrentExecutionsNumber(0);
            atGroupTaskEntity.setSuccessfullyAttractGroupsNumber(0);
            atGroupTaskEntity.setGroupStatus(GroupStatus.GroupStatus1.getKey());
            atGroupTaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            atGroupTaskEntity.setAddType(AddType.AddType2.getKey());
            atGroupTaskEntity.setCreateTime(DateUtil.date());
            atGroupTaskEntity.setMaterialPhoneType(MaterialPhoneType.MaterialType1.getKey());
            atGroupService.save(atGroupTaskEntity);
            //水军
            List<String> navyTextLists = onGroupPreVO.getNavyTextLists();

            AtDataTaskEntity atDataTask = new AtDataTaskEntity();
            atDataTask.setCreateTime(DateUtil.date());
            atDataTask.setDeleteFlag(DeleteFlag.NO.getKey());
            atDataTask.setAddTotalQuantity(0);
            atDataTask.setSuccessfulQuantity(0);
            atDataTask.setFailuresQuantity(0);
            atDataTask.setUpdateTime(DateUtil.date());
            atDataTask.setTaskStatus(TaskStatus.TaskStatus0.getKey());
            atDataTaskService.save(atDataTask);
            for (String navyTextList : navyTextLists) {
                String[] parts = navyTextList.split("\\s+");
                AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                save.setGroupType(GroupType.GroupType5.getKey());
                save.setDataTaskId(atDataTask.getId());
//                c.setUserId(atUserEntity.getId());
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
                save.setDeleteFlag(DeleteFlag.NO.getKey());
                save.setCreateTime(DateUtil.date());
            }

            for (String materialUrl : materialUrls) {
                String[] parts = materialUrl.split("\\s+");
                AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                save.setGroupType(GroupType.GroupType5.getKey());
                save.setDataTaskId(atDataTask.getId());
//                c.setUserId(atUserEntity.getId());
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
                save.setDeleteFlag(DeleteFlag.NO.getKey());
                save.setCreateTime(DateUtil.date());
            }


        }
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
        System.out.println("Found numbers:");
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
