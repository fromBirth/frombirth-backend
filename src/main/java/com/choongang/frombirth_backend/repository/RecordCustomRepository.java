package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.dto.MonthRecordPhotoDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import com.choongang.frombirth_backend.model.entity.Record;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public interface RecordCustomRepository {
    List<RecordPhotoDTO> getRecordByIdAndMonth(Integer childId, String month);
    Slice<RecordDTO> getRecordPage (Integer childId, Integer recordId, PageRequest pageRequest, String query);
    Slice<MonthRecordPhotoDTO> getRecordPhotoByMonth(Integer childId, LocalDate lastMonth, PageRequest pageRequest, String query);
    RecordDTO findByChildIdAndDate(Integer childId, String date);

    List<PhotoDTO> getRandomPhotoList(Integer childId);

    RecordDTO getRecordDetail(Integer recordId);
}
