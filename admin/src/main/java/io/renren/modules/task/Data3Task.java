package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.AddFriendsByHomeRecommendDTO;
import io.renren.modules.client.dto.AddFriendsByUserTicket;
import io.renren.modules.client.dto.getUserTicketDTO;
import io.renren.modules.client.vo.SearchPhoneVO;
import io.renren.modules.client.vo.getUserTicketVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"task"})
public class Data3Task {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;


    static ReentrantLock task1Lock = new ReentrantLock();
    static ReentrantLock task2Lock = new ReentrantLock();
    static ReentrantLock task3Lock = new ReentrantLock();
    static ReentrantLock task4Lock = new ReentrantLock();

    @Autowired
    private LineService lineService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtAvatarService atAvatarService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Autowired
    private AtGroupService atGroupService;

    @Resource(name = "caffeineCacheDate")
    private Cache<Integer, Date> caffeineCacheDate;

//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {}



//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task3() {}


    /**
     * 获取type为1 加粉类型为手机号模式
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {


        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus2.getKey()).setGroupType(GroupType.GroupType3.getKey());
        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);

        //任务为空
        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
            log.info("Data2Task task2 atDataSubtaskEntities isEmpty");
            return;
        }

        //获取用户MAP
        List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskVO::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));

        for (AtDataSubtaskVO atDataSubtaskEntity : atDataSubtaskEntities) {
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskEntity.getUserId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        AtGroupEntity atGroupEntityConfig = null;
                        if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                            atGroupEntityConfig = atGroupService.getByIdCache(atDataSubtaskEntity.getGroupId());
                            if (ObjectUtil.isNull(atGroupEntityConfig)) {
                                return;
                            }
                            Date nextTime = caffeineCacheDate.getIfPresent(atGroupEntityConfig.getId());
                            if (ObjectUtil.isNotNull(nextTime)) {
                                DateTime now = DateUtil.date();
                                boolean after = now.after(nextTime);
                                if (!after) {
                                    return;
                                }
                            }else {
                                int i = RandomUtil.randomInt(3, 5);
                                Thread.sleep(i * 1000L);
                            }

                        }else {
                            int i = RandomUtil.randomInt(3, 5);
                            Thread.sleep(i * 1000L);
                        }
                        //查询mid
                        AtDataSubtaskVO atDataSubtaskVO = atDataSubtaskService.getById(atDataSubtaskEntity.getId());
                        if (ObjectUtil.isNull(atDataSubtaskVO)) {
                            return;
                        }
                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
                        //去设置区号
                        if (ObjectUtil.isNotNull(atGroupEntityConfig.getIpCountryCode())) {
                            cdLineIpProxyDTO.setCountryCode(atGroupEntityConfig.getIpCountryCode().longValue());
                        }
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }
                        SearchPhoneVO searchPhoneVO = null;
                        if (DataType.DataType3.getKey().equals(atDataSubtaskEntity.getDataType())) {
                            AtUserEntity one = atUserService.getOne(new QueryWrapper<AtUserEntity>().lambda()
                                    .eq(AtUserEntity::getTelephone,atDataSubtaskVO.getContactKey())
                            );
                            if (ObjectUtil.isNull(one)) {
                                return;
                            }
                            AtUserTokenVO atUserTokenVO = atUserTokenService.getById(one.getUserTokenId());
                            if (ObjectUtil.isNull(atUserTokenVO)) {
                                return;
                            }

                            //获取代理
                            CdLineIpProxyDTO cdLineIpProxyDTO1 = new CdLineIpProxyDTO();
                            cdLineIpProxyDTO1.setTokenPhone(one.getTelephone());
                            cdLineIpProxyDTO1.setLzPhone(one.getTelephone());
                            //去设置区号
                            if (ObjectUtil.isNotNull(atGroupEntityConfig.getIpCountryCode())) {
                                cdLineIpProxyDTO.setCountryCode(atGroupEntityConfig.getIpCountryCode().longValue());
                            }
                            String proxyIp1 = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO1);
                            if (StrUtil.isEmpty(proxyIp1)) {
                                return;
                            }
                            getUserTicketDTO getUserTicketDTO = new getUserTicketDTO();
                            getUserTicketDTO.setProxy(proxyIp1);
                            getUserTicketDTO.setToken(atUserTokenVO.getToken());
                            getUserTicketVO userTicket = lineService.getUserTicket(getUserTicketDTO);
                            if (200 == userTicket.getCode()) {
                                String id = userTicket.getData().getId();
                                AddFriendsByUserTicket addFriendsByHomeRecommendDTO = new AddFriendsByUserTicket();
                                addFriendsByHomeRecommendDTO.setProxy(proxyIp);
                                addFriendsByHomeRecommendDTO.setMid(atDataSubtaskVO.getMid());
                                addFriendsByHomeRecommendDTO.setToken(atUserTokenEntity.getToken());
                                addFriendsByHomeRecommendDTO.setTicketId(id);
                                searchPhoneVO = lineService.addFriendsByUserTicket(addFriendsByHomeRecommendDTO);
                            }else {
                                return;
                            }
                        }else {
                            AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO = new AddFriendsByHomeRecommendDTO();
                            addFriendsByHomeRecommendDTO.setProxy(proxyIp);
                            addFriendsByHomeRecommendDTO.setMid(atDataSubtaskVO.getMid());
                            addFriendsByHomeRecommendDTO.setToken(atUserTokenEntity.getToken());
                            searchPhoneVO = lineService.addFriendsBySearchV3(addFriendsByHomeRecommendDTO);
                        }
                        AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                        if (ObjectUtil.isNull(searchPhoneVO)) {
                            return;
                        }
                        update.setMsg(searchPhoneVO.getMsg());
                        if (200 == searchPhoneVO.getCode()) {
                            update.setTaskStatus(TaskStatus.TaskStatus8.getKey());
                        } else if (201 == searchPhoneVO.getCode()) {
                            if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode10.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode11.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else {
                                UserStatus userStatus = UserStatus.UserStatus4;
                                update.setTaskStatus(TaskStatus.TaskStatus13.getKey());
                                //需要刷新token
                                if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
                                    userStatus = UserStatus.UserStatus7;
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
                                    userStatus = UserStatus.UserStatus3;
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
                                    userStatus = UserStatus.UserStatus2;
                                    //如果失败，修改状态
                                    update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
                                    userStatus = UserStatus.UserStatus3;
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode5.getValue())) {
                                    userStatus = UserStatus.UserStatus3;
                                }
                                //任务失败
                                AtDataTaskEntity atDataTaskEntity = new AtDataTaskEntity();
                                atDataTaskEntity.setId(atDataSubtaskEntity.getDataTaskId());
                                atDataTaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                atDataTaskService.updateById(atDataTaskEntity);
                                if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                                    //拉群改状态
                                    AtGroupEntity atGroupEntity = new AtGroupEntity();
                                    atGroupEntity.setId(atDataSubtaskEntity.getGroupId());
                                    atGroupEntity.setGroupStatus(GroupStatus.GroupStatus11.getKey());
                                    atGroupService.updateById(atGroupEntity);
                                }

                                if (TaskStatus.TaskStatus5.getKey().equals(update.getTaskStatus()) || TaskStatus.TaskStatus13.getKey().equals(update.getTaskStatus())) {
                                    update.setId(null);
                                    atDataSubtaskService.update(update,new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                            .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                            .eq(AtDataSubtaskEntity::getUserId,atDataSubtaskEntity.getUserId())
                                            .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                                    );
                                    atUserService.unlock(atDataSubtaskEntity.getUserId(),userStatus);
                                }
                            }
                        }
                        update.setId(atDataSubtaskEntity.getId());
                        //设置加好友的时间
                        update.setCreateTime(DateUtil.date());
                        atDataSubtaskService.updateById(update);
                        //如果配置不为空
                        if (ObjectUtil.isNotNull(atGroupEntityConfig)) {
                            //获取间隔的秒,设置下次可以执行的时间
                            Integer intervalSecond = atGroupEntityConfig.getIntervalSecond();
                            if (ObjectUtil.isNotNull(intervalSecond)) {
                                int i = RandomUtil.randomInt(intervalSecond, intervalSecond + 2);
                                DateTime nextTime = DateUtil.offsetSecond(DateUtil.date(), i);
                                caffeineCacheDate.put(atGroupEntityConfig.getId(),nextTime);
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("err = ",e.getMessage());
                    } finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });
        }
    }


//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task1() {}
}
