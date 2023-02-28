package com.zuzex.demowebsocketchatroomclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatAndUserDTO {
    private String chatroomName;
    private String username;
}
