package com.example.whosbookupdate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    // 1. 단순 텍스트 응답으로 테스트
    @GetMapping("/test/login")
    public String testLogin() {
        return "로그인 페이지 테스트 - 이 텍스트가 보이면 정상입니다";
    }

    // 2. 큐레이션 리스트도 텍스트로 테스트
    @GetMapping("/test/curation")
    public String testCuration() {
        return "큐레이션 리스트 테스트 - 이 텍스트가 보이면 정상입니다";
    }

    // 3. JSON 응답으로도 테스트
    @GetMapping("/test/json")
    public Map<String, String> testJson() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "인증 없이 접근 가능");
        return result;
    }
}
