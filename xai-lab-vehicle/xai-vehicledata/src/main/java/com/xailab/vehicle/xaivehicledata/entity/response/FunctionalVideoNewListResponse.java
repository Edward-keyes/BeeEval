package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalVideoNewVo;
import lombok.Data;

import java.util.List;

@Data
public class FunctionalVideoNewListResponse {

    /**
     * 高分表现功能域视频list
     */
    private List<FunctionalVideoNewVo> highScoreFunctionalVideoList;

    /**
     * 高频问题功能域视频list
     */
    private List<FunctionalVideoNewVo> highFrequencyFunctionalVideoList;

}
