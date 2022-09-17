package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long id;

    @NotBlank(message = "댓글 내용은 필수 값 입니다")
    @Column(columnDefinition = "text")
    private String content;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne
    @NotNull
    private Member member;

    @JsonIgnore
    @ManyToOne
    @NotNull
    private Board board;

    @Builder
    public Comment(String content, Member member, Board board){
        this.content = content;
        this.member = member;
        this.board = board;
    }
}
