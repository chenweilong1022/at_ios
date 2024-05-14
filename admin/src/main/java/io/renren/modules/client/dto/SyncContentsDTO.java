package io.renren.modules.client.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/12 23:25
 */
@Data
public class SyncContentsDTO {
    private String proxy;
    private List<String> phoneList;
    private String token;
}
