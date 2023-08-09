package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.book.model.BookDTO;
import com.example.jdnc_library.feature.book.model.BorrowDTO;
import com.example.jdnc_library.feature.book.model.BookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
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
    public BookDTO getBookById(Long id){
        Optional<BookInfo> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookInfo newBookInfo = book.get();
            return new BookDTO(id, newBookInfo.getTitle(), newBookInfo.getImage(), newBookInfo.getContent(), newBookInfo.getAuthor(), newBookInfo.getPublisher());
        }
        else {
            return null;
        }
    }

    /**
     * 제목으로 검색했을 때 검색어가 포함된 모든 항목을 리턴
     * @param title
     * @return
     */
    public List<BookDTO> searchBooks(String title, Pageable pageable) {
        return collectionRepository.findAllByBookInfo_TitleContaining(title, pageable)
                .getContent()
                .stream()
                .map(BookDTO::of)
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchBooksByTitle(String title, Pageable pageable){
        List<BookInfo> bookInfoList = bookRepository.findAllByTitleContaining(title, pageable).getContent();

        return bookInfoList.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getImage(), book.getContent(), book.getAuthor(), book.getPublisher()))
                .collect(Collectors.toList());
    }

    /**
     * 책의 QR을 찍었을 때 책 정보 리턴
     * @param bookNumber
     * @return
     */
    public BookDTO qrBook(Long bookNumber){
        Optional<CollectionInfo> collectionInfoOptional = collectionRepository.findByBookNumber(bookNumber);
        CollectionInfo collectionInfo = collectionInfoOptional.get();
        return BookDTO.of(collectionInfo);
    }

    /**
     * 책을 빌릴 때 데이터베이스에 저장
     * @param bookNumber
     */
    public void borrowBook(Long bookNumber){
        CollectionInfo collectionInfo = collectionRepository.findByBookNumber(bookNumber)
                .orElseThrow(() -> new EntityNotFoundException(bookNumber, CollectionInfo.class));

        BorrowInfo borrowInfo = new BorrowInfo(collectionInfo);
        borrowRepository.save(borrowInfo);
    }

    /**
     * 반납함 QR을 찍었을 때 반납 가능한 도서 리스트를 리턴
     * @param member
     * @return
     */
    public List<BorrowDTO> returnBookList(Member member, Pageable pageable){
        List<BorrowInfo> borrowInfoList = borrowRepository.findAllByCreatedByAndReturnDateIsNull(member, pageable).getContent();

        return borrowInfoList.stream()
                .map(BorrowDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * 책 한권을 반납하면 데이터베이스에 저장
     * @param bookNumber
     */
    public void returnBook(Long bookNumber){
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
    public List<BorrowDTO> searchBooksReturnedInMonth(int year, int month, Pageable pageable) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<BorrowInfo> returnedInMonth = borrowRepository.findByReturnDateBetween(startOfMonth, endOfMonth, pageable).getContent();

        return returnedInMonth.stream()
                .map(BorrowDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * 아직 미반납인 책 리스트를 리턴(Admin)
     * @return List<BorrowDTO>
     */
    public List<BorrowDTO> searchNotReturnBooks(Pageable pageable) {
        List<BorrowInfo> notReturnedBorrows = borrowRepository.findByReturnDateIsNull(pageable).getContent();

        return notReturnedBorrows.stream()
                .map(BorrowDTO::of)
                .collect(Collectors.toList());


    }

    /**
     * 책 정보 업데이트(Admin)
     * @param id
     * @param bookRequest
     */
    public void updateBook(Long id, BookRequest bookRequest){
        BookInfo bookInfo = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, BookInfo.class));

        bookInfo.update( bookRequest.getTitle(), bookRequest.getImage(), bookRequest.getContent(), bookRequest.getAuthor(), bookRequest.getPublisher());
    }

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

    public void addBookNumber(Long bookNumber, Long id){
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
}
