package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
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
        Optional<BookInfo> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookInfo newBookInfo = book.get();
            return new BookDetailDTO(id, newBookInfo.getTitle(), newBookInfo.getImage(), newBookInfo.getContent(), newBookInfo.getAuthor(), newBookInfo.getPublisher(), newBookInfo.getBookGroup(), available(newBookInfo.getId()));
        }
        else {
            return null;
        }
    }

    /**
     * 대출 가능 여부 확인
     * @param id
     * @return
     */
    public boolean available(long id){
        List<CollectionInfo> collectionInfo = collectionRepository.findByBookInfo_id(id);

        return collectionInfo.stream().anyMatch(CollectionInfo::isAvailable);
    }

    /**
     * 제목으로 검색했을 때 검색어가 포함된 모든 항목을 리턴
     * @param title
     * @return
     */
    public List<BookListDTO> searchBooks(String title, Pageable pageable) {
        Page<CollectionInfo> collectionPage = collectionRepository.findAllByBookInfo_TitleContaining(title, pageable);

        return collectionPage.getContent()
                .stream()
                .map(BookListDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * @param title
     * @param pageable
     * @return
     */
    //TODO : 이부분 컨트롤러 까지 수정 - by KBJ
    public List<BookListDTO> getBookListDTOs(String title, Pageable pageable){
        return bookInfoQueryRepository.getBookListDTOs(title, pageable).getContent();
    }

    /**
     * 모든 책 리턴
     * @return
     */
    public List<BookListDTO> searchAllBooks(Pageable pageable){
        Page<BookInfo> bookInfoList = bookRepository.findAll(pageable);

        return bookInfoList.getContent()
                .stream()
                .map(bookInfo -> {
                    boolean availableForBorrow = available(bookInfo.getId());
                    return BookListDTO.of(bookInfo, availableForBorrow);
                })
                .collect(Collectors.toList());
    }

    /**
     * 그룹으로 나눠서 검색
     * @param group
     * @return
     */
    public List<BookListDTO> searchBooksByGroup(String group, Pageable pageable){
        Page<BookInfo> bookInfoList = bookRepository.findByBookGroup(group, pageable);

        return bookInfoList.getContent()
                .stream()
                .map(bookInfo -> {
                    boolean availableForBorrow = available(bookInfo.getId());
                    return BookListDTO.of(bookInfo, availableForBorrow);
                })
                .collect(Collectors.toList());
    }

    public List<BookListDTO> searchBooksByGroupAndTitle(String group, String title,Pageable pageable){
        Page<BookInfo> bookInfoList = bookRepository.findByBookGroupAndTitleContaining(group, title, pageable);

        return bookInfoList.getContent()
                .stream()
                .map(bookInfo -> {
                    boolean availableForBorrow = available(bookInfo.getId());
                    return BookListDTO.of(bookInfo, availableForBorrow);
                })
                .collect(Collectors.toList());
    }
}
