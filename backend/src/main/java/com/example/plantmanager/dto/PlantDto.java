package com.example.plantmanager.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantDto {
    private Long id;
    private String name;
    private String species;
    private String location;
    private String image;

    private List<TaskDto> tasks;
    private List<LogDto> logs;
}
