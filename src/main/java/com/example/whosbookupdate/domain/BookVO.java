package com.example.whosbookupdate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookVO {

    private Long bookId;
    private String title;
    private String authors;
    private String thumbnail;
    private String url;
    private String isbn;
    private String publisher;

    @Builder
    public BookVO(Long bookId, String title, String authors, String thumbnail, String url, String isbn, String publisher) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.url = url;
        this.isbn = isbn;
        this.publisher = publisher;
    }
}