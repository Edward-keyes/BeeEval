package com.xailab.vehicle.operation.testplatform.service.impl;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ZipFileService {

    public String unzip(String zipFilePath,String extractDir) throws IOException {
        Path extractLocation=Paths.get(extractDir).toAbsolutePath().normalize();
        Path extractRoot = extractLocation.resolve(
                Paths.get(zipFilePath).getFileName().toString().replace(".zip", "")
        );

        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(
                new BufferedInputStream(new FileInputStream(zipFilePath)))) {

            ZipArchiveEntry entry;
            while ((entry = zis.getNextZipEntry()) != null) {
                // 防御路径遍历攻击
                Path resolvedPath = extractRoot.resolve(entry.getName()).normalize();
                if (!resolvedPath.startsWith(extractRoot)) {
                    throw new SecurityException("Entry is outside of the target directory");
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    try (OutputStream os = Files.newOutputStream(resolvedPath)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
            }

            return extractRoot.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to unzip file", e);
        }
    }
}