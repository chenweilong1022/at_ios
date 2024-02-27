package io.renren.modules.app.code;

import io.renren.common.base.interfaces.BaseCode;
import lombok.Getter;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2022/4/22 14:52
 */
@Getter
public enum UserCode implements BaseCode {

    USER_ID_MIN_ERROR(1_0001,"UserUpdateDTO.id.min.error"),
    ;

    private Integer code;
    private String key;

    UserCode(Integer code, String key) {
        this.code = code;
        this.key = key;
    }
}
