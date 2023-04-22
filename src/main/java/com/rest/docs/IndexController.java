package com.rest.docs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
//API 테스트를 위한 컨트롤러
public class IndexController {

    @GetMapping
    public String helloWorld(){
        return "Hello World";
    }
}