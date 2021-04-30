package com.example.sipsupporterapp.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentRegisterProductBinding;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.ProductInfo;
import com.example.sipsupporterapp.model.ProductResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProductFragment extends DialogFragment {
    private FragmentRegisterProductBinding binding;
    private RegisterProductViewModel viewModel;
    private String value;
    private int customerID, customerProductID;
    private boolean finish, invoicePayment, isAdd;
    private long invoicePrice, expireDate;
    private String description, productName;
    private Map<String, Integer> map = new HashMap<>();

    private static final String ARGS_FINISH = "finish";
    private static final String ARGS_IS_ADD = "isAdd";
    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_PRODUCT_NAME = "productName";
    private static final String ARGS_DESCRIPTION = "description";
    private static final String ARGS_INVOICE_PRICE = "invoicePrice";
    private static final String ARGS_INVOICE_PAYMENT = "invoicePayment";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";
    private static final String ARGS_EXPIRE_DATE = "expireDate";

    public static final String TAG = RegisterProductFragment.class.getSimpleName();

    public static RegisterProductFragment newInstance(int customerID,
                                                      String productName,
                                                      String description,
                                                      long invoicePrice,
                                                      boolean invoicePayment,
                                                      boolean finish,
                                                      boolean isAdd,
                                                      int customerProductID, long expireDate) {
        RegisterProductFragment fragment = new RegisterProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putString(ARGS_PRODUCT_NAME, productName);
        args.putString(ARGS_DESCRIPTION, description);
        args.putLong(ARGS_INVOICE_PRICE, invoicePrice);
        args.putBoolean(ARGS_INVOICE_PAYMENT, invoicePayment);
        args.putBoolean(ARGS_FINISH, finish);
        args.putBoolean(ARGS_IS_ADD, isAdd);
        args.putInt(ARGS_CUSTOMER_PRODUCT_ID, customerProductID);
        args.putLong(ARGS_EXPIRE_DATE, expireDate);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        invoicePrice = getArguments().getLong(ARGS_INVOICE_PRICE);
        finish = getArguments().getBoolean(ARGS_FINISH);
        invoicePayment = getArguments().getBoolean(ARGS_INVOICE_PAYMENT);
        productName = getArguments().getString(ARGS_PRODUCT_NAME);
        description = getArguments().getString(ARGS_DESCRIPTION);
        isAdd = getArguments().getBoolean(ARGS_IS_ADD);
        customerProductID = getArguments().getInt(ARGS_CUSTOMER_PRODUCT_ID);
        expireDate = getArguments().getLong(ARGS_EXPIRE_DATE);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterProductViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetProductResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchProductResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));

        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_register_product,
                null,
                false);

        String customerName = Converter.convert(SipSupportSharedPreferences.getCustomerName(getContext()));
        binding.txtCustomerName.setText(customerName);
        binding.edTextDescription.setText(description);
        binding.edTextDescription.setSelection(binding.edTextDescription.getText().toString().length());

        if (expireDate != 0) {
            String date = String.valueOf(expireDate);
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6);
            String dateFormat = year + "/" + month + "/" + day;
            String persianDateFormat = convertEnToPer(dateFormat);
            binding.btnDateExpiration.setText(dateFormat);
        } else {
            binding.btnDateExpiration.setText(SipSupportSharedPreferences.getDate(getContext()));
        }

        binding.edTextInvoicePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.edTextInvoicePrice.removeTextChangedListener(this);
                try {
                    String text = editable.toString();
                    Long textToLong;
                    if (text.contains(",")) {
                        text = text.replaceAll(",", "");
                    }
                    textToLong = Long.parseLong(text);
                    NumberFormat numberFormat = new DecimalFormat("#,###");
                    String currencyFormat = numberFormat.format(textToLong);
                    binding.edTextInvoicePrice.setText(currencyFormat);
                    binding.edTextInvoicePrice.setSelection(binding.edTextInvoicePrice.getText().length());
                } catch (NumberFormatException exception) {
                    Log.e(TAG, exception.getMessage());
                }
                binding.edTextInvoicePrice.addTextChangedListener(this);
            }
        });


        if (finish) {
            binding.checkBoxFinish.setChecked(true);
        } else {
            binding.checkBoxFinish.setChecked(false);
        }

        if (invoicePayment) {
            binding.checkBoxInvoicePayment.setChecked(true);
        } else {
            binding.checkBoxInvoicePayment.setChecked(false);
        }

        setListener();
        setItemSelectedListener();

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    private void setListener() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productID = map.get(value);

                String text = binding.edTextInvoicePrice.getText().toString();
                String newText = text.replace(",", "");

                long invoicePrice = Long.valueOf(newText);
                boolean paymentPrice;
                if (binding.checkBoxInvoicePayment.isChecked()) {
                    paymentPrice = true;
                } else {
                    paymentPrice = false;
                }

                boolean finish;
                if (binding.checkBoxFinish.isChecked()) {
                    finish = true;
                } else {
                    finish = false;
                }

                String description = binding.edTextDescription.getText().toString();

                CustomerProducts customerProducts = new CustomerProducts();
                customerProducts.setCustomerID(customerID);
                customerProducts.setProductID(productID);
                customerProducts.setInvoicePrice(invoicePrice);
                customerProducts.setInvoicePayment(paymentPrice);
                customerProducts.setFinish(finish);
                customerProducts.setDescription(description);
                if (expireDate == 0) {
                    String date = binding.btnDateExpiration.getText().toString().replaceAll("/", "");
                    expireDate = Long.valueOf(expireDate);
                    Log.d("Arezoo", expireDate + "");
                }
                customerProducts.setExpireDate(expireDate);
                Log.d("Arezoo", expireDate + "");

                if (isAdd) {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServicePostCustomerProducts(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.postCustomerProducts(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProducts);
                } else {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServiceForEditCustomerProduct(serverData.getIpAddress() + ":" + serverData.getPort());
                    customerProducts.setCustomerProductID(customerProductID);
                    viewModel.editCustomerProduct(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProducts);
                }
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnDateExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                if (String.valueOf(monthOfYear + 1).length() == 1 & String.valueOf(dayOfMonth).length() == 1) {
                                    expireDate = Long.valueOf(year + "0" + (monthOfYear + 1) + "0" + dayOfMonth);
                                    binding.btnDateExpiration.setText(year + "/" + "0" + (monthOfYear + 1) + "/" + "0" + dayOfMonth);
                                } else if (String.valueOf(monthOfYear + 1).length() == 1) {
                                    expireDate = Long.valueOf(year + "0" + (monthOfYear + 1) + dayOfMonth);
                                    binding.btnDateExpiration.setText(year + "/" + "0" + (monthOfYear + 1) + "/" + dayOfMonth);
                                } else if (String.valueOf(dayOfMonth).length() == 1) {
                                    expireDate = Long.valueOf(year + (monthOfYear + 1) + "0" + dayOfMonth);
                                    binding.btnDateExpiration.setText(year + "/" + (monthOfYear + 1) + "/" + "0" + dayOfMonth);
                                } else {
                                    expireDate = Long.valueOf(String.valueOf(year + (monthOfYear + 1) + dayOfMonth));
                                    binding.btnDateExpiration.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                }
                            }
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay());
                datePickerDialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }


    private void setItemSelectedListener() {
        binding.spinnerProducts.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                value = (String) item;

                int productID = map.get(value);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceForGetCustomerProductInfo(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchProductInfo(SipSupportSharedPreferences.getUserLoginKey(getContext()), productID);

            }
        });
    }


    private void setObserver() {
        viewModel.getGetProductResultSingleLiveEvent().observe(this, new Observer<ProductResult>() {
            @Override
            public void onChanged(ProductResult productResult) {
                Log.d("Arezoo", productResult.getProducts()[0].getAddTime() + "");
                for (ProductInfo productInfo : productResult.getProducts()) {
                    map.put(productInfo.getProductName(), productInfo.getProductID());
                }
                setupSpinner(productResult.getProducts());

                int productID = map.get(value);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceForGetCustomerProductInfo(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchProductInfo(SipSupportSharedPreferences.getUserLoginKey(getContext()), productID);

            }
        });

        viewModel.getPostCustomerProductsSingleLiveEvent().observe(this, new Observer<CustomerProductResult>() {
            @Override
            public void onChanged(CustomerProductResult customerProductResult) {
                RegisterProductSuccessfulDialogFragment fragment = RegisterProductSuccessfulDialogFragment.newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), RegisterProductSuccessfulDialogFragment.TAG);
            }
        });

        viewModel.getErrorPostCustomerProductsSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(s);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getProductInfoSingleLiveEvent().observe(this, new Observer<ProductResult>() {
            @Override
            public void onChanged(ProductResult productResult) {
                if (invoicePrice == 0) {
                    binding.edTextInvoicePrice.setText(String.valueOf(productResult.getProducts()[0].getCost()));
                } else {
                    binding.edTextInvoicePrice.setText(String.valueOf(invoicePrice));
                }
            }
        });

        viewModel.getErrorProductInfoSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDialogDismissSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean dialogDismissed) {
                dismiss();
            }
        });

        viewModel.getEditCustomerProductSingleLiveEvent().observe(this, new Observer<CustomerProductResult>() {
            @Override
            public void onChanged(CustomerProductResult customerProductResult) {
                RegisterProductSuccessfulDialogFragment fragment = RegisterProductSuccessfulDialogFragment.newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), RegisterProductSuccessfulDialogFragment.TAG);
            }
        });

        viewModel.getErrorEditCustomerProductSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getActivity().getSupportFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnection().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String noConnection) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(noConnection);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("دسترسی به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDangerousUser) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void setupSpinner(ProductInfo[] productInfoArray) {
        List<String> productNameList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoArray) {
            productNameList.add(productInfo.getProductName());
        }

        if (!productName.isEmpty()) {
            value = productName;
            productNameList.remove(productName);
            productNameList.add(0, productName);
            binding.spinnerProducts.setItems(productNameList);
        } else {
            value = productNameList.get(0);
            binding.spinnerProducts.setItems(productNameList);
        }
    }

    private String convertEnToPer(String date) {
        return date = date
                .replaceAll("0", "۰")
                .replaceAll("1", "۱")
                .replaceAll("2", "۲")
                .replaceAll("3", "۳")
                .replaceAll("4", "۴")
                .replaceAll("5", "۵")
                .replaceAll("6", "۶")
                .replaceAll("7", "۷")
                .replaceAll("8", "۸")
                .replaceAll("9", "۹ ");
    }
}