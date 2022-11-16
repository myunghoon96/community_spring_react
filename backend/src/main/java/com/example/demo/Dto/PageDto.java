package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private T data;
    private PageInfo pageInfo;
}
