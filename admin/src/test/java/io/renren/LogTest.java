package io.renren;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import io.renren.modules.client.vo.CardMeGetPhoneVO;
import io.renren.modules.ltt.dto.IosTokenDTO;
import okhttp3.*;

import java.io.*;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2020-01-09 09:53
 */
public class LogTest {

    private static String  privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ0m87tfr+m1HvLB"
    +"iUkZtHGrKU5ue56u0aTUmeyk3u1zZuQSPCOHd6hPu4nSLa7bYTmieE+Gb8rwJbqa"
    +"u0aOno+arSG33GhavP8vA/SSa9mFFkHjtNcUaZVlOtHfTsvs6mnLb5RTKptIEzOz"
    +"XD27j/eSq1MsGgrCa/YtTjJX1gQPAgMBAAECgYBUhPGK8cCbkhTN/LbIQPHiPGrf"
    +"yt3jjyQjYVBTjKx0yp8oxIHhnecF97PmQMrfAw/8Plw0cRpI6/Vuse9M2EGAJJxq"
    +"94H56dpPhhMLvS/uRgWIAGhpT3cSi0HBuNY9VEXSzprn7fO1pGdRYGVRIq7am3+e"
    +"jTrB+k3nfI8InLXO2QJBAM4iOz5okJWGr/SOou/M6GLZSDkX9Qi3WQuuhTrNTzFV"
    +"15cnZuIEp9WiKjCfORwcJL6HGOLKVP0P/XKH+Sj9DPUCQQDDK1CLU8YFUGNm+fya"
    +"8jz/Fid5jimyRWJ4lopYUSD3yFHSVbY/20/PZr89tJqGR2LfsCZK13Rd7WS8F6e8"
    +"RSpzAkAMaU874Lvj5OovRW9WFPZhDUgl9+VPEwsPgwCOm9IK3GpQtZSiQzl/yXXU"
    +"26Fqqd8kganj7d7UJeRSwxEjgKkJAkEAmdfu6aTjlxTDBk1QPaNtSXZhL4RMgeYH"
    +"tR6VdwCciDUzqiU+QB/UTZykazOOCwMCgWkNBjnH1LJokYvkAJhU2QJBAKJRUvns"
    +"sYenn3k/dO0H7MZtkbHLQdMj3tNjYLNwhbh1Bv6IhveTFwAY0OEtYa52nRAu95Kw"
            +"KrZw1n9Cxo19XZE=";

    public static void main(String[] args) {

        String json = "{\n" +
                "  \"country\": \"TH\",\n" +
                "  \"bundleId\": \"jp.naver.line\",\n" +
                "  \"appUserId\": \"u8715b8c05a30a8e052a14ea94a7eef93\",\n" +
                "  \"userName\": \"laocha\",\n" +
                "  \"deviceId\": \"8ed1c3d7ad9d78421f9f5f7a25d087e1\",\n" +
                "  \"token\": {\n" +
                "    \"appVersion\": \"13.21.0\",\n" +
                "    \"carrier\": \"\",\n" +
                "    \"phoneNumber\": \"+66 84 081 8469\",\n" +
                "    \"host\": \"\",\n" +
                "    \"mid\": \"u8715b8c05a30a8e052a14ea94a7eef93\",\n" +
                "    \"keychainDic\": {\n" +
                "      \"48ACE145-4AF3-4F32-AB5E-7ED18AA8B6F4\": {\n" +
                "        \"pdmn\": \"cku\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"jp.naver.line\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"auth-token-v3\"\n" +
                "      },\n" +
                "      \"AFA64F47-066F-4186-AE40-7D79976FE021\": {\n" +
                "        \"pdmn\": \"ck\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"jp.naver.line\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"e2ee.keychain.u8715b8c05a30a8e052a14ea94a7eef93:0\"\n" +
                "      },\n" +
                "      \"1A4F734A-B592-4C70-ACD0-91D559FD354E\": {\n" +
                "        \"pdmn\": \"cku\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"com.linecorp.uts\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"ClientID\"\n" +
                "      },\n" +
                "      \"8A996FCB-52B9-4CAC-B03C-458D7B2297C2\": {\n" +
                "        \"pdmn\": \"ak\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"bundleSeedID\"\n" +
                "      },\n" +
                "      \"68EBD0B2-6AE3-453F-828F-1964480988F8\": {\n" +
                "        \"pdmn\": \"ck\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"com.linecorp.line.channel\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"cte-u8715b8c05a30a8e052a14ea94a7eef93\"\n" +
                "      },\n" +
                "      \"5D07737B-7193-4482-B861-C21E233FBDCF\": {\n" +
                "        \"pdmn\": \"dk\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"_LineUserInfo\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"region\"\n" +
                "      },\n" +
                "      \"012ADA3B-7CCF-40EC-AA6A-EC9C0E94FB20\": {\n" +
                "        \"pdmn\": \"cku\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"jp.naver.line\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"device-attestation-key\"\n" +
                "      },\n" +
                "      \"DEFDCA0F-060B-41C6-B7FF-E20AFFECD58B\": {\n" +
                "        \"pdmn\": \"dku\",\n" +
                "        \"gena\": \"_pfo\",\n" +
                "        \"svce\": \"jp.naver.line\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"_pfo\"\n" +
                "      },\n" +
                "      \"49E60EFD-7FAE-4EBE-B0B9-77EF115CFCD8\": {\n" +
                "        \"pdmn\": \"cku\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"jp.naver.line\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"auth-token\"\n" +
                "      },\n" +
                "      \"EB69208F-1B85-443D-84DE-EA7074608CE7\": {\n" +
                "        \"pdmn\": \"dk\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"com.linecorp.line.userdefault\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"com.linecorp.keys.userDefaultsEncryptionShared\"\n" +
                "      },\n" +
                "      \"572579B1-40E9-4BEB-8847-6F784C9A610B\": {\n" +
                "        \"pdmn\": \"cku\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"jp.naver.line\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"UUID\"\n" +
                "      },\n" +
                "      \"055853B6-186B-433F-ACF8-BACB2E41C12C\": {\n" +
                "        \"pdmn\": \"cku\",\n" +
                "        \"gena\": \"\",\n" +
                "        \"svce\": \"com.linecorp.trackingservice\",\n" +
                "        \"agrp\": \"ZW4U99SQQ3.jp.naver.line\",\n" +
                "        \"acct\": \"ClientID\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"updateInfoArray\": [\n" +
                "      \"13.21.0\",\n" +
                "      \"52003\",\n" +
                "      \"iPhone10,4\",\n" +
                "      \"iPhone\",\n" +
                "      \"15.2.1\"\n" +
                "    ],\n" +
                "    \"osMachine\": \"\",\n" +
                "    \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyYWE4NzQyNC03MDI2LTRjMGQtYmRiOC0wNDA1NTk2MzczOGEiLCJhdWQiOiJMSU5FIiwiaWF0IjoxNzExODc0NjQ4LCJleHAiOjE3MTI0Nzk0NDgsInNjcCI6IkxJTkVfQ09SRSIsInJ0aWQiOiIxOTMzNzg0Mi05MDU1LTQ1MDMtYWViZS1jOWY4MzZiMTNiMTYiLCJyZXhwIjoxODY5NTU0NjQ4LCJ2ZXIiOiIzLjEiLCJhaWQiOiJ1ODcxNWI4YzA1YTMwYThlMDUyYTE0ZWE5NGE3ZWVmOTMiLCJsc2lkIjoiMTM0OTlhZDgtNTA4Yy00NjhlLWFlZDQtNGU0NzZmZmFiZTkxIiwiZGlkIjoiN2VlZDNjZDQyOWI4NDU0ZWE2ZTI3ZDM5ZmYzMTI5ZWQiLCJjdHlwZSI6IklPUyIsImNtb2RlIjoiUFJJTUFSWSIsImNpZCI6IjAwMDAwMDAwMDAifQ.NTE4k8FvZGpuansX-BNMKAkY7THLTnZVNGiTdmCHijw\",\n" +
                "    \"refreshToken\": \"\",\n" +
                "    \"neloInstallID\": \"\"\n" +
                "  }\n" +
                "}\n";

        IosTokenDTO c = JSON.parseObject(json, IosTokenDTO.class);


        OkHttpClient client = new OkHttpClient();

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
//        String json = "{\"name\": \"John\", \"age\": 30}";

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON,json);
        Request request = new Request.Builder()
                .url("http://146.190.167.155:8808/api/v1/magicServer/conversionAppToken")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main2(String[] args) {

        for (int i = 0; i < 100; i++) {
            String s = RandomUtil.randomString(10);
            System.out.println(s);
        }

    }


}
