package com.shoppr.app.data.transaction.model;

import java.time.LocalDateTime;

public class Transaction {
	private String transactionId;
	private String sellerId;
	private String buyerId;
	private LocalDateTime dateOfPurchase;
	private double amount;
	private String currency;
	private PaymentMethod paymentMethod;
	private String listingId;

	public Transaction() {
	}

	public Transaction(String transactionId, String sellerId, String buyerId, LocalDateTime dateOfPurchase, double amount, String currency, PaymentMethod paymentMethod, String listingId) {
		this.transactionId = transactionId;
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.dateOfPurchase = dateOfPurchase;
		this.amount = amount;
		this.currency = currency;
		this.paymentMethod = paymentMethod;
		this.listingId = listingId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public LocalDateTime getDateOfPurchase() {
		return dateOfPurchase;
	}

	public void setDateOfPurchase(LocalDateTime dateOfPurchase) {
		this.dateOfPurchase = dateOfPurchase;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
