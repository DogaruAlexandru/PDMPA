package com.example.client.ui.login;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.client.data.LoginDataSource;
import com.example.client.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final Context applicationContext;

    public LoginViewModelFactory(Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()), applicationContext);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}