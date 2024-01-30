package com.example.client.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.MainActivity;
import com.example.client.R;
import com.example.client.databinding.FragmentRegisterBinding;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(requireContext())).get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final EditText confirmPasswordEditText = binding.confirmPassword;
        final Button registerBtn = binding.registerBtn;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            registerBtn.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                changeActivity();
            }
            return false;
        });

        registerBtn.setOnClickListener(v -> {
            if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                Toast.makeText(requireContext().getApplicationContext(),
                        "Password and confirm password are different", Toast.LENGTH_SHORT).show();
                return;
            }
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            changeActivity();
        });
    }

    private void changeActivity() {
        if (getUserId() != -1) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.displayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    private long getUserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("userId", -1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}