package me.hzhou.paypal.model;

import java.util.ArrayList;
import java.util.List;

import me.hzhou.paypal.config.SystemVariables;
import me.hzhou.paypal.util.MoneyFormat;
import me.hzhou.paypal.util.PayPaySecurity;

import com.paypal.api.payments.*;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;

public class PayPal {
	
	// apiContext is the token to communicate with PayPal API
	private static APIContext apiContext = PayPaySecurity.getAPIContext();
	
	private Address billingAddress;
	private CreditCard creditCard;
	private Details details;
	private Amount amount;
	private FundingInstrument fundingInstrument;
	private List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
	private List<Transaction> transactions = new ArrayList<Transaction>();
	private Payer payer = new Payer();;
	private Payment payment = new Payment();
	
	// Wrap for bill Address
	public PayPal setBillAddress(String line1, String line2, String city, String countryCode, String state, String zipCode) {
		Address _billingAddress = new Address();
		_billingAddress.setCity(city);
		_billingAddress.setCountryCode(countryCode);
		_billingAddress.setLine1(line1);
		_billingAddress.setLine2(line2);
		_billingAddress.setPostalCode(zipCode);
		_billingAddress.setState(state);
		this.billingAddress = _billingAddress;
		
		return this;
	}
	
	public PayPal setBillAddress(String line1, String city, String countryCode, String state, String zipCode) {
		return setBillAddress(line1, "", city, countryCode, state, zipCode);
	}
	
	// Wrap for Credit Card
	public PayPal setCreditCard(String number, String type, 
			int expireMonth, int expireYear, String cvv2, String firstName, String LastName) {
		CreditCard _creditCard = new CreditCard();
		_creditCard.setNumber(number).setType(type).setExpireMonth(expireMonth).setExpireYear(expireYear);
		_creditCard.setBillingAddress(this.billingAddress);
		_creditCard.setCvv2(cvv2);
		_creditCard.setFirstName(firstName).setLastName(LastName);
		
		this.creditCard = _creditCard;
		return this;
	}
	
	public PayPal setDetails(double subTotal, double tax) {
		Details _details = new Details();
		
		_details.setShipping("0");
		_details.setSubtotal(MoneyFormat.get(subTotal));
		_details.setTax(MoneyFormat.get(tax));
		
		this.details = _details;
		return this;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public PayPal setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
		return this;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public PayPal setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
		return this;
	}

	public Details getDetails() {
		return details;
	}

	public FundingInstrument getFundingInstrument() {
		return fundingInstrument;
	}

	public PayPal setFundingInstrument(FundingInstrument fundingInstrument) {
		this.fundingInstrument = fundingInstrument;
		return this;
	}

	public Payer getPayer() {
		return payer;
	}

	public PayPal setPayer(Payer payer) {
		this.payer = payer;
		return this;
	}

	public Payment getPayment() {
		return payment;
	}

	public PayPal setPayment(Payment payment) {
		this.payment = payment;
		return this;
	}

	public Amount getAmount() {
		return amount;
	}

	public PayPal setAmount(Amount amount) {
		this.amount = amount;
		return this;
	}
	
	public PayPal setAmount(double subTotal) {
		Amount _amount = new Amount();
		_amount.setCurrency("USD");
		// Total must be equal to sum of shipping, tax and subtotal.
		double tax =(double)SystemVariables.get("taxRate") * subTotal;
		double total = tax + subTotal;
		
		_amount.setTotal(MoneyFormat.get(total));
		// set details
		this.setDetails(subTotal, tax);
		
		_amount.setDetails(this.details);
		
		this.amount = _amount;
		return this;
	}
	
	/**
	 * This is the final pay method
	 * @return Payment details
	 */
	public Payment pay() {
		this.paymentPrepare();
		// now comes the real payment
		try {
			return this.payment.create(apiContext);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void paymentPrepare() {
		
		Transaction transaction = new Transaction();
		transaction.setAmount(this.amount);
		transaction.setDescription("This is the payment transaction description.");

		// The Payment creation API requires a list of Transaction; add the created `Transaction` to a List
		this.transactions.add(transaction);

		// ###FundingInstrument
		// A resource representing a Payeer's funding instrument.
		// Use a Payer ID (A unique identifier of the payer generated and provided by the facilitator. This is required when
		// creating or using a tokenized funding instrument) and the `CreditCardDetails`
		FundingInstrument fundingInstrument = new FundingInstrument();
		fundingInstrument.setCreditCard(this.creditCard);

		// The Payment creation API requires a list of FundingInstrument; add the created `FundingInstrument` to a List
		this.fundingInstrumentList.add(fundingInstrument);

		// ###Payer
		// A resource representing a Payer that funds a payment
		// Use the List of `FundingInstrument` and the Payment Method as 'credit_card'
		this.payer.setFundingInstruments(this.fundingInstrumentList);
		this.payer.setPaymentMethod("credit_card");

		// ###Payment
		// A Payment Resource; create one using the above types and intent as 'sale'
		this.payment.setIntent("sale");
		this.payment.setPayer(this.payer);
		this.payment.setTransactions(this.transactions);
	}
}
