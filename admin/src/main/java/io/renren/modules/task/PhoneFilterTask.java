//package io.renren.modules.task;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import io.renren.modules.client.LineService;
//import io.renren.modules.client.dto.SearchPhoneDTO;
//import io.renren.modules.client.dto.SearchUserIdDTO;
//import io.renren.modules.client.vo.SearchPhoneVO;
//import io.renren.modules.client.vo.SearchUserIdVO;
//import io.renren.modules.client.vo.The818051863582;
//import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
//import io.renren.modules.ltt.entity.AtUserEntity;
//import io.renren.modules.ltt.entity.AtUserTokenEntity;
//import io.renren.modules.ltt.entity.CdLineRegisterEntity;
//import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
//import io.renren.modules.ltt.enums.PhoneFilterStatus;
//import io.renren.modules.ltt.enums.RegisterStatus;
//import io.renren.modules.ltt.enums.UserStatus;
//import io.renren.modules.ltt.service.AtUserService;
//import io.renren.modules.ltt.service.AtUserTokenService;
//import io.renren.modules.ltt.service.CdLineIpProxyService;
//import io.renren.modules.ltt.service.CdPhoneFilterService;
//import io.renren.modules.ltt.vo.AtDataSubtaskVO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//import java.util.concurrent.locks.ReentrantLock;
//import java.util.stream.Collectors;
//
///**
// * @author liuyuchan
// * @email liuyuchan286@gmail.com
// * @date 2023/11/21 18:45
// */
//@Component
//@Slf4j
//@EnableAsync
//public class PhoneFilterTask {
//
//
//
//    static ReentrantLock task1Lock = new ReentrantLock();
//
//    @Autowired
//    private LineService lineService;
//
//    public static final Object phoneFilterObj = new Object();
//    @Autowired
//    ThreadPoolTaskExecutor threadPoolTaskExecutor;
//    @Autowired
//    private CdPhoneFilterService cdPhoneFilterService;
//    @Autowired
//    private AtUserService atUserService;
//    @Autowired
//    private AtUserTokenService atUserTokenService;
//    @Autowired
//    private CdLineIpProxyService cdLineIpProxyService;
//    /**
//     * 获取初始化的添加粉任务
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task1() {
//        boolean b = task1Lock.tryLock();
//        if (!b) {
//            return;
//        }
//        try{
//            List<CdPhoneFilterEntity> cdPhoneFilterEntities = cdPhoneFilterService.list(new QueryWrapper<CdPhoneFilterEntity>().lambda()
//                    .eq(CdPhoneFilterEntity::getTaskStatus, PhoneFilterStatus.PhoneFilterStatus1.getKey())
//                    .last("limit 10")
//            );
//
//            if (CollUtil.isEmpty(cdPhoneFilterEntities)) {
//                log.info("PhoneFilterTask task1 cdPhoneFilterEntities isEmpty");
//                return;
//            }
//
//
//            List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
//                    .in(AtUserEntity::getStatus, UserStatus.UserStatus4.getKey(),UserStatus.UserStatus6.getKey())
//            );
//
//            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
//            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
//            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
//            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
//
//
//            Queue<AtUserEntity> cdLineRegisterEntityQueue = new LinkedList<>(atUserEntities);
//            AtUserEntity poll = cdLineRegisterEntityQueue.poll();
//
//            List<CdPhoneFilterEntity> cdPhoneFilterEntitiesUpdate = new ArrayList<>();
//
//            for (int i = 0; i < cdPhoneFilterEntities.size(); i++) {
//
//                CdPhoneFilterEntity cdPhoneFilterEntity = cdPhoneFilterEntities.get(i);
//
//                //获取用户token
//                AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(poll.getId());
//                if (ObjectUtil.isNull(atUserTokenEntity)) {
//                    return;
//                }
//
//                //获取代理
//                CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                cdLineIpProxyDTO.setTokenPhone(poll.getTelephone());
//                cdLineIpProxyDTO.setLzPhone(poll.getTelephone());
//                String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                if (StrUtil.isEmpty(proxyIp)) {
//                    return;
//                }
//                SearchPhoneDTO searchPhoneDTO = new SearchPhoneDTO();
//                searchPhoneDTO.setProxy(proxyIp);
//                searchPhoneDTO.setPhone(cdPhoneFilterEntity.getContactKey());
//                searchPhoneDTO.setToken(atUserTokenEntity.getToken());
//                SearchPhoneVO andAddContactsByPhone = null;
//                if (cdPhoneFilterEntity.getContactKey().contains("@")) {
//                    SearchUserIdDTO searchUserIdDTO = new SearchUserIdDTO();
//                    searchUserIdDTO.setProxy(proxyIp);
//                    searchUserIdDTO.setUserId(cdPhoneFilterEntity.getContactKey());
//                    searchUserIdDTO.setToken(atUserTokenEntity.getToken());
//                    SearchUserIdVO searchUserIdVO = lineService.searchUserId(searchUserIdDTO);
//                    if (ObjectUtil.isNotNull(searchUserIdVO)) {
//                        if (200 != searchUserIdVO.getCode()) {
//                            cdPhoneFilterEntity.setMsg(andAddContactsByPhone.getMsg());
////                        cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus4.getKey());
//                            cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
//                            atUserService.unlock(poll.getId(),UserStatus.UserStatus2);
//                            if (CollUtil.isNotEmpty(cdPhoneFilterEntitiesUpdate)) {
//                                synchronized (phoneFilterObj) {
//                                    cdPhoneFilterService.updateBatchById(cdPhoneFilterEntitiesUpdate);
//                                }
//                            }
//                            return;
//                        } else {
//                            String mid = searchUserIdVO.getData().getMid();
//                            String displayName = searchUserIdVO.getData().getDisplayName();
//                            cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus3.getKey());
//                            cdPhoneFilterEntity.setMid(mid);
//                            cdPhoneFilterEntity.setDisplayName(displayName);
//                            cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
//                        }
//                    }
//                    continue;
//                }else {
//                    andAddContactsByPhone = lineService.searchPhone(searchPhoneDTO);
//                }
//
//
//
//                if (ObjectUtil.isNotNull(andAddContactsByPhone)) {
//                    if (200 != andAddContactsByPhone.getCode()) {
//                        cdPhoneFilterEntity.setMsg(andAddContactsByPhone.getMsg());
////                        cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus4.getKey());
//                        cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
//                        atUserService.unlock(poll.getId(),UserStatus.UserStatus2);
//                        if (CollUtil.isNotEmpty(cdPhoneFilterEntitiesUpdate)) {
//                            synchronized (phoneFilterObj) {
//                                cdPhoneFilterService.updateBatchById(cdPhoneFilterEntitiesUpdate);
//                            }
//                        }
//                        return;
//                    } else {
//                        Map<String, The818051863582> data = andAddContactsByPhone.getData();
//                        if (CollUtil.isNotEmpty(data)) {
//                            The818051863582 the8180518635821 = data.values().stream().findFirst().get();
//                            if (ObjectUtil.isNotNull(the8180518635821)) {
//                                cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus3.getKey());
//                                cdPhoneFilterEntity.setMid(the8180518635821.getMid());
//                                cdPhoneFilterEntity.setDisplayName(the8180518635821.getDisplayName());
//                                cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
//                            }
//                        }else {
//                            cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus4.getKey());
//                            cdPhoneFilterEntitiesUpdate.add(cdPhoneFilterEntity);
//                        }
//                    }
//
//                }
//            }
//            if (CollUtil.isNotEmpty(cdPhoneFilterEntitiesUpdate)) {
//                synchronized (phoneFilterObj) {
//                    cdPhoneFilterService.updateBatchById(cdPhoneFilterEntitiesUpdate);
//                }
//            }
//        }catch (Exception e) {
//            log.error("err = {}",e.getMessage());
//        }finally {
//            task1Lock.unlock();
//        }
//
//    }
//}
