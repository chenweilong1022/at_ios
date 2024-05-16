package io.renren.modules.ltt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@TableName("cd_line_register")
@ApiModel("")
@Accessors(chain = true)
@Getter
@Setter
public class CdLineRegisterEntity implements Serializable {
	private static final long serialVersionUID = 2L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer id;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String ab;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String appVersion;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String countryCode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phone;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String proxy;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer proxyStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String txtToken;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String taskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String smsCode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer registerStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer deleteFlag;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date createTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date openTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String token;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer getPhoneId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pkey;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer subtasksId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer groupTaskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer openStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer accountStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer groupCount;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="首次卡/二次卡")
	private Integer accountExistStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String errMsg;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer exportStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAb() {
		return ab;
	}

	public void setAb(String ab) {
		this.ab = ab;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public Integer getProxyStatus() {
		return proxyStatus;
	}

	public void setProxyStatus(Integer proxyStatus) {
		this.proxyStatus = proxyStatus;
	}

	public String getTxtToken() {
		return txtToken;
	}

	public void setTxtToken(String txtToken) {
		this.txtToken = txtToken;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public Integer getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(Integer registerStatus) {
		this.registerStatus = registerStatus;
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

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getGetPhoneId() {
		return getPhoneId;
	}

	public void setGetPhoneId(Integer getPhoneId) {
		this.getPhoneId = getPhoneId;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public Integer getSubtasksId() {
		return subtasksId;
	}

	public void setSubtasksId(Integer subtasksId) {
		this.subtasksId = subtasksId;
	}

	public Integer getGroupTaskId() {
		return groupTaskId;
	}

	public void setGroupTaskId(Integer groupTaskId) {
		this.groupTaskId = groupTaskId;
	}

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	public Integer getAccountExistStatus() {
		return accountExistStatus;
	}

	public void setAccountExistStatus(Integer accountExistStatus) {
		this.accountExistStatus = accountExistStatus;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Integer getExportStatus() {
		return exportStatus;
	}

	public void setExportStatus(Integer exportStatus) {
		this.exportStatus = exportStatus;
	}
}
