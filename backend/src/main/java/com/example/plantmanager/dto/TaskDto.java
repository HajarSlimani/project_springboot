package com.example.plantmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private String type;
    private int frequencyDays;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDone;
    private Boolean completed;
    private Long plantId;
}
