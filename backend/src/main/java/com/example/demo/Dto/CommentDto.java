package com.example.demo.Dto;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    @NotBlank(message = "댓글 내용은 필수 값 입니다")
    private String content;
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Member member;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Board board;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.member = comment.getMember();
        this.board = comment.getBoard();
    }

    public CommentDto(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        this.board = board;
    }
}
