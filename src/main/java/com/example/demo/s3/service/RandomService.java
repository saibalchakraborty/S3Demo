package com.example.demo.s3.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RandomService {
	@Autowired
	private AmazonS3 s3Client;

	@Value("${aws.s3.bucketName}")
	private String bucketName;
	private final long expTimeMillis = 1000 * 60 * 60;
	
	
	public URL generatePreSignedUrl(String objectKey) throws Exception{
		java.util.Date expiration = new java.util.Date();
		final long validity = expTimeMillis + expiration.getTime();
		expiration.setTime(validity);
		
		log.info("Generating pre-signed url");
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET).withExpiration(expiration);
		
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url;
	}


	public String uploadFile(MultipartFile file) throws Exception {
		String objectKey = file.getOriginalFilename();
		File convert = convert(file);
		TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
		transferManager.upload(bucketName, objectKey, convert);
		return objectKey;
	}
	
	private File convert(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile();
	      try(FileOutputStream fos = new FileOutputStream(convFile)) {
	    	  fos.write(file.getBytes());
	      }
	    return convFile;
	  }
}
