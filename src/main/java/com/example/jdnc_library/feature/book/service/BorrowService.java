package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.exception.clienterror._400.LimitOutBookBorrowSizeException;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.exception.clienterror._400.EntityExistsException;
import com.example.jdnc_library.feature.book.DTO.BookNumberRequest;
import com.example.jdnc_library.feature.book.DTO.CollectionDetailDTO;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import com.example.jdnc_library.feature.book.DTO.CountDTO;
import com.example.jdnc_library.feature.book.repository.BorrowInfoQueryRepository;
import com.example.jdnc_library.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final CollectionRepository collectionRepository;
    private final BorrowRepository borrowRepository;
    private final BorrowInfoQueryRepository borrowInfoQueryRepository;
    private final int LIMIT_BOOK_BORROW_COUNT = 3;
    /**
     * 책의 QR을 찍었을 때 책 정보 리턴
     * @param bookNumber
     * @return
     */
    @Deprecated
    public CollectionDetailDTO qrBook(long bookNumber){
        CollectionInfo collectionInfo = collectionRepository.findByBookNumber(bookNumber)
                .orElseThrow(() -> new EntityNotFoundException(bookNumber, CollectionInfo.class));
        return CollectionDetailDTO.of(collectionInfo);
    }

    /**
     * 책을 빌릴 때 데이터베이스에 저장
     * @param bookNumberRequest
     */
    @Transactional
    public void borrowBook(BookNumberRequest bookNumberRequest, Member member){

        if (!isBorrowBook(member)) throw new LimitOutBookBorrowSizeException(member.getId());

        if (borrowInfoQueryRepository.isExistNonReturnBorrowInfo(bookNumberRequest.getBookNumber())){
            throw new EntityExistsException();
        }

        CollectionInfo collectionInfo = collectionRepository.findByBookNumber(bookNumberRequest.getBookNumber())
            .orElseThrow(() -> new EntityNotFoundException(bookNumberRequest.getBookNumber(), CollectionInfo.class));

        BorrowInfo borrowInfo = new BorrowInfo(collectionInfo);
        borrowRepository.save(borrowInfo);
        collectionInfo.updateAvailable(false);
    }

    /**
     * 반납함 QR을 찍었을 때 반납 가능한 도서 리스트를 리턴
     *
     * @return
     */
    @Transactional
    public List<BorrowListDTO> returnBookList(PrincipalDetails principalDetails, Pageable pageable){
        Member me = principalDetails.getMember();
        return borrowInfoQueryRepository.getNotYetReturnBookList(me.getId(), pageable).getContent();
    }

    /**
     * 책 한권을 반납하면 데이터베이스에 저장
     * @param bookNumber
     */
    @Transactional
    public void returnBook(long bookNumber, int state) {
        BorrowInfo borrowInfo = borrowInfoQueryRepository.getNonReturnBorrowInfo(bookNumber)
            .orElseThrow(() -> new EntityNotFoundException(bookNumber, BorrowInfo.class));
        borrowInfo.returnBook(LocalDateTime.now(), state);
    }

    private boolean isBorrowBook(Member member) {
        return borrowInfoQueryRepository.getNotYetReturnBookListSize(member.getId()) < LIMIT_BOOK_BORROW_COUNT;
    }

    public CountDTO getNotChecked(){
        return new CountDTO(borrowRepository.countByAdminCheckIsFalse());
    }

    public CountDTO getReturned(){
        return new CountDTO(borrowRepository.countByAdminCheckIsFalseAndReturnDateIsNotNull());
    }
}
