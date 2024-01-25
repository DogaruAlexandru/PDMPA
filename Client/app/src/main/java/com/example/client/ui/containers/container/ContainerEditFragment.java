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


public class ContainerEditFragment extends Fragment {
    private long containerId;

    public static ContainerEditFragment newInstance() {
        return new ContainerEditFragment();
    }

    private ContainerEditViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContainerEditViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_container_edit, container, false);

        assert getArguments() != null;
        containerId = getArguments().getLong("containerId", -1);
        mViewModel.setContainerId(containerId);

        setButtonsAction(rootView);

        setFieldsValue(rootView);

        return rootView;
    }

    private void setButtonsAction(View rootView) {
        Button btnEdit = rootView.findViewById(R.id.btnEdit);
        Button btnBack = rootView.findViewById(R.id.btnBack);
        Button btnRemove = rootView.findViewById(R.id.btnRemove);

        btnEdit.setOnClickListener(view -> {
            mViewModel.updateContainer(getValues(rootView));
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnRemove.setOnClickListener(view -> {
            mViewModel.deleteContainer();
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void setFieldsValue(View rootView) {
        mViewModel.getContainerMutableLiveData().observe(getViewLifecycleOwner(), container -> {
            setFieldString(rootView, R.id.tilContainerName, container.name());
        });
    }

    private Container getValues(View rootView) {
        return new Container(containerId, getFieldString(rootView, R.id.tilContainerName));
    }

    private void setFieldString(View rootView, int id, String value) {
        if (value == null) {
            return;
        }
        Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText(value);
    }

    private String getFieldString(View rootView, int id) {
        return Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .getText().toString();
    }
}