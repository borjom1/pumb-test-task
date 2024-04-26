package com.example.task.unit.reader;

import com.example.task.dto.Animal;
import com.example.task.reader.AnimalsCsvFileReader;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.example.task.dto.AnimalType.cat;
import static com.example.task.dto.Sex.female;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalsCsvFileReaderTest {

    private final AnimalsCsvFileReader reader = new AnimalsCsvFileReader();

    @Test
    @DisplayName("Should throw exception when file type not supported")
    void Should_ThrowException_When_FileTypeNotSupported() {
        final String unsupportedContentType = "text";
        final var file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(unsupportedContentType);

        assertThrows(IllegalArgumentException.class, () -> reader.readAnimals(file));
    }

    @SneakyThrows
    @Test
    @DisplayName("Should read animals when file type supported and not empty")
    void Should_ReadAnimals_When_FileTypeSupportedAndNotEmpty() {

        // prepare data
        final String fileContent = """
                Name,Type,Sex,Weight,Cost
                Buddy,cat,female,41,78
                Cooper,,female,46,23
                """;

        final List<Animal> expectedAnimals = List.of(
                Animal.builder()
                        .name("Buddy").type(cat).sex(female).weight(41.).cost(78.)
                        .build(),
                Animal.builder()
                        .name("Cooper").sex(female).weight(46.).cost(23.)
                        .build()
        );

        // mock file
        final var file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(reader.getContentType());
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes()));

        final List<Animal> animals = reader.readAnimals(file);

        assertThat(animals).isNotNull();
        assertThat(animals).isNotEmpty();
        assertThat(animals).hasSize(expectedAnimals.size());
        assertThat(animals).isEqualTo(expectedAnimals);
    }

    @SneakyThrows
    @Test
    @DisplayName("Should return empty collection when error occurred during reading")
    void Should_ReturnEmptyCollection_When_ErrorOccurredDuringReading() {

        final String fileContent = """
                Name,Type,Sex,Weight,Cost
                Buddy,cat,female,41,78
                Cooper,,xx,46,23z
                """;

        final var file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(reader.getContentType());
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes()));

        final List<Animal> animals = reader.readAnimals(file);

        assertThat(animals).isNotNull();
        assertThat(animals).isEmpty();
    }

}
