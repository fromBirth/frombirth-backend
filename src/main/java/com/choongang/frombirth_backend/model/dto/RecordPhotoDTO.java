package com.choongang.frombirth_backend.model.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecordPhotoDTO {
    private Integer recordId;
    private Integer childId;
    private LocalDate recordDate;
    private String photoUrl;
}
