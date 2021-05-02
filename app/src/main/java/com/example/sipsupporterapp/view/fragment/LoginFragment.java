package com.example.sipsupporterapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentLoginBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.model.UserInfo;
import com.example.sipsupporterapp.model.UserLoginParameter;
import com.example.sipsupporterapp.model.UserResult;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.CustomerContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.view.dialog.EnterIPAddressDialogFragment;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.view.dialog.IPAddressListDialogFragment;
import com.example.sipsupporterapp.viewmodel.SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel viewModel;

    private String spinnerValue;
    private IPAddressListDialogFragment fragment;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel.class);

        if (viewModel.getServerDataList().size() == 0) {
            EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
            fragment.show(getParentFragmentManager(), EnterIPAddressDialogFragment.TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login,
                container,
                false);

        if (SipSupportSharedPreferences.getUserName(getContext()) != null) {
            binding.edTextUserName.setText(SipSupportSharedPreferences.getUserName(getContext()));
            binding.edTextUserName.setSelection(binding.edTextUserName.getText().toString().length());
        }

        if (viewModel.getServerDataList().size() != 0) {
            setupSpinner();
        }

        setListener();
        setItemSelected();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void setObserver() {
        viewModel.getUserResultSingleLiveEvent().observe(
                getViewLifecycleOwner(), new Observer<UserResult>() {
                    @Override
                    public void onChanged(UserResult userResult) {
                        binding.edTextPassword.setEnabled(true);
                        binding.edTextUserName.setEnabled(true);
                        binding.btnLogin.setEnabled(true);
                        binding.imgBtnMore.setEnabled(true);

                        SipSupportSharedPreferences
                                .setUserName(getContext(), binding.edTextUserName.getText().toString());
                        SipSupportSharedPreferences.setLastValueSpinner(getContext(), spinnerValue);
                        binding.loadingLayout.setVisibility(View.GONE);
                        UserInfo[] userInfoArray = userResult.getUsers();
                        if (userInfoArray.length != 0) {
                            SipSupportSharedPreferences
                                    .setUserLoginKey(getContext(), userInfoArray[0].getUserLoginKey());
                            SipSupportSharedPreferences
                                    .setUserFullName(getContext(), userInfoArray[0].getUserFullName());

                            Intent intent = CustomerContainerActivity.newIntent(getContext());
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });

        viewModel.getErrorUserResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.loadingLayout.setVisibility(View.GONE);
                binding.edTextPassword.setEnabled(true);
                binding.edTextUserName.setEnabled(true);
                binding.btnLogin.setEnabled(true);
                binding.imgBtnMore.setEnabled(true);

                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnection().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.loadingLayout.setVisibility(View.GONE);
                binding.edTextPassword.setEnabled(true);
                binding.edTextUserName.setEnabled(true);
                binding.btnLogin.setEnabled(true);
                binding.imgBtnMore.setEnabled(true);

                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isTimeOutExceptionHappen) {
                        binding.loadingLayout.setVisibility(View.GONE);
                        binding.edTextPassword.setEnabled(true);
                        binding.edTextUserName.setEnabled(true);
                        binding.btnLogin.setEnabled(true);
                        binding.imgBtnMore.setEnabled(true);

                        ErrorDialogFragment fragment = ErrorDialogFragment
                                .newInstance("اتصال به اینترنت با خطا مواجه شد");
                        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    }
                });

        viewModel.getWrongIpAddressSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String error) {
                        binding.loadingLayout.setVisibility(View.GONE);
                        binding.edTextPassword.setEnabled(true);
                        binding.edTextUserName.setEnabled(true);
                        binding.btnLogin.setEnabled(true);
                        binding.imgBtnMore.setEnabled(true);

                        ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    }
                });

        viewModel.getDangerousUserSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        viewModel.getInsertSpinnerSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isInsert) {
                        setupSpinner();
                    }
                });

        /*viewModel.getDeleteSpinnerSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<ServerData>() {
                    @Override
                    public void onChanged(ServerData serverData) {
                        if (viewModel.getServerDataList().size() == 0) {
                            SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
                            setupSpinner();
                            fragment.dismiss();
                            binding.edTextPassword.setText("");
                            binding.edTextUserName.setText("");
                            EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
                            fragment.show(getChildFragmentManager(), EnterIPAddressDialogFragment.TAG);
                        } else {
                            SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
                            setupSpinner();
                        }
                    }
                });
        */
        viewModel.getYesDeleteSpinner().observe(getViewLifecycleOwner(), new Observer<ServerData>() {
            @Override
            public void onChanged(ServerData serverData) {
                if (viewModel.getServerDataList().size() == 0) {
                    SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
                    setupSpinner();
                    fragment.dismiss();
                    binding.edTextPassword.setText("");
                    binding.edTextUserName.setText("");
                    EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
                    fragment.show(getChildFragmentManager(), EnterIPAddressDialogFragment.TAG);
                } else {
                    SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
                    setupSpinner();
                }
            }
        });
    }


    private void setupSpinner() {
        String lastValueSpinner = SipSupportSharedPreferences.getLastValueSpinner(getContext());
        List<ServerData> serverDataList = viewModel.getServerDataList();
        List<String> centerNameList = new ArrayList<>();
        for (ServerData serverData : serverDataList) {
            centerNameList.add(serverData.getCenterName());
        }

        List<String> newCenterNameList = new ArrayList<>();

        if (lastValueSpinner != null) {
            for (String centerName : centerNameList) {
                if (!centerName.equals(lastValueSpinner)) {
                    newCenterNameList.add(centerName);
                }
            }
            newCenterNameList.add(0, lastValueSpinner);
            binding.spinner.setItems(newCenterNameList);

        } else {
            binding.spinner.setItems(centerNameList);
        }
        if (binding.spinner.getItems().size() > 0) {
            spinnerValue = (String) binding.spinner.getItems().get(0);
        }
    }


    private void setListener() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getServerDataList() != null & viewModel.getServerDataList().size() == 0) {
                    EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
                    fragment.show(getParentFragmentManager(), EnterIPAddressDialogFragment.TAG);
                } else {
                    binding.edTextPassword.setEnabled(false);
                    binding.edTextUserName.setEnabled(false);
                    binding.btnLogin.setEnabled(false);
                    binding.imgBtnMore.setEnabled(false);

                    String userName = binding.edTextUserName.getText().toString();
                    String password = binding.edTextPassword.getText().toString();

                    UserLoginParameter userLoginParameter = new UserLoginParameter(userName, password);

                    if (spinnerValue != null) {
                        ServerData serverData = viewModel.getServerData(spinnerValue);
                        viewModel.getSipSupportServicePostUserLoginParameter(
                                serverData.getIpAddress() + ":" + serverData.getPort());
                        viewModel.fetchUserResult(userLoginParameter);
                        binding.loadingLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = IPAddressListDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), IPAddressListDialogFragment.TAG);
            }
        });

        binding.edTextUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 0 || actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.edTextPassword.requestFocus();
                }
                return false;
            }
        });
    }


    private void setItemSelected() {
        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                spinnerValue = (String) item;
            }
        });
    }
}