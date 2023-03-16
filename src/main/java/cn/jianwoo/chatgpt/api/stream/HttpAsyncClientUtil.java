package cn.jianwoo.chatgpt.api.stream;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-23 12:12
 */
@Log4j2
public class HttpAsyncClientUtil
{

    public static OkHttpClient createHttpClient()
    {
        return createHttpClient(null);
    }


    public static OkHttpClient createHttpClient(Proxy proxy)
    {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        if (null != proxy)
        {
            client.proxy(proxy);
        }
        return client.build();

    }


    public static void execute(OkHttpClient httpClient, Request request, Callback<String> succCallback)
    {
        execute(httpClient, request, succCallback, null, null);
    }


    public static void execute(OkHttpClient httpClient, Request request, Callback<String> succCallback,
            Callback<String> completeCallback)
    {
        execute(httpClient, request, succCallback, completeCallback, null);
    }


    public static void execute(OkHttpClient httpClient, Request request, Callback<String> succCallback,
            Callback<String> completeCallback, Callback<String> failCallback)
    {
        EventSource.Factory factory = createFactory(httpClient);
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response)
            {

                log.debug("AsyncClient opens connection.");

            }


            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data)
            {
                log.debug("AsyncClient.onEvent, id={}, type={}, data= {}", id, type, data);
                if (null != succCallback)
                {
                    succCallback.call(data);
                }

            }


            @Override
            public void onClosed(EventSource eventSource)
            {
                log.debug("AsyncClient closes connection. ");
                if (null != completeCallback)
                {
                    completeCallback.call("done");
                }

            }


            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response)
            {
                if (null == response)
                {
                    if (null != failCallback && null != t)
                    {
                        failCallback.call(t.getMessage());
                    }
                    log.error(" asyncClient.execute failed, e: ", t);
                    return;
                }
                ResponseBody body = response.body();
                if (body != null)
                {
                    try
                    {
                        String content = body.string();
                        if (null != failCallback && StrUtil.isNotBlank(content))
                        {
                            failCallback.call(content);
                        }
                        log.error(" asyncClient.execute failed, body: {}, e: {}", content, t);
                    }
                    catch (IOException e)
                    {
                        log.error(">>>>onFailure.exec failed, e: ", e);
                    }

                }
                else
                {
                    if (null != failCallback && null != t)
                    {
                        failCallback.call(t.getMessage());
                    }
                    log.error(" asyncClient.execute failed, e: ", t);

                }
                eventSource.cancel();

            }
        });

    }


    private static EventSource.Factory createFactory(OkHttpClient client)
    {
        return (request, listener) -> {
            RealEventSourceEx eventSource = new RealEventSourceEx(request, listener);
            eventSource.connect(client);
            return eventSource;
        };
    }

}
