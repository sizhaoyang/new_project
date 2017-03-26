package com.user.util;

import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox.KeySelectionManager;

import com.thoughtworks.xstream.XStream;

public class XStreamUtil {
	private static XStreamUtil util;
	private XStreamUtil() {
	}
	
	public static XStreamUtil getInstance() {
		if (util == null) {
			util = new XStreamUtil();
		}
		return util;
	}
	
	public String obj2xml(Object obj, Map<String, Class<?>> alias){
		XStream xStream = new XStream();
		Set<String> keys = alias.keySet();
		for (String key : keys) {
			xStream.alias(key, alias.get(key));
		}
		return xStream.toXML(obj);
	}
	
	public Object xml2obj(String xml, Map<String, Class<?>> alias){
		XStream xStream = new XStream();
		Set<String> keys = alias.keySet();
		for (String key : keys) {
			xStream.alias(key, alias.get(key));
		}
		return xStream.fromXML(xml);
	}

}
