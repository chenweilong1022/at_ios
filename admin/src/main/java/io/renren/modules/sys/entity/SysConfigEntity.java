/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统配置信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月4日 下午6:43:36
 */
@TableName("sys_config")
@Data
public class SysConfigEntity extends AbstractEntity implements Serializable {
	@TableId(type = IdType.AUTO)
	private Long id;
	@NotBlank(message="参数名不能为空")
	private String paramKey;
	@NotBlank(message="参数值不能为空")
	private String paramValue;
	@NotBlank(message="备注不能为空")
	private String remark;
	private Integer status;

	@TableField(exist = false)
	private String lineBaseHttp;
	@TableField(exist = false)
	private String firefoxBaseUrl;
	@TableField(exist = false)
	private String firefoxToken;
	@TableField(exist = false)
	private String firefoxIid;
	@TableField(exist = false)
	private String firefoxCountry;
	@TableField(exist = false)
	private String firefoxCountry1;
	@TableField(exist = false)
	private Integer proxy;
	@TableField(exist = false)
	private Integer proxyUseCount;

	@TableField(exist = false)
	private String lineAb;
	@TableField(exist = false)
	private String lineAppVersion;
	@TableField(exist = false)
	private String lineTxtToken;
}
