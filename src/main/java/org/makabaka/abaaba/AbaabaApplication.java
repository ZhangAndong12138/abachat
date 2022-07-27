package org.makabaka.abaaba;

import org.makabaka.abaaba.websocket.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class AbaabaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbaabaApplication.class);
        List<String> chatList = null;

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            if (msg.startsWith("ls")) {
                chatList = new ArrayList<>(WebSocketServer.WEBSOCKET_SESSION_MAP.keySet());
                for (int i = 0; i < chatList.size(); i++) {
                    if (chatList.get(i).equals(WebSocketServer.CURRENT_IP)) {
                        System.out.print("->");
                    }
                    System.out.print("[" + (i + 1) + "]: " + chatList.get(i) + " ");
                }
                System.out.println();
            } else if (msg.startsWith("su")) {
                if (chatList == null) {
                    System.out.println("请先使用[ls]命令查看聊天列表");
                } else {
                    String[] s = msg.split(" ");
                    if (s.length < 2) {
                        System.out.println("指令错误");
                    } else {
                        int i = Integer.parseInt(s[1]);
                        if (i <= chatList.size() && i > 0) {
                            WebSocketServer.CURRENT_IP = chatList.get(i - 1);
                            System.out.println("正在与[" + WebSocketServer.CURRENT_IP + "]聊天");
                        } else {
                            System.out.println("指令错误");
                        }
                    }
                }
            } else {
                if (WebSocketServer.CURRENT_IP != null && !"".equals(WebSocketServer.CURRENT_IP)) {
                    try {
                        WebSocketServer.WEBSOCKET_SESSION_MAP.get(WebSocketServer.CURRENT_IP).getBasicRemote().sendText(msg);
                        System.out.println("[你] 发送给 [" + WebSocketServer.CURRENT_IP + "]：" + msg);
                    } catch (IOException e) {
                        System.out.println("消息\"" + msg + "\"发送失败");
                    }
                } else {
                    System.out.println("当前没有可用的聊天");
                }
            }
        }
    }
}
