package com.sirionlabs.integration.entities;

import org.springframework.http.HttpStatus;	

public class MappingJson {
	
	private HttpStatus status;
	private String error;
	private String message;
	private String path;

	
	
	public MappingJson(HttpStatus status, String error, String message, String path) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public MappingJson() {
		super();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return String.format("MappingJson [status=%s, error=%s, message=%s, path=%s]", status, error, message, path);
	}


}
