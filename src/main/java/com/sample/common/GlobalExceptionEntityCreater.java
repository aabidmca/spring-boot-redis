package com.sample.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import com.sample.exception.GlobalException;

/*
@author Ramandeep Singh
*/
@Component
public class GlobalExceptionEntityCreater {
    private static final String FIVE_HUNDRED = "500";

    public GlobalException createGlobalException(String technicalErrorMessage, String displayMessage,
                                                 String errorCode) {
        if (ObjectUtils.isEmpty(errorCode)) {
            errorCode = FIVE_HUNDRED;
        }
        ArrayList<HashMap<String, String>> errorList = new ArrayList<>();
        errorList.add(createErrorMap(technicalErrorMessage, displayMessage, errorCode));
        return new GlobalException(getExceptionMap(errorList, errorCode), new HashMap<>());
    }

    public GlobalException createGlobalException(List<String> errorsList) {
        ArrayList<HashMap<String, String>> errorMapList = new ArrayList<>();
        errorsList.stream().forEach(error -> {
            errorMapList.add(createErrorMap(error, error, FIVE_HUNDRED));
        });

        return new GlobalException(getExceptionMap(errorMapList, FIVE_HUNDRED), new HashMap<>());
    }

    public HashMap<String, String> createErrorMap(String technicalErrorMessage, String displayMessage,
                                                   String errorCode){
        HashMap<String, String> errorListMap = new HashMap<>();
        errorListMap.put("displayErrorMessage", displayMessage);
        errorListMap.put("technicalErrorMessage", technicalErrorMessage);
        errorListMap.put("code", errorCode);
        return errorListMap;
    }

    private HashMap<String, Object> getExceptionMap(List<HashMap<String, String>> errorList, String errorCode) {
        HashMap<String, Object> exceptionMap = new HashMap<>();

        exceptionMap.put("httpStatusCode", errorCode);
        exceptionMap.put("success", false);
        exceptionMap.put("errors", errorList);
        return exceptionMap;
    }
}
