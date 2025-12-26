package com.example.plantmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "plant_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image; // Base64 encoded image

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @JsonBackReference
    private Plant plant;
}
