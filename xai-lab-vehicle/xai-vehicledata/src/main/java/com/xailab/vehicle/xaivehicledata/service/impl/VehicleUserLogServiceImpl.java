package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.VehicleUserLogDao;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserLogEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("vehicleUserLogService")
public class VehicleUserLogServiceImpl extends ServiceImpl<VehicleUserLogDao, VehicleUserLogEntity>
        implements VehicleUserLogService {

    private static final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(0, 0);

    @Override
    public Result saveLog(VehicleUserLogEntity log) {
        try {
            log.setId(snowflakeIdGenerator.nextId());
            log.setCreateDate(new Date());
            save(log);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("保存操作日志失败：" + e.getMessage());
        }
    }

    @Override
    public Result getLogList() {
        try {
            QueryWrapper<VehicleUserLogEntity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_date");
            List<VehicleUserLogEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取操作日志列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getLogListByUserId(Long userId) {
        try {
            QueryWrapper<VehicleUserLogEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                    .orderByDesc("create_date");
            List<VehicleUserLogEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取用户操作日志列表失败：" + e.getMessage());
        }
    }
}