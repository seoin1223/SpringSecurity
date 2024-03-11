package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View return
public class IndexController {

    @GetMapping({"","/"})
    public String index(){
        // 기본폴더 src/main/resources/
        // 뷰 리졸버 설정 : templates(prefix), .mustacheZ(suffix) 생략 가능
        return "index";
    }
}
