package com.example.task.controller;

import com.example.task.dto.Animal;
import com.example.task.service.AnimalsReaderService;
import com.example.task.service.AnimalsService;
import com.example.task.validation.ValidFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FilesController {

    private final AnimalsReaderService animalsReaderService;
    private final AnimalsService animalsService;

    @PostMapping("/uploads")
    @ResponseStatus(CREATED)
    public void uploadFile(@ValidFiles @RequestPart MultipartFile[] files) {
        final List<Animal> animals = animalsReaderService.readAnimals(files);
        animalsService.persistAnimals(animals);
    }

}
