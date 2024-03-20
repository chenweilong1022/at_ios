package io.renren.modules.app.service.impl;

import io.renren.config.FileConfig;
import io.renren.modules.app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        String saveUrl = fileConfig.getSaveurl() + fileName + ".txt";

        File file = new File(saveUrl);
        file.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {

            for (String text : textList) {
                bufferedWriter.write(text);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveUrl;
    }
}
