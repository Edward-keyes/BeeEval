package com.xailab.vehicle.operation.testplatform.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.entity.TestCaseMaterialEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.TestCaseMaterialShowRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestStateInfoResponse;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseMaterialVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface TestCaseMaterialService extends BaseService<TestCaseMaterialEntity> {

    /**
     * 上传文件
     * @param file
     * @param recordId
     * @param testCaseId
     * @return
     */
    String uploadFile(MultipartFile file,Integer recordId,Integer testCaseId);

    /**
     * 查询素材详情
     * @param recordId
     * @param testCaseId
     * @return
     */
    TestStateInfoResponse findMaterialList(Integer recordId, Integer testCaseId);

    /**
     * 修改状态
     * @param recordId
     * @param testCaseId
     */
    void editState(Integer recordId, Integer testCaseId,String state);

    /**
     * 获取图片 url
     * @param photoName
     * @return
     */
    String queryPhoto(String photoName);

    /**
     * 获取视频url
     * @param videoName
     * @return
     */
    String queryVideo(String videoName);



    /**
     * 设置素材显示
     */
    void setMaterialShow(TestCaseMaterialShowRequest request);

    /**
     * 素材删除
     */
    void deleteMaterial(List<Long> ids);

    @DS("test_platform")
    void saveOrUpdateN(TestCaseMaterialEntity entity);
}
