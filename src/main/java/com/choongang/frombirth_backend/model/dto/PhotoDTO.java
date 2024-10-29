package com.choongang.frombirth_backend.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private Integer photoId;
    private Integer recordId;
    private String url;
    private LocalDateTime createdAt;
}
