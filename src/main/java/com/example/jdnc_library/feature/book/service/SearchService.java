package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.book.DTO.BookDetailDTO;
import com.example.jdnc_library.feature.book.DTO.BookListDTO;
import com.example.jdnc_library.feature.book.repository.BookInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookRepository bookRepository;
    private final CollectionRepository collectionRepository;
    private final BookInfoQueryRepository bookInfoQueryRepository;

    /**
     * 책 1권의 정보를 리턴
     * @param id
     * @return
     */
    public BookDetailDTO getBookById(long id){
        BookInfo book = bookRepository.findWithBookCollectionById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, CollectionInfo.class));

        return BookDetailDTO.of(book);
    }

    /**
     * @param title
     * @param pageable
     * @return
     */
    public List<BookListDTO> getBookListDTOs(String title, BookGroup bookGroup,Pageable pageable){
        return bookInfoQueryRepository.getBookListDTOs(title, bookGroup ,pageable).getContent();
    }
}
