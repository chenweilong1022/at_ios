package io.renren;

import cn.hutool.core.collection.CollUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VcfGeneratorMultipleContacts {

    static class Contact {
        String name;
        List<String> phones;

        Contact(String name) {
            this.name = name;
            this.phones = new ArrayList<>();
        }

        void addPhone(String phone) {
            phones.add(phone);
        }
    }

    public static void main(String[] args) {
        List<Contact> contacts = new ArrayList<>();

        ArrayList<String> phones = CollUtil.newArrayList(
                "+886917600078",
                "+886975323303",
                "+886928313295",
                "+886953554740",
                "+886978396355",
                "+886928504409",
                "+886982131938",
                "+886919613556",
                "+886952250207",
                "+886989130621",
                "+886934295566",
                "+886927993231",
                "+886939580937",
                "+886912816612",
                "+886961553142",
                "+886934146589",
                "+886915180606",
                "+886910769755",
                "+886960922322",
                "+886907488335",
                "+886983573629",
                "+886921359859",
                "+886953224415",
                "+886936627019",
                "+886932102970",
                "+886933070467",
                "+886928717188",
                "+886958787333",
                "+886976705592",
                "+886917175939",
                "+886961425125",
                "+886920028737",
                "+886928909072",
                "+886910791067",
                "+886965662833",
                "+886925190034"
        );

        for (String phone : phones) {
            // 示例联系人
            Contact johnDoe = new Contact("phone=" + phone);
            johnDoe.addPhone("TEL;TYPE=HOME:"+phone);
            contacts.add(johnDoe);
        }

        // 构建vCard字符串
        StringBuilder vCardBuilder = new StringBuilder();

        for (Contact contact : contacts) {
            vCardBuilder.append("BEGIN:VCARD\n");
            vCardBuilder.append("VERSION:3.0\n");
            vCardBuilder.append("FN:").append(contact.name).append("\n");
            vCardBuilder.append("N:").append(contact.name).append(";;;;\n");
            for (String phone : contact.phones) {
                vCardBuilder.append(phone).append("\n");
            }
            vCardBuilder.append("END:VCARD\n");
        }

        // 文件路径和名称
        String filePath = "contacts.vcf";

        // 将vCard信息写入文件
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(vCardBuilder.toString());
            System.out.println("VCF文件已成功生成，包含多个联系人和电话号码！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
