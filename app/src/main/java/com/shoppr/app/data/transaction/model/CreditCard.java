package com.shoppr.app.data.transaction.model;

public class CreditCard extends PaymentMethod {
	private CreditCardCircuit circuit;
	private String ownerName;
	private String cardNumber;
	private String expirationDate;
	private String CVV;
	public CreditCard() {
	}

	public CreditCard(CreditCardCircuit circuit, String ownerName, String cardNumber, String expirationDate, String cvv) {
		this.circuit = circuit;
		this.ownerName = ownerName;
		this.cardNumber = cardNumber;
		this.expirationDate = expirationDate;
		this.CVV = cvv;
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.CREDIT_CARD;
	}

	public CreditCardCircuit getCircuit() {
		return circuit;
	}

	public void setCircuit(CreditCardCircuit circuit) {
		this.circuit = circuit;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCVV() {
		return CVV;
	}

	public void setCVV(String CVV) {
		this.CVV = CVV;
	}

	public enum CreditCardCircuit {
		VISA,
		MASTERCARD,
		AMERICAN_EXPRESS,
		DISCOVER
	}
}
