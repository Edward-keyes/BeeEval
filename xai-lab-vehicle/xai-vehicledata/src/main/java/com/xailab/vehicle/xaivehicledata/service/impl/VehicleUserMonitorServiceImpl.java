package com.xailab.vehicle.xaivehicledata.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.VehicleUserDao;
import com.xailab.vehicle.xaivehicledata.dao.VehicleUserMonitorDao;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserMonitorEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserMonitorService;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("vehicleUserMonitorService")
public class VehicleUserMonitorServiceImpl extends ServiceImpl<VehicleUserMonitorDao, VehicleUserMonitorEntity>
        implements VehicleUserMonitorService {

    @Autowired
    private VehicleUserService vehicleUserService;

    private static final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(0, 0);

    @Override
    public Result saveMonitor(VehicleUserMonitorEntity monitor) {
        try {
            monitor.setId(snowflakeIdGenerator.nextId());
            // 如果没有设置持续时间，且有开始和结束时间，则计算持续时间
            if (monitor.getDuration() == null && monitor.getStartTime() != null && monitor.getEndTime() != null) {
                monitor.setDuration((int) (monitor.getEndTime() - monitor.getStartTime()));
            }
            Object loginId = StpUtil.getTokenInfo().getLoginId();
            monitor.setUserId(loginId+"");
            save(monitor);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("保存监控记录失败：" + e.getMessage());
        }
    }

    @Override
    public Result getMonitorList() {
        try {
            QueryWrapper<VehicleUserMonitorEntity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("start_time");
            List<VehicleUserMonitorEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取监控记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getMonitorListByUserId(Long userId) {
        try {
            QueryWrapper<VehicleUserMonitorEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                    .orderByDesc("start_time");
            List<VehicleUserMonitorEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取用户监控记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getMonitorListByEventType(String eventType) {
        try {
            QueryWrapper<VehicleUserMonitorEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("event_type", eventType)
                    .orderByDesc("start_time");
            List<VehicleUserMonitorEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取事件类型监控记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getMonitorListByTargetId(String targetId) {
        try {
            QueryWrapper<VehicleUserMonitorEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("target_id", targetId)
                    .orderByDesc("start_time");
            List<VehicleUserMonitorEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取目标监控记录列表失败：" + e.getMessage());
        }
    }
}