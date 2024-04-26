package com.example.task.entity;

import com.example.task.dto.AnimalType;
import com.example.task.dto.Sex;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "animals")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private AnimalType type;

    @Basic(optional = false)
    private Sex sex;

    @Basic(optional = false)
    private double weight;

    @Enumerated
    private AnimalPriceCategory category;

    @Basic(optional = false)
    private double cost;

}
