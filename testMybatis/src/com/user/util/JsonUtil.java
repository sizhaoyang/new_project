package com.user.util;

import java.io.IOException;
import java.io.StringWriter;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {

	private static JsonUtil jsonUtil;
	private static JsonFactory jsonFactory;
	private static ObjectMapper mapper;

	private JsonUtil() {
		
	}
	
	public static JsonUtil getInstance(){
		if (jsonUtil == null) {
			jsonUtil = new JsonUtil();
		}
		return jsonUtil;
	}
	
	public static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	public static JsonFactory getFactory() {
		if (jsonFactory == null) {
			jsonFactory = new JsonFactory();
		}
		return jsonFactory;
	}
	

	public String obj2json(Object object) {
		JsonGenerator jsonGenerator = null;
		StringWriter out = new StringWriter();
		try {
			JsonFactory jsonFactory = new JsonFactory();
			jsonGenerator = jsonFactory.createGenerator(out);
			jsonGenerator.useDefaultPrettyPrinter();
			ObjectMapper mapper = new ObjectMapper();
			
			mapper.writeValue(jsonGenerator, object);
			
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (jsonGenerator != null) {
					jsonGenerator.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Object json2obj(String json, Class<?> clz) {
		try {
			mapper = getMapper();
			return mapper.readValue(json, clz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
