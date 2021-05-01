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
import com.example.sipsupporterapp.databinding.CustomerSupportInfoAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.viewmodel.SupportHistoryViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class CustomerSupportInfoAdapter extends RecyclerView.Adapter<CustomerSupportInfoAdapter.CustomerSupportInfoHolder> {
    private Context context;
    private List<CustomerSupportInfo> customerSupportInfoList;
    private SupportHistoryViewModel viewModel;

    public CustomerSupportInfoAdapter(Context context, List<CustomerSupportInfo> customerSupportInfoList, SupportHistoryViewModel viewModel) {
        this.context = context;
        this.customerSupportInfoList = customerSupportInfoList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CustomerSupportInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerSupportInfoHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customer_support_info_adapter_item,
                parent,
                false));
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerSupportInfoHolder holder, int position) {
        CustomerSupportInfo customerSupportInfo = customerSupportInfoList.get(position);
        holder.bindCustomerSupportInfo(customerSupportInfoList.get(position));

        holder.binding.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(context)
                        .addItem(new PowerMenuItem("مشاهده مستندات"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setTextGravity(Gravity.RIGHT)
                        .build();

                powerMenu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        switch (position) {
                            case 0:
                                viewModel.getCustomerSupportInfoAdapterSeeDocumentClickedSingleLiveEvent().setValue(customerSupportInfo);
                                powerMenu.dismiss();
                                break;
                        }
                    }
                });
                powerMenu.showAsDropDown(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerSupportInfoList == null ? 0 : customerSupportInfoList.size();
    }

    public class CustomerSupportInfoHolder extends RecyclerView.ViewHolder {
        private CustomerSupportInfoAdapterItemBinding binding;

        public CustomerSupportInfoHolder(CustomerSupportInfoAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerSupportInfo(CustomerSupportInfo customerSupportInfo) {
            String question = Converter.convert(customerSupportInfo.getQuestion());
            binding.txtQuestion.setText(question);
            String answer = Converter.convert(customerSupportInfo.getAnswer());
            binding.txtAnswer.setText(answer);
            String userFullName = Converter.convert(customerSupportInfo.getUserFullName());
            binding.txtUserFullName.setText(userFullName + " :");

            String customerSupportID = String.valueOf(customerSupportInfo.getCustomerSupportID());
            binding.txtCustomerSupportID.setText(customerSupportID);

            binding.txtRegTime.setText(customerSupportInfo.getRegTime());

        }
    }
}
