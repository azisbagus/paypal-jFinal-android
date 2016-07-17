package me.hzhou.paypal.test;

import me.hzhou.paypal.config.MyConfig;
import me.hzhou.paypal.model.PayPal;
import me.hzhou.paypal.util.MoneyFormat;
import me.hzhou.paypal.util.PayPaySecurity;

import org.junit.Ignore;
import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;
import com.paypal.api.payments.Payment;


public class UnitTest extends ControllerTestCase<MyConfig> {

	@Ignore
	public void test() {
		System.out.println(MoneyFormat.get(1.232, 1));
	}
	
	@Ignore
	public void testAccessToekn() {
		System.out.println(PayPaySecurity.getAccessToken());
	}
	
	@Test
	public void paymentTest() {
		PayPal paypal = new PayPal();
		paypal.setBillAddress("52 N Main ST", "Johnstown", "US", "OH", "43210");
		
		// Never mind, this is a fake information from PayPal Developer Center
		// sorry
		paypal.setCreditCard("4417119669820331", "visa", 11, 2018, "874", "Joe", "Shopper");
		
		// 1.50 doesn't include tax
		paypal.setAmount(1.50);
		
		// output details as
		/*{
		  "shipping": "0",
		  "subtotal": "1.50",
		  "tax": "0.09"
		}
		*/
		System.out.println(paypal.getDetails().toJSON());
		
		Payment payment = paypal.pay();
		System.out.println(payment.toJSON());
		
	}
	
}
