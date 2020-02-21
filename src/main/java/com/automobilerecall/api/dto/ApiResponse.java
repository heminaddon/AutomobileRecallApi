package com.automobilerecall.api.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponse {
	boolean success;
	Map<String,Object> errors;
	public ApiResponse(boolean success) {
		super();
		this.success = success;
	}
	public ApiResponse(Map<String, Object> errors) {
		super();
		this.errors = errors;
		this.success=errors.isEmpty();
	}

}


