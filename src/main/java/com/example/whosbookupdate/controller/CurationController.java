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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("=== 인증 정보 디버깅 ===");
            System.out.println("Authentication: " + authentication);
            System.out.println("isAuthenticated: " + authentication.isAuthenticated());
            System.out.println("Principal: " + authentication.getPrincipal());
            System.out.println("Authorities: " + authentication.getAuthorities());

            // 익명 사용자 체크 추가
            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal()) ||
                    authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ANONYMOUS"))) {

                System.out.println("인증되지 않은 사용자 요청");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Object principal = authentication.getPrincipal();
            Long memberId;

            // 2. Principal이 UserDetails 타입인지 확인하고 member_id 추출
            if (principal instanceof UserDetails) {
                String memberIdString = ((UserDetails) principal).getUsername();
                System.out.println("추출된 memberIdString: " + memberIdString); // 디버깅용

                try {
                    memberId = Long.parseLong(memberIdString);
                } catch (NumberFormatException e) {
                    System.out.println("memberId 파싱 실패: " + memberIdString);
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                System.out.println("Principal에서 member_id를 추출할 수 없음: " + principal.getClass());
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            System.out.println("인증된 사용자 memberId: " + memberId);
            CurationVO createdCuration = curationService.createCuration(curationResponseDto, memberId);

            return new ResponseEntity<>(createdCuration, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("큐레이션 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/list")
    public ResponseEntity<List<CurationVO>> list() {
        log.info("큐레이션 목록 API 요청");
        List<CurationVO> curationList = curationService.getCuration();

        // ResponseEntity를 사용해 HTTP 상태 코드와 함께 데이터를 반환
        return ResponseEntity.ok(curationList);
    }


}
