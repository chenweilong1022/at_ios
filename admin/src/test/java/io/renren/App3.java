package io.renren;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.enums.DataType;
import io.renren.modules.ltt.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/5/17 18:01
 */
public class App3 {

    public static void main(String[] args) throws Exception {
        List<String> strings1 = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/12W.txt", "UTF-8");
        List<String> strings2 = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/11060.txt", "UTF-8");
        List<String> strings3 = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/78524.txt", "UTF-8");
        strings1.addAll(strings2);
        strings1.addAll(strings3);
        System.out.println(strings1.size());
        List<String> phones = new ArrayList<>();
        for (String navyTextList : strings1) {
            try {
                if (StrUtil.isEmpty(navyTextList)) {
                    continue;
                }
                String[] parts = navyTextList.split("\\s+");
                String phone = parts[0].trim();
                PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(phone);
                String number = phoneNumberInfo.getNumber();
                phones.add(number);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        long count = phones.stream().distinct().count();
        System.out.println(count);
    }
}
