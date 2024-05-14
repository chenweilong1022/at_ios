package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/13 02:01
 */
@Data
public class GetChatsDTO {
    private String proxy;
    private String chatRoomId;
    private String token;
}
