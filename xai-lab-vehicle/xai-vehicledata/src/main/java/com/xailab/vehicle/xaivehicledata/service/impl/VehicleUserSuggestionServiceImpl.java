package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.VehicleUserSuggestionDao;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserSuggestionEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserSuggestionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("vehicleUserSuggestionService")
public class VehicleUserSuggestionServiceImpl extends ServiceImpl<VehicleUserSuggestionDao, VehicleUserSuggestionEntity>
        implements VehicleUserSuggestionService {

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);

    @Override
    public Result saveSuggestion(VehicleUserSuggestionEntity suggestion) {
        try {
            suggestion.setId(snowflakeIdGenerator.nextId());
            suggestion.setCreateDate(new Date());
            save(suggestion);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("保存反馈失败：" + e.getMessage());
        }
    }

    @Override
    public Result getSuggestionList() {
        try {
            QueryWrapper<VehicleUserSuggestionEntity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            List<VehicleUserSuggestionEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取反馈列表失败：" + e.getMessage());
        }
    }
}