package com.example.plantmanager.service;

import com.example.plantmanager.dto.LogDto;
import com.example.plantmanager.model.LogEntry;
import com.example.plantmanager.model.Plant;
import com.example.plantmanager.repository.LogEntryRepository;
import com.example.plantmanager.repository.PlantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {
    private final LogEntryRepository logRepository;
    private final PlantRepository plantRepository;

    public List<LogDto> listByPlant(Long plantId) {
        return logRepository.findByPlant_Id(plantId).stream().map(this::toDto).toList();
    }

    public LogDto create(LogDto dto) {
        Plant plant = plantRepository.findById(dto.getPlantId()).orElseThrow(() -> new EntityNotFoundException("Plant not found"));
        LogEntry l = LogEntry.builder()
                .date(dto.getDate())
                .note(dto.getNote())
                .image(dto.getImage())
                .plant(plant)
                .build();
        return toDto(logRepository.save(l));
    }

    public LogDto update(Long id, LogDto dto) {
        LogEntry l = logRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Log not found"));
        l.setDate(dto.getDate());
        l.setNote(dto.getNote());
        l.setImage(dto.getImage());
        if (dto.getPlantId() != null && (l.getPlant() == null || !dto.getPlantId().equals(l.getPlant().getId()))) {
            Plant plant = plantRepository.findById(dto.getPlantId()).orElseThrow(() -> new EntityNotFoundException("Plant not found"));
            l.setPlant(plant);
        }
        return toDto(l);
    }

    public void delete(Long id) {
        logRepository.deleteById(id);
    }

    private LogDto toDto(LogEntry l) {
        return LogDto.builder()
                .id(l.getId())
                .date(l.getDate())
                .note(l.getNote())
                .image(l.getImage())
                .plantId(l.getPlant() != null ? l.getPlant().getId() : null)
                .build();
    }
}
