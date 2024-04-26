package com.example.task.unit.controller;

import com.example.task.controller.FilesController;
import com.example.task.dto.Animal;
import com.example.task.service.AnimalsReaderService;
import com.example.task.service.AnimalsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.task.dto.AnimalType.cat;
import static com.example.task.dto.AnimalType.dog;
import static com.example.task.dto.Sex.female;
import static com.example.task.dto.Sex.male;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilesControllerTest {

    @Mock
    private AnimalsReaderService animalsReaderService;

    @Mock
    private AnimalsService animalsService;

    @InjectMocks
    private FilesController controller;

    @Test
    @DisplayName("Should process file and persist animals when files provided")
    void Should_ProcessFileAndPersistAnimals_WhenFilesProvided() {
        final var files = new MultipartFile[]{mock(MultipartFile.class)};

        final List<Animal> animals = List.of(
                Animal.builder()
                        .name("Mia").cost(24.0).sex(female)
                        .type(cat).weight(31.0).build(),
                Animal.builder()
                        .name("Simon").cost(68.0).sex(male)
                        .type(dog).weight(6.0).build()
        );

        when(animalsReaderService.readAnimals(files)).thenReturn(animals);

        controller.uploadFile(files);

        verify(animalsReaderService, only()).readAnimals(files);
        verify(animalsService, only()).persistAnimals(animals);
    }

}
