package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.xailab.vehicle.xaicommon.utils.ExcelImport;
import lombok.Data;

@Data
public class FunctionTreeInputVo {

    @ExcelImport("一级标签")
    private String firstLevelLabel;

    @ExcelImport("二级标签")
    private String secondLevelLabel;

    @ExcelImport("三级标签")
    private String thirdLevelLabel;

    @ExcelImport("功能说明")
    private String description;

}
