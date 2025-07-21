package com.example.whosbookupdate.controller;

import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.CurationResponseDto;
import com.example.whosbookupdate.service.CurationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/curation")
public class CurationController {

    private final CurationService curationService;


    public CurationController(CurationService curationService) {
        this.curationService = curationService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<CurationVO> createCuration(@RequestBody CurationResponseDto curationResponseDto, HttpSession session) {
        // 1. 사용자 인증 확인
        HttpSession loginResponseDto = (HttpSession) session.getAttribute("loginResponseDto");
        if (loginResponseDto == null) {
            // 로그인하지 않은 사용자에게는 401 Unauthorized 응답을 반환
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인한 사용자만 큐레이션을 생성할 수 있습니다.");
        }

        CurationVO createdCuration = curationService.createCuration(curationResponseDto);

        return new ResponseEntity<>(createdCuration, HttpStatus.CREATED);
    }


}
