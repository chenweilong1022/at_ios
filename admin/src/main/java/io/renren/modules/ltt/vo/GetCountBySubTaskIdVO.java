package io.renren.modules.ltt.vo;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/28 17:42
 */
@Data
public class GetCountBySubTaskIdVO {
    private Integer subtasksId;
    private Integer totalCount;
    private Integer successCount;
    private Integer registerSuccessCount;
    private Integer errorCount;
}
