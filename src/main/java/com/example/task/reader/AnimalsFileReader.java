package com.example.task.reader;

import com.example.task.dto.Animal;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

public interface AnimalsFileReader {
    String getContentType();

    default boolean supports(@NonNull MultipartFile file) {
        return Objects.equals(file.getContentType(), getContentType());
    }

    List<Animal> readAnimals(@NonNull MultipartFile file);
}
