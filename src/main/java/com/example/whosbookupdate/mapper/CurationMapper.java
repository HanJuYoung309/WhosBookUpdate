package com.example.whosbookupdate.mapper;


import com.example.whosbookupdate.domain.BookVO;
import com.example.whosbookupdate.domain.CurationBookVO;
import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.BookDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CurationMapper {
    int insertCuration(CurationVO curationVO);

    List<CurationVO> selectCuration();

    BookDto getBook(String isbn);

    void insertBook(BookVO newBook);

    void insertCurationBook(CurationBookVO curationBook);

     List<BookVO> getBooksByIsbnList(@Param("isbn") List<String> isbnList);
}
