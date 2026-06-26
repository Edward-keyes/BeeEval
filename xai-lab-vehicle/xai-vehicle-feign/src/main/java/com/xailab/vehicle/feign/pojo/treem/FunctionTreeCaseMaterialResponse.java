package com.xailab.vehicle.feign.pojo.treem;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FunctionTreeCaseMaterialResponse implements Serializable {


    /**
     * id
     */
    private String id;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * FunctionTreeCaseId
     */
    private Long functionTreeCaseId;

    /**
     * 素材url
     */
    private String materialUrl;

    /**
     * 文件素材类型
     *  0:image/1:video
     */
    private String materialType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
