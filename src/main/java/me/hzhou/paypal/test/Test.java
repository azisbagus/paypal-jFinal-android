package me.hzhou.paypal.test;

import java.io.IOException;
import java.io.InputStream;

import me.hzhou.paypal.util.Util;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class Test {
	
	public static void getConfig() {
		String str = "";
		InputStream in = new Test().getClass().getResourceAsStream("/config.json");
		try {
			str = IOUtils.toString(in);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println( "null");
		}
		
		JSONObject obj = new JSONObject(str);
		
		for(Object i : obj.keySet()) {
			System.out.println(i.toString());
		}
	}
	
	public static void main(String[] args) {
		// check Card Type
		System.out.println(Util.getCreditCardTypeByNumber("4888937088492170"));
		System.out.println(Util.getCreditCardTypeByNumber("6011002303226740"));
		System.out.println(Util.getCreditCardTypeByNumber("5474151327962392"));
	}
}
