package io.renren.modules.task;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientExample {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet("http://sms.szfangmm.com:3000/api/smslist?token=wA54jX77SdvDSCeDkFSB6i");
            request.addHeader("User-Agent", "Mozilla/5.0");

            String result = httpClient.execute(request, httpResponse ->
                EntityUtils.toString(httpResponse.getEntity()));

            System.out.println(result);
        } finally {
            httpClient.close();
        }
    }
}
