package com.shoppr.app.ui.checkout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.transaction.TransactionRepository;
import com.shoppr.app.data.transaction.model.PaymentMethod;
import com.shoppr.app.data.transaction.model.Transaction;
import com.shoppr.app.domain.checkout.model.CheckoutFormState;

import java.time.LocalDateTime;
import java.util.Objects;

public class CheckoutViewModel extends ViewModel {
	private final TransactionRepository repository;
	private final MutableLiveData<CheckoutFormState> checkoutFormState = new MutableLiveData<>();

	CheckoutViewModel(TransactionRepository repository) {
		this.repository = repository;
	}

	public LiveData<CheckoutFormState> getCheckoutFormState() {
		return checkoutFormState;
	}

	public boolean validateForm(String creditCardOwnerName, String creditCardNumber, String creditCardExpiration, String creditCardCvv, String creditCardZip, String creditCardBillingAddressString) {
		this.updateForm(new CheckoutFormState(creditCardNumber, creditCardOwnerName, creditCardExpiration, creditCardCvv, creditCardZip, creditCardBillingAddressString));
		return Objects.requireNonNull(this.checkoutFormState.getValue()).isDataValid();
	}

	public void updateForm(CheckoutFormState newState) {
		checkoutFormState.setValue(newState);
	}

	public void buy(String listingId, String sellerId, String userId, String price, String currency, PaymentMethod paymentMethod, Callback<Boolean> callback) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			Transaction transaction = new Transaction("1", sellerId, userId, LocalDateTime.now(), Double.parseDouble(price), currency, paymentMethod, listingId);
			repository.saveTransaction(transaction, callback);
		}
	}
}