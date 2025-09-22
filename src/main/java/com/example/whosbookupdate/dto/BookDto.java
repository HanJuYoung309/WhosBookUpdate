package com.example.whosbookupdate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDto {

    private String bookId;
    private String title;
    private String authors;
    private String thumbnail;
    private String url;
    private String isbns;



}
