package io.renren.modules.app.service.impl;

import io.renren.config.FileConfig;
import io.renren.modules.app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author huyan
 * @date 2024/3/20
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileConfig fileConfig;

    /**
     * 写入文件
     *
     * @param fileName 文件名
     * @param textList 文件行内容
     */
    @Override
    public String writeTxtFile(String fileName, List<String> textList) throws IOException {
        fileName = fileName + ".txt";
        String saveUrl = fileConfig.getSaveurl() + fileName;

        File file = new File(saveUrl);
        try {
            file.createNewFile(); // 创建文件，如果文件已存在则不执行任何操作

            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter bufferedWriter = new BufferedWriter(osw)) {

                for (String text : textList) {
                    bufferedWriter.write(text);
                    bufferedWriter.newLine();
                }

                bufferedWriter.flush(); // 确保所有内容都被写出
                return fileConfig.getBaseurl() + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
