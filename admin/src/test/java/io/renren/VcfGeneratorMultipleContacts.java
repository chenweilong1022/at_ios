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
                "+821062181138",
                "+821053602838",
                "+821067682383",
                "+821077428186",
                "+821072353201",
                "+821087887446",
                "+821047827385",
                "+821066714455",
                "+821045264814",
                "+821047840548",
                "+821087205314",
                "+821081311118",
                "+821064538335",
                "+821041706036",
                "+821046157255",
                "+821042637734",
                "+821075474781",
                "+821021040583",
                "+821066220571",
                "+821026243157",
                "+821033870171",
                "+821030031550",
                "+821041057000",
                "+821048860250",
                "+821072020716",
                "+821024688876",
                "+821051217414",
                "+821051836666",
                "+821020488117",
                "+821022257642",
                "+821094702265",
                "+821082549048",
                "+818080564208"
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
