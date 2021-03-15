package com.sirionlabs.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sirionlabs.integration.services.MappingService;

@RestController
@RequestMapping("/integration")
public class JsonController {

	@Autowired
	private MappingService mappingService;

	@PostMapping("/mapping")
	public ResponseEntity<?> getMappingJsonFile(@RequestParam("jsonFile") MultipartFile jsonFile,
			@RequestParam("textFile") MultipartFile textFile, @RequestParam("type") String type) {

		if (jsonFile != null && textFile != null && (type.equals("2") || type.equals("1"))) {
			return new ResponseEntity<>(this.mappingService.getMappingJsonFile(jsonFile, textFile, type), HttpStatus.OK)
					.getBody();
		} else {
			return new ResponseEntity<>(this.mappingService.getMappingJsonFile(), HttpStatus.OK);
		}
	}

}
