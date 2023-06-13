package com.mkasahar.sampleteacher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkasahar.sampleteacher.entity.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIService {
    private final Logger log = LoggerFactory.getLogger(OpenAIService.class);

    private final String CHAT_METHOD = "/chat/completions";

    @Value("${openai.api.baseUrl}")
    private String baseUrl;

    @Value("${openai.api.key:default_value}")
    private String apiKey;

    private Map<String, List<Message>> chatLogMap = new HashMap<>();

    public List<String> chat(String userId, String prompt) {
        log.info("chat start : prompt[" + prompt + "]");
        RestTemplate restTemplate = new RestTemplate();

        List<Message> messages = new ArrayList<>();

        List<Message> chatLogs;
        if (chatLogMap.containsKey(userId)) {
            chatLogs = chatLogMap.get(userId);
            messages.addAll(chatLogs);
        } else {
            chatLogs = new ArrayList<>();
            Message systemMessage = new Message();
            systemMessage.setRole("system");
            systemMessage.setContent("あなたは日本の中学校の先生です。優しく分かりやすく簡潔に中学生に勉強を教えてくれます。");
            messages.add(systemMessage);
            chatLogs.add(systemMessage);
            chatLogMap.put(userId, chatLogs);
        }

        Message message = new Message();
        message.setRole("user");
        message.setContent(prompt);
        messages.add(message);

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");
        chatRequest.setMessages(messages);

        logRequest(chatRequest);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(chatRequest, creatHttpHeaders());

        ResponseEntity<ChatResponse> response = restTemplate.exchange(baseUrl + CHAT_METHOD, HttpMethod.POST, entity, ChatResponse.class);

        logResponse(response);
        log.info("chat end");
        if (response.getStatusCode() == HttpStatus.OK) {
            chatLogMap.get(userId).add(message);
            chatLogMap.get(userId).addAll(response.getBody().getChoices().stream().map(Choice::getMessage).toList());

            return response.getBody().getChoices().stream().map(a -> a.getMessage().getContent()).toList();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCode());
        }
    }

    private void logRequest(Object response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(response);
            log.info("request : " + jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void logResponse(Object response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(response);
            log.info("response : " + jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private HttpHeaders creatHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;
    }
}
