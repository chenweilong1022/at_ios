package io.renren.modules.ltt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Options;

import java.io.Serializable;
import java.util.Date;

/**
 * 加粉任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 10:53:33
 */

@TableName("at_data_subtask")
@ApiModel("加粉任务子任务")
@Accessors(chain = true)
public class AtDataSubtaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")

	private Integer id;
	/**
	 * 数据任务id
	 */
	@ApiModelProperty(required=false,value="数据任务id")
	private Integer dataTaskId;
	/**
	 * 账户id
	 */
	@ApiModelProperty(required=false,value="账户id")
	private Integer userId;

	@ApiModelProperty(required=false,value="修改群信息水军id")
	private Integer changeUserId;
	/**
	 * 任务状态
	 */
	@ApiModelProperty(required=false,value="任务状态")
	private Integer taskStatus;
	/**
	 * 类型
	 */
	@ApiModelProperty(required=false,value="类型")
	private Integer groupType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String luid;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String contactType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String contactKey;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String mid;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String createdTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String type;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String status;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String relation;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String displayName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phoneticName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pictureStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String thumbnailUrl;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String statusMessage;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String displayNameOverridden;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String favoriteTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableVoiceCall;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableVideoCall;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableMyhome;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableBuddy;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String attributes;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String settings;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String picturePath;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String recommendpArams;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String friendRequestStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String musicProfile;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String videoProfile;
	/**
	 * 删除标志
	 */
	@ApiModelProperty(required=false,value="删除标志")
	private Integer deleteFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required=false,value="创建时间")
	private Date createTime;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required=false,value="修改时间")
	private Date updateTime;
	/**
	 * line协议的任务id
	 */
	@ApiModelProperty(required=false,value="line协议的任务id")
	private String lineTaskId;
	/**
	 * line的协议返回信息
	 */
	@ApiModelProperty(required=false,value="line的协议返回信息")
	private String msg;
	/**
	 * 刷新联系人状态
	 */
	@ApiModelProperty(required=false,value="刷新联系人状态")
	private Integer refreshContactStatus;

	/**
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;
	/**
	 * 数据类型
	 */
	@ApiModelProperty(required=false,value="数据类型")
	private Integer dataType;
	/**
	 * 群id
	 */
	@ApiModelProperty(required=false,value="群id")
	private Integer groupId;
	/**
	 * 主键
	 */
	@ApiModelProperty(required=false,value="记录id")
	private Integer recordId;
	/**
	 * 打开app
	 */
	@ApiModelProperty(required=false,value="打开app")
	private Integer openApp;
	/**
	 * mod
	 */
	@ApiModelProperty(required=false,value="mod")
	@TableField(exist = false)
	private Integer mod;
	/**
	 * totalMod
	 */
	@ApiModelProperty(required=false,value="totalMod")
	@TableField(exist = false)
	private Integer totalMod;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDataTaskId() {
		return dataTaskId;
	}

	public void setDataTaskId(Integer dataTaskId) {
		this.dataTaskId = dataTaskId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getChangeUserId() {
		return changeUserId;
	}

	public void setChangeUserId(Integer changeUserId) {
		this.changeUserId = changeUserId;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public String getLuid() {
		return luid;
	}

	public void setLuid(String luid) {
		this.luid = luid;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getContactKey() {
		return contactKey;
	}

	public void setContactKey(String contactKey) {
		this.contactKey = contactKey;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPhoneticName() {
		return phoneticName;
	}

	public void setPhoneticName(String phoneticName) {
		this.phoneticName = phoneticName;
	}

	public String getPictureStatus() {
		return pictureStatus;
	}

	public void setPictureStatus(String pictureStatus) {
		this.pictureStatus = pictureStatus;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getDisplayNameOverridden() {
		return displayNameOverridden;
	}

	public void setDisplayNameOverridden(String displayNameOverridden) {
		this.displayNameOverridden = displayNameOverridden;
	}

	public String getFavoriteTime() {
		return favoriteTime;
	}

	public void setFavoriteTime(String favoriteTime) {
		this.favoriteTime = favoriteTime;
	}

	public String getCapableVoiceCall() {
		return capableVoiceCall;
	}

	public void setCapableVoiceCall(String capableVoiceCall) {
		this.capableVoiceCall = capableVoiceCall;
	}

	public String getCapableVideoCall() {
		return capableVideoCall;
	}

	public void setCapableVideoCall(String capableVideoCall) {
		this.capableVideoCall = capableVideoCall;
	}

	public String getCapableMyhome() {
		return capableMyhome;
	}

	public void setCapableMyhome(String capableMyhome) {
		this.capableMyhome = capableMyhome;
	}

	public String getCapableBuddy() {
		return capableBuddy;
	}

	public void setCapableBuddy(String capableBuddy) {
		this.capableBuddy = capableBuddy;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getRecommendpArams() {
		return recommendpArams;
	}

	public void setRecommendpArams(String recommendpArams) {
		this.recommendpArams = recommendpArams;
	}

	public String getFriendRequestStatus() {
		return friendRequestStatus;
	}

	public void setFriendRequestStatus(String friendRequestStatus) {
		this.friendRequestStatus = friendRequestStatus;
	}

	public String getMusicProfile() {
		return musicProfile;
	}

	public void setMusicProfile(String musicProfile) {
		this.musicProfile = musicProfile;
	}

	public String getVideoProfile() {
		return videoProfile;
	}

	public void setVideoProfile(String videoProfile) {
		this.videoProfile = videoProfile;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getLineTaskId() {
		return lineTaskId;
	}

	public void setLineTaskId(String lineTaskId) {
		this.lineTaskId = lineTaskId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getRefreshContactStatus() {
		return refreshContactStatus;
	}

	public void setRefreshContactStatus(Integer refreshContactStatus) {
		this.refreshContactStatus = refreshContactStatus;
	}

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getOpenApp() {
		return openApp;
	}

	public void setOpenApp(Integer openApp) {
		this.openApp = openApp;
	}

	public Integer getMod() {
		return mod;
	}

	public void setMod(Integer mod) {
		this.mod = mod;
	}

	public Integer getTotalMod() {
		return totalMod;
	}

	public void setTotalMod(Integer totalMod) {
		this.totalMod = totalMod;
	}
}
