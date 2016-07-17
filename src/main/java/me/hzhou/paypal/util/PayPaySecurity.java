package me.hzhou.paypal.util;

import com.paypal.core.ConfigManager;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class PayPaySecurity {
	
	public static String getAccessToken() {

		// ###AccessToken
		// Retrieve the access token from
		// OAuthTokenCredential by passing in
		// ClientID and ClientSecret
		String clientID = ConfigManager.getInstance().getValue("clientID");
		String clientSecret = ConfigManager.getInstance().getValue("clientSecret");

		try {
			return new OAuthTokenCredential(clientID, clientSecret).getAccessToken();
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static APIContext getAPIContext() {
		String accessToken = getAccessToken();
		return accessToken != null ? new APIContext(accessToken):null;
	}
}
