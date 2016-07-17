package me.hzhou.paypal.util;

import java.util.Locale;

public class MoneyFormat {
	public static String get(double number) {
		return String.format(Locale.US,"%1$,.2f", number);
	}
	
	public static String get(double number, int n) {
		return String.format(Locale.US,"%1$,."+n+"f", number);
	}
}
