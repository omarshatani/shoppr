package com.shoppr.app.ui.checkout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.shoppr.app.R;
import com.shoppr.app.data.transaction.model.Cash;
import com.shoppr.app.data.transaction.model.CreditCard;
import com.shoppr.app.data.transaction.model.PaymentMethod;
import com.shoppr.app.databinding.FragmentCheckoutBinding;
import com.shoppr.app.domain.checkout.model.CheckoutFormState;
import com.shoppr.app.ui.login.LoginViewModel;
import com.shoppr.app.ui.login.LoginViewModelFactory;

public class CheckoutFragment extends Fragment {

    private CheckoutViewModel viewModel;
    private FragmentCheckoutBinding binding;
    private final CheckoutFormState formState = new CheckoutFormState();

    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        binding.checkoutToolbar.setNavigationIcon(R.drawable.ic_back);
        binding.checkoutToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.checkoutToolbar.setTitle("Checkout");

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new CheckoutViewModelFactory()).get(CheckoutViewModel.class);
        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory()).get(LoginViewModel.class);

        String itemName = CheckoutFragmentArgs.fromBundle(requireArguments()).getItemName();
        String price = CheckoutFragmentArgs.fromBundle(requireArguments()).getPrice();
        String currency = CheckoutFragmentArgs.fromBundle(requireArguments()).getCurrency();
        String sellerId = CheckoutFragmentArgs.fromBundle(requireArguments()).getSellerId();
        String listingId = CheckoutFragmentArgs.fromBundle(requireArguments()).getListingId();

        binding.itemName.setText(itemName);
        binding.itemPrice.setText(price);
        binding.itemCurrency.setText(currency);

        binding.selectPaymentMethodRadio.setOnCheckedChangeListener((group, checkedId) -> {
            binding.creditCard.setError(null);
            binding.cash.setError(null);
            binding.errorTextView.setError(null);
            binding.errorTextView.setVisibility(View.GONE);
        });

        binding.creditCard.setOnCheckedChangeListener((group, checkedId) -> {
            binding.creditCardNumber.addTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardOwnerName.addTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardExp.addTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardCvv.addTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardZip.addTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardAddress.addTextChangedListener(fieldChangeTextWatcher());
        });

        binding.cash.setOnCheckedChangeListener((group, checkedId) -> {
            binding.creditCardNumber.removeTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardOwnerName.removeTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardExp.removeTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardCvv.removeTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardZip.removeTextChangedListener(fieldChangeTextWatcher());
            binding.creditCardAddress.removeTextChangedListener(fieldChangeTextWatcher());

            this.clearErrors();
        });

        binding.buyCta.setOnClickListener(v -> {
            PaymentMethod paymentMethod;

            if (!binding.creditCard.isChecked() && !binding.cash.isChecked()) {
                binding.creditCard.setError(getString(R.string.field_required));
                binding.cash.setError(getString(R.string.field_required));
                binding.errorTextView.setVisibility(View.VISIBLE);
                return;
            }

            if (binding.creditCard.isChecked()) {
                boolean isFormValid = viewModel.validateForm(
                    binding.creditCardOwnerName.getText().toString(),
                    binding.creditCardNumber.getText().toString(),
                    binding.creditCardExp.getText().toString(),
                    binding.creditCardCvv.getText().toString(),
                    binding.creditCardZip.getText().toString(),
                    binding.creditCardAddress.getText().toString()
                );
                if (!isFormValid) {
                    return;
                }

                this.clearErrors();

                paymentMethod = new CreditCard(CreditCard.CreditCardCircuit.VISA, binding.creditCardOwnerName.getText().toString(), binding.creditCardNumber.getText().toString(), binding.creditCardExp.getText().toString(), binding.creditCardCvv.getText().toString());
            } else {
                paymentMethod = new Cash();
            }

            viewModel.buy(listingId, sellerId, loginViewModel.getCurrentUser(requireActivity()).getUuid(), price, currency, paymentMethod);
        });

        viewModel.getCheckoutFormState().observe(getViewLifecycleOwner(), checkoutFormState -> {
            if (checkoutFormState == null) {
                return;
            }

            if (checkoutFormState.isDataValid()) {
                return;
            }

            if (!checkoutFormState.isCreditCardNumberValid()) {
                binding.creditCardNumber.setError(getString(R.string.field_required));
            }
            if (!checkoutFormState.isCreditCardOwnerNameValid()) {
                binding.creditCardOwnerName.setError(getString(R.string.field_required));
            }
            if (!checkoutFormState.isCreditCardExpirationDateValid()) {
                binding.creditCardExp.setError(getString(R.string.field_required));
            }
            if (!checkoutFormState.isCreditCardSecurityCodeValid()) {
                binding.creditCardCvv.setError(getString(R.string.field_required));
            }
            if (!checkoutFormState.isPostalCodeValid()) {
                binding.creditCardZip.setError(getString(R.string.field_required));
            }
            if (!checkoutFormState.isBillingAddressValid()) {
                binding.creditCardAddress.setError(getString(R.string.field_required));
            }
        });
    }

    @NonNull
    private TextWatcher fieldChangeTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.creditCard.setChecked(true);
                binding.cash.setChecked(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                formState.setCreditCardOwnerName(binding.creditCardOwnerName.getText().toString());
                formState.setCreditCardOwnerName(binding.creditCardOwnerName.getText().toString());
                formState.setCreditCardExpirationDate(binding.creditCardExp.getText().toString());
                formState.setCreditCardSecurityCode(binding.creditCardCvv.getText().toString());
                formState.setPostalCode(binding.creditCardZip.getText().toString());
                formState.setBillingAddress(binding.creditCardAddress.getText().toString());
                formState.setCreditCardNumber(binding.creditCardNumber.getText().toString());
                viewModel.updateForm(formState);
            }
        };
    }

    private void clearErrors() {
        binding.creditCardNumber.setError(null);
        binding.creditCardOwnerName.setError(null);
        binding.creditCardExp.setError(null);
        binding.creditCardCvv.setError(null);
        binding.creditCardZip.setError(null);
        binding.creditCardAddress.setError(null);
    }
}