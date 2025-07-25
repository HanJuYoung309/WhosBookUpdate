package com.example.whosbookupdate.controller;

import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.CurationResponseDto;
import com.example.whosbookupdate.service.CurationService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Log4j2 // 로그 추가
@Controller
@RequestMapping("/curation")
public class CurationController {

    private final CurationService curationService;


    public CurationController(CurationService curationService) {
        this.curationService = curationService;
    }

    @PostMapping
    @ResponseBody // @Controller 사용 시 JSON 응답을 위해 필요
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CurationVO> createCuration(@RequestBody CurationResponseDto curationResponseDto) {
        CurationVO createdCuration = curationService.createCuration(curationResponseDto);
        return new ResponseEntity<>(createdCuration, HttpStatus.CREATED);
    }

    @GetMapping("/listPage")
    public String listPage() {

        return "curation/list";
    }


    @GetMapping("/list")
    public String list(Model model) { // Model 객체 주입
        log.info("큐레이션 목록 페이지 요청");
        List<CurationVO> curationList = curationService.getCuration(); // 서비스에서 큐레이션 목록 조회
        model.addAttribute("curationVOList", curationList); // 모델에 "curationVOList" 이름으로 추가

        // 디버깅 로그: 조회된 큐레이션 개수 확인
        log.info("조회된 큐레이션 개수: {}", curationList != null ? curationList.size() : 0);

        return "curation/list"; // src/main/resources/templates/curation/listPage.html
    }



}
