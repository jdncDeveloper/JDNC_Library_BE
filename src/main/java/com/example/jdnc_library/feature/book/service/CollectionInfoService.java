package com.example.jdnc_library.feature.book.service;

import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.book.DTO.CollectionDetailDTO;
import com.example.jdnc_library.feature.book.repository.CollectionInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionInfoService {

    private final CollectionInfoQueryRepository collectionInfoQueryRepository;

    //진행중
    public CollectionDetailDTO qrBook(long bookNumber){
        return collectionInfoQueryRepository.getCollectionDetailByBookNumber(bookNumber);
    }
}
