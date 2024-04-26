package com.example.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@NoArgsConstructor
@Getter
@SuperBuilder
public class ApiError {

    @Builder.Default
    private Instant timestamp = Instant.now();

    private int status;
    private String error;
    private String path;

}
