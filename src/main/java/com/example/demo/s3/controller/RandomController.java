package com.example.demo.s3.controller;

import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.s3.api.model.ResponseMsg;
import com.example.demo.s3.service.RandomService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1")
@Slf4j
public class RandomController {
	
	@Autowired
	private RandomService service;
	
	/**
	 * Generates presigned url of objects from private S3 bucket
	 * @param object
	 * @return
	 */
	@GetMapping(value = "/getPresignedUrl/{object}")
	public ResponseEntity<ResponseMsg> getPresignedUrl(@PathVariable String object){
		
		URL generatedUrl;
		try {
			generatedUrl = service.generatePreSignedUrl(object);
			ResponseMsg responseMsg = ResponseMsg.builder().url(generatedUrl).build();
			return new ResponseEntity<ResponseMsg>(responseMsg, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Failed generating presigned url {}", e.getLocalizedMessage());
			return new ResponseEntity<ResponseMsg>(ResponseMsg.builder().error(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
