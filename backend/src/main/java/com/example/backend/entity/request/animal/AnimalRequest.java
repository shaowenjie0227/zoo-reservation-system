package com.example.backend.entity.request.animal;

import lombok.Data;

import java.util.List;

/**
 * @Author 
 * @Description 动物信息请求参数
 * @Version 1.0
 */
@Data
public class AnimalRequest {

    private Integer typeId;

    private String name;

    private String description;

    private String coverUrl;

    private String intro;

    private String season;

    private Integer viewCount;

    private List<String> tagList;
}