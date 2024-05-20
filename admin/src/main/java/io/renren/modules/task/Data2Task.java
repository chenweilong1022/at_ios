package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.constant.SystemConstant;
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
import io.renren.modules.ltt.vo.AtUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.parser.feature.Feature.update;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"prod","task"})
public class Data2Task {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SystemConstant systemConstant;


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

//    /**
//     * 更新头像结果返回
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {}

//    /**
//     * 更新头像结果返回
//     */
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
        Set<String> members = stringRedisTemplate.opsForSet().members(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(systemConstant.getSERVERS_MOD())));
        //任务为空
        if (CollUtil.isEmpty(members)) {
            log.info("Data2Task task2 atDataSubtaskEntities isEmpty");
            return;
        }
        for (String userId : members) {
            threadPoolTaskExecutor.execute(() -> {
                String USER_TASKS_WORKING_NX_KEY = RedisKeys.USER_TASKS_WORKING_NX.getValue(userId);
                Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(USER_TASKS_WORKING_NX_KEY, userId);
                if (!b) {
                    return;
                }
                redisTemplate.expire(USER_TASKS_WORKING_NX_KEY, 2, TimeUnit.MINUTES);
                AtDataSubtaskEntity atDataSubtaskEntity = null;
                try {
                    atDataSubtaskEntity = (AtDataSubtaskEntity) redisTemplate.opsForList().rightPop(RedisKeys.USER_TASKS_WORKING.getValue(userId));
                    if (ObjectUtil.isNull(atDataSubtaskEntity)) {
                        return;
                    }
                    if (!TaskStatus.TaskStatus2.getKey().equals(atDataSubtaskEntity.getTaskStatus())) {
                        return;
                    }
                    AtUserTokenEntity atUserTokenEntity = atUserTokenService.getByUserIdCache(Integer.valueOf(userId));
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        return;
                    }
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
                            nextTime = DateUtil.offsetSecond(DateUtil.date(), i);
                            caffeineCacheDate.put(atGroupEntityConfig.getId(),nextTime);
                        }

                    }else {
                        int i = RandomUtil.randomInt(3, 5);
                        Date nextTime = DateUtil.offsetSecond(DateUtil.date(), i);
                        caffeineCacheDate.put(atGroupEntityConfig.getId(),nextTime);
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
                        AtUserTokenEntity atUserTokenVO = atUserTokenService.getByUserIdCache(atDataSubtaskEntity.getChangeUserId());
                        if (ObjectUtil.isNull(atUserTokenVO)) {
                            return;
                        }

                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO1 = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO1.setTokenPhone(atUserTokenVO.getTelephone());
                        cdLineIpProxyDTO1.setLzPhone(atUserTokenVO.getTelephone());
                        //去设置区号
                        if (ObjectUtil.isNotNull(atGroupEntityConfig.getIpCountryCode())) {
                            cdLineIpProxyDTO1.setCountryCode(atGroupEntityConfig.getIpCountryCode().longValue());
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
                            addFriendsByHomeRecommendDTO.setMid(atDataSubtaskEntity.getMid());
                            addFriendsByHomeRecommendDTO.setToken(atUserTokenEntity.getToken());
                            addFriendsByHomeRecommendDTO.setTicketId(id);
                            searchPhoneVO = lineService.addFriendsByUserTicket(addFriendsByHomeRecommendDTO);
                        }else {
                            return;
                        }
                    }else {
                        AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO = new AddFriendsByHomeRecommendDTO();
                        addFriendsByHomeRecommendDTO.setProxy(proxyIp);
                        addFriendsByHomeRecommendDTO.setMid(atDataSubtaskEntity.getMid());
                        addFriendsByHomeRecommendDTO.setToken(atUserTokenEntity.getToken());
                        if (GroupType.GroupType2.getKey().equals(atDataSubtaskEntity.getGroupType())) {
                            searchPhoneVO = lineService.addFriendsByHomeRecommend(addFriendsByHomeRecommendDTO);
                        }else if (GroupType.GroupType4.getKey().equals(atDataSubtaskEntity.getGroupType())) {
                            searchPhoneVO = lineService.addFriendsByFriendRecommend(addFriendsByHomeRecommendDTO);
                        }
                    }
                    if (ObjectUtil.isNull(searchPhoneVO)) {
                        return;
                    }
                    atDataSubtaskEntity.setMsg(searchPhoneVO.getMsg());
                    if (200 == searchPhoneVO.getCode()) {
                        //加粉成功
                        atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus8.getKey());
                    } else if (201 == searchPhoneVO.getCode()) {
                        if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode10.getValue())) {
                            atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                        } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode11.getValue())) {
                            atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                        } else {
                            UserStatus userStatus = UserStatus.UserStatus4;
                            atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus13.getKey());
                            //需要刷新token
                            if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
                                userStatus = UserStatus.UserStatus7;
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
                                userStatus = UserStatus.UserStatus3;
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
                                userStatus = UserStatus.UserStatus2;
                                //如果失败，修改状态
                                atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
                                userStatus = UserStatus.UserStatus3;
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode5.getValue())) {
                                userStatus = UserStatus.UserStatus3;
                            }

                            AtDataTaskEntity atDataTaskEntity = new AtDataTaskEntity();
                            atDataTaskEntity.setId(atDataSubtaskEntity.getDataTaskId());
                            atDataTaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            //任务失败 设置加粉任务队列
                            redisTemplate.opsForList().leftPush(RedisKeys.ATDATATASKENTITY_LIST.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), atDataTaskEntity);
                            if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                                //拉群改状态
                                AtGroupEntity atGroupEntity = new AtGroupEntity();
                                atGroupEntity.setId(atDataSubtaskEntity.getGroupId());
                                atGroupEntity.setGroupStatus(GroupStatus.GroupStatus11.getKey());
                                // 任务失败 设置群任务队列
                                redisTemplate.opsForList().leftPush(RedisKeys.ATGROUPENTITY_LIST.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), atGroupEntity);
                            }

                            if (TaskStatus.TaskStatus5.getKey().equals(atDataSubtaskEntity.getTaskStatus()) || TaskStatus.TaskStatus13.getKey().equals(atDataSubtaskEntity.getTaskStatus())) {
                                //如果错误了清空队列，状态设置和其他一致
                                List<AtDataSubtaskEntity> elements = new ArrayList<>();
                                AtDataSubtaskEntity element = null;
                                while ((element = (AtDataSubtaskEntity) redisTemplate.opsForList().rightPop(RedisKeys.USER_TASKS_WORKING.getValue(userId))) != null) {
                                    element.setTaskStatus(atDataSubtaskEntity.getTaskStatus());
                                    element.setMsg(atDataSubtaskEntity.getMsg());
                                    elements.add(element);
                                }
                                for (AtDataSubtaskEntity dataSubtaskEntity : elements) {
                                    //工作完成队列
                                    Long l = redisTemplate.opsForList().leftPush(RedisKeys.USER_TASKS_WORK_FINISH.getValue(userId), dataSubtaskEntity);
                                    log.info("工作完成队列保存成功条数 ====》 {}",l);
                                }
                                AtUserEntity atUserEntity = new AtUserEntity();
                                atUserEntity.setId(atDataSubtaskEntity.getUserId());
                                atUserEntity.setStatus(userStatus.getKey());
                                Long l1 = redisTemplate.opsForList().leftPush(RedisKeys.ATUSERENTITY_LIST.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), atUserEntity);
                                log.info("用户任务队列保存成功条数 ====》 {}",l1);
                            }
                        }
                    }
                    //设置加好友的时间
                    atDataSubtaskEntity.setCreateTime(DateUtil.date());
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
                }catch (Exception e){
                    log.error("data2Task error = {}",e.getMessage());
                }finally {
                    stringRedisTemplate.delete(USER_TASKS_WORKING_NX_KEY);
                    Long l = 0L;
                    if (ObjectUtil.isNull(atDataSubtaskEntity)) {
                        return;
                    }
                    //如果状态还是2
                    if (TaskStatus.TaskStatus2.getKey().equals(atDataSubtaskEntity.getTaskStatus())) {
                        l = redisTemplate.opsForList().rightPush(RedisKeys.USER_TASKS_WORKING.getValue(userId), atDataSubtaskEntity);
                        log.info("任务失败，队列返回 ====》 {}",l);
                    }else {
                        //任务结束，队列移除到任务结束队列 释放用户锁
                        l = redisTemplate.opsForList().leftPush(RedisKeys.USER_TASKS_WORK_FINISH.getValue(userId), atDataSubtaskEntity);
                        log.info("任务结束，队列移除到任务结束队列保存成功条数 ====》 {}",l);
                    }
                }
            });
        }

    }


//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task1() {}
}
