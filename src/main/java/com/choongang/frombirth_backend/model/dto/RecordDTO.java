package com.choongang.frombirth_backend.model.dto;

import com.choongang.frombirth_backend.model.entity.Record;
import java.util.List;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {
    private Integer recordId;
    private Integer childId;
    private LocalDate recordDate;
    private Float height;
    private Float weight;
    private String title;
    private String content;
    private Integer videoResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PhotoDTO> images;

//    public RecordDTO(Record record) {
//    }
}
