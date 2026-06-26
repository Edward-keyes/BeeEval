package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.xailab.vehicle.xaicommon.utils.ExcelImport;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: DomainTreeDataImportTemplate
 * @Description:
 *
 * @date: 2025/3/1 14:58
 */
@Data
public class DomainTreeDataImportTemplateVo implements Serializable{

    @ExcelImport(value = "车型")
    private String vehicleName;

    @ExcelImport(value = "系统版本")
    private String systemVersion;

    @ExcelImport(value = "测试时间")
    private String testDate;

    @ExcelImport(value = "类别")
    private String type;

    @ExcelImport(value = "功能域")
    private String functionDomain;

    @ExcelImport(value = "指标")
    private String domainIndex;

    @ExcelImport(value = "展示用数值")
    private Double showData;

    @ExcelImport(value = "计算用评分")
    private Double calculateData;


}
