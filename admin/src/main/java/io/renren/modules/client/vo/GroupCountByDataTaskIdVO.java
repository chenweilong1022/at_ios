package io.renren.modules.client.vo;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/4/6 02:50
 */
@Data
public class GroupCountByDataTaskIdVO {
    private Integer success8;
    private Integer fail5;
    private Integer success10;
    private Integer dataTaskId;
    private Integer groupId;
    private Integer groupType;
    private Integer addTotalQuantity;
}
