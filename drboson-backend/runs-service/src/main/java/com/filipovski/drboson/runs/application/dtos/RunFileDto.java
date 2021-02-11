package com.filipovski.drboson.runs.application.dtos;

import com.filipovski.drboson.runs.domain.model.files.RunFile;
import lombok.Getter;

@Getter
public class RunFileDto {

    private String id;

    private String name;

    public RunFileDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RunFileDto from(RunFile file) {
        return new RunFileDto(
                file.id().getId(),
                file.getName().name()
        );
    }
}
