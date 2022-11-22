package com.example.demo.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {
    private Integer roomId;
    private String email;
    private String message;
    private String timeStamp;
}
