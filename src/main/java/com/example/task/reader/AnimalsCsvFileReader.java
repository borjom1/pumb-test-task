package com.example.task.reader;

import com.example.task.dto.Animal;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Component
public class AnimalsCsvFileReader implements AnimalsFileReader {

    @Override
    public String getContentType() {
        return "text/csv";
    }

    @Override
    public List<Animal> readAnimals(@NonNull MultipartFile file) {
        if (!supports(file)) {
            throw new IllegalArgumentException("Not supported content type");
        }
        try (final var reader = new InputStreamReader(file.getInputStream())) {
            return new CsvToBeanBuilder<Animal>(reader)
                    .withType(Animal.class)
                    .build()
                    .parse();
        } catch (IOException | RuntimeException e) {
            return Collections.emptyList();
        }
    }

}
