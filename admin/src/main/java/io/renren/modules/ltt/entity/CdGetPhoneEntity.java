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
 * @date 2024-03-27 12:36:12
 */

@TableName("cd_get_phone")
@ApiModel("")
@Accessors(chain = true)
@Getter
@Setter
public class CdGetPhoneEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private String number;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pkey;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String time;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String country;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String countrycode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String other;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String com;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phone;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String code;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer phoneStatus;
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
	 *首次取号时间
	 */
	@ApiModelProperty(required=false,value="")
	private Date firstEnterTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer subtasksId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer lineRegisterId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String sfApi;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer timeZone;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPhoneStatus() {
		return phoneStatus;
	}

	public void setPhoneStatus(Integer phoneStatus) {
		this.phoneStatus = phoneStatus;
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

	public Integer getSubtasksId() {
		return subtasksId;
	}

	public void setSubtasksId(Integer subtasksId) {
		this.subtasksId = subtasksId;
	}

	public Integer getLineRegisterId() {
		return lineRegisterId;
	}

	public void setLineRegisterId(Integer lineRegisterId) {
		this.lineRegisterId = lineRegisterId;
	}

	public String getSfApi() {
		return sfApi;
	}

	public void setSfApi(String sfApi) {
		this.sfApi = sfApi;
	}

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}
}
