package com.example.backend.entity.vo.animallocation;

import com.example.backend.entity.AnimalLocation;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AnimalLocationVO对象", description = "动物地点视图对象")
public class AnimalLocationVO extends AnimalLocation {

}