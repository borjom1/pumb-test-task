package com.example.task.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.function.Predicate.not;

public class FilesValidator implements ConstraintValidator<ValidFiles, MultipartFile[]> {

    private final Set<String> supportedTypes = Set.of(
            "text/csv",
            MediaType.APPLICATION_XML_VALUE
    );

    @Override
    public boolean isValid(MultipartFile[] files, @NonNull ConstraintValidatorContext context) {
        final List<MultipartFile> multipartFiles = Arrays.stream(files)
                .filter(not(MultipartFile::isEmpty))
                .toList();

        if (multipartFiles.isEmpty()) {
            return false;
        }

        return multipartFiles.stream()
                .map(MultipartFile::getContentType)
                .allMatch(supportedTypes::contains);
    }

}
