package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/4/15 00:28
 */
@Data
public class AddFriendsByUserTicket {
    private String proxy;
    private String mid;
    private String ticketId;
    private String token;
}
