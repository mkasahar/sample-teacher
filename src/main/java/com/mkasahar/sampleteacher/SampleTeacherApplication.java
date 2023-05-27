package com.mkasahar.sampleteacher;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@LineMessageHandler
public class SampleTeacherApplication {

	private final Logger log = LoggerFactory.getLogger(SampleTeacherApplication.class);
	private final MessagingApiClient messagingApiClient;

	public SampleTeacherApplication(MessagingApiClient messagingApiClient) {
		this.messagingApiClient = messagingApiClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleTeacherApplication.class, args);
	}

	@EventMapping
	public void handleTextMessageEvent(MessageEvent event) {
		log.info("event: " + event);
		if (event.message() instanceof TextMessageContent) {
			TextMessageContent message = (TextMessageContent) event.message();
			final String originalMessageText = message.text();
			messagingApiClient.replyMessage(new ReplyMessageRequest(
					event.replyToken(),
					List.of(new TextMessage(originalMessageText)),
					false));
		}
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}
}
