package com.example.plantmanager.controller;

import com.example.plantmanager.dto.TaskDto;
import com.example.plantmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<TaskDto>> listByPlant(@PathVariable Long plantId) {
        return ResponseEntity.ok(taskService.listByPlant(plantId));
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@Valid @RequestBody TaskDto dto) {
        return ResponseEntity.ok(taskService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable Long id, @Valid @RequestBody TaskDto dto) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
