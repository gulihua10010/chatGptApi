package cn.jianwoo.chatgpt.api.stream;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * 从okhttp3.internal.sse.RealEventSource复制<br>
 * 增加onFailure的场景，对错误响应的支持<br>
 * 
 * @author gulihua
 * @Description
 * @date 2023-03-04 11:43
 */
public class RealEventSourceEx implements EventSource, ServerSentEventReaderEx.Callback, Callback
{
    private final Request request;
    private final EventSourceListener listener;
    @Nullable
    private Call call;

    public RealEventSourceEx(Request request, EventSourceListener listener)
    {
        this.request = request;
        this.listener = listener;
    }


    public void connect(OkHttpClient client)
    {
        client = client.newBuilder().eventListener(EventListener.NONE).build();
        call = client.newCall(request);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call call, Response response)
    {
        processResponse(response);
    }


    public void processResponse(Response response)
    {
        try
        {
            if (!response.isSuccessful())
            {
                listener.onFailure(this, null, response);
                return;
            }

            ResponseBody body = response.body();

            // noinspection ConstantConditions main body is never null
            MediaType contentType = body.contentType();
            if (!isEventStream(contentType))
            {
                listener.onFailure(this, new IllegalStateException("Invalid content-type: " + contentType), response);
                return;
            }

            // This is a long-lived response. Cancel full-call timeouts.
            Exchange exchange = Internal.instance.exchange(response);
            if (exchange != null) exchange.timeoutEarlyExit();

            // Replace the body with an empty one so the callbacks can't see real data.
            Response newResponse = response.newBuilder().body(Util.EMPTY_RESPONSE).build();

            ServerSentEventReaderEx reader = new ServerSentEventReaderEx(response, this);
            try
            {
                listener.onOpen(this, newResponse);
                while (reader.processNextEvent())
                {}
            }
            catch (Exception e)
            {
                listener.onFailure(this, e, newResponse);
                return;
            }
            listener.onClosed(this);
        }
        finally
        {
            response.close();
        }
    }


    private static boolean isEventStream(@Nullable
    MediaType contentType)
    {
        return contentType != null && contentType.type().equals("text") && contentType.subtype().equals("event-stream");
    }


    @Override
    public void onFailure(Call call, IOException e)
    {
        listener.onFailure(this, e, null);
    }


    @Override
    public Request request()
    {
        return request;
    }


    @Override
    public void cancel()
    {
        call.cancel();
    }


    @Override
    public void onEvent(@Nullable
    String id, @Nullable
    String type, String data)
    {
        listener.onEvent(this, id, type, data);
    }


    @Override
    public void onRetryChange(long timeMs)
    {
        // Ignored. We do not auto-retry.
    }


    @Override
    public void onFailure(Response response)
    {
        listener.onFailure(this, null, response);

    }
}
