package com.example.task.service;

import com.example.task.dto.Animal;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnimalsReaderService {
    List<Animal> readAnimals(@NonNull MultipartFile[] files);
}
