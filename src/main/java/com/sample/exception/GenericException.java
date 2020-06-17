package com.sample.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String userMessage;
    private String errorMessage;
    private String errorCode;
    private List<String> errorsList = new ArrayList<>();

    public GenericException(String userMessage){
        this.userMessage = userMessage;
    }

    public GenericException(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public GenericException(String userMessage, String errorMessage, String errorCode){
        this.userMessage = userMessage;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public GenericException(Exception e) {
    	super(e);
    	this.errorMessage = e.getMessage();
    	this.userMessage = e.getLocalizedMessage();
    }

    public GenericException(List<String> errorsList){
        this.errorsList = errorsList;
    }
}
