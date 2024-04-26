package com.example.task.unit.controller;

import com.example.task.controller.AnimalsController;
import com.example.task.dto.AnimalDto;
import com.example.task.service.AnimalsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.task.dto.AnimalType.dog;
import static com.example.task.dto.Sex.female;
import static com.example.task.dto.Sex.male;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalsControllerTest {

    @Mock
    private AnimalsService animalsService;

    @InjectMocks
    private AnimalsController controller;

    @Test
    @DisplayName("Should call animals service when get animals")
    void Should_CallAnimalsService_When_GetAnimals() {

        final List<AnimalDto> animals = List.of(
                AnimalDto.builder()
                        .name("Mia").cost(24.0).sex(female)
                        .type(dog).weight(31.0).category(2)
                        .build(),
                AnimalDto.builder()
                        .name("Simba").cost(68.0).sex(male)
                        .type(dog).weight(6.0).category(4)
                        .build()
        );

        when(animalsService.getAnimalsBy(dog, null, null, null)).thenReturn(animals);

        final List<AnimalDto> actualAnimals = controller.getAnimals(dog, null, null, null);

        assertThat(actualAnimals).isNotNull();
        assertThat(actualAnimals).isNotEmpty();
        assertThat(actualAnimals).hasSize(animals.size());
        assertThat(actualAnimals).isEqualTo(animals);

        verify(animalsService, only()).getAnimalsBy(dog, null, null, null);
    }

}
