package com.example.sipsupporterapp.view.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.sipsupporterapp.view.fragment.ImageListFragment;

public class ImageListContainerActivity extends SingleFragmentActivity {

    private static final String EXTRA_CUSTOMER_ID = "customerID";
    private static final String EXTRA_CUSTOMER_SUPPORT_ID = "customerSupportID";
    private static final String EXTRA_CUSTOMER_PRODUCT_ID = "customerProductID";
    private static final String EXTRA_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    @Override
    public Fragment createFragment() {
        int customerID = getIntent().getIntExtra(EXTRA_CUSTOMER_ID, 0);
        int customerSupportID = getIntent().getIntExtra(EXTRA_CUSTOMER_SUPPORT_ID, 0);
        int customerProductID = getIntent().getIntExtra(EXTRA_CUSTOMER_PRODUCT_ID, 0);
        int customerPaymentID = getIntent().getIntExtra(EXTRA_CUSTOMER_PAYMENT_ID, 0);
        return ImageListFragment.newInstance(customerID, customerSupportID, customerProductID, customerPaymentID);
    }

    public static Intent newIntent(Context context, int customerID, int customerSupportID, int customerProductID, int customerPaymentID) {
        Intent intent = new Intent(context, ImageListContainerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_ID, customerID);
        intent.putExtra(EXTRA_CUSTOMER_SUPPORT_ID, customerSupportID);
        intent.putExtra(EXTRA_CUSTOMER_PRODUCT_ID, customerProductID);
        intent.putExtra(EXTRA_CUSTOMER_PAYMENT_ID, customerPaymentID);
        return intent;
    }
}