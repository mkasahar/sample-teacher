package com.mkasahar.sampleteacher;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.mkasahar.sampleteacher.service.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@LineMessageHandler
public class SampleMessageHandler {
    private final Logger log = LoggerFactory.getLogger(SampleTeacherApplication.class);
    private final MessagingApiClient messagingApiClient;
    private final OpenAIService openAIService;

    public SampleMessageHandler(MessagingApiClient messagingApiClient, OpenAIService openAIService) {
        this.messagingApiClient = messagingApiClient;
        this.openAIService = openAIService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent event) {
        log.info("event: " + event);
        if (event.message() instanceof TextMessageContent) {
            TextMessageContent message = (TextMessageContent) event.message();
            final String originalMessageText = message.text();
            final String userId = event.source().userId();
            List<String> openAiRes = openAIService.chat(userId, originalMessageText);

            messagingApiClient.replyMessage(new ReplyMessageRequest(
                    event.replyToken(),
                    openAiRes.stream().map(TextMessage::new).map(b -> ((Message) b)).toList(),
                    false));
        } else {
            String res = """
                    申し訳ありません。今はまだテキストでのやり取りしか対応していません。
                    """;

            messagingApiClient.replyMessage(new ReplyMessageRequest(
                    event.replyToken(),
                    List.of(new TextMessage(res)),
                    false));
        }
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
