package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.book.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CollectionRepository collectionRepository;
    private final BorrowRepository borrowRepository;

    /**
     * 책 1권의 정보를 리턴
     * @param id
     * @return
     */
    public BookDetailDTO getBookById(long id){
        Optional<BookInfo> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookInfo newBookInfo = book.get();
            return new BookDetailDTO(id, newBookInfo.getTitle(), newBookInfo.getImage(), newBookInfo.getContent(), newBookInfo.getAuthor(), newBookInfo.getPublisher(),available(newBookInfo.getId()));
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

    public List<BookListDTO> searchBooksByTitle(String title, Pageable pageable){
        Page<BookInfo> bookInfoList = bookRepository.findAllByTitleContaining(title, pageable);

        return bookInfoList.getContent()
                .stream()
                .map(bookInfo -> {
                    boolean availableForBorrow = available(bookInfo.getId());
                    return BookListDTO.of(bookInfo, availableForBorrow);
                })
                .collect(Collectors.toList());
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
     * 책의 QR을 찍었을 때 책 정보 리턴
     * @param bookNumber
     * @return
     */
    public BorrowDetailDTO qrBook(long bookNumber){
        Optional<CollectionInfo> collectionInfoOptional = collectionRepository.findByBookNumber(bookNumber);
        CollectionInfo collectionInfo = collectionInfoOptional.get();
        return BorrowDetailDTO.of(collectionInfo);
    }

    /**
     * 책을 빌릴 때 데이터베이스에 저장
     * @param bookNumber
     */
    @Transactional
    public void borrowBook(long bookNumber){

        CollectionInfo collectionInfo = collectionRepository.findByBookNumber(bookNumber)
                .orElseThrow(() -> new EntityNotFoundException(bookNumber, CollectionInfo.class));

        BorrowInfo borrowInfo = new BorrowInfo(collectionInfo);
        borrowRepository.save(borrowInfo);
        collectionInfo.updateAvailable(false);
    }

    /**
     * 반납함 QR을 찍었을 때 반납 가능한 도서 리스트를 리턴
     * @param member
     * @return
     */
    public List<BorrowListDTO> returnBookList(Member member, Pageable pageable){
        List<BorrowInfo> borrowInfoList = borrowRepository.findAllByCreatedByAndReturnDateIsNull(member, pageable).getContent();

        return borrowInfoList.stream()
                .map(BorrowListDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * 책 한권을 반납하면 데이터베이스에 저장
     * @param bookNumber
     */
    public void returnBook(long bookNumber){
        Optional<BorrowInfo> borrowInfoOptional = borrowRepository.findByCollectionInfo_BookNumber(bookNumber);
        BorrowInfo borrowInfo = borrowInfoOptional.get();
        borrowInfo.returnBook(LocalDateTime.now());
        borrowRepository.save(borrowInfo);
    }

    /**
     * 입력한 달에 빌린 책 리스트를 리턴(Admin)
     * @param year
     * @param month
     * @return
     */
    public List<BorrowListDTO> searchBooksReturnedInMonth(int year, int month, Pageable pageable) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<BorrowInfo> returnedInMonth = borrowRepository.findByReturnDateBetween(startOfMonth, endOfMonth, pageable).getContent();

        return returnedInMonth.stream()
                .map(BorrowListDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * 아직 미반납인 책 리스트를 리턴(Admin)
     * @return List<BorrowDTO>
     */
    public List<BorrowListDTO> searchNotReturnBooks(Pageable pageable) {
        List<BorrowInfo> notReturnedBorrows = borrowRepository.findByReturnDateIsNull(pageable).getContent();

        return notReturnedBorrows.stream()
                .map(BorrowListDTO::of)
                .collect(Collectors.toList());


    }

    /**
     * 책 정보 업데이트(Admin)
     * @param id
     * @param bookRequest
     */
    public void updateBook(long id, BookRequest bookRequest){
        BookInfo bookInfo = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, BookInfo.class));

        bookInfo.update( bookRequest.getTitle(), bookRequest.getImage(), bookRequest.getContent(), bookRequest.getAuthor(), bookRequest.getPublisher());
    }
    // TODO : 책정보 추가 수정필요
    /**
     * 책 정보 추가(Admin)
     * 제목과 저자, 출판사가 동일하면 같은 책으로 규정
     * @param bookRequest
     * @exception BookInfo 존재할 경우
     */
    public void saveBook(BookRequest bookRequest){

        String title = bookRequest.getTitle();
        String author = bookRequest.getAuthor();
        String publisher = bookRequest.getPublisher();

        Optional<BookInfo> existingBook = bookRepository.findByTitleAndAuthorAndPublisher(title, author, publisher);

        if(existingBook.isPresent()){
            throw new RuntimeException("이미 존재하는 책 정보입니다.");
        }
        else {
            BookInfo bookInfo = bookRequest.toEntity();
            bookRepository.save(bookInfo);
        }
    }

    /**
     * 책 번호 추가(Admin)
     * @param bookNumber
     * @param id
     * @exception CollectionInfo 존재할 경우
     */

    public void addBookNumber(long bookNumber, Long id){
        Optional<CollectionInfo> existingCollection = collectionRepository.findByBookNumber(bookNumber);

        if(existingCollection.isPresent()){
            throw new RuntimeException("이미 존재하는 책 정보입니다.");
        }
        else {
            Optional<BookInfo> book = bookRepository.findById(id);
            BookInfo bookInfo =book.get();
            CollectionInfo collectionInfo = new CollectionInfo(bookInfo, bookNumber);
            collectionRepository.save(collectionInfo);
        }
    }

    /**
     * 반납 최종 확인(Admin)
     * @param id
     */
    public void adminCheck(int id){
        try{
            BorrowInfo borrowInfo = borrowRepository.getById(id);
            borrowInfo.updateAdminCheck(true);
            borrowRepository.save(borrowInfo);
            CollectionInfo collectionInfo = collectionRepository.findById(borrowInfo.getCollectionInfo().getId());
            collectionInfo.updateAvailable(true);
            collectionRepository.save(collectionInfo);
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException();
        }
    }
}
