package com.example.whosbookupdate.controller;

import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.CurationResponseDto;
import com.example.whosbookupdate.security.CustomUserDetails;
import com.example.whosbookupdate.service.CurationService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@Log4j2 // 로그 추가
@RestController
@RequestMapping("/curation")
public class CurationController {

    private final CurationService curationService;


    public CurationController(CurationService curationService) {
        this.curationService = curationService;
    }

    @PostMapping
    public ResponseEntity<CurationVO> createCuration(@RequestBody CurationResponseDto curationResponseDto) {
        try {
            // 1. SecurityContextHolder에서 현재 인증 정보(Authentication) 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 2. 인증 객체가 유효한지 확인하고 Principal(사용자 정보) 가져오기
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("인증되지 않은 사용자 요청");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // 3. Principal 객체에서 member_id 추출하기
            Object principal = authentication.getPrincipal();
            Long memberId;

            if (principal instanceof UserDetails) {
                // UserDetails 객체에서 member_id를 추출하는 로직.
                // 이 예시에서는 UserDetails에 memberId를 저장했다고 가정합니다.
                UserDetails userDetails = (UserDetails) principal;
                // 'username' 필드에 memberId를 저장했다면, 아래와 같이 추출 가능
                memberId = Long.parseLong(userDetails.getUsername());
            } else if (principal instanceof String && !principal.equals("anonymousUser")) {
                // Security 설정에 따라 principal이 String(예: username)일 수 있음
                // 이 경우 username이 memberId와 동일하다고 가정
                memberId = Long.parseLong((String) principal);
            } else {
                System.out.println("Principal에서 member_id를 추출할 수 없음: " + principal.getClass());
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            System.out.println("=== 요청 데이터 ===");
            System.out.println("인증된 사용자 memberId: " + memberId);
            System.out.println("curationResponseDto: " + curationResponseDto);

            // 4. 추출한 member_id를 서비스 레이어로 전달
            CurationVO createdCuration = curationService.createCuration(curationResponseDto, memberId);
            System.out.println("Service 호출 후: " + createdCuration);

            return new ResponseEntity<>(createdCuration, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("에러 발생: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
