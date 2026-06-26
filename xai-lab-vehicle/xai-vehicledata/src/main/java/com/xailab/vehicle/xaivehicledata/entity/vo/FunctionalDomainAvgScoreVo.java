package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionalDomainAvgScoreVo
 * @Description:
 *
 * @date: 2025/3/1 22:53
 */
@Data
public class FunctionalDomainAvgScoreVo implements Serializable {
    private String functionDomainId;
    /**
     * 功能域名称
     */
    private String functionalDomainName;

    /**
     * 行业平均分
     */
    private Double industryAvgScore;

}
