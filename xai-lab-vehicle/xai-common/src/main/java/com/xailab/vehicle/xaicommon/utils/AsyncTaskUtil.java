package com.xailab.vehicle.xaicommon.utils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 异步任务工具类
 */
public class AsyncTaskUtil {

    // 私有构造器防止实例化
    private AsyncTaskUtil() {}

    // 使用静态内部类实现单例线程池
    private static class ThreadPoolHolder {
        private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
                5,               // 核心线程数
                20,              // 最大线程数
                60L,             // 空闲线程存活时间
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),  // 任务队列
                new CustomThreadFactory(),      // 自定义线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
    }

    /**
     * 获取单例线程池
     */
    private static ExecutorService getExecutor() {
        return ThreadPoolHolder.EXECUTOR;
    }

    /**
     * 执行无返回值的异步任务
     * @param task 可运行任务
     */
    public static void execute(Runnable task) {
        getExecutor().execute(wrapRunnable(task));
    }

    /**
     * 提交带返回值的异步任务
     * @param task 支持的任务
     * @return Future 对象
     */
    public static <T> Future<T> submit(Supplier<T> task) {
        return getExecutor().submit(wrapCallable(task));
    }

    /**
     * 提交带超时控制的异步任务
     * @param task 任务
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 任务结果
     * @throws TimeoutException 任务超时时抛出
     */
    public static <T> T submitWithTimeout(Supplier<T> task, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Future<T> future = submit(task);
        return future.get(timeout, unit);
    }

    /**
     * 关闭线程池（应用结束时调用）
     */
    public static void shutdown() {
        getExecutor().shutdown();
        try {
            if (!getExecutor().awaitTermination(60, TimeUnit.SECONDS)) {
                getExecutor().shutdownNow();
            }
        } catch (InterruptedException e) {
            getExecutor().shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // 包装Runnable任务（添加异常处理）
    private static Runnable wrapRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handleException(e);
            }
        };
    }

    // 包装Callable任务（添加异常处理）
    private static <T> Callable<T> wrapCallable(Supplier<T> task) {
        return () -> {
            try {
                return task.get();
            } catch (Exception e) {
                handleException(e);
                throw e;
            }
        };
    }

    // 异常处理（可替换为项目中的日志框架）
    private static void handleException(Exception e) {
        System.err.println("异步任务执行异常: " + e.getMessage());
        e.printStackTrace();
        // 此处可添加自定义异常处理逻辑
    }

    // 自定义线程工厂（命名线程）
    private static class CustomThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CustomThreadFactory() {
            namePrefix = "async-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(false); // 非守护线程
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}