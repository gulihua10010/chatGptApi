package cn.jianwoo.chatgpt.api.bo;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-21 15:47
 */
public class ProxyBO
{
    private String host;
    private int port;

    private boolean flagArg;

    public Proxy getProxy()
    {
        if (flagArg)
        {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        }
        return null;
    }


    public boolean getFlagArg()
    {
        return this.flagArg;
    }


    public void setFlagArg(boolean flagArg)
    {
        this.flagArg = flagArg;
    }


    public String getHost()
    {
        return this.host;
    }


    public void setHost(String host)
    {
        this.host = host;
    }


    public int getPort()
    {
        return this.port;
    }


    public void setPort(int port)
    {
        this.port = port;
    }
}
