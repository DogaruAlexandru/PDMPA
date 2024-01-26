package com.example.client.ui.containers.container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.R;
import com.example.client.data.model.Container;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ContainerAddFragment extends Fragment {

    public static ContainerAddFragment newInstance() {
        return new ContainerAddFragment();
    }

    private ContainerAddViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContainerAddViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_container_add, container, false);

        setButtonsAction(rootView);

        return rootView;
    }

    private void setButtonsAction(View rootView) {
        Button btnAdd = rootView.findViewById(R.id.btnAdd);
        Button btnBack = rootView.findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(view -> {
            mViewModel.addContainer(getValues(rootView));
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private Container getValues(View rootView) {
        return new Container(null, getFieldString(rootView, R.id.tilContainerName));
    }

    private String getFieldString(View rootView, int id) {
        return Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .getText().toString();
    }
}