package com.example.backend.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 
 * @Description 举报请求参数类
 * @Version 1.0
 */
@Data
@ApiModel(value = "ReportRequest对象", description = "举报请求参数")
public class ReportRequest {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("地点")
    private String location;

    @ApiModelProperty("发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurrenceTime;

    @ApiModelProperty("图片证据")
    private String imgUrl;

    @ApiModelProperty("联系方式")
    private String contactInfo;

    @ApiModelProperty("是否匿名")
    private Boolean isAnonymous;

    @ApiModelProperty("状态（0-待处理，1-处理中，2-已处理）")
    private Integer status;

    @ApiModelProperty("处理结果")
    private String processResult;
}