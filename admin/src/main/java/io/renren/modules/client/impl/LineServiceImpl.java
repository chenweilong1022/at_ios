package io.renren.modules.client.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.ConfigConstant;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.*;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.*;
import io.renren.modules.ltt.entity.CdLineIpProxyEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.ProxyStatus;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.IssueLiffViewVO;
import io.renren.modules.sys.entity.SysConfigEntity;
import io.renren.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:57
 */
@Service("lineServiceImpl")
@Game
@Slf4j
public class LineServiceImpl implements LineService {


    @Autowired
    private SysConfigService sysConfigService;
    @Resource
    private SystemConstant systemConstant;

    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    @Override
    public LineRegisterVO lineRegister(LineRegisterDTO lineRegisterDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/lineRegister",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(lineRegisterDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr,30000);
            if (code300(lineRegisterDTO.getProxy(), jsonStr, resp)) return new LineRegisterVO().setCode(300L).setMsg("网络异常");

            LineRegisterVO lineRegisterVO = JSON.parseObject(resp, LineRegisterVO.class);

            extracted(jsonStr, resp);
            return lineRegisterVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public IssueLiffViewVO issueLiffView(IssueLiffViewDTO issueLiffViewDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/issueLiffView",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(issueLiffViewDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr,20000);
            log.info("param = {},resp = {}", jsonStr, resp);
            if (code300(issueLiffViewDTO.getProxy(), jsonStr, resp)) return new IssueLiffViewVO().setCode(300L).setMsg("网络异常");
            IssueLiffViewVO issueLiffViewVO = JSON.parseObject(resp, IssueLiffViewVO.class);
            extracted(jsonStr, resp);
            return issueLiffViewVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public RefreshAccessTokenVO refreshAccessToken(RefreshAccessTokenDTO issueLiffViewDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/refreshAccessToken",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(issueLiffViewDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr,20000);
            if (code300(issueLiffViewDTO.getProxy(), jsonStr, resp)) return new RefreshAccessTokenVO().setCode(300L).setMsg("网络异常");

            RefreshAccessTokenVO registerResultVO = JSON.parseObject(resp, RefreshAccessTokenVO.class);
            extracted(jsonStr, resp);
            return registerResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    private void extracted(String jsonStr, String resp) {
        CdLineErrLogsDTO cdLineErrLogsDTO = new CdLineErrLogsDTO();
        cdLineErrLogsDTO.setParams(jsonStr);
        cdLineErrLogsDTO.setResults(resp);
        cdLineErrLogsDTO.setCreateTime(DateUtil.date());
        cdLineErrLogsDTO.setDeleteFlag(DeleteFlag.NO.getKey());

        log.info("param = {},resp = {}", jsonStr, resp);
    }

    @Override
    public RegisterResultVO registerResult(RegisterResultDTO registerResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/registerResult",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(registerResultDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            RegisterResultVO registerResultVO = JSON.parseObject(resp, RegisterResultVO.class);

            extracted(jsonStr, resp);
            return registerResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }


    @Override
    public SMSCodeVO smsCode(SMSCodeDTO smsCodeDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/smsCode",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(smsCodeDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);

            SMSCodeVO smsCodeVO = JSON.parseObject(resp, SMSCodeVO.class);

            extracted(jsonStr, resp);
            return smsCodeVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SyncLineTokenVO SyncLineTokenDTO(SyncLineTokenDTO syncLineTokenDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/syncLineToken",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(syncLineTokenDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            SyncLineTokenVO syncLineTokenVO = JSON.parseObject(resp, SyncLineTokenVO.class);

            extracted(jsonStr, resp);
            return syncLineTokenVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO searchPhone(SearchPhoneDTO searchPhoneDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/searchPhone",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(searchPhoneDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(searchPhoneDTO.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    private boolean code300(String proxy, String jsonStr, String resp) {
        log.info("param = {},resp = {}", jsonStr, resp);
        if (resp.contains("网络异常")) {
            CdLineIpProxyEntity cdLineIpProxyEntity = new CdLineIpProxyEntity();
            cdLineIpProxyEntity.setTokenPhone("");
            cdLineIpProxyService.update(cdLineIpProxyEntity,new QueryWrapper<CdLineIpProxyEntity>().lambda().eq(CdLineIpProxyEntity::getIp, proxy.replace("socks5://","")));
            return true;
        }
        return false;
    }

    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    @Override
    public getUserTicketVO getUserTicket(getUserTicketDTO searchPhoneDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/getUserTicket",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(searchPhoneDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            log.info("param = {},resp = {}", jsonStr, resp);
            if (code300(searchPhoneDTO.getProxy(), jsonStr, resp)) return new getUserTicketVO().setCode(300).setMsg("网络异常");
            getUserTicketVO searchPhoneVO = JSON.parseObject(resp, getUserTicketVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override///work/searchUserId
    public SearchUserIdVO searchUserId(SearchUserIdDTO searchPhoneDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/searchUserId",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(searchPhoneDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            log.info("param = {},resp = {}", jsonStr, resp);
            if (code300(searchPhoneDTO.getProxy(), jsonStr, resp)) return new SearchUserIdVO().setCode(300).setMsg("网络异常");
            SearchUserIdVO searchPhoneVO = JSON.parseObject(resp, SearchUserIdVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override

    public LineRegisterVO createGroupMax(CreateGroupMax createGroupMax) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/createGroupMax",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(createGroupMax);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr,20000);
            if (code300(createGroupMax.getProxy(), jsonStr, resp)) return new LineRegisterVO().setCode(300).setMsg("网络异常");
            LineRegisterVO lineRegisterVO = JSON.parseObject(resp, LineRegisterVO.class);

            extracted(jsonStr, resp);
            return lineRegisterVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO updateChat(UpdateGroup updateGroup) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/msg/updateChat", projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(updateGroup);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr, 20000);
            log.info("updateChat_result {}, {}", jsonStr, resp);
            if (code300(updateGroup.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO lineRegisterVO = JSON.parseObject(resp, SearchPhoneVO.class);
            extracted(jsonStr, resp);
            return lineRegisterVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO findAndAddContactsByPhone(SearchPhoneDTO searchPhoneDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/findAndAddContactsByPhone",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(searchPhoneDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(searchPhoneDTO.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO addFriendsByMid(AddFriendsByMid addFriendsByMid) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/addFriendsByMid",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(addFriendsByMid);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(addFriendsByMid.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO addFriendsByUserTicket(AddFriendsByUserTicket addFriendsByMid) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/addFriendsByUserTicket",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(addFriendsByMid);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(addFriendsByMid.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO addFriendsByReference(AddFriendsByMid addFriendsByMid) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/addFriendsByReference",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(addFriendsByMid);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(addFriendsByMid.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO addFriendsByHomeRecommend(AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/addFriendsByHomeRecommend",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(addFriendsByHomeRecommendDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr,1000*180);
            if (code300(addFriendsByHomeRecommendDTO.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("reqp = {} err = {}",JSONUtil.toJsonStr(addFriendsByHomeRecommendDTO),e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO addFriendsByFriendRecommend(AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/addFriendsByFriendRecommend",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(addFriendsByHomeRecommendDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(addFriendsByHomeRecommendDTO.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SearchPhoneVO addFriendsBySearchV3(AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/addFriendsBySearchV3",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(addFriendsByHomeRecommendDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(addFriendsByHomeRecommendDTO.getProxy(), jsonStr, resp)) return new SearchPhoneVO().setCode(300).setMsg("网络异常");
            SearchPhoneVO searchPhoneVO = JSON.parseObject(resp, SearchPhoneVO.class);

            extracted(jsonStr, resp);
            return searchPhoneVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public CreateGroupResultVO createGroupResult(RegisterResultDTO registerResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/createGroupResult",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(registerResultDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            CreateGroupResultVO createGroupResultVO = JSON.parseObject(resp, CreateGroupResultVO.class);

            extracted(jsonStr, resp);
            return createGroupResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public LineRegisterVO inviteIntoChat(InviteIntoChatDTO registerResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/inviteIntoChat",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(registerResultDTO);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(registerResultDTO.getProxy(), jsonStr, resp)) return new LineRegisterVO().setCode(300).setMsg("网络异常");
            LineRegisterVO createGroupResultVO = JSON.parseObject(resp, LineRegisterVO.class);

            extracted(jsonStr, resp);
            return createGroupResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public LineRegisterVO openApp(OpenApp openApp) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/openApp",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(openApp);
            String resp = HttpUtil.post(getPhoneHttp, jsonStr);
            if (code300(openApp.getProxy(), jsonStr, resp)) return new LineRegisterVO().setCode(300).setMsg("网络异常");
            LineRegisterVO lineRegisterVO = JSON.parseObject(resp, LineRegisterVO.class);

            extracted(jsonStr, resp);
            return lineRegisterVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public OpenAppResult openAppResult(RegisterResultDTO registerResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/openAppResult",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(registerResultDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            OpenAppResult openAppResult = JSON.parseObject(resp, OpenAppResult.class);
            if (!resp.contains("正在运行")) {
                extracted(jsonStr, resp);
            }
            return openAppResult;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public LineRegisterVO syncContents(SyncContentsDTO syncContentsDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/syncContents",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(syncContentsDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(syncContentsDTO.getProxy(), jsonStr, resp)) return new LineRegisterVO().setCode(300).setMsg("网络异常");
            LineRegisterVO lineRegisterVO = JSON.parseObject(resp, LineRegisterVO.class);
            extracted(jsonStr, resp);
            return lineRegisterVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public SyncContentsResultVO syncContentsResult(SyncContentsResultDTO syncContentsResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/syncContentsResult",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(syncContentsResultDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            log.info("syncContentsResult resp = {}",resp);
            SyncContentsResultVO syncContentsResultVO = JSON.parseObject(resp, SyncContentsResultVO.class);
//            SyncContentsResultVO syncContentsResultVO1 = new SyncContentsResultVO();
//            syncContentsResultVO1.setCode(syncContentsResultVO.getCode());
//            syncContentsResultVO1.setMsg(syncContentsResultVO.getMsg());
//            extracted(jsonStr, JSONUtil.toJsonStr(syncContentsResultVO1));
            return syncContentsResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public GetChatsVO getChats(GetChatsDTO getChatsDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/getChats",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(getChatsDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr);
            if (code300(getChatsDTO.getProxy(), jsonStr, resp)) return new GetChatsVO().setCode(300).setMsg("网络异常");

            GetChatsVO getChatsVO = JSON.parseObject(resp, GetChatsVO.class);
            extracted(jsonStr, resp);
            return getChatsVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public RegisterResultVO createThread(CreateThreadDTO createThreadDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/msg/createThread",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(createThreadDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(createThreadDTO.getProxy(), jsonStr, resp)) return new RegisterResultVO().setCode(300L).setMsg("网络异常");

            RegisterResultVO syncContentsResultVO = JSON.parseObject(resp, RegisterResultVO.class);
            extracted(jsonStr, resp);
            return syncContentsResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public CreateThreadResultVO createThreadResult(RegisterResultDTO registerResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/msg/createThreadResult",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(registerResultDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);

            CreateThreadResultVO syncContentsResultVO = JSON.parseObject(resp, CreateThreadResultVO.class);
            extracted(jsonStr, resp);
            return syncContentsResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public ShareTextMsgVO shareTextMsg(ShareTextMsgDTO shareTextMsgDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/msg/shareTextMsg",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(shareTextMsgDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(shareTextMsgDTO.getProxy(), jsonStr, resp)) return new ShareTextMsgVO().setCode(300L).setMsg("网络异常");

            ShareTextMsgVO syncContentsResultVO = JSON.parseObject(resp, ShareTextMsgVO.class);
            extracted(jsonStr, resp);
            return syncContentsResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new ShareTextMsgVO().setCode(201);
        }
    }

    @Override
    public EncryptedAccessTokenVO encryptedAccessToken(EncryptedAccessTokenDTO dto) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/msg/encryptedAccessToken",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(dto);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(dto.getProxy(), jsonStr, resp)) return new EncryptedAccessTokenVO().setCode(300L).setMsg("网络异常");

            EncryptedAccessTokenVO syncContentsResultVO = JSON.parseObject(resp, EncryptedAccessTokenVO.class);
            extracted(jsonStr, resp);
            return syncContentsResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        return null;
    }

    @Override
    public ShareImgMsgVO shareImgMsg(ShareImgMsgDTO shareImgMsgDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/msg/shareImgMsg",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(shareImgMsgDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(shareImgMsgDTO.getProxy(), jsonStr, resp)) return new ShareImgMsgVO().setCode(300L).setMsg("网络异常");
            shareImgMsgDTO.setImgData("");
            String jsonStr1 = JSONUtil.toJsonStr(shareImgMsgDTO);
            ShareImgMsgVO syncContentsResultVO = JSON.parseObject(resp, ShareImgMsgVO.class);
            extracted(jsonStr1, resp);
            return syncContentsResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new ShareImgMsgVO().setCode(201);
        }
    }

    @Override
    public UpdateProfileImageVO updateProfileImage(UpdateProfileImageDTO updateProfileImageDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/updateProfileImage",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(updateProfileImageDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(updateProfileImageDTO.getProxy(), jsonStr, resp)) return new UpdateProfileImageVO().setCode(300L).setMsg("网络异常");

            UpdateProfileImageVO updateProfileImageVO = JSON.parseObject(resp, UpdateProfileImageVO.class);
            extracted(jsonStr, resp);
            return updateProfileImageVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new UpdateProfileImageVO();
        }
    }

    @Override
    public UpdateProfileImageResultVO updateProfileImageResult(UpdateProfileImageResultDTO updateProfileImageResultDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/updateProfileImageResult",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(updateProfileImageResultDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);

            UpdateProfileImageResultVO updateProfileImageResultVO = JSON.parseObject(resp, UpdateProfileImageResultVO.class);
            extracted(jsonStr, resp);
            return updateProfileImageResultVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new UpdateProfileImageResultVO();
        }
    }

    @Override
    public GetAllContactIdsVO getAllContactIds(GetAllContactIdsDTO getAllContactIdsDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/work/getAllContactIds",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(getAllContactIdsDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(getAllContactIdsDTO.getProxy(), jsonStr, resp)) return new GetAllContactIdsVO().setCode(300L).setMsg("网络异常");

            GetAllContactIdsVO updateProfileImageVO = JSON.parseObject(resp, GetAllContactIdsVO.class);
            extracted(jsonStr, resp);
            return updateProfileImageVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new GetAllContactIdsVO();
        }
    }

//    AppVersion:  "14.3.1",
//Ab:          "2024.307.2034",
    @Override
    public GetContactsInfoV3VO getContactsInfoV3(GetContactsInfoV3DTO getContactsInfoV3DTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp;
            if (getContactsInfoV3DTO.getToken().contains("14.3.1")) {
                getPhoneHttp = String.format("%s/api/v1/work/getContactsInfoV3",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            }else {
                getPhoneHttp = String.format("%s/api/v1/work/getContactsInfo",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            }
            String jsonStr = JSONUtil.toJsonStr(getContactsInfoV3DTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(getContactsInfoV3DTO.getProxy(), jsonStr, resp)) return new GetContactsInfoV3VO().setCode(300L).setMsg("网络异常");

            GetContactsInfoV3VO updateProfileImageVO = JSON.parseObject(resp, GetContactsInfoV3VO.class);
            extracted(jsonStr, resp);
            return updateProfileImageVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new GetContactsInfoV3VO();
        }
    }

    @Override
    public UpdateProfileNameVO updateProfileName(UpdateProfileNameDTO updateProfileNameDTO) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s/api/v1/account/updateProfileName",projectWorkEntity.getLineBaseHttp(systemConstant.getSERVERS_MOD()));
            String jsonStr = JSONUtil.toJsonStr(updateProfileNameDTO);
            String resp = HttpUtil.post(getPhoneHttp,jsonStr,20000);
            if (code300(updateProfileNameDTO.getProxy(), jsonStr, resp)) return new UpdateProfileNameVO().setCode(300L).setMsg("网络异常");

            UpdateProfileNameVO updateProfileImageVO = JSON.parseObject(resp, UpdateProfileNameVO.class);
            extracted(jsonStr, resp);
            return updateProfileImageVO;
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
            return new UpdateProfileNameVO();
        }
    }

    @EventListener
    @Order(value = 9999)//t35323ha-1027-61697		tha-1027-44108
    public void handlerApplicationReadyEvent(ApplicationReadyEvent event) {
        //配置项
        SysConfigEntity one = sysConfigService.getOne(new QueryWrapper<SysConfigEntity>().lambda()
                .eq(SysConfigEntity::getParamKey, ConfigConstant.PROJECT_WORK_KEY)
        );
        if (ObjectUtil.isNull(one)) {
            SysConfigEntity config = new SysConfigEntity();
            config.setLineBaseHttp("http://137.184.112.207:22117");
            config.setFirefoxBaseUrl("http://www.firefox.fun");
            config.setFirefoxToken("721713f744ee967b493bfb0362afcee2_46759");
            config.setFirefoxIid("1027");
            config.setFirefoxCountry("tha");
            config.setProxy(ProxyStatus.ProxyStatus1.getKey());
            config.setProxyUseCount(3);
            config.setFirefoxCountry1("th");
            config.setLineAb("2024.307.2034");
            config.setLineAppVersion("14.3.1");
            config.setLineTxtToken("81f9933e3a434a1aaf7af09893937fd0");
            config.setSfGetPhoneCodeUrl("http://sms.newszfang.vip:3000/api/smslist?token=iLrsPw55gHmkGkK52znKvm");
            config.setSfTimeZone(1);
            sysConfigService.save(config);
        }else {
            ProjectWorkEntity bean = JSONUtil.toBean(one.getParamValue(), ProjectWorkEntity.class);
            if (ObjectUtil.isNotNull(bean)) {
                SysConfigEntity config = new SysConfigEntity();
                config.setId(one.getId());
                config.setLineBaseHttp(bean.getLineBaseHttp());
                config.setFirefoxBaseUrl(bean.getFirefoxBaseUrl());
                config.setFirefoxToken(bean.getFirefoxToken());
                config.setFirefoxIid(bean.getFirefoxIid());
                config.setFirefoxCountry(bean.getFirefoxCountry());
                config.setProxy(bean.getProxy());
                config.setProxyUseCount(bean.getProxyUseCount());
                config.setFirefoxCountry1(bean.getFirefoxCountry1());

                config.setLineAb(bean.getLineAb());
                config.setLineAppVersion(bean.getLineAppVersion());
                config.setLineTxtToken(bean.getLineTxtToken());
                config.setSfGetPhoneCodeUrl(bean.getSfGetPhoneCodeUrl());
                config.setSfTimeZone(bean.getSfTimeZone());

                sysConfigService.update(config);
            }
        }
    }

    @Override
    public ConversionAppTokenVO conversionAppToken(String iosToken) {
        try {
            String getPhoneHttp = String.format("%s/api/v1/magicServer/conversionAppToken",systemConstant.getMAGIC_SERVER_URL());
            String resp = HttpUtil.post(getPhoneHttp, iosToken);
            log.info("param = {},resp = {}", iosToken, resp);
            ConversionAppTokenVO issueLiffViewVO = JSON.parseObject(resp, ConversionAppTokenVO.class);
            extracted(iosToken, resp);
            return issueLiffViewVO;
        }catch (Exception e) {
            log.error("err = {}",e);
        }
        return null;
    }
}
