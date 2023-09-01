package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.exception.clienterror._400.ExistEntityException;
import com.example.jdnc_library.feature.book.DTO.CollectionDetailDTO;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final CollectionRepository collectionRepository;
    private final BorrowRepository borrowRepository;

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
     * @param bookNumber
     */
    //TODO : 중복 대여 처리하기
    @Transactional
    public void borrowBook(long bookNumber){
        Optional<BorrowInfo> borrowInfoOptional = borrowRepository.findByCollectionInfo_BookNumberAndReturnDateIsNull(bookNumber);
        if(borrowInfoOptional.isPresent()){
            throw new ExistEntityException();
        }
        else {
            CollectionInfo collectionInfo = collectionRepository.findByBookNumber(bookNumber)
                    .orElseThrow(() -> new EntityNotFoundException(bookNumber, CollectionInfo.class));

            BorrowInfo borrowInfo = new BorrowInfo(collectionInfo);
            borrowRepository.save(borrowInfo);
            collectionInfo.updateAvailable(false);
        }
    }

    /**
     * 반납함 QR을 찍었을 때 반납 가능한 도서 리스트를 리턴
     * @param member
     * @return
     */
    @Transactional
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
    public void returnBook(long bookNumber, int state) {
        Optional<BorrowInfo> borrowInfoOptional = borrowRepository.findByCollectionInfo_BookNumberAndReturnDateIsNull(bookNumber);
        BorrowInfo borrowInfo = borrowInfoOptional.get();
        borrowInfo.returnBook(LocalDateTime.now(), state);
        borrowRepository.save(borrowInfo);
    }
}
