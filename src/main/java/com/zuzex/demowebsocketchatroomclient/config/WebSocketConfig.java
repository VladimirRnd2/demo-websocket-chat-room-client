package com.zuzex.demowebsocketchatroomclient.config;

import com.zuzex.demowebsocketchatroomclient.handler.MyStompSessionHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.stomp.DefaultStompSession;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketStompClient webSocketStompClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        WebSocketClient webSocketClient = new SockJsClient(transports);
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(webSocketClient);
        webSocketStompClient.setMessageConverter(new CompositeMessageConverter(Arrays.asList(
                new StringMessageConverter(),
                new ByteArrayMessageConverter(),
                new MappingJackson2MessageConverter())));
        return webSocketStompClient;
    }

    @Bean
    public StompSessionHandler sessionHandler() {
        return new MyStompSessionHandler();
    }

    @Bean
    public StompSession session() {
        return new DefaultStompSession(sessionHandler(), new StompHeaders());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
