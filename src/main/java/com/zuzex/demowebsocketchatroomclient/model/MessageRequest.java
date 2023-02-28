package com.zuzex.demowebsocketchatroomclient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String chatroomName;
    private String recipientsName;
    private String content;
}
