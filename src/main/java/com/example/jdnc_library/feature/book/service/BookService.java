package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.book.DTO.AdminRequest;
import com.example.jdnc_library.feature.book.DTO.BookRequest;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
     * 입력한 달에 빌린 책 리스트를 리턴(Admin)
     * @param year
     * @param month
     * @return
     */
    public List<BorrowListDTO> searchBooksBorrowedInMonth(int year, int month, Pageable pageable) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<BorrowInfo> returnedInMonth = borrowRepository.findByCreatedAtBetween(startOfMonth, endOfMonth, pageable).getContent();

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

        bookInfo.update( bookRequest.getTitle(), bookRequest.getImage(), bookRequest.getContent(), bookRequest.getAuthor(), bookRequest.getPublisher(), BookGroup.valueOf(bookRequest.getBookGroup()));
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

    public void addBookNumber(long bookNumber, long id){
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
     * 반납 최종 확인
     * @param adminRequest
     */
    public void adminCheck(AdminRequest adminRequest){
        List<Long> ids = adminRequest.getIds();

        for(Long id : ids){
            updateAdminCheck(id);
        }
    }

    /**
     * 반납 확인 트랜잭션처리
     * @param id
     */
    @Transactional
    public void updateAdminCheck(Long id){
        Optional<BorrowInfo> borrowInfoOptional = borrowRepository.findById(id);

        if (borrowInfoOptional.isPresent()) {
            BorrowInfo borrowInfo = borrowInfoOptional.get();
            borrowInfo.updateAdminCheck(true);
            borrowRepository.save(borrowInfo);

            CollectionInfo collectionInfo = borrowInfo.getCollectionInfo();
            collectionInfo.updateAvailable(true);
            collectionRepository.save(collectionInfo);
        } else {
            throw new EntityNotFoundException(id, BookInfo.class);
        }
    }

    /**
     * 책 소실 처리
     * @param adminRequest
     */
    public void lostBook(AdminRequest adminRequest){
        List<Long> ids = adminRequest.getIds();
        for(Long id : ids) {
            updateLostBook(id);
        }

    }

    /**
     * 소실 트랜잭션 처리
     * @param id
     */
    @Transactional
    public void updateLostBook(Long id) {
        Optional<CollectionInfo> collectionInfoOptional = collectionRepository.findById(id);

        if (collectionInfoOptional.isPresent()) {
            CollectionInfo collectionInfo = collectionInfoOptional.get();
            collectionInfo.lostBook(!collectionInfo.isLost());
            collectionRepository.save(collectionInfo);
        } else {
            throw new EntityNotFoundException(id, CollectionInfo.class);
        }
    }
}
