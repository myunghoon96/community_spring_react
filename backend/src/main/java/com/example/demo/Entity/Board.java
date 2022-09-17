package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Board extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "멤버는 필수 값 입니다")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(unique = true)
    @NotBlank(message = "제목은 필수 값 입니다")
    private String title;

    @NotBlank(message = "내용은 필수 값 입니다")
    @Column(columnDefinition = "text")
    private String content;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private int view;

    @Builder
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public Board(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    @Builder
    public Board(Member member, String title, String content, int view) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.view = view;
    }
}
