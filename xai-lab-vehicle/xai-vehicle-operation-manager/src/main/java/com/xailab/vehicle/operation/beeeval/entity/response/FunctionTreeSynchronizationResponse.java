package com.xailab.vehicle.operation.beeeval.entity.response;

import com.xailab.vehicle.feign.vo.FunctionTreeOneAndTwoTagResponse;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.operation.beeeval.entity.vo.FunctionTreeSynchronizationVoData;
import com.xailab.vehicle.operation.beeeval.entity.vo.PcafeRelevancyFunctionThreeTagVos;
import com.xailab.vehicle.operation.beeeval.entity.vo.PcafeUnrelevancyVo;
import com.xailab.vehicle.operation.testplatform.vo.PcafeRelevancyFunctionThreeTagVo;
import lombok.Data;

import java.util.List;

@Data
public class FunctionTreeSynchronizationResponse {

    /**
     * 关联数据
     */
    List<PcafeRelevancyFunctionThreeTagVos> relevancyData;

    /**
     * beeeval一二级标签数据
     */
    List<FunctionTreeOneAndTwoTagResponse> oneAndTwoTagData;

    /**
     * pcafe未关联标签数据
     */
    List<PcafeUnrelevancyVo> pcafeUnrelevancyData;

}
