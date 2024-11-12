package com.choongang.frombirth_backend.model.dto;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgeGenderStatisticsDTO {

    private Integer ageGenderStatisticsId;
    private String gender;
    private Integer age;
    private Double avgHeight;
    private Double avgWeight;
}
