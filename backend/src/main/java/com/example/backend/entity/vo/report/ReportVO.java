package com.example.backend.entity.vo.report;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.Report;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 举报VO类
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReportVO extends Report {
    @TableField(exist = false)
    private String reporterName;
}