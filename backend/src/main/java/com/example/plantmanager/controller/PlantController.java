package com.example.plantmanager.controller;

import com.example.plantmanager.dto.PlantDto;
import com.example.plantmanager.service.PlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plants")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;

    @GetMapping
    public ResponseEntity<List<PlantDto>> list() {
        return ResponseEntity.ok(plantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(plantService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlantDto> create(@Valid @RequestBody PlantDto dto) {
        return ResponseEntity.ok(plantService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantDto> update(@PathVariable Long id, @Valid @RequestBody PlantDto dto) {
        return ResponseEntity.ok(plantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        plantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
