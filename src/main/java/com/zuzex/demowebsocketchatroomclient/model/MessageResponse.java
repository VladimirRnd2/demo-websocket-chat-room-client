package com.zuzex.demowebsocketchatroomclient.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String chatroomName;
    private String senderName;
    private String recipientNames;
    private String content;
    private Date dateCreate;
}
