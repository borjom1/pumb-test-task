package com.example.task.unit.service;

import com.example.task.dto.Animal;
import com.example.task.reader.AnimalsFileReader;
import com.example.task.service.impl.AnimalsReaderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.task.dto.AnimalType.cat;
import static com.example.task.dto.AnimalType.dog;
import static com.example.task.dto.Sex.female;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalsReaderServiceImplTest {

    @Mock
    private AnimalsFileReader reader;

    @Mock
    private AnimalsFileReader reader2;

    private AnimalsReaderServiceImpl animalsReaderService;

    @BeforeEach
    void setUp() {
        animalsReaderService = new AnimalsReaderServiceImpl(List.of(reader, reader2));
    }

    @Test
    @DisplayName("Should return animals when different files and readers provided")
    void Should_ReturnAnimals_When_DifferentFilesAndReadersProvided() {

        final var mia = Animal.builder()
                .name("Mia").cost(24.0).sex(female)
                .type(cat).weight(31.0).build();

        final var nala = Animal.builder()
                .name("Nala").cost(68.0).sex(female)
                .type(dog).weight(6.0).build();

        final List<Animal> expectedAnimals = List.of(mia, nala);

        // mock files
        final var file = mock(MultipartFile.class);
        final var file2 = mock(MultipartFile.class);

        // mock readers
        // reads only first file
        when(reader.supports(any(MultipartFile.class))).thenReturn(FALSE);
        when(reader.supports(file)).thenReturn(TRUE);

        // reads only second file
        when(reader2.supports(any(MultipartFile.class))).thenReturn(FALSE);
        when(reader2.supports(file2)).thenReturn(TRUE);

        // mock files content
        when(reader.readAnimals(file)).thenReturn(List.of(mia));
        when(reader2.readAnimals(file2)).thenReturn(List.of(nala));

        final List<Animal> actualAnimals = animalsReaderService.readAnimals(file, file2);

        assertThat(actualAnimals).isNotNull();
        assertThat(actualAnimals).isNotEmpty();
        assertThat(actualAnimals).hasSize(2);
        assertThat(actualAnimals).isEqualTo(expectedAnimals);
    }


}
