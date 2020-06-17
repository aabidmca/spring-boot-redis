package com.sample.common;

import java.time.format.DateTimeFormatter;

/*
@author Ramandeep Singh
*/
public class Constants {
	public static final String KUWAIT_ZONE = "Asia/Kuwait";
	public static final String FILTER_DATE_FORMAT = "YYYY-MM-dd";
	public static final DateTimeFormatter SQL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FILTER_DATE_FORMAT);
}
