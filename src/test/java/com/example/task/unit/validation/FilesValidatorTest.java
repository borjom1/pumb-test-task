package com.example.task.unit.validation;

import com.example.task.validation.FilesValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@ExtendWith(MockitoExtension.class)
public class FilesValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private final FilesValidator validator = new FilesValidator();

    @Test
    @DisplayName("Should return true when files content types are supported")
    void Should_ReturnTrue_When_FilesContentTypesAreSupported() {

        final var xmlFile = mock(MultipartFile.class);
        when(xmlFile.getContentType()).thenReturn(APPLICATION_XML_VALUE);

        final var csvFile = mock(MultipartFile.class);
        when(csvFile.getContentType()).thenReturn("text/csv");

        final boolean isValid = validator.isValid(new MultipartFile[]{xmlFile, csvFile}, context);

        assertThat(isValid).isTrue();
        verify(xmlFile, times(1)).getContentType();
        verify(csvFile, times(1)).getContentType();
    }

    @Test
    @DisplayName("Should skip empty files when exist")
    void Should_SkipEmptyFiles_When_Exist() {

        final var xmlFile = mock(MultipartFile.class);
        when(xmlFile.getContentType()).thenReturn(APPLICATION_XML_VALUE);

        final var emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(TRUE);

        final boolean isValid = validator.isValid(new MultipartFile[]{xmlFile, emptyFile}, context);

        assertThat(isValid).isTrue();
        verify(emptyFile, only()).isEmpty();
    }

    @Test
    @DisplayName("Should return false when at least one file has unsupported type")
    void Should_ReturnFalse_When_AtLeastOneFileHasUnsupportedType() {
        final String unsupportedContentType = "text";

        final var xmlFile = mock(MultipartFile.class);
        when(xmlFile.getContentType()).thenReturn(APPLICATION_XML_VALUE);

        final var textFile = mock(MultipartFile.class);
        when(textFile.getContentType()).thenReturn(unsupportedContentType);

        final boolean isValid = validator.isValid(new MultipartFile[]{xmlFile, textFile}, context);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false when files not present")
    void Should_ReturnFalse_When_FilesNotPresent() {
        final boolean isValid = validator.isValid(new MultipartFile[0], context);

        assertThat(isValid).isFalse();
    }

}
