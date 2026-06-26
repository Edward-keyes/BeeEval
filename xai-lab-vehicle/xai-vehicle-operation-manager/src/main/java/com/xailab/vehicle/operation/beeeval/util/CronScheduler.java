package com.xailab.vehicle.operation.beeeval.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 独立定时任务工具类（线程安全）
 */
@Slf4j
public final class CronScheduler {

    // 单例调度器（懒加载）
    private static class SchedulerHolder {
        static final ScheduledExecutorService INSTANCE = createScheduler();
    }

    // 任务注册表（用于批量管理）
    private static final Map<String, ScheduledFuture<?>> TASK_REGISTRY = new ConcurrentHashMap<>();

    // 私有构造器
    private CronScheduler() {}

    /**
     * 创建调度线程池
     */
    private static ScheduledExecutorService createScheduler() {
        return new ScheduledThreadPoolExecutor(
                3, // 核心线程数（根据CPU核心数调整）
                new NamedThreadFactory("cron-worker"),
                new ThreadPoolExecutor.DiscardPolicy() // 拒绝策略
        );
    }

    /**
     * 执行单次延迟任务
     * @param taskName 任务标识（用于取消）
     * @param task 待执行任务
     * @param delay 延迟时间
     * @param unit 时间单位
     */
    public static void scheduleOnce(String taskName, Runnable task,
                                    long delay, TimeUnit unit) {
        ScheduledFuture<?> future = SchedulerHolder.INSTANCE.schedule(
                wrapTask(taskName, task),
                delay,
                unit
        );
        TASK_REGISTRY.put(taskName, future);
    }

    /**
     * 固定频率执行（无视任务耗时）
     * @param taskName 任务标识
     * @param task 待执行任务
     * @param initialDelay 初始延迟
     * @param period 间隔周期
     * @param unit 时间单位
     */
    public static void scheduleAtFixedRate(String taskName, Runnable task,
                                           long initialDelay, long period,
                                           TimeUnit unit) {
        ScheduledFuture<?> future = SchedulerHolder.INSTANCE.scheduleAtFixedRate(
                wrapTask(taskName, task),
                initialDelay,
                period,
                unit
        );
        TASK_REGISTRY.put(taskName, future);
    }

    /**
     * 固定延迟执行（任务结束后计时）
     * @param taskName 任务标识
     * @param task 待执行任务
     * @param initialDelay 初始延迟
     * @param delay 间隔延迟
     * @param unit 时间单位
     */
    public static void scheduleWithFixedDelay(String taskName, Runnable task,
                                              long initialDelay, long delay,
                                              TimeUnit unit) {
        ScheduledFuture<?> future = SchedulerHolder.INSTANCE.scheduleWithFixedDelay(
                wrapTask(taskName, task),
                initialDelay,
                delay,
                unit
        );
        TASK_REGISTRY.put(taskName, future);
    }

    /**
     * 取消指定任务
     * @param taskName 任务标识
     * @param mayInterrupt 是否中断正在执行的任务
     */
    public static void cancelTask(String taskName, boolean mayInterrupt) {
        ScheduledFuture<?> future = TASK_REGISTRY.get(taskName);
        if (future != null) {
            future.cancel(mayInterrupt);
            TASK_REGISTRY.remove(taskName);
        }
    }

    /**
     * 关闭调度器（释放资源）
     */
    public static void shutdown() {
        SchedulerHolder.INSTANCE.shutdown();
        try {
            if (!SchedulerHolder.INSTANCE.awaitTermination(10, TimeUnit.SECONDS)) {
                SchedulerHolder.INSTANCE.shutdownNow();
            }
        } catch (InterruptedException e) {
            SchedulerHolder.INSTANCE.shutdownNow();
            Thread.currentThread().interrupt();
        }
        TASK_REGISTRY.clear();
    }

    // 包装任务（添加异常处理+日志）
    private static Runnable wrapTask(String taskName, Runnable task) {
        return () -> {
            long start = System.currentTimeMillis();
            try {
                task.run();
                logTaskSuccess(taskName, start);
            } catch (Throwable e) {
                logTaskFailure(taskName, e);
            }
        };
    }

    // 任务成功日志（可替换为SLF4J等）
    private static void logTaskSuccess(String taskName, long startTime) {
        log.info("{} 执行成功, 耗时: {}",
                taskName, System.currentTimeMillis() - startTime);
    }

    // 任务失败日志
    private static void logTaskFailure(String taskName, Throwable e) {
        log.info("{} 执行失败: {}", taskName, e.getMessage());
        e.printStackTrace();
    }

    /**
     * 带命名的线程工厂
     */
    private static class NamedThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger counter = new AtomicInteger(1);

        NamedThreadFactory(String poolName) {
            this.namePrefix = poolName + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + counter.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}