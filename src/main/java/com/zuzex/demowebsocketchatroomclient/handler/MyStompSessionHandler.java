package com.zuzex.demowebsocketchatroomclient.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zuzex.demowebsocketchatroomclient.dto.FrameMessage;
import com.zuzex.demowebsocketchatroomclient.dto.LoginEvent;
import com.zuzex.demowebsocketchatroomclient.dto.LogoutEvent;
import com.zuzex.demowebsocketchatroomclient.model.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@JsonSerialize
@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    public static final TypeReference<List<String>> stringTypeReference = new TypeReference<List<String>>() {
    };

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Сессия с Id: " + session.getSessionId() + " добавлена");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        throw new RuntimeException("Какой то exception, не знаю как обозвать его", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return FrameMessage.class;
    }

    @SneakyThrows
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        FrameMessage frameMessage = objectMapper.convertValue(payload, FrameMessage.class);
        switch (frameMessage.getMessageType()) {
            case "message":
                MessageResponse messageResponse = objectMapper.convertValue(frameMessage.getPayload(), MessageResponse.class);
                System.out.println(messageResponse.toString());
                break;
            case "users":
                List<String> users = objectMapper.convertValue(frameMessage.getPayload(), stringTypeReference);
                System.out.println("USERS: ");
                users.forEach(user -> System.out.println(" --- " + user + " --- "));
                break;
            case "login":
                LoginEvent loginEvent = objectMapper.convertValue(frameMessage.getPayload(), LoginEvent.class);
                System.out.println("User " + loginEvent.getUsername() + " connect to chatroom " + loginEvent.getChatroomName() + " at time " + loginEvent.getTime());
                break;
            case "logout":
                LogoutEvent logoutEvent = objectMapper.convertValue(frameMessage.getPayload(), LogoutEvent.class);
                System.out.println("User " + logoutEvent.getUsername() + " disconnect from chatroom");
        }
    }
}
