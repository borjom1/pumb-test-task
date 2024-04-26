package com.example.task.reader;

import com.example.task.dto.Animal;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimalsXmlFileReader implements AnimalsFileReader {

    private final XmlMapper xmlMapper;

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_XML_VALUE;
    }

    @Override
    public List<Animal> readAnimals(@NonNull MultipartFile file) {
        if (!supports(file)) {
            throw new IllegalArgumentException("Not supported content type");
        }
        try {
            final Animal[] animals = xmlMapper.readValue(file.getBytes(), Animal[].class);
            return Arrays.asList(animals);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
