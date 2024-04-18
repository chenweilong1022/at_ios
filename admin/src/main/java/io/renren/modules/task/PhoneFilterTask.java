package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.SearchPhoneDTO;
import io.renren.modules.client.dto.SearchUserIdDTO;
import io.renren.modules.client.vo.SearchPhoneVO;
import io.renren.modules.client.vo.SearchUserIdVO;
import io.renren.modules.client.vo.The818051863582;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.vo.CdPhoneFilterRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static io.renren.modules.ltt.enums.PhoneFilterStatus.PhoneFilterStatus3;
import static io.renren.modules.ltt.enums.PhoneFilterStatus.PhoneFilterStatus4;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"prod","register"})
public class PhoneFilterTask {



    static ReentrantLock task1Lock = new ReentrantLock();

    @Autowired
    private LineService lineService;

    public static final Object phoneFilterObj = new Object();
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private CdPhoneFilterRecordService cdPhoneFilterRecordService;
    @Autowired
    private CdPhoneFilterService cdPhoneFilterService;
    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    /**
     * 获取初始化的添加粉任务
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {
        boolean b = task1Lock.tryLock();
        if (!b) {
            return;
        }
        try{
            List<CdPhoneFilterEntity> cdPhoneFilterEntities = cdPhoneFilterService.list(new QueryWrapper<CdPhoneFilterEntity>().lambda()
                    .eq(CdPhoneFilterEntity::getTaskStatus, PhoneFilterStatus.PhoneFilterStatus2.getKey())
                    .last("limit 10")
            );

            if (CollUtil.isEmpty(cdPhoneFilterEntities)) {
                log.info("PhoneFilterTask task1 cdPhoneFilterEntities isEmpty");
                return;
            }


            List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                    .in(AtUserEntity::getStatus, UserStatus.UserStatus4.getKey(),UserStatus.UserStatus6.getKey())
                    .eq(AtUserEntity::getNation, CountryCode.CountryCode1.getValue())
                    .orderByDesc(AtUserEntity::getId)
                    .last("limit 50")
            );

            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().filter(item -> ObjectUtil.isNotNull(atUserTokenEntityMap.get(item.getUserTokenId()))).collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));


            Queue<AtUserEntity> cdLineRegisterEntityQueue = new LinkedList<>(atUserEntities);
            AtUserEntity poll = cdLineRegisterEntityQueue.poll();

            List<CdPhoneFilterEntity> cdPhoneFilterEntitiesUpdate = new ArrayList<>();

            for (int i = 0; i < cdPhoneFilterEntities.size(); i++) {

                CdPhoneFilterEntity cdPhoneFilterEntity = cdPhoneFilterEntities.get(i);

                //获取用户token
                AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(poll.getId());
                if (ObjectUtil.isNull(atUserTokenEntity)) {
                    continue;
                }

                //获取代理
                CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                cdLineIpProxyDTO.setTokenPhone(poll.getTelephone());
                cdLineIpProxyDTO.setLzPhone(poll.getTelephone());
                String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                if (StrUtil.isEmpty(proxyIp)) {
                    continue;
                }
                SearchPhoneDTO searchPhoneDTO = new SearchPhoneDTO();
                searchPhoneDTO.setProxy(proxyIp);
                searchPhoneDTO.setPhone(cdPhoneFilterEntity.getContactKey());
                searchPhoneDTO.setToken(atUserTokenEntity.getToken());
                SearchPhoneVO andAddContactsByPhone = null;
                if (cdPhoneFilterEntity.getContactKey().contains("@") || cdPhoneFilterEntity.getContactKey().contains("#")) {
                    SearchUserIdDTO searchUserIdDTO = new SearchUserIdDTO();
                    searchUserIdDTO.setProxy(proxyIp);
                    searchUserIdDTO.setUserId(cdPhoneFilterEntity.getContactKey().replace("#",""));
                    searchUserIdDTO.setToken(atUserTokenEntity.getToken());
                    SearchUserIdVO searchUserIdVO = lineService.searchUserId(searchUserIdDTO);
                    if (ObjectUtil.isNotNull(searchUserIdVO)) {
                        if (200 != searchUserIdVO.getCode()) {
                            cdPhoneFilterEntity.setMsg(andAddContactsByPhone.getMsg());
//                        cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus4.getKey());
                            cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
                            atUserService.unlock(poll.getId(),UserStatus.UserStatus2);
                            continue;
                        } else {
                            String mid = searchUserIdVO.getData().getMid();
                            String displayName = searchUserIdVO.getData().getDisplayName();
                            cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus3.getKey());
                            cdPhoneFilterEntity.setMid(mid);
                            cdPhoneFilterEntity.setDisplayName(displayName);
                            cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
                        }
                    }
                    continue;
                }else {
                    andAddContactsByPhone = lineService.searchPhone(searchPhoneDTO);
                    if (ObjectUtil.isNotNull(andAddContactsByPhone)) {
                        if (200 != andAddContactsByPhone.getCode()) {
                            cdPhoneFilterEntity.setMsg(andAddContactsByPhone.getMsg());
//                        cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus4.getKey());
                            cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
                            atUserService.unlock(poll.getId(),UserStatus.UserStatus2);
                            continue;
                        } else {
                            Map<String, The818051863582> data = andAddContactsByPhone.getData();
                            if (CollUtil.isNotEmpty(data)) {
                                The818051863582 the8180518635821 = data.values().stream().findFirst().get();
                                if (ObjectUtil.isNotNull(the8180518635821)) {
                                    cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus3.getKey());
                                    cdPhoneFilterEntity.setMid(the8180518635821.getMid());
                                    cdPhoneFilterEntity.setDisplayName(the8180518635821.getDisplayName());
                                    cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
                                }
                            }else {
                                cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus4.getKey());
                                cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
                            }
                        }

                    }
                }
            }
            if (CollUtil.isNotEmpty(cdPhoneFilterEntitiesUpdate)) {
                synchronized (phoneFilterObj) {
                    for (CdPhoneFilterEntity entity : cdPhoneFilterEntitiesUpdate) {
                       if (PhoneFilterStatus3.getKey().equals(entity.getTaskStatus())) {
                           entity.setMsg("");
                       }
                    }
                    cdPhoneFilterService.updateBatchById(cdPhoneFilterEntitiesUpdate);
                    Map<Integer, List<CdPhoneFilterEntity>> recordMap = cdPhoneFilterEntitiesUpdate.stream()
                            .collect(Collectors.groupingBy(CdPhoneFilterEntity::getRecordId));



                    //更新主表
                    List<Integer> recordIdList = recordMap.keySet().stream().collect(Collectors.toList());
                    List<CdPhoneFilterRecordVO> recordList = cdPhoneFilterRecordService.getByIds(recordIdList);

                    List<CdPhoneFilterRecordEntity> updateRecordList = new ArrayList<>(recordMap.size());
                    CdPhoneFilterRecordEntity updateRecord;
                    List<CdPhoneFilterEntity> list;
                    for (CdPhoneFilterRecordVO recordVO : recordList) {
                        list = recordMap.get(recordVO.getRecordId());
                        updateRecord = new CdPhoneFilterRecordEntity();
                        updateRecord.setRecordId(recordVO.getRecordId());
                        updateRecord.setSuccessCount(recordVO.getSuccessCount() + list.stream()
                                .filter(i->PhoneFilterStatus3.getKey().equals(i.getTaskStatus())).count());
                        updateRecord.setFailCount(recordVO.getFailCount() + list.stream()
                                .filter(i->PhoneFilterStatus4.getKey().equals(i.getTaskStatus())).count());
                        if (updateRecord.getSuccessCount()+ updateRecord.getFailCount() >= recordVO.getTotalCount()) {
                            updateRecord.setTaskStatus(PhoneFilterStatus3.getKey());
                        }
                        if (updateRecord.getFailCount() >= recordVO.getTotalCount()) {
                            updateRecord.setTaskStatus(PhoneFilterStatus4.getKey());
                        }
                        updateRecordList.add(updateRecord);
                    }
                    cdPhoneFilterRecordService.updateBatchById(updateRecordList);

                }
            }
        }catch (Exception e) {
            log.error("PhoneFilterTask_err = {}", e);
        }finally {
            task1Lock.unlock();
        }

    }
}
