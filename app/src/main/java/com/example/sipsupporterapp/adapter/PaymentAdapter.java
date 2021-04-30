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
import com.example.sipsupporterapp.databinding.PaymentAdapterItemBinding;
import com.example.sipsupporterapp.model.PaymentInfo;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder> {
    private Context context;
    private List<PaymentInfo> paymentInfoList;
    private WithdrawViewModel viewModel;

    public PaymentAdapter(Context context, List<PaymentInfo> paymentInfoList, WithdrawViewModel viewModel) {
        this.context = context;
        this.paymentInfoList = paymentInfoList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.payment_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        holder.bindPaymentInfo(paymentInfoList.get(position));

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
                                viewModel.getEditClickedSingleLiveEvent().setValue(paymentInfoList.get(position));
                                powerMenu.dismiss();
                                return;
                            case 1:
                                viewModel.getDeleteClickedSingleLiveEvent().setValue(paymentInfoList.get(position));
                                powerMenu.dismiss();
                                return;
                            case 2:
                                viewModel.getSeeDocumentsClickedSingleLiveEvent().setValue(paymentInfoList.get(position));
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
        return paymentInfoList == null ? 0 : paymentInfoList.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        private PaymentAdapterItemBinding binding;

        public PaymentHolder(PaymentAdapterItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindPaymentInfo(PaymentInfo paymentInfo) {
            String paymentSubject = Converter.convert(paymentInfo.getPaymentSubject());
            binding.txtPaymentSubject.setText(paymentSubject);

            if (!paymentInfo.getDescription().isEmpty()) {
                binding.txtDescription.setVisibility(View.VISIBLE);
                String description = Converter.convert(paymentInfo.getDescription());
                binding.txtDescription.setText(description);
            }

            if (paymentInfo.getDatePayment() != 0) {
                String date = String.valueOf(paymentInfo.getDatePayment());
                String year = date.substring(0, 4);
                String month = date.substring(4, 6);
                String day = date.substring(6);
                String dateFormat = year + "/" + month + "/" + day;
                binding.txtDatePayment.setText(dateFormat);
            } else {
                binding.txtDatePayment.setText(paymentInfo.getDatePayment() + "");
            }
        }
    }
}
