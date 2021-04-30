package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.CustomerUsersAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerUsers;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.CustomerUsersViewModel;

import java.util.List;

public class CustomerUsersAdapter extends RecyclerView.Adapter<CustomerUsersAdapter.CustomerUsersHolder> {
    private Context context;
    private List<CustomerUsers> customerUsers;
    private CustomerUsersViewModel viewModel;
    private String date;

    public CustomerUsersAdapter(Context context, List<CustomerUsers> customerUsers, CustomerUsersViewModel viewModel, String date) {
        this.context = context;
        this.customerUsers = customerUsers;
        this.viewModel = viewModel;
        this.date = date;
    }

    @NonNull
    @Override
    public CustomerUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerUsersHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customer_users_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerUsersHolder holder, int position) {
        holder.bindCustomerSupportInfo(customerUsers.get(position));
        String date = customerUsers.get(position).getLastSeen().substring(0, 10);
        if (this.date.equals(date)) {
            holder.binding.imgUserStatus.setImageResource(R.drawable.user_online);
            holder.binding.txtCustomerUserName.setTextColor(Color.parseColor("#000000"));
            holder.binding.txtLastSeen.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.binding.imgUserStatus.setImageResource(R.drawable.offline_user);
            holder.binding.txtCustomerUserName.setTextColor(Color.parseColor("#696969"));
            holder.binding.txtLastSeen.setTextColor(Color.parseColor("#696969"));
        }
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getItemClicked().setValue(customerUsers.get(position).getCustomerID());
                SipSupportSharedPreferences.setCustomerUserId(context, customerUsers.get(position).getCustomerUserID());
            }
        });

        String str = (position + 1) + "";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            stringBuilder.append((char) ((int) str.charAt(i) - 48 + 1632));
        }
        holder.binding.txtRow.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return customerUsers == null ? 0 : customerUsers.size();
    }

    public class CustomerUsersHolder extends RecyclerView.ViewHolder {
        private CustomerUsersAdapterItemBinding binding;

        public CustomerUsersHolder(CustomerUsersAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerSupportInfo(CustomerUsers customerUsers) {
            String userName = Converter.convert(customerUsers.getUserName());
            binding.txtCustomerUserName.setText(userName);
            binding.txtLastSeen.setText(customerUsers.getLastSeen());
        }
    }
}
