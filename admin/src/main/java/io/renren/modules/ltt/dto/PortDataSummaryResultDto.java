package io.renren.modules.ltt.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huyan
 * @date 2024/3/21
 */
@Data
public class PortDataSummaryResultDto implements Serializable {
    //端口总数
    private Integer portNumTotal;

    //过期时间->取最大的时间
    private String expireTime;
    //端口剩余
    private Integer portNumSurplus;

}
