package com.example.whosbookupdate.service;

import com.example.whosbookupdate.domain.BookVO;
import com.example.whosbookupdate.domain.CurationBookVO;
import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.BookDto;
import com.example.whosbookupdate.dto.CurationRequestDto;
import com.example.whosbookupdate.dto.CurationResponseDto;
import com.example.whosbookupdate.mapper.CurationMapper;
import com.example.whosbookupdate.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;

import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurationService {

    private final CurationMapper curationMapper;


    @Autowired
    public CurationService(CurationMapper curationMapper) {
        this.curationMapper = curationMapper;

    }


    private boolean isValidCurationStatus(String status){

        return "PUBLIC".equals(status) || "PRIVATE".equals(status) || "DRAFT".equals(status);
    }

    public List<CurationVO> getCuration() {

        List<CurationVO> curationVOList= curationMapper.selectCuration();

        return curationVOList;
    }

    @Transactional
    public CurationVO createCuration(CurationResponseDto curationResponseDto, Long memberId) {

        // 1. DTO를 VO(도메인 객체)로 변환
        CurationVO curationVO = new CurationVO();
        curationVO.setMemberId(memberId);
        curationVO.setTitle(curationResponseDto.getTitle());
        curationVO.setContent(curationResponseDto.getContent());
        curationVO.setCategoryId(curationResponseDto.getCategoryId());
        curationVO.setEmoji(curationResponseDto.getEmoji());
        curationVO.setCurationStatus("PUBLIC");
        curationVO.setCurationLikeCount(0);

        // 2. 큐레이션 저장 및 ID 확보
        curationMapper.insertCuration(curationVO);
        Long curationId = curationVO.getCurationId();

        // 3. 책 정보 처리
        List<BookVO> books = curationResponseDto.getBooks();
        if (books != null && !books.isEmpty()) {

            // 모든 ISBN을 한 번에 추출하여 DB 조회
            List<String> isbns = books.stream()
                    .map(BookVO::getIsbn)
                    .collect(Collectors.toList());

            List<BookVO> existingBooks = curationMapper.getBooksByIsbnList(isbns);

            // ISBN을 키로, BookVO를 값으로 하는 맵 생성
            Map<String, BookVO> existingBookMap = existingBooks.stream()
                    .collect(Collectors.toMap(BookVO::getIsbn, Function.identity()));

            for (BookVO bookVO : books) {
                Long bookId;
                if (existingBookMap.containsKey(bookVO.getIsbn())) {
                    // 3-1. 이미 존재하는 책이면 해당 ID 사용
                    bookId = existingBookMap.get(bookVO.getIsbn()).getBookId();
                } else {
                    // 3-2. 새로운 책이면 book 테이블에 저장
                    curationMapper.insertBook(bookVO);
                    bookId = bookVO.getBookId();
                }

                // 3-3. 큐레이션-책 관계 테이블에 저장
                CurationBookVO curationBook = new CurationBookVO();
                curationBook.setCurationId(curationId);
                curationBook.setBookId(bookId);
                curationMapper.insertCurationBook(curationBook);
            }
        }

        // 4. 삽입된 객체 반환
        return curationVO;
    }
}

