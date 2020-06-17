package com.sample.exception;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sample.common.GlobalExceptionEntityCreater;

@ControllerAdvice
public class GenericExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionHandler.class);

    private final MessageSource messageSource;
    private static final String FIVE_HUNDRED = "500";

    @Autowired
    private GlobalExceptionEntityCreater globalExceptionEntityCreater;

    @Autowired
    public GenericExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
    	ex.printStackTrace();
        LOGGER.error("Error message :{}, {}", ex.getMessage(), ex.getStackTrace());
        return new ResponseEntity<>(globalExceptionEntityCreater.createGlobalException(ex.getMessage(), "", null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<?> handleException(GenericException ex, Locale locale) {
		LOGGER.error("Error message :{}, {}", ex.getErrorMessage(), ex.getStackTrace());
		if(!ObjectUtils.isEmpty(ex.getErrorsList())){
            return new ResponseEntity<>(
                globalExceptionEntityCreater.createGlobalException(ex.getErrorsList()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
		String userMessage = ex.getUserMessage();
		if (Objects.isNull(userMessage)) {
			String errorCode = ObjectUtils.isEmpty(ex.getErrorCode()) ? FIVE_HUNDRED : ex.getErrorCode();
			userMessage = messageSource.getMessage(errorCode, new Object[] {}, locale);
		}
		return new ResponseEntity<>(
                globalExceptionEntityCreater.createGlobalException(ex.getErrorMessage(), userMessage, ex.getErrorCode()),
                HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleRequestFormatMethodArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale) {
        BindingResult bindingResult = ex.getBindingResult();
        List<Object[]> errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object[] errorMsgKeyValue = new Object[2];
            errorMsgKeyValue[0] = fieldName;
            errorMsgKeyValue[1] = errorMessage;
            errors.add(errorMsgKeyValue);
        });
        Object[] errorMsgKV = errors.get(0);
        String userMessage = messageSource.getMessage("2001", errorMsgKV, locale);
        LOGGER.error("Error message :{}, {}", ex.getMessage(), ex.getStackTrace());
        return new ResponseEntity<>(globalExceptionEntityCreater.createGlobalException(ex.getMessage(), userMessage, "2001"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, Locale locale) {
        LOGGER.error("Error message :{}, {}", ex.getMessage(), ex.getStackTrace());
        String userMessage = messageSource.getMessage("403", new Object[]{}, locale);
        return new ResponseEntity<>(
                globalExceptionEntityCreater.createGlobalException(ex.getMessage(), userMessage, "403"),
                HttpStatus.FORBIDDEN);
    }

}
