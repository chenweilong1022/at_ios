package io.renren.App2;

import cn.hutool.core.io.FileUtil;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/5/10 00:02
 */
public class txt {

    public static void main(String[] args) {
        List<String> updateNames = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/0509/律吕/25群测试/改群号.txt", "UTF-8");


        int i = 1;

        for (String updateName : updateNames) {
            List<String> strings = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/0509/律吕/25群测试/乘风1.txt", "UTF-8");
            List<String> newStrings = strings;
            newStrings.add(updateName);
            FileUtil.writeUtf8Lines(newStrings,"/Users/chenweilong/Desktop/java代码/at_ios/file/0509/律吕/25群测试/s" + i + ".txt");
            i++;
        }


    }
}
