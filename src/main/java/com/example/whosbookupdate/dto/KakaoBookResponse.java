package com.example.whosbookupdate.dto;

import lombok.Data;

import java.util.List;

@Data
public class KakaoBookResponse {

    private List<Document> documents;
    private Meta meta;

    @Data
    public static class Document {
        private List<String> authors;
        private String contents;
        private String datetime;
        private String isbn;
        private int price;
        private String publisher;
        private String thumbnail;
        private String title;
        private List<String> translators;
        private String url;
    }

    @Data
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }



}
