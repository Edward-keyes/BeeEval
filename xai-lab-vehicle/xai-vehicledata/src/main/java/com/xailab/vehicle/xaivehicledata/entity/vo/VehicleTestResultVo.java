package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.xailab.vehicle.xaicommon.utils.ExcelImport;
import lombok.Data;

@Data
public class VehicleTestResultVo {

    @ExcelImport("车型")
    private String vehicleModel;

    @ExcelImport("用例")
    private String caseName;

    @ExcelImport("功能域")
    private String functionDomain;

    @ExcelImport("三级指标")
    private String functionIndex;

    @ExcelImport("评分")
    private String score;

    @ExcelImport("user_case")
    private String caseNameEn;

}
