package io.renren.modules.client.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/13 01:35
 */
@Data
public class CreateGroupMaxDTO {
    private List<String> userMidList;
    private String proxy;
    private String groupName;
    private String token;
}
