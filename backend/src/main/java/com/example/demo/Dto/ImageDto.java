package com.example.demo.Dto;

import com.example.demo.Entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
public class ImageDto {

    private Long id;
    private String originalFileName;
    private String newFileName;
    private String filePath;

    @Builder
    public ImageDto(String originalFileName, String newFileName, String filePath) {
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
        this.filePath = filePath;
    }

    @Builder
    public ImageDto(Image image) {
        this.id = image.getId();
        this.originalFileName = image.getOriginalFileName();
        this.newFileName = image.getNewFileName();
        this.filePath = image.getFilePath();
    }
}
