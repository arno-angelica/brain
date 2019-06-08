package com.arno.manager.util;

import com.arno.manager.transaction.BrainTransactionManager;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 请求工具
 * @className HttpClient
 * @create Create in 2019/06/07 2019/6/7
 **/
public class HttpClient {

    public static String get(String url) {
        String result = "";
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 将事务组ID和子事务数量放入请求头，以便后面服务取
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-type", "application/json");
            httpGet.addHeader("groupId", BrainTransactionManager.getCurrentGroupId());
            httpGet.addHeader("transactionCount", String.valueOf(BrainTransactionManager.getTransactionCount()));
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
