//package io.renren.modules.task;
//import java.util.*;
//
//public class ListFiller {
//
//    public static void main(String[] args) {
//        List<Integer> originalList = Arrays.asList(1, 2, 3); // 原始列表
//        int n = 10; // 目标总数
//
//        List<Integer> resultList = new ArrayList<>(originalList); // 以原始列表初始化结果列表
//
//        while (resultList.size() < n) {
//            for (Integer element : originalList) {
//                if (resultList.size() < n) {
//                    resultList.add(element);
//                } else {
//                    break; // 如果达到目标大小，则停止添加
//                }
//            }
//        }
//
//    }
//}
