package com.mkasahar.sampleteacher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello World!";
    }
}
