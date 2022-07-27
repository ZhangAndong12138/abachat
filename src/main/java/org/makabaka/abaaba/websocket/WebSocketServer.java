package org.makabaka.abaaba.websocket;

import org.makabaka.abaaba.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/abaaba")
public class WebSocketServer {

    // 当前正在进行的聊天
    public static String CURRENT_IP = "";

    // 所有的聊天
    public static final ConcurrentHashMap<String, Session> WEBSOCKET_SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        //开启连接时触发
        String ip = WebSocketUtil.getIp(session);
        System.out.println("[" + ip + "]开启了聊天");
        WEBSOCKET_SESSION_MAP.put(ip, session);
        System.out.println("====现在共有" + WEBSOCKET_SESSION_MAP.size() + "个聊天====");
        session.getBasicRemote().sendText("开始聊天");
        CURRENT_IP = ip;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //接收到消息时触发
        String ip = WebSocketUtil.getIp(session);
        System.out.println("[" + ip + "] 发送给 [你]：" + message);
        CURRENT_IP = ip;
    }

    @OnClose
    public void onClose(Session session) {
        //关闭连接时触发
        String ip = WebSocketUtil.getIp(session);
        System.out.println("[" + ip + "] 结束了聊天");
        WEBSOCKET_SESSION_MAP.remove(ip);
        System.out.println("====现在共有" + WEBSOCKET_SESSION_MAP.size() + "个聊天====");
        if (WEBSOCKET_SESSION_MAP.size() > 0) {
            CURRENT_IP = WEBSOCKET_SESSION_MAP.keys().nextElement();
            System.out.println("正在与[" + CURRENT_IP + "]聊天");
        } else {
            CURRENT_IP = "";
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        // 发生异常时触发
        System.out.println("[" + session.getId() + "]发生了错误：" + error.getMessage());
    }


}
