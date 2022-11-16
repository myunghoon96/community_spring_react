package com.example.demo.Api;

import com.example.demo.Dto.ImageDto;
import com.example.demo.Dto.MemberResponseDto;
import com.example.demo.Entity.Image;
import com.example.demo.Entity.Member;
import com.example.demo.Redis.RedisService;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Service.BoardService;
import com.example.demo.Service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageApiController {

    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public ImageApiController(ImageService imageService, ImageRepository imageRepository){
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @PostMapping
    public ApiResponse<Null> addImage(MultipartFile[] multipartFiles) throws Exception{
//        String IMAGE_UPLOAD_PATH = "C:\\Users\\NKNK\\Desktop\\demo\\frontend\\public\\";
        String IMAGE_UPLOAD_PATH = "/home/ubuntu/community_spring_react/frontend/build/";

        try {
            for(int i=0; i<multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                try (InputStream input = file.getInputStream()) {
                    try {
                        ImageIO.read(input).toString();
                    } catch (Exception e) {
                        throw new RuntimeException("올바른 이미지 형식이 아닙니다");
                    }
                }

                UUID uuid = UUID.randomUUID();
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String newFileName = LocalDate.now() + "_" + uuid.toString() + "." + fileExtension;

                Image image = new Image(originalFileName, newFileName, IMAGE_UPLOAD_PATH + newFileName);
                imageService.addImage(image);

                File savedFile = new File(IMAGE_UPLOAD_PATH, newFileName);
                file.transferTo(savedFile);
            }


            return ApiResponse.success(null);
        } catch(IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("이미지 저장 에러");
        }
    }

    @GetMapping
    public ApiResponse<List<ImageDto>> Board(MultipartFile[] multipartFiles) throws Exception{
        List<Image> images = imageRepository.findAll();
        List<ImageDto> imageDtoList = images.stream()
                .map(i-> new ImageDto(i))
                .collect(Collectors.toList());

        return ApiResponse.success(imageDtoList);
    }

}
