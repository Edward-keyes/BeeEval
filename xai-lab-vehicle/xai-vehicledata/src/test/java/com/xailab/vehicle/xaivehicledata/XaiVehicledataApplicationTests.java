package com.xailab.vehicle.xaivehicledata;

import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.util.Random;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@RequiredArgsConstructor
class XaiVehicledataApplicationTests {

	private final ALiYunOssConfig ossConfig;

	@Test
	void contextLoads(MultipartFile file) throws com.aliyuncs.exceptions.ClientException{

		// 获取阿里云OSS参数
		String endpoint = ossConfig.getEndpoint();

		String bucketName = ossConfig.getBucketName();

		String accessKeyId = ossConfig.getAccessKeyId();

		String accessKeySecret = ossConfig.getAccessKeySecret();

		// 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
		EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

	}

	/** 生成一个唯一的 Bucket 名称 */
	public static String generateUniqueBucketName(String prefix) {
		// 获取当前时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());
		// 生成一个 0 到 9999 之间的随机数
		Random random = new Random();
		int randomNum = random.nextInt(10000); // 生成一个 0 到 9999 之间的随机数
		// 连接以形成一个唯一的 Bucket 名称
		return prefix + "-" + timestamp + "-" + randomNum;
	}

}
