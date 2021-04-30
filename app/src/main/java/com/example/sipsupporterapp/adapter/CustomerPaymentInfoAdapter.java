package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.CustomerPaymentInfoAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerPaymentInfo;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class CustomerPaymentInfoAdapter extends RecyclerView.Adapter<CustomerPaymentInfoAdapter.CustomerPaymentInfoHolder> {
    private Context context;
    private List<CustomerPaymentInfo> customerPaymentInfoList;
    private DepositAmountsViewModel viewModel;

    public CustomerPaymentInfoAdapter(Context context, List<CustomerPaymentInfo> customerPaymentInfoList, DepositAmountsViewModel viewModel) {
        this.context = context;
        this.customerPaymentInfoList = customerPaymentInfoList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CustomerPaymentInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerPaymentInfoHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customer_payment_info_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPaymentInfoHolder holder, int position) {
        holder.bindCustomerPaymentInfo(customerPaymentInfoList.get(position));

        holder.binding.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(context)
                        .addItem(new PowerMenuItem("ویرایش", R.drawable.new_edit))
                        .addItem(new PowerMenuItem("حذف", R.drawable.new_delete))
                        .addItem(new PowerMenuItem("مشاهده مستندات"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setTextGravity(Gravity.RIGHT)
                        .build();

                powerMenu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        switch (position) {
                            case 0:
                                viewModel.getEditCustomerPaymentsClickedSingleLiveEvent().setValue(customerPaymentInfoList.get(position));
                                powerMenu.dismiss();
                                return;
                            case 1:
                                viewModel.getDeleteCustomerPaymentsClickedSingleLiveEvent().setValue(customerPaymentInfoList.get(position));
                                powerMenu.dismiss();
                                return;
                            case 2:
                                viewModel.getSeeDocumentsClickedSingleLiveEvent().setValue(customerPaymentInfoList.get(position));
                                powerMenu.dismiss();
                                return;
                        }
                    }
                });
                powerMenu.showAsDropDown(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerPaymentInfoList == null ? 0 : customerPaymentInfoList.size();
    }

    public class CustomerPaymentInfoHolder extends RecyclerView.ViewHolder {
        private CustomerPaymentInfoAdapterItemBinding binding;

        public CustomerPaymentInfoHolder(CustomerPaymentInfoAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerPaymentInfo(CustomerPaymentInfo customerPaymentInfo) {
            String bankAccountName = Converter.convert(customerPaymentInfo.getBankAccountName());
            binding.txtBankAccountName.setText(bankAccountName);
            binding.txtBankAccountNo.setText(customerPaymentInfo.getBankAccountNO());
            String bankName = Converter.convert(customerPaymentInfo.getBankName());
            binding.txtBankName.setText(bankName);
        }
    }
}
