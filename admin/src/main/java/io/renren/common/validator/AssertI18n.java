package io.renren.common.validator;

import cn.hutool.core.lang.Validator;
import io.renren.common.base.interfaces.BaseCode;
import io.renren.common.exception.RRException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:50
 */
public abstract class AssertI18n {

    public static void isBlank(String str, BaseCode baseCode) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(baseCode);
        }
    }

    public static void isTrue(boolean flag,  BaseCode baseCode) {
        if (flag) {
            throw new RRException(baseCode);
        }
    }

    public static void isNull(Object object,  BaseCode baseCode) {
        if (object == null) {
            throw new RRException(baseCode);
        }
    }

    public static<T> void isNullArray(T[] object,  BaseCode baseCode) {
        if (object == null || object.length == 0) {
            throw new RRException(baseCode);
        }
    }

    public static void isPhone(String phone) {
        if (!Validator.isMobile(phone)) {
            throw new RRException("手机格式错误");
        }
    }

}
