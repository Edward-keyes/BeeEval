package com.xailab.vehicle.xaivehicledata.service;

import java.util.List;

/**
 * 车辆数据同步服务接口
 * 用于将车辆数据同步到qa_knowledge_base表
 */
public interface VehicleDataSyncService {

    /**
     * 同步所有车辆数据到知识库
     * 
     * @return 同步的车辆数量
     */
    int syncAllVehiclesToKnowledgeBase();

    /**
     * 根据车辆ID列表同步车辆数据到知识库
     * 
     * @param vehicleIds 车辆ID列表
     * @return 同步的车辆数量
     */
    int syncVehiclesToKnowledgeBase(List<Long> vehicleIds);

    /**
     * 清空知识库中的车辆数据
     * 
     * @return 删除的记录数
     */
    int clearVehicleDataFromKnowledgeBase();

    /**
     * 生成车辆数据的INSERT SQL语句（用于手动执行）
     * 
     * @return INSERT SQL语句
     */
    String generateVehicleDataInsertSql();
}
