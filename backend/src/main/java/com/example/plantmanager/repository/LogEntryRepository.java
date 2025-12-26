package com.example.plantmanager.repository;

import com.example.plantmanager.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    List<LogEntry> findByPlant_Id(Long plantId);
}
