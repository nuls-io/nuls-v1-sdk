/**
 * HttpClientUtil.java
 */
package io.nuls.sdk.core.utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * <HTTP请求工具类>
 *
 * @author WangZhijian
 * @version V1.0.0
 * @date [2016年3月17日]
 */
public class HttpClientUtil {

    private final static int DISPATCHER_SOCKET_TIMEOUT = 60000;

    private final static int DISPATCHER_CONNECT_TIMEOUT = 60000;

    private final static int DISPATCHER_CONN_REQUEST_TIMEOUT = 60000;

    private final static int HTTP_CLIENT_POOL_MAX_PER_ROUTE_COUNT = 100;

    private final static int HTTP_CLIENT_POOL_MAX_CONNECTION_COUNT = 50;

    static final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1";

    private static volatile CloseableHttpClient httpClient = ConnectionManager.getHttpClient();

    private static HttpEntity fetchContentByPost(String targetUrl, HttpEntity entity) {
        if (StringUtils.isBlank(targetUrl) || (!targetUrl.startsWith("http:") && !targetUrl.startsWith("https:"))) {
            return null;
        }
        HttpPost httpPost = new HttpPost(targetUrl);
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(DISPATCHER_CONN_REQUEST_TIMEOUT)
                .setConnectTimeout(DISPATCHER_CONNECT_TIMEOUT)
                .setSocketTimeout(DISPATCHER_SOCKET_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("User-Agent", UA);
        try {
            HttpContext context = new BasicHttpContext();
            if (entity != null) {
                httpPost.setEntity(entity);
            }
            HttpResponse remoteResponse = httpClient.execute(httpPost, context);
            if (remoteResponse != null && remoteResponse.getStatusLine().getStatusCode() == 200
                    && remoteResponse.getEntity() != null) {
                return remoteResponse.getEntity();
            }
            assert remoteResponse != null;
        } catch (Exception e) {
            httpPost.abort();
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    private static String getString(HttpEntity entity, String encoding) throws IllegalStateException, IOException {
        if (entity != null) {
            try {
                String result = IOUtils.toString(entity.getContent(), encoding);
                return result;
            } finally {
                EntityUtils.consume(entity);
            }
        }
        return null;
    }

    public static String fetchStringByPost(String url, String data) {
        String encoding = "utf-8";
        HttpEntity dataEntity = data == null ? null : new StringEntity(data, Charset.forName(encoding));
        HttpEntity entity = fetchContentByPost(url, dataEntity);
        try {
            return getString(entity, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class ConnectionManager {
        static CloseableHttpClient getHttpClient() {
            ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
            LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", plainsf)
                    .register("https", sslsf)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            // 将最大连接数增加到500
            cm.setMaxTotal(HTTP_CLIENT_POOL_MAX_CONNECTION_COUNT);
            // 将每个路由基础的连接增加到200
            cm.setDefaultMaxPerRoute(HTTP_CLIENT_POOL_MAX_PER_ROUTE_COUNT);

            //          HttpParams params = new BasicHttpParams();
            //          params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
            //          params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            //          params.setParameter(CoreProtocolPNames.USER_AGENT, UA);
            //          params.setParameter(ClientPNames.COOKIE_POLICY, "easy");
            //          
            //          DefaultHttpClient client = new DefaultHttpClient(cm, params);
            //          
            //          client.getCookieSpecs().register("easy", new CookieSpecFactory() {
            //              public CookieSpec newInstance(HttpParams params) {
            //                  return new BrowserCompatSpec() {
            //                      @Override
            //                      public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
            //                          // Oh, I am easy 忽略cookie
            //                      }
            //                  };
            //              }
            //          });
            //请求重试处理
            HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                        return false;
                    }
                    if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                        return true;
                    }
                    if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {// 超时
                        return false;
                    }
                    if (exception instanceof UnknownHostException) {// 目标服务器不可达
                        return false;
                    }
                    if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                        return false;
                    }
                    if (exception instanceof SSLException) {// ssl握手异常
                        return false;
                    }

                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    // 如果请求是幂等的，就再次尝试
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setRetryHandler(httpRequestRetryHandler)
                    .build();
            return httpClient;
        }

        static void shutdown(CloseableHttpClient httpclient) {
            if (httpclient != null) {
                //              httpclient.getConnectionManager().shutdown();
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpclient = null;
            }
        }
    }

}
