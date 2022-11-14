package com.example.demo.Service;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Image;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.ImageRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public Long addImage(Image image){
        imageRepository.save(image);
        return image.getId();
    }

}
