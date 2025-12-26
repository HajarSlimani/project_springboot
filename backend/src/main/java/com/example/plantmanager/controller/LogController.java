package com.example.plantmanager.controller;

import com.example.plantmanager.dto.LogDto;
import com.example.plantmanager.service.LogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<LogDto>> listByPlant(@PathVariable Long plantId) {
        return ResponseEntity.ok(logService.listByPlant(plantId));
    }

    @PostMapping
    public ResponseEntity<LogDto> create(@Valid @RequestBody LogDto dto) {
        return ResponseEntity.ok(logService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogDto> update(@PathVariable Long id, @Valid @RequestBody LogDto dto) {
        return ResponseEntity.ok(logService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
