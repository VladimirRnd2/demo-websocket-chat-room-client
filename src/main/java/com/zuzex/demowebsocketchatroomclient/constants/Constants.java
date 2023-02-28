package com.zuzex.demowebsocketchatroomclient.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String WEB_SOCKET_SERVER_URL = "http://192.168.2.40:9000/";
    public static final String WEB_SOCKET_SERVER_HANDSHAKE_URL = "ws://192.168.2.40:9000/chat/";
    public static final String WEB_SOCKET_SEND_MESSAGE_URL = "/app/message";
    public static final String WEB_SOCKET_SUBSCRIBE_PERSON_EVENT_URL = "/topic/persons.event.";
    public static final String WEB_SOCKET_SUBSCRIBE_USER_MESSAGE_PRIVATE_URL = "/user/queue/message.private.";
    public static final String WEB_SOCKET_SUBSCRIBE_TOPIC_MESSAGE_URL = "/topic/chatroom.message.";
    public static final String WEB_SOCKET_SUBSCRIBE_PERSON_CHANGE_URL = "/app/persons.change.";
    public static final String WEB_SOCKET_CHAT_CREATE = "/chat/create";
    public static final String WEB_SOCKET_GET_USERNAME = "username";
}
