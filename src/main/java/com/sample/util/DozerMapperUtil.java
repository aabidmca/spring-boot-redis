package com.sample.util;


import static org.dozer.loader.api.TypeMappingOptions.dateFormat;
import static org.dozer.loader.api.TypeMappingOptions.mapEmptyString;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;
import static org.dozer.loader.api.TypeMappingOptions.stopOnErrors;
import static org.dozer.loader.api.TypeMappingOptions.trimStrings;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

public class DozerMapperUtil {
	
	@SuppressWarnings("rawtypes")
	public static DozerBeanMapper getBeanMapper(Class src, Class tgt) {
		return  getBeanMapper(src, tgt, true) ;
	}
	
	@SuppressWarnings("rawtypes")
	public static DozerBeanMapper getBeanMapper(Class src, Class tgt, boolean stopOnErrors) {
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		mapper.addMapping(new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(src, tgt, mapNull(false), mapEmptyString(false), stopOnErrors(stopOnErrors));
			}
		});
		return mapper;
	}
	
	@SuppressWarnings("rawtypes")
	public static DozerBeanMapper getBeanMapper(Class src, Class tgt, String dateFormat, String[] fieldNames) {
		DozerBeanMapper mapper = new DozerBeanMapper();
		mapper.addMapping(new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(src, tgt, mapNull(false), mapEmptyString(false), trimStrings(true), dateFormat(dateFormat))
				.fields(fieldNames[0], fieldNames[1]).fields(fieldNames[2], fieldNames[3]);
			}
		});
		return mapper;
	}
}
