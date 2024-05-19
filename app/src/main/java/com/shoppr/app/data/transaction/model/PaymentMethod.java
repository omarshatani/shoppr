package com.shoppr.app.data.transaction.model;

abstract public class PaymentMethod {
	public PaymentMethod() {
	}

	abstract public PaymentType getPaymentType();

	public enum PaymentType {
		CREDIT_CARD,
		CASH
	}
}
