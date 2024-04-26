package com.example.task.controller;

import com.example.task.dto.AnimalDto;
import com.example.task.dto.AnimalType;
import com.example.task.dto.Sex;
import com.example.task.dto.AnimalOption;
import com.example.task.service.AnimalsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalsController {

    private final AnimalsService animalsService;

    @GetMapping
    public List<AnimalDto> getAnimals(@RequestParam(required = false) AnimalType type,
                                      @RequestParam(required = false) @Min(1) @Max(4) Integer category,
                                      @RequestParam(required = false) Sex sex,
                                      @RequestParam(required = false) List<AnimalOption> sortBy) {
        return animalsService.getAnimalsBy(type, category, sex, sortBy);
    }

}
