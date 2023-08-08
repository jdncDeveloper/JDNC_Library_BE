package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.Book;
import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.book.model.BookDTO;
import com.example.jdnc_library.feature.book.model.BorrowDTO;
import com.example.jdnc_library.feature.book.model.BookRequest;
import com.example.jdnc_library.feature.book.model.BorrowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    //책 1권의 정보를 리턴
    public BookDTO getBookById(Long id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book newBook = book.get();
            return new BookDTO(id, newBook.getTitle(), newBook.getImage(), newBook.getContent(), newBook.getAuthor(), newBook.getPublisher());
        }
        else {
            return null;
        }
    }

    // 제목으로 검색했을 때 검색어가 포함된 모든 항목을 리턴
    public List<BookDTO> searchBooks(String title) {
        return collectionRepository.findAllByBook_TitleContaining(title)
                .stream()
                .map(BookDTO::of)
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchBooksByTitle(String title){
        List<Book> bookList = bookRepository.findAllByTitleContaining(title);

        return bookList.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getImage(), book.getContent(), book.getAuthor(), book.getPublisher()))
                .collect(Collectors.toList());
    }

    //책의 QR을 찍었을 때 책 정보 리턴
    public BookDTO qrBook(Long bookNumber){
        Optional<CollectionInfo> collectionInfoOptional = collectionRepository.findByBookNumber(bookNumber);
        CollectionInfo collectionInfo = collectionInfoOptional.get();
        return BookDTO.of(collectionInfo);
    }

    //책을 빌릴 때 데이터베이스에 저장
    public void borrowBook(BorrowRequest borrowRequest){
        CollectionInfo collectionInfo = collectionRepository.findByBookNumber(borrowRequest.getBookNumber())
                .orElseThrow(() -> new EntityNotFoundException(borrowRequest.getBookNumber(), CollectionInfo.class));

        BorrowInfo borrowInfo = new BorrowInfo(borrowRequest.getBorrowId(), collectionInfo);
        borrowRepository.save(borrowInfo);
    }

    //반납함 QR을 찍었을 때 반납 가능한 도서 리스트를 리턴
    public List<BorrowDTO> returnBookList(BorrowRequest borrowRequest){
        List<BorrowInfo> borrowInfoList = borrowRepository.findAllByCreatedByAndReturnDateIsNull(borrowRequest.getBorrowId());

        return borrowInfoList.stream()
                .map(BorrowDTO::of)
                .collect(Collectors.toList());
    }

    //책 한권을 반납하면 데이터베이스에 저장
    public void returnBook(BorrowRequest borrowRequest){
        Optional<BorrowInfo> borrowInfoOptional = borrowRepository.findByCollectionInfo_BookNumber(borrowRequest.getBookNumber());
        BorrowInfo borrowInfo = borrowInfoOptional.get();
        borrowInfo.returnBook(LocalDateTime.now());
        borrowRepository.save(borrowInfo);
    }


    //입력한 달에 빌린 책 리스트를 리턴(Admin)
    public List<BorrowDTO> searchBooksReturnedInMonth(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<BorrowInfo> returnedInMonth = borrowRepository.findByReturnDateBetween(startOfMonth, endOfMonth);

        return returnedInMonth.stream()
                .map(BorrowDTO::of)
                .collect(Collectors.toList());
    }

    //아직 미반납인 책 리스트를 리턴(Admin)
    public List<BorrowDTO> searchNotReturnBooks() {
        List<BorrowInfo> notReturnedBorrows = borrowRepository.findByReturnDateIsNull();

        return notReturnedBorrows.stream()
                .map(BorrowDTO::of)
                .collect(Collectors.toList());


    }

    //책 정보 업데이트(Admin)
    public void updateBook(Long id, BookRequest bookRequest){
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Book.class));

        book.update( bookRequest.getTitle(), bookRequest.getImage(), bookRequest.getContent(), bookRequest.getAuthor(), bookRequest.getPublisher());
    }

    //책 정보 추가(Admin)
    public void saveBook(BookRequest bookRequest){

        Book book = bookRequest.toEntity();

        bookRepository.save(book);
    }
}
