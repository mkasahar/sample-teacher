package com.mkasahar.sampleteacher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @Value("${sample.val:ERR}")
    String val;

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping(value = "/val")
    public String val() {
        return val;
    }
}
