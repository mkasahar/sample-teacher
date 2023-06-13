package com.mkasahar.sampleteacher.entity;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String id;
    private String object;
    private long created;
    private List<Choice> choices;
    private Usage usage;
}

