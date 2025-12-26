package com.example.plantmanager.service;

import com.example.plantmanager.dto.TaskDto;
import com.example.plantmanager.model.Plant;
import com.example.plantmanager.model.PlantTask;
import com.example.plantmanager.repository.PlantRepository;
import com.example.plantmanager.repository.PlantTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final PlantTaskRepository taskRepository;
    private final PlantRepository plantRepository;

    public List<TaskDto> listByPlant(Long plantId) {
        return taskRepository.findByPlant_Id(plantId).stream().map(this::toDto).toList();
    }

    public TaskDto create(TaskDto dto) {
        Plant plant = plantRepository.findById(dto.getPlantId()).orElseThrow(() -> new EntityNotFoundException("Plant not found"));
        PlantTask t = PlantTask.builder()
                .type(dto.getType())
                .frequencyDays(dto.getFrequencyDays())
                .lastDone(dto.getLastDone())
                .completed(dto.getCompleted() != null ? dto.getCompleted() : false)
                .plant(plant)
                .build();
        return toDto(taskRepository.save(t));
    }

    public TaskDto update(Long id, TaskDto dto) {
        PlantTask t = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        t.setType(dto.getType());
        t.setFrequencyDays(dto.getFrequencyDays());
        t.setLastDone(dto.getLastDone());
        if (dto.getCompleted() != null) {
            t.setCompleted(dto.getCompleted());
        }
        if (dto.getPlantId() != null && (t.getPlant() == null || !dto.getPlantId().equals(t.getPlant().getId()))) {
            Plant plant = plantRepository.findById(dto.getPlantId()).orElseThrow(() -> new EntityNotFoundException("Plant not found"));
            t.setPlant(plant);
        }
        return toDto(t);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDto toDto(PlantTask t) {
        return TaskDto.builder()
                .id(t.getId())
                .type(t.getType())
                .frequencyDays(t.getFrequencyDays())
                .lastDone(t.getLastDone())
                .completed(t.getCompleted())
                .plantId(t.getPlant() != null ? t.getPlant().getId() : null)
                .build();
    }
}
