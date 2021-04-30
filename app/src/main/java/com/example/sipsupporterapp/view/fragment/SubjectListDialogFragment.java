package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.TreeNodeHolder;
import com.example.sipsupporterapp.databinding.FragmentSubjectListDialogBinding;
import com.example.sipsupporterapp.model.PaymentSubjectInfo;
import com.example.sipsupporterapp.model.PaymentSubjectResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectListDialogFragment extends DialogFragment {
    private FragmentSubjectListDialogBinding binding;
    private WithdrawViewModel viewModel;

    private PaymentSubjectInfo[] paymentSubjectInfoArray;
    private List<Integer> parentIDList = new ArrayList<>();
    private List<TreeNode> noChildNodeList = new ArrayList<>();
    private boolean flag, generalFlag;
    private TreeNode newTreeNode;
    private String paymentSubject;
    private int paymentSubjectID;

    public static final String TAG = SubjectListDialogFragment.class.getSimpleName();

    public static SubjectListDialogFragment newInstance() {
        SubjectListDialogFragment fragment = new SubjectListDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WithdrawViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServicePaymentSubjectsList(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchPaymentSubjectsList(SipSupportSharedPreferences.getUserLoginKey(getContext()));

        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_subject_list_dialog,
                null,
                false);

        handleClicked();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    private void setObserver() {
        viewModel.getPaymentSubjectResultPaymentSubjectsListSingleLiveEvent().observe(this, new Observer<PaymentSubjectResult>() {
            @Override
            public void onChanged(PaymentSubjectResult paymentSubjectResult) {
                paymentSubjectInfoArray = paymentSubjectResult.getPaymentSubjects();
                List<String> parentNodePaymentSubject = new ArrayList<>();
                for (PaymentSubjectInfo paymentSubjectInfo : paymentSubjectResult.getPaymentSubjects()) {
                    if (paymentSubjectInfo.getParentID() == 0 || paymentSubjectInfo.getParentPaymentSubject() == null) {
                        parentNodePaymentSubject.add(paymentSubjectInfo.getPaymentSubject());
                    } else {
                        parentIDList.add(paymentSubjectInfo.getParentID());
                    }
                }

                TreeNode root = TreeNode.root();

                for (int i = 0; i < parentNodePaymentSubject.size(); i++) {
                    TreeNode parentNode = new TreeNode(new TreeNodeHolder.TreeNode(R.drawable.ic_folder, parentNodePaymentSubject.get(i)));
                    root.addChild(parentNode);
                    addNodeToTree(parentNode, parentNodePaymentSubject.get(i));
                    if (!generalFlag) {
                        noChildNodeList.add(parentNode);
                        Object object = parentNode.getValue();
                        TreeNodeHolder.TreeNode treeNode = (TreeNodeHolder.TreeNode) object;
                        treeNode.setIcon(R.drawable.document);
                        generalFlag = false;
                    } else {
                        generalFlag = false;
                    }
                }

                TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        TreeNodeHolder.TreeNode treeNode = (TreeNodeHolder.TreeNode) value;
                        paymentSubject = treeNode.getText();
                    }
                };

                AndroidTreeView androidTreeView = new AndroidTreeView(getContext(), root);
                androidTreeView.setDefaultAnimation(true);
                androidTreeView.setDefaultViewHolder(TreeNodeHolder.class);
                androidTreeView.setDefaultNodeClickListener(nodeClickListener);

                binding.treeViewContainer.addView(androidTreeView.getView());
            }
        });

        viewModel.getErrorPaymentSubjectResultPaymentSubjectsListSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }


    private void addNodeToTree(TreeNode node, String paymentSubject) {
        flag = false;
        String newPaymentSubject = "";
        for (int i = 0; i < paymentSubjectInfoArray.length; i++) {
            if (paymentSubjectInfoArray[i].getParentPaymentSubject() != null) {
                if (paymentSubjectInfoArray[i].getParentPaymentSubject().equals(paymentSubject)) {
                    generalFlag = true;
                    newPaymentSubject = paymentSubjectInfoArray[i].getPaymentSubject();
                    flag = true;
                    TreeNode childNode = new TreeNode(new TreeNodeHolder.TreeNode(R.drawable.ic_folder, paymentSubjectInfoArray[i].getPaymentSubject()));
                    newTreeNode = childNode;
                    node.addChild(childNode);
                    addNodeToTree(childNode, paymentSubjectInfoArray[i].getPaymentSubject());
                }
            }
        }
        if (!flag) {
            noChildNodeList.add(newTreeNode);
            TreeNodeHolder.TreeNode treeNode = (TreeNodeHolder.TreeNode) newTreeNode.getValue();
            treeNode.setIcon(R.drawable.document);
        }
    }


    private void handleClicked() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentSubject == null || paymentSubject.isEmpty()) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("موردی انتخاب نشده است");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else if (!hasNotChild(paymentSubject)) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("مورد انتخاب شده صحیح نمی باشد");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    for (PaymentSubjectInfo paymentSubjectInfo : paymentSubjectInfoArray) {
                        if (paymentSubjectInfo.getPaymentSubject().equals(paymentSubject)) {
                            paymentSubjectID = paymentSubjectInfo.getPaymentSubjectID();
                            break;
                        }
                    }
                    HashMap<Integer, String> hashMap = new HashMap<>();
                    hashMap.put(paymentSubjectID, paymentSubject);
                    viewModel.getSubjectSingleLiveEvent().setValue(hashMap);
                    dismiss();
                }
            }
        });
    }


    private boolean hasNotChild(String paymentSubject) {
        for (TreeNode node : noChildNodeList) {
            Object object = node.getValue();
            TreeNodeHolder.TreeNode treeNode = (TreeNodeHolder.TreeNode) object;
            String treeNodePaymentSubject = treeNode.getText();
            if (paymentSubject.equals(treeNodePaymentSubject)) {
                return true;
            }
        }
        return false;
    }
}