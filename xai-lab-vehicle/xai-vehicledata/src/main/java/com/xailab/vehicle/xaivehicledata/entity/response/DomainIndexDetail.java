package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.vo.IndexDetailVo;
import lombok.Data;

import java.util.List;

@Data
public class DomainIndexDetail {

    List<IndexDetailVo> cognitiveAbility;

    List<IndexDetailVo> actionAbility;

}
