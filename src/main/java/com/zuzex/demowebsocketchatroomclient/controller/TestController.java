package com.zuzex.demowebsocketchatroomclient.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.demowebsocketchatroomclient.dto.ChatAndUserDTO;
import com.zuzex.demowebsocketchatroomclient.handler.MyStompSessionHandler;
import com.zuzex.demowebsocketchatroomclient.model.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.stomp.DefaultStompSession;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

import static com.zuzex.demowebsocketchatroomclient.constants.Constants.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final WebSocketStompClient webSocketStompClient;
    private final StompSessionHandler sessionHandler = new MyStompSessionHandler();
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private StompSession session = new DefaultStompSession(sessionHandler, new StompHeaders());

    @PostMapping("/send")
    public void send(@RequestBody MessageRequest messageRequest) throws JsonProcessingException {
        String request = objectMapper.writeValueAsString(messageRequest);
        session.send(WEB_SOCKET_SEND_MESSAGE_URL, request);
    }

    @GetMapping("/connect/{room}")
    public String getConnect(HttpServletRequest request, @PathVariable(name = "room") String room) throws ExecutionException, InterruptedException {
        if (session.isConnected()) {
            throw new RuntimeException("???????????? ?????? ????????????????????!");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));
        HttpEntity headerRequest = new HttpEntity(headers);
        String username = restTemplate.exchange(WEB_SOCKET_SERVER_URL + WEB_SOCKET_GET_USERNAME, HttpMethod.GET, headerRequest, String.class).getBody();
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("user-name", username);
        connectHeaders.add("room-name", room);
        session = webSocketStompClient.connect(WEB_SOCKET_SERVER_HANDSHAKE_URL + room,
                        handshakeHeaders,
                        connectHeaders,
                        sessionHandler)
                .get();

        session.subscribe(WEB_SOCKET_SUBSCRIBE_PERSON_CHANGE_URL + room, sessionHandler);
        session.subscribe(WEB_SOCKET_SUBSCRIBE_TOPIC_MESSAGE_URL + room, sessionHandler);
        session.subscribe(WEB_SOCKET_SUBSCRIBE_USER_MESSAGE_PRIVATE_URL + room, sessionHandler);
        session.subscribe(WEB_SOCKET_SUBSCRIBE_PERSON_EVENT_URL + room, sessionHandler);
        return session.getSessionId();
    }

    @GetMapping("/create")
    public String createNewChatRoom(HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (session.isConnected()) {
            throw new RuntimeException("???????????? ?????? ????????????????????!");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));
        HttpEntity headerRequest = new HttpEntity(headers);
        ChatAndUserDTO chatAndUserDTO = restTemplate.exchange(WEB_SOCKET_SERVER_URL + WEB_SOCKET_CHAT_CREATE, HttpMethod.GET, headerRequest, ChatAndUserDTO.class).getBody();
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("user-name", chatAndUserDTO.getUsername());
        connectHeaders.add("room-name", chatAndUserDTO.getChatroomName());
        session = webSocketStompClient.connect(WEB_SOCKET_SERVER_HANDSHAKE_URL + chatAndUserDTO.getChatroomName(),
                handshakeHeaders, connectHeaders, sessionHandler).get();
        session.subscribe(WEB_SOCKET_SUBSCRIBE_PERSON_EVENT_URL + chatAndUserDTO.getChatroomName(), sessionHandler);
        session.subscribe(WEB_SOCKET_SUBSCRIBE_TOPIC_MESSAGE_URL + chatAndUserDTO.getChatroomName(), sessionHandler);
        session.subscribe(WEB_SOCKET_SUBSCRIBE_USER_MESSAGE_PRIVATE_URL + chatAndUserDTO.getChatroomName(),
                sessionHandler);
        session.subscribe(WEB_SOCKET_SUBSCRIBE_PERSON_CHANGE_URL + chatAndUserDTO.getChatroomName(), sessionHandler);
        return chatAndUserDTO.getChatroomName();
    }

    @GetMapping("/disconnect")
    public String disconnectSession(HttpServletRequest request) {
        session.setAutoReceipt(true);
        if (!session.isConnected()) {
            throw new RuntimeException("???????????? ?????????? ???? ???????????? ???????? ?????? ???? ????????????????????!");
        }
        session.disconnect();
        System.out.println("Session with id: " + session.getSessionId() + " was disconnected!");
        System.out.println(session.isConnected());
        return "???????????? ?????????????? ??????????????";
    }
}
