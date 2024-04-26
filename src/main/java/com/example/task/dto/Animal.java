package com.example.task.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Animal {

    @NotBlank(message = "can't be blank")
    @CsvBindByName(column = "Name")
    private String name;

    @NotNull(message = "can't be null")
    @CsvBindByName(column = "Type")
    private AnimalType type;

    @NotNull(message = "can't be null")
    @CsvBindByName(column = "Sex")
    private Sex sex;

    @NotNull(message = "can't be null")
    @DecimalMin(value = "0.0", message = "minimal value is 0.0")
    @CsvBindByName(column = "Weight")
    private Double weight;

    @NotNull(message = "can't be null")
    @DecimalMin(value = "0.0", message = "minimal value is 0.0")
    @CsvBindByName(column = "cost")
    private Double cost;

}
