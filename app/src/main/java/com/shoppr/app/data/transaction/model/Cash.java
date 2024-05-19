package com.shoppr.app.data.transaction.model;

public class Cash extends PaymentMethod {

	public Cash() {
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.CASH;
	}
}
