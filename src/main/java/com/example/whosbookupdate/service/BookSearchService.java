package com.example.whosbookupdate.service;

import com.example.whosbookupdate.dto.KakaoBookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BookSearchService {

    @Value("${kakao.restapi.key}")
    private String kakaoRestApiKey;

    private final WebClient webClient;

    public BookSearchService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> searchBooks(String query) {
        return webClient.get()
                .uri("https://dapi.kakao.com/v3/search/book?query={query}", query)
                .header("Authorization", "KakaoAK " + kakaoRestApiKey) // 이 부분이 정확한지 확인
                .retrieve()
                .bodyToMono(String.class);
    }
}
