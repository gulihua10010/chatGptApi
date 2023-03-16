package cn.jianwoo.chatgpt.api.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import lombok.extern.log4j.Log4j2;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-27 21:39
 */
@Log4j2
public class WebSocketConfigurator extends ServerEndpointConfig.Configurator
{

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response)
    {

        sec.getUserProperties().put(HandshakeRequest.class.getName(), request);
    }
}
