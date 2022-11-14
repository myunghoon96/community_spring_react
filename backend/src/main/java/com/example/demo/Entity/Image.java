package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class Image extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long id;
    private String originalFileName;
    private String newFileName;
    private String filePath;

    @Builder
    public Image(String originalFileName, String newFileName, String filePath) {
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
        this.filePath = filePath;
    }

}
