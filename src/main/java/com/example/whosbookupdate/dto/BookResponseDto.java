package com.example.whosbookupdate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponseDto {
    private String bookId;
    private String title;
    private String authors;
    private String thumbnail;
    private String url;
    private String isbn;



}
