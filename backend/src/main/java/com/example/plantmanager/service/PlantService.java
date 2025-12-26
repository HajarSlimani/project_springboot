package com.example.plantmanager.service;

import com.example.plantmanager.dto.LogDto;
import com.example.plantmanager.dto.PlantDto;
import com.example.plantmanager.dto.TaskDto;
import com.example.plantmanager.model.LogEntry;
import com.example.plantmanager.model.Plant;
import com.example.plantmanager.model.PlantTask;
import com.example.plantmanager.repository.PlantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantService {
    private final PlantRepository plantRepository;

    public List<PlantDto> findAll() {
        return plantRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public PlantDto findById(Long id) {
        Plant plant = plantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plant not found"));
        return toDto(plant);
    }

    public PlantDto create(PlantDto dto) {
        Plant plant = new Plant();
        plant.setName(dto.getName());
        plant.setSpecies(dto.getSpecies());
        plant.setLocation(dto.getLocation());
        plant.setImage(dto.getImage());
        Plant saved = plantRepository.save(plant);
        return toDto(saved);
    }

    public PlantDto update(Long id, PlantDto dto) {
        Plant plant = plantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plant not found"));
        plant.setName(dto.getName());
        plant.setSpecies(dto.getSpecies());
        plant.setLocation(dto.getLocation());
        plant.setImage(dto.getImage());
        return toDto(plant);
    }

    public void delete(Long id) {
        plantRepository.deleteById(id);
    }

    private PlantDto toDto(Plant plant) {
        List<TaskDto> tasks = plant.getTasks() == null ? List.of() : plant.getTasks().stream().map(this::toTaskDto).toList();
        List<LogDto> logs = plant.getLogs() == null ? List.of() : plant.getLogs().stream().map(this::toLogDto).toList();
        return PlantDto.builder()
                .id(plant.getId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .location(plant.getLocation())
                .image(plant.getImage())
                .tasks(tasks)
                .logs(logs)
                .build();
    }

    private TaskDto toTaskDto(PlantTask t) {
        return TaskDto.builder()
                .id(t.getId())
                .type(t.getType())
                .frequencyDays(t.getFrequencyDays())
                .lastDone(t.getLastDone())
                .plantId(t.getPlant() != null ? t.getPlant().getId() : null)
                .build();
    }

    private LogDto toLogDto(LogEntry l) {
        return LogDto.builder()
                .id(l.getId())
                .date(l.getDate())
                .note(l.getNote())
                .image(l.getImage())
                .plantId(l.getPlant() != null ? l.getPlant().getId() : null)
                .build();
    }
}
