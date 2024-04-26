package com.example.task.unit.reader;

import com.example.task.dto.Animal;
import com.example.task.reader.AnimalsXmlFileReader;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.task.dto.AnimalType.cat;
import static com.example.task.dto.Sex.female;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalsXmlFileReaderTest {

    @Mock
    private XmlMapper xmlMapper;

    @InjectMocks
    private AnimalsXmlFileReader reader;

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

        final List<Animal> expectedAnimals = List.of(
                Animal.builder()
                        .name("Buddy").type(cat).sex(female).weight(41.).cost(78.)
                        .build(),
                Animal.builder()
                        .name("Cooper").sex(female).weight(46.).cost(23.)
                        .build()
        );

        final var file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(reader.getContentType());
        when(xmlMapper.readValue(file.getBytes(), Animal[].class)).thenReturn(expectedAnimals.toArray(Animal[]::new));

        final List<Animal> actualAnimals = reader.readAnimals(file);

        assertThat(actualAnimals).isNotNull();
        assertThat(actualAnimals).isNotEmpty();
        assertThat(actualAnimals).hasSize(expectedAnimals.size());
        assertThat(actualAnimals).isEqualTo(expectedAnimals);
    }

    @SneakyThrows
    @Test
    @DisplayName("Should return empty collection when error occurred during reading")
    void Should_ReturnEmptyCollection_When_ErrorOccurredDuringReading() {
        final var file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(reader.getContentType());
        when(xmlMapper.readValue(file.getBytes(), Animal[].class)).thenThrow(IOException.class);

        final List<Animal> actualAnimals = reader.readAnimals(file);

        assertThat(actualAnimals).isNotNull();
        assertThat(actualAnimals).isEmpty();
    }

}
