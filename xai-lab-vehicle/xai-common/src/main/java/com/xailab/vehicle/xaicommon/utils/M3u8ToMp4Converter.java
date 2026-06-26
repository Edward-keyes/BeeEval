package com.xailab.vehicle.xaicommon.utils;
import ws.schild.jave.*;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.AudioAttributes;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
public class M3u8ToMp4Converter {

    public static void convertM3u8ToMp4(String m3u8Url, String outputPath)
            throws IOException, InterruptedException {

        // 构建FFmpeg命令
        String[] command = {
                "ffmpeg",
                "-i", m3u8Url,
                "-c:v", "copy",      // 复制视频流
                "-c:a", "copy",      // 复制音频流
                "-bsf:a", "aac_adtstoasc",  // 修复AAC音频
                "-y",                // 覆盖输出文件
                outputPath
        };

        // 打印执行的命令（用于调试）
        System.out.println("正在执行命令: " + String.join(" ", command));

        // 创建进程并执行
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // 合并标准错误和标准输出

        Process process = processBuilder.start();

        // 捕获并打印FFmpeg输出
        Thread outputThread = new Thread(() -> {
            try (InputStream inputStream = process.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputThread.start();

        // 等待进程结束
        int exitCode = process.waitFor();
        outputThread.join(); // 等待输出线程完成

        if (exitCode != 0) {
            throw new IOException("FFmpeg执行失败，退出码: " + exitCode);
        }
    }

}