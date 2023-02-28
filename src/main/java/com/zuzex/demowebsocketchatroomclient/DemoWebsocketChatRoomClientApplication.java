package com.zuzex.demowebsocketchatroomclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DemoWebsocketChatRoomClientApplication {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		SpringApplication.run(DemoWebsocketChatRoomClientApplication.class, args);
	}

}
