package io.renren;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/4/17 04:42
 */
public class App {
    public static void main(String[] args) {
//        List<String> strings = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/file/4月16号/泰国洋洋/料子.txt", "UTF-8");
//        int i = strings.size() / 3;
//        List<List<String>> partition = Lists.partition(strings, i);
//        int j = 0;
//        for (List<String> stringList : partition) {
//            j++;
//            FileUtil.writeUtf8Lines(stringList,"/Users/chenweilong/Desktop/java代码/at_ios/file/4月16号/泰国洋洋/" +j + ".txt");
//
//        }

        List<String> strings = FileUtil.readLines("/Users/chenweilong/Desktop/java代码/at_ios/admin/src/main/resources/banner.txt", "");
        List<String> collect = strings.stream().distinct().collect(Collectors.toList());
        System.out.println(collect.size());
        for (String s : collect) {

        }
    }
}
