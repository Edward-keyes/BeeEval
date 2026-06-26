package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("vehicle_user")
public class VehicleUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 郵箱
     */
    private String email;

    /**
     * 密碼
     */
    private String password;

    /**
     * 鹽值
     */
    private String salt;

    /**
     * 所屬團隊
     */
    private String team;

    /**
     * 管理員郵箱
     */
    private String adminEmail;

    /**
     * 創建時間
     */
    private Date createDate;

    /**
     * 0-管理員賬號 1-普通用戶賬號
     */
    private Integer accountType;

    /**
     * 0-注冊中 1-测试账号 2-未通過 3-正式账号 -1邏輯刪除
     */
    private Integer status;
}
