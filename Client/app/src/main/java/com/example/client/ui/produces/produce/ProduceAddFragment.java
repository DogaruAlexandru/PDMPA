package com.example.client.ui.produces.produce;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.client.R;
import com.example.client.data.model.ProductFull;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProduceAddFragment extends Fragment {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static ProduceAddFragment newInstance() {
        return new ProduceAddFragment();
    }

    private ProduceAddViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProduceAddViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_produce_add, container, false);

        setButtonsAction(rootView);
        setProductContainerValues(rootView);

        return rootView;
    }

    private void setButtonsAction(View rootView) {
        Button btnAdd = rootView.findViewById(R.id.btnAdd);
        Button btnBack = rootView.findViewById(R.id.btnBack);

        setDatePickerAction(rootView);

        btnAdd.setOnClickListener(view -> {
            mViewModel.addProduct(getValues(rootView));
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void setDatePickerAction(View rootView) {
        var dateEdt = ((TextInputLayout) rootView.findViewById(R.id.tilExpirationDate)).getEditText();
        assert dateEdt != null;
        dateEdt.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        c.set(year1, monthOfYear, dayOfMonth);
                        var formattedDate = dateFormat.format(c.getTime());
                        dateEdt.setText(formattedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    public void setProductContainerValues(View rootView) {
        Spinner spinnerProductContainer = rootView.findViewById(R.id.spinnerProductContainer);

        spinnerProductContainer.setBackground(Objects.requireNonNull(((TextInputLayout) rootView
                .findViewById(R.id.tilProductName)).getEditText()).getBackground());

        mViewModel.getContainerNamesMutableLiveData().observe(getViewLifecycleOwner(), containers -> {
            containers.add(0, "Chose storage");
            ProduceAdapter adapter = new ProduceAdapter(requireContext(),
                    android.R.layout.simple_spinner_item, containers, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductContainer.setAdapter(adapter);
        });
    }

    private ProductFull getValues(View rootView) {
        return new ProductFull(
                null,
                getFieldString(rootView, R.id.tilProductName),
                getStringFromSpinner(rootView, R.id.spinnerProductContainer),
                getFieldDate(rootView, R.id.tilExpirationDate),
                getFieldFloat(rootView, R.id.tilQuantity),
                getFieldDate(rootView, R.id.tilAddedDate),

                getFieldFloat(rootView, R.id.tilEnergyValue),
                getFieldFloat(rootView, R.id.tilFatValue),
                getFieldFloat(rootView, R.id.tilCarbohydrateValue),
                getFieldFloat(rootView, R.id.tilSodium),
                getFieldFloat(rootView, R.id.tilCalcium),
                getFieldFloat(rootView, R.id.tilProtein),
                getFieldFloat(rootView, R.id.tilVitamin),
                getFieldString(rootView, R.id.tilVitaminType),
                getFieldString(rootView, R.id.tilAllergens)
        );
    }

    private String getFieldString(View rootView, int id) {
        return Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .getText().toString();
    }

    private Float getFieldFloat(View rootView, int id) {
        String stringValue = Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id))
                .getEditText()).getText().toString();
        return stringValue.isEmpty() ? null : Float.parseFloat(stringValue);
    }

    private Date getFieldDate(View rootView, int id) {
        String dateString = Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id))
                .getEditText()).getText().toString();

        try {
            return dateString.isEmpty() ? null : dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getStringFromSpinner(View rootView, int id) {
        return ((Spinner) rootView.findViewById(id)).getSelectedItem().toString();
    }

}