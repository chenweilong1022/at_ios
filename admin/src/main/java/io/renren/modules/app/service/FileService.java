package io.renren.modules.app.service;

import java.io.IOException;
import java.util.List;

/**
 * @author huyan
 * @date 2024/3/20
 */
public interface FileService {

    /**
     * 写入文件
     *
     * @param fileName 文件名
     * @param textList 文件行内容
     */
    String writeTxtFile(String fileName, List<String> textList) throws IOException;
}
