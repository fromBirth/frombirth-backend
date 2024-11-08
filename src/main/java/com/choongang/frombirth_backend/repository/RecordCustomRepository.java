package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import java.util.List;

public interface RecordCustomRepository {
    List<RecordPhotoDTO> getRecordByIdAndMonth(Integer childId, String month);
}
