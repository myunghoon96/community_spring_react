package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {

    private int page;
    private int size;
    private int totalElements;
    private int totalPages;

}
