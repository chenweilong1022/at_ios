package io.renren;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/4/17 04:42
 */
public class App {
    public static void main(String[] args) {
        List<String> strings1 = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/0514/泰国/1.txt", "");
        List<String> strings2 = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/0514/泰国/2.txt", "");
        strings1.addAll(strings2);
        long count = strings1.stream().distinct().count();
        System.out.println(count);
    }
}
