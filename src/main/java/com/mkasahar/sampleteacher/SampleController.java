package com.mkasahar.sampleteacher;

import com.mkasahar.sampleteacher.service.OpenAIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleController {

    private final OpenAIService openAIService;

    public SampleController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping(value = "/chat")
    public List<String> chat(@RequestParam String userId, @RequestParam String prompt) {
        return openAIService.chat(userId, prompt);
    }
}
