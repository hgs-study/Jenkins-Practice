package com.jenkinscicd.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class requestController {

    @GetMapping("/test")
    public String test(){
       return "test";
    }

    @GetMapping("/push")
    public String push(){
       return "push test";
    }
}
