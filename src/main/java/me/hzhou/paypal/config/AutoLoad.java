package me.hzhou.paypal.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

public class AutoLoad {
	public static void loadConfig() {
		
		// config.json is located in /src/main/resource folder
		InputStream in =  AutoLoad.class.getResourceAsStream("/config.json");
		String configStr = null;;
		try {
			configStr = IOUtils.toString(in);
			JSONObject confObj = new JSONObject(configStr);
			for(Object key : confObj.keySet()) {
				String keyStr = key.toString();
				SystemVariables.set(keyStr, confObj.get(keyStr));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// sdk_config.properties is located in /src/main/resource folder
	public static void loadPayPalConfig() {
		InputStream is = AutoLoad.class.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			
		}
	}
}
