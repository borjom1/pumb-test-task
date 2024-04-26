package com.example.task.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AnimalDto {

    private String name;
    private AnimalType type;
    private Sex sex;
    private double weight;
    private int category;
    private double cost;

}
