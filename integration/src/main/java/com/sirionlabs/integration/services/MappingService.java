package com.sirionlabs.integration.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.sirionlabs.integration.entities.MappingJson;

public interface MappingService {

	public ResponseEntity<?> getMappingJsonFile(MultipartFile jsonFile, MultipartFile textFile, String type);
	public MappingJson getMappingJsonFile();
} 
