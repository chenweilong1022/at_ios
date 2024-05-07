package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.Query;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.app.service.FileService;
import io.renren.modules.ltt.conver.CdPhoneFilterConver;
import io.renren.modules.ltt.dao.CdPhoneFilterDao;
import io.renren.modules.ltt.dao.CdPhoneFilterRecordDao;
import io.renren.modules.ltt.dto.CdPhoneFilterDTO;
import io.renren.modules.ltt.dto.CdPhoneFilterStatusDto;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
import io.renren.modules.ltt.entity.CdPhoneFilterRecordEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.service.CdPhoneFilterRecordService;
import io.renren.modules.ltt.service.CdPhoneFilterService;
import io.renren.modules.ltt.vo.CdPhoneFilterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static io.renren.modules.ltt.enums.PhoneFilterStatus.PhoneFilterStatus3;


@Service("cdPhoneFilterService")
@Game
@Slf4j
public class CdPhoneFilterServiceImpl extends ServiceImpl<CdPhoneFilterDao, CdPhoneFilterEntity> implements CdPhoneFilterService {

    @Resource
    private CdPhoneFilterRecordService cdPhoneFilterRecordService;

    @Resource
    private CdPhoneFilterRecordDao cdPhoneFilterRecordDao;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Autowired
    private AtUserService atUserService;

    @Resource
    private FileService fileService;

    @Override
    public PageUtils<CdPhoneFilterVO> queryPage(CdPhoneFilterDTO cdPhoneFilter) {
        IPage<CdPhoneFilterEntity> page = baseMapper.selectPage(
                new Query<CdPhoneFilterEntity>(cdPhoneFilter).getPage(),
                new QueryWrapper<CdPhoneFilterEntity>().lambda()
                        .eq(CdPhoneFilterEntity::getRecordId, cdPhoneFilter.getRecordId())
                        .eq(ObjectUtil.isNotNull(cdPhoneFilter.getTaskStatus()), CdPhoneFilterEntity::getTaskStatus, cdPhoneFilter.getTaskStatus())
                        .orderByDesc(CdPhoneFilterEntity::getRecordId)
        );

        return PageUtils.<CdPhoneFilterVO>page(page).setList(CdPhoneFilterConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public CdPhoneFilterVO getById(Integer id) {
        return CdPhoneFilterConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(CdPhoneFilterDTO cdPhoneFilter) {
        List<String> textUrlList = cdPhoneFilter.getTextUrlList();
        Assert.isTrue(CollectionUtil.isEmpty(textUrlList),"文件链接不能为空");

        List<CdPhoneFilterEntity> cdPhoneFilterEntities = new ArrayList<>();
        for (String textUrl : textUrlList) {
        //料子数据
        String materialText = HttpUtil.downloadString(textUrl, "UTF-8");
        String[] materialTextSplit = materialText.split("\n");
        if (ObjectUtil.isNull(materialTextSplit) || materialTextSplit.length == 0) {
            log.error("料子数据为空 {}, {}", textUrl, cdPhoneFilter);
            continue;
        }

        for (String s : materialTextSplit) {
            if (StrUtil.isEmpty(s)) {
                continue;
            }
            PhoneCountryVO phoneNumberInfo = null;
            try {
                s = StrUtil.cleanBlank(s);
                s = s.replace("-","");
                phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(s);
            } catch (Exception e) {
                if (s.contains("@") || s.contains("#")) {
                    CdPhoneFilterEntity cdPhoneFilterEntity = new CdPhoneFilterEntity();
                    cdPhoneFilterEntity.setContactKey(s);
                    cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus2.getKey());
                    cdPhoneFilterEntities.add(cdPhoneFilterEntity);
                }
            }
            if (ObjectUtil.isNotNull(phoneNumberInfo)) {
                CdPhoneFilterEntity cdPhoneFilterEntity = new CdPhoneFilterEntity();
                cdPhoneFilterEntity.setCountryCode(phoneNumberInfo.getCountryCode());
                cdPhoneFilterEntity.setContactKey(s);
                cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus2.getKey());
                cdPhoneFilterEntities.add(cdPhoneFilterEntity);
            }
        }
        }

        Assert.isTrue(CollectionUtil.isEmpty(cdPhoneFilterEntities), "上传数据不能为空");

        //数据记录总表
        CdPhoneFilterRecordEntity recordEntity = new CdPhoneFilterRecordEntity();
        recordEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus2.getKey());
        recordEntity.setFileUrl(JSON.toJSONString(cdPhoneFilter.getTextUrlList()));
        recordEntity.setCreateTime(new Date());
        recordEntity.setTotalCount((long)cdPhoneFilterEntities.size());
        recordEntity.setSuccessCount(0l);
        recordEntity.setFailCount(0l);
        cdPhoneFilterRecordService.save(recordEntity);


        List<AtDataSubtaskEntity> atDataSubtaskEntities = new ArrayList<>();
        //获取通讯录map
        Map<Long, List<CdPhoneFilterEntity>> longListMap = cdPhoneFilterEntities.stream().collect(Collectors.groupingBy(CdPhoneFilterEntity::getCountryCode));
        for (Long key : longListMap.keySet()) {
            if (ObjectUtil.isNotNull(key)) {
                List<CdPhoneFilterEntity> cdPhoneFilterEntitiesNew = longListMap.get(key);
                //给用户分配一个user
                if (CollectionUtil.isNotEmpty(cdPhoneFilterEntitiesNew) && cdPhoneFilterEntitiesNew.size() > 20) {

                    String regions = EnumUtil.queryValueByKey(key.intValue(), CountryCode.values());
                    List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                            .in(AtUserEntity::getStatus, UserStatus.UserStatus4.getKey())
                            .eq(AtUserEntity::getNation, regions)
                            .orderByDesc(AtUserEntity::getId)
                            .last("limit 1")
                    );
                    Assert.isTrue(CollUtil.isEmpty(atUserEntities),"账号不足，请增加账号");
                    //修改用户为已使用
                    AtUserEntity poll = atUserEntities.get(0);
                    AtUserEntity atUserEntity = new AtUserEntity();
                    atUserEntity.setId(poll.getId());
                    atUserEntity.setStatus(UserStatus.UserStatus6.getKey());
                    atUserService.updateById(atUserEntity);

                    for (CdPhoneFilterEntity cdPhoneFilterEntity : cdPhoneFilterEntitiesNew) {
                        //s设置为通讯录
                        cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus5.getKey());
                        GroupType groupType4 = GroupType.GroupType5;
                        AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                        save.setGroupType(groupType4.getKey());
                        save.setRecordId(recordEntity.getRecordId());
                        save.setUserId(atUserEntity.getId());
                        save.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                        save.setDataType(DataType.DataType4.getKey());
                        save.setContactKey(cdPhoneFilterEntity.getContactKey());
                        //校验通讯录模式的国家
                        if (GroupType.GroupType5.getKey().equals(groupType4.getKey())) {
                            Assert.isTrue(StrUtil.isEmpty(save.getContactKey()),"手机号不能为空");
                        }
                        save.setDeleteFlag(DeleteFlag.NO.getKey());
                        save.setCreateTime(DateUtil.date());
                        atDataSubtaskEntities.add(save);
                    }

                }
            }
        }
        //如果加粉任务不为空
        if (CollUtil.isNotEmpty(atDataSubtaskEntities)) {
            atDataSubtaskService.saveBatchOnMe(atDataSubtaskEntities);
        }



        //数据记录
        cdPhoneFilterEntities.forEach(i->{
            i.setRecordId(recordEntity.getRecordId());
            i.setCreateTime(new Date());
            i.setDeleteFlag(DeleteFlag.NO.getKey());
        });
        boolean b = this.saveBatch(cdPhoneFilterEntities, cdPhoneFilterEntities.size() + 1);
        return b;
    }

    @Override
    public boolean updateById(CdPhoneFilterDTO cdPhoneFilter) {
        CdPhoneFilterEntity cdPhoneFilterEntity = CdPhoneFilterConver.MAPPER.converDTO(cdPhoneFilter);
        return this.updateById(cdPhoneFilterEntity);
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
    public List<String> exportSJ(Long recordId) throws Exception{
        CdPhoneFilterRecordEntity recordEntity = cdPhoneFilterRecordService.getById(recordId);
        Assert.isTrue(ObjectUtil.isNull(recordEntity) || ObjectUtil.isNull(recordEntity.getFileUrl()),"数据为空，请刷新重试");
        //文件地址
        List<String> textUrlList = JSON.parseArray(recordEntity.getFileUrl(), String.class);

        List<CdPhoneFilterEntity> list = baseMapper.selectList(new QueryWrapper<CdPhoneFilterEntity>().lambda()
                .eq(CdPhoneFilterEntity::getRecordId, recordEntity.getRecordId())
        );
        Map<String, CdPhoneFilterEntity> contactKeyMap = list.stream()
                .collect(Collectors.toMap(CdPhoneFilterEntity::getContactKey, item -> item,(s1,s2) -> s1));

        Boolean phoneFlag = false;
        List<String> fileUrlList = new ArrayList<>();
        for (String textUrl : textUrlList) {
            //料子数据
            String materialText = HttpUtil.downloadString(textUrl, "UTF-8");
            String[] materialTextSplit = materialText.split("\n");
            if (ObjectUtil.isNull(materialTextSplit) || materialTextSplit.length == 0) {
                log.error("料子数据为空 {}, {}", textUrl, recordId);
                continue;
            }

            List<String> fileTextList = new ArrayList<>();
            for (String s : materialTextSplit) {
                if (StrUtil.isEmpty(s)) {
                    continue;
                }
                PhoneCountryVO phoneNumberInfo = null;
                phoneFlag = false;
                try {
                    s = StrUtil.cleanBlank(s);
                    phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(s.replace("-",""));
                    if (ObjectUtil.isNotNull(phoneNumberInfo)) {
                        phoneFlag = true;
                    }
                } catch (Exception e) {
                    if (s.contains("@") || s.contains("#")) {
                        phoneFlag = true;
                    }
                }
                if (Boolean.TRUE.equals(phoneFlag)) {
                    CdPhoneFilterEntity cdPhoneFilterEntity = contactKeyMap.get(s);
                    if (cdPhoneFilterEntity != null
                            && PhoneFilterStatus3.getKey().equals(cdPhoneFilterEntity.getTaskStatus())) {
                        s = String.format("%s   %s  %s", s, cdPhoneFilterEntity.getMid(), cdPhoneFilterEntity.getDisplayName());
                        fileTextList.add(s);
                    } else {
                        fileTextList.add(s);
                    }
                } else {
                    fileTextList.add(s);
                }
            }
            //上传文件
            fileUrlList.add(fileService
                    .writeTxtFile(String.valueOf(System.currentTimeMillis()), fileTextList));
        }
        return fileUrlList;
    }

    @Override
    public CdPhoneFilterStatusDto queryByTaskStatus(Integer recordId) {
        return baseMapper.queryByTaskStatus(recordId);
    }

}
