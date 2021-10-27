package com.example.demo.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class S3Client {
	@Value("${aws.accessKey}")
	private String accessKey;
	@Value("${aws.asecretKey}")
	private String secretkey;
	@Value("${aws.region}")
	private String clientRegion;

	@Bean
	public AmazonS3 getS3Client() {
		Regions region = null;
		try {
			region = Regions.fromName(clientRegion);
		} catch (IllegalArgumentException e) {
			log.error("Invalid region provided! Using default region : {}", Regions.DEFAULT_REGION);
			region = Regions.DEFAULT_REGION;
		}
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretkey);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region).build();
	}
}
