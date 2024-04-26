package com.example.task.service;

import com.example.task.dto.*;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AnimalsService {
    void persistAnimals(@NonNull List<Animal> animals);

    List<AnimalDto> getAnimalsBy(AnimalType type, Integer category, Sex sex, List<AnimalOption> sortBy);
}
