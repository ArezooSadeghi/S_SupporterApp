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
import com.example.sipsupporterapp.databinding.ProductsAdapeterItemBinding;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {
    private Context context;
    private List<CustomerProducts> customerProductsList;
    private RegisterProductViewModel viewModel;

    public ProductsAdapter(Context context, List<CustomerProducts> customerProductsList, RegisterProductViewModel viewModel) {
        this.context = context;
        this.customerProductsList = customerProductsList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.products_adapeter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsHolder holder, int position) {
        CustomerProducts customerProducts = customerProductsList.get(position);
        holder.bindCustomerProducts(customerProducts);

        int customerProductID = customerProducts.getCustomerProductID();

        holder.binding.imgMore.setOnClickListener(new View.OnClickListener() {
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
                                viewModel.getEditClickedSingleLiveEvent().setValue(customerProducts);
                                powerMenu.dismiss();
                                break;
                            case 1:
                                viewModel.getDeleteClickedSingleLiveEvent().setValue(customerProductID);
                                powerMenu.dismiss();
                                break;
                            case 2:
                                viewModel.getProductAdapterSeeDocumentsClickedSingleLiveEvent().setValue(customerProducts);
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
        return customerProductsList == null ? 0 : customerProductsList.size();
    }

    public class ProductsHolder extends RecyclerView.ViewHolder {
        private ProductsAdapeterItemBinding binding;

        public ProductsHolder(ProductsAdapeterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerProducts(CustomerProducts customerProducts) {
            String productName = Converter.convert(customerProducts.getProductName());
            binding.txtProductName.setText(productName);
            if (!customerProducts.getDescription().isEmpty()) {
                binding.txtDescription.setVisibility(View.VISIBLE);
                String description = Converter.convert(customerProducts.getDescription());
                binding.txtDescription.setText(description);
            }

            String userFullName = Converter.convert(customerProducts.getUserFullName());
            binding.txtUserName.setText(userFullName);

           /* String addTime = String.valueOf(customerProducts.getAddTime());
            String str = addTime.substring(0, 4) + "/" + addTime.substring(4, 6) + "/" + addTime.substring(6, 10) + ":" + addTime.substring(10, 12) + ":" + addTime.substring(12);
            binding.txtAddTime.setText(str);
*/

            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(customerProducts.getInvoicePrice());

            binding.txtInvoicePrice.setText(formattedNumber + "تومان");

            if (customerProducts.isFinish()) {
                binding.checkBoxFinish.setChecked(true);
            } else {
                binding.checkBoxFinish.setChecked(false);
            }

            if (customerProducts.isInvoicePayment()) {
                binding.checkBoxInvoicePayment.setChecked(true);
            } else {
                binding.checkBoxInvoicePayment.setChecked(false);
            }
        }
    }
}
