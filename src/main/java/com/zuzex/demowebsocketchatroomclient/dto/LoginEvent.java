package com.zuzex.demowebsocketchatroomclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginEvent {
    private String username;
    private String chatroomName;
    private Date time;
}
