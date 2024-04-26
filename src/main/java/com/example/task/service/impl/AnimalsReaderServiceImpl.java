package com.example.task.service.impl;

import com.example.task.dto.Animal;
import com.example.task.reader.AnimalsFileReader;
import com.example.task.service.AnimalsReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalsReaderServiceImpl implements AnimalsReaderService {

    private final List<AnimalsFileReader> animalsFileReaders;

    @Override
    public List<Animal> readAnimals(@NonNull MultipartFile... files) {
        final List<Animal> animals = new ArrayList<>();

        for (MultipartFile file : files) {
            for (AnimalsFileReader reader : animalsFileReaders) {
                if (reader.supports(file)) {
                    animals.addAll(reader.readAnimals(file));
                    break;
                }
            }
        }

        return animals;
    }

}
