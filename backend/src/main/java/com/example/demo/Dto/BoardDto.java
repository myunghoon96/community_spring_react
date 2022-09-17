package com.example.demo.Dto;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    @ManyToOne
    private Member member;
    @NotBlank(message = "제목은 필수 값 입니다")
    private String title;
    @NotBlank(message = "내용은 필수 값 입니다")
    private String content;

    private int view;

    @JsonFormat(pattern = "MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDate;

    public BoardDto(Board board){
        this.id = board.getId();
        this.member = board.getMember();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdDate = board.getCreatedDate();
        this.modifiedDate = board.getModifiedDate();
        this.view = board.getView();
    }

    public BoardDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
