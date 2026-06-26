package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalVideoVo;
import lombok.Data;

import java.util.List;

@Data
public class FunctionalVideoListResponse {

    /**
     * 高分表现功能域视频list
     */
    private List<FunctionalVideoVo> highScoreFunctionalVideoList;

    /**
     * 高频问题功能域视频list
     */
    private List<FunctionalVideoVo> highFrequencyFunctionalVideoList;

}
