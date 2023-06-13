package com.mkasahar.sampleteacher.entity;

import lombok.Data;

@Data
public class Choice {
    private int index;
    private Message message;
    private String finish_reason;

    // getters and setters
}
