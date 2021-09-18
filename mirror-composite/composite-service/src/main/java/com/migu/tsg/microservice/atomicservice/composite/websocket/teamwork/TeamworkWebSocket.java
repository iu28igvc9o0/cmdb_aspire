package com.migu.tsg.microservice.atomicservice.composite.websocket.teamwork;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明: 协同作战的消息聊天室websocket通信类. 访问地址如: ws://localhost:28129/v1/ws/teamwork/xxx/zhangsan?toAccount=lisi
 * 工程: CMDB
 * 作者: zhujuwang
 * 时间: 2021/3/15 22:42
 */
@ServerEndpoint("/ws/teamwork/{teamwork_id}/{fromAccount}")
@Slf4j
@Component
public class TeamworkWebSocket {
    private static Map<String, Map<String, Session>> CLIENT_SESSION_MAP = new HashMap<>();

    private void sessionAssert(Session session) throws IOException {
        String teamworkId = session.getPathParameters().get("teamwork_id");
        // todo 验证作战ID的真实性
        if (StringUtils.isEmpty(teamworkId)) {
            CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing teamwork id, Can't accept.");
            session.close(closeReason);
        }
        // todo 验证用户有效性
        String fromAccount = session.getPathParameters().get("fromAccount");
        if (StringUtils.isEmpty(fromAccount)) {
            CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing from account name, Can't accept.");
            session.close(closeReason);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessionAssert(session);
        // 作战ID
        String teamworkId = session.getPathParameters().get("teamwork_id");
        // 登录账户
        String fromAccount = session.getPathParameters().get("fromAccount");
        if (!CLIENT_SESSION_MAP.containsKey(teamworkId)) {
            CLIENT_SESSION_MAP.put(teamworkId, new HashMap<>());
        }
        CLIENT_SESSION_MAP.get(teamworkId).put(fromAccount, session);
        log.info("用户 {} 加入 -> 当前在线人数为：{}", fromAccount, CLIENT_SESSION_MAP.get(teamworkId).size());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        sessionAssert(session);
        // 作战ID
        String teamworkId = session.getPathParameters().get("teamwork_id");
        // 登录账户
        String fromAccount = session.getPathParameters().get("fromAccount");
        Map<String, Session> sessionMap = CLIENT_SESSION_MAP.get(teamworkId);
        // 指定发送人
        String queryString = session.getQueryString();
        Map<String, String> queryMap = new HashMap<>();
        if (StringUtils.isNotEmpty(queryString)) {
            Arrays.asList(queryString.split("&")).stream().forEach((item -> {
                queryMap.put(item.split("=")[0], item.split("=")[1]);
            }));
        }
        String toAccount = queryMap.get("toAccount");
        if (StringUtils.isNotEmpty(toAccount)) {
            log.info("用户 {} 给 用户 {}，发送消息: {}", fromAccount, toAccount, message);
            handleMessage(fromAccount, toAccount, message, session);
            return ;
        }
        // 给除自己外的所有人 发送消息
        for (String sessionKey : sessionMap.keySet()) {
            if (!sessionKey.equals(fromAccount)) {
                log.info("用户 {} 给 用户 {}，发送消息: {}", fromAccount, sessionKey, message);
                handleMessage(fromAccount, sessionKey, message, session);
            }
        }
    }

    /**
     * 保存到数据库
     */
    private void saveToDB(String fromAccount, String toAccount, String message) {
        //todo 调用db接口
    }

    /**
     * 消息处理
     * @param fromAccount
     * @param toAccount
     * @param message
     * @param session
     */
    private void handleMessage(String fromAccount, String toAccount, String message, Session session) throws IOException {
        JSONObject messageObject = (new ObjectMapper()).convertValue(message, new TypeReference<JSONObject>(){});
        String messageType = messageObject.get("type").toString();
        // 文本
        if (messageType.equals("text")) {
            log.info("用户 {} 给 用户 {}，发送消息: {}", fromAccount, toAccount, message);
            this.sendText(fromAccount, toAccount, messageObject.get("value").toString(), session);
            return;
        }
        // 图片
        if (messageType.equals("image")) {
            // todo 解析图片信息, 上传到ftp服务端 然后从客户端文件的地址
            BufferedImage image = ImageIO.read(new File("image.jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte [] byteArray = baos.toByteArray();
            ByteBuffer buffer=ByteBuffer.allocate(byteArray.length);
            buffer.put(byteArray);
            buffer.flip();
            sendBinary(fromAccount, toAccount, buffer, session);
            return;
        }
        // 视频
        if (messageType.equals("video")) {
            return;
        }
        // 文件
        if (messageType.equals("file")) {
            return;
        }
        throw new RuntimeException("不支持的消息类型.");
    }

    private void sendText(String fromAccount, String toAccount, String message, Session session) {
        session.getAsyncRemote().sendText(message, sendResult -> {
            if (sendResult.isOK()) {
                saveToDB(fromAccount, toAccount, message);
            }
        });
    }

    private void sendBinary(String fromAccount, String toAccount, ByteBuffer buffer, Session session) {
        session.getAsyncRemote().sendBinary(buffer , sendResult -> {
            if (sendResult.isOK()) {
                saveToDB(fromAccount, toAccount, buffer.toString());
            }
        });
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessionAssert(session);
        String teamworkId = session.getPathParameters().get("teamwork_id");
        CLIENT_SESSION_MAP.get(teamworkId).remove(session.getId());
        log.info("连接离开：{}，当前在线人数为：{}", session.getId(), CLIENT_SESSION_MAP.get(teamworkId).size());
    }
}
