package com.shoppr.app.domain.checkout.model;

public class CheckoutFormState {
	private String creditCardNumber;
	private String creditCardOwnerName;
	private String creditCardExpirationDate;
	private String creditCardSecurityCode;
	private String postalCode;
	private String billingAddress;


	public CheckoutFormState() {
	}

	public CheckoutFormState(String creditCardNumber, String creditCardOwnerName, String creditCardExpirationDate, String creditCardSecurityCode, String postalCode, String billingAddress) {
		this.creditCardNumber = creditCardNumber;
		this.creditCardOwnerName = creditCardOwnerName;
		this.creditCardExpirationDate = creditCardExpirationDate;
		this.creditCardSecurityCode = creditCardSecurityCode;
		this.postalCode = postalCode;
		this.billingAddress = billingAddress;
	}


	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public boolean isCreditCardNumberValid() {
		return this.creditCardNumber != null && this.creditCardNumber.length() == 16;
	}

	public void setCreditCardOwnerName(String creditCardOwnerName) {
		this.creditCardOwnerName = creditCardOwnerName;
	}

	public boolean isCreditCardOwnerNameValid() {
		return this.creditCardOwnerName != null && !this.creditCardOwnerName.isEmpty();
	}

	public void setCreditCardExpirationDate(String creditCardExpirationDate) {
		this.creditCardExpirationDate = creditCardExpirationDate;
	}

	public boolean isCreditCardExpirationDateValid() {
		return this.creditCardExpirationDate != null && !this.creditCardExpirationDate.isEmpty() && this.creditCardExpirationDate.matches("\\d{2}/\\d{2}");
	}

	public void setCreditCardSecurityCode(String creditCardSecurityCode) {
		this.creditCardSecurityCode = creditCardSecurityCode;
	}

	public boolean isCreditCardSecurityCodeValid() {
		return this.creditCardSecurityCode != null && this.creditCardSecurityCode.length() == 3;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public boolean isPostalCodeValid() {
		return this.postalCode != null && this.postalCode.length() == 5;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public boolean isBillingAddressValid() {
		return this.billingAddress != null && !this.billingAddress.isEmpty();
	}

	public boolean isDataValid() {
		return isCreditCardNumberValid()
				&& isCreditCardOwnerNameValid()
				&& isCreditCardExpirationDateValid()
				&& isCreditCardSecurityCodeValid()
				&& isPostalCodeValid()
				&& isBillingAddressValid();
	}

}
