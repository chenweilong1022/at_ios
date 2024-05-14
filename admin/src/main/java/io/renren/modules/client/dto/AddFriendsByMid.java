package io.renren.modules.client.dto;

import lombok.Data;

@Data
public class AddFriendsByMid {

    private String proxy;
    private String phone;
    private String mid;
    private String friendAddType;
    private String token;
}
