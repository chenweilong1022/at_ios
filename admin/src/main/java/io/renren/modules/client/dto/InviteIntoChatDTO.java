package io.renren.modules.client.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/4/14 19:56
 */
@Data
public class InviteIntoChatDTO {
    private String proxy;
    private String chatRoomId;
    private List<String> userMids;
    private String token;
}
