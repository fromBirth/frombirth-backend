package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public interface RecordCustomRepository {
    List<RecordPhotoDTO> getRecordByIdAndMonth(Integer childId, String month);
    Slice<RecordDTO> getRecordPage (Integer childId, Integer recordId, PageRequest pageRequest);
}
