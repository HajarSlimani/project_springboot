package com.example.plantmanager.repository;

import com.example.plantmanager.model.PlantTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantTaskRepository extends JpaRepository<PlantTask, Long> {
    List<PlantTask> findByPlant_Id(Long plantId);
}
