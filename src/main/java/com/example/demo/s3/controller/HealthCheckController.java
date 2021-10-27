package com.example.demo.s3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	/**
	 * Startup endpoint to satisfy AWS healthcheck
	 * @return
	 */
	@GetMapping(value = "/")
	public ResponseEntity<String> getStartUpMsg(){
		return new ResponseEntity<String>("System is up !!", HttpStatus.OK);
	}
}
