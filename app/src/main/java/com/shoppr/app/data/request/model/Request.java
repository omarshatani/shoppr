package com.shoppr.app.data.request.model;

import java.util.Date;

public class Request {
	private RequestState state;
	private String buyerId;
	private String sellerId;
	private double offer;
	private Date creationDate;

	public Request() {
	}

	public Request(RequestState state, String buyerId, String sellerId, double offer, Date creationDate) {
		this.state = state;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.offer = offer;
		this.creationDate = creationDate;
	}

	public RequestState getState() {
		return state;
	}

	public void setState(RequestState state) {
		this.state = state;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public double getOffer() {
		return offer;
	}

	public void setOffer(double offer) {
		this.offer = offer;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
