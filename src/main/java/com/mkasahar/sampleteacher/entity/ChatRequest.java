package com.mkasahar.sampleteacher.entity;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private String model;
    private List<Message> messages;
}