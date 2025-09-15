package com.example.whosbookupdate.controller;


import com.example.whosbookupdate.dto.KakaoBookResponse;
import com.example.whosbookupdate.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/books")
public class BookSearchController {


    private final BookSearchService bookSearchService;

    public BookSearchController(BookSearchService bookSearchService) {
        this.bookSearchService = bookSearchService;
    }

    @GetMapping("/search")
    public Mono<String> searchBooks(@RequestParam String query) {
        return bookSearchService.searchBooks(query);
    }


}
