package io.renren.modules.client;

import io.renren.modules.client.dto.*;
import io.renren.modules.client.vo.*;
import io.renren.modules.ltt.vo.IssueLiffViewVO;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:56
 */
public interface LineService {

    LineRegisterVO lineRegister(LineRegisterDTO lineRegisterDTO);

    IssueLiffViewVO issueLiffView(IssueLiffViewDTO issueLiffViewDTO);

    RefreshAccessTokenVO refreshAccessToken(RefreshAccessTokenDTO issueLiffViewDTO);

    RegisterResultVO registerResult(RegisterResultDTO registerResultDTO);

    SMSCodeVO smsCode(SMSCodeDTO smsCodeDTO);

    SyncLineTokenVO SyncLineTokenDTO(SyncLineTokenDTO syncLineTokenDTO);

    SearchPhoneVO searchPhone(SearchPhoneDTO searchPhoneDTO);

    LineRegisterVO createGroupMax(CreateGroupMax createGroupMax);

    SearchPhoneVO findAndAddContactsByPhone(SearchPhoneDTO searchPhoneDTO);

    SearchPhoneVO addFriendsByMid(AddFriendsByMid addFriendsByMid);

    SearchPhoneVO addFriendsByReference(AddFriendsByMid addFriendsByMid);
    SearchPhoneVO addFriendsByHomeRecommend(AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO);

    CreateGroupResultVO createGroupResult(RegisterResultDTO registerResultDTO);

    LineRegisterVO openApp(OpenApp openApp);

    OpenAppResult openAppResult(RegisterResultDTO registerResultDTO);

    LineRegisterVO syncContents(SyncContentsDTO syncContentsDTO);
    SyncContentsResultVO syncContentsResult(SyncContentsResultDTO syncContentsResultDTO);

    GetChatsVO getChats(GetChatsDTO getChatsDTO);
    RegisterResultVO createThread(CreateThreadDTO createThreadDTO);

    CreateThreadResultVO createThreadResult(RegisterResultDTO registerResultDTO);

    ShareTextMsgVO shareTextMsg(ShareTextMsgDTO shareTextMsgDTO);

    EncryptedAccessTokenVO encryptedAccessToken(EncryptedAccessTokenDTO dto);

    ShareImgMsgVO shareImgMsg(ShareImgMsgDTO shareImgMsgDTO);

    UpdateProfileImageVO updateProfileImage(UpdateProfileImageDTO updateProfileImageDTO);

    UpdateProfileImageResultVO updateProfileImageResult(UpdateProfileImageResultDTO updateProfileImageResultDTO);

    GetAllContactIdsVO getAllContactIds(GetAllContactIdsDTO getAllContactIdsDTO);

    GetContactsInfoV3VO getContactsInfoV3(GetContactsInfoV3DTO getContactsInfoV3DTO);

    UpdateProfileNameVO updateProfileName(UpdateProfileNameDTO updateProfileNameDTO);

    ConversionAppTokenVO conversionAppToken(String iosToken);
}
