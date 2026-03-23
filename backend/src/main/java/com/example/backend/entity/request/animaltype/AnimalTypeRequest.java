package com.example.backend.entity.request.animaltype;

import lombok.Data;

/**
 * @Author 
 * @Description 动物物种请求参数
 * @Version 1.0
 */
@Data
public class AnimalTypeRequest {
    /**
     * 物种名称
     */
    private String name;
}