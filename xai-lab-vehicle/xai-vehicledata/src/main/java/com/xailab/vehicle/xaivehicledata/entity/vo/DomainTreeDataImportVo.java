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
public class DomainTreeDataImportVo implements Serializable {

    
    private Long vehicleId;
    private String vehicleName;


    /**
     * 汽车详情表id
     */
    private Long vehicleInfoId;
    /**
     * 汽车型号 需要拆分
     */
    private String vehicleModel;


    private String systemVersion;

    private String testDate;

    private String type;

    /**
     * 域 tag
     */
    private String domainTag;
    private Long domainTagId;

    private String functionDomain;
    private Long functionDomainId;

    private String domainIndex;
    private Long domainIndexId;

    private Double showData;

    private Double calculateData;


}
