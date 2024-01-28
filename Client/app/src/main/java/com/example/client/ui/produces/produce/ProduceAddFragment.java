package com.example.client.ui.produces.produce;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.client.R;
import com.example.client.data.model.ProductFull;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.EditText;



import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class ProduceAddFragment extends Fragment {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private static final int CAMERA_REQUEST_CODE = 123;

    private static final String TAG = "ProduceAddFragment";
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

        //detailsFragment = new ProduceDetailsFragment();
        //detailsFragment.setProduceAddFragment(this);
        //detailsFragment.onCreateView( inflater, container,
        // savedInstanceState);

        setButtonsAction(rootView);
        setProductContainerValues(rootView);

        setFieldDate(rootView, R.id.tilAddedDate, new Date());

        setStringValue(rootView,R.id.tilProductName," ");
        setDoubleValue(rootView,R.id.tilEnergyValue,0.0);
        setDoubleValue(rootView,R.id.tilFatValue,0.0);
        setDoubleValue(rootView,R.id.tilCarbohydrateValue,0.0);
        setDoubleValue(rootView,R.id.tilSodium,0.0);
        setDoubleValue(rootView,R.id.tilCalcium,0.0);
        setDoubleValue(rootView,R.id.tilProtein,0.0);
        setStringValue(rootView,R.id.tilVitamin," ");
        setStringValue(rootView,R.id.tilVitaminType," ");
        setStringValue(rootView,R.id.tilAllergens," ");




        // Find the button and set a click listener for it
        Button openCameraButton = rootView.findViewById(R.id.openCam);
        openCameraButton.setOnClickListener(v -> openCamera());

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Pass the captured image bitmap to the barcode scanner
            scanBarcodes(imageBitmap);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            Log.d(TAG, "Cam opened " );
        } else {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }




    private void scanBarcodes(Bitmap bitmap) {

        Log.d(TAG, "scanBarcodes method " );
        InputImage image = InputImage.fromBitmap(bitmap, 0); // Rotation is 0

        // Create a barcode scanner
        BarcodeScanner scanner = BarcodeScanning.getClient();
        Log.d(TAG, "Scanner " + scanner);

        // Perform the barcode scanning process
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    Log.d(TAG, "Hello " );
                    // Process the detected barcodes
                    for (Barcode barcode : barcodes) {
                        String rawValue = barcode.getRawValue();
                        Log.d(TAG, "Barcode scanned successfully: " + rawValue);
                        // Assuming rawValue is the barcode information you want to use in the Nutritionix API call
                        searchNutritionixAPI(rawValue);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the barcode scanning process
                    Log.e(TAG, "Barcode scanning failed: " + e.getMessage());
                });
    }

    private void searchNutritionixAPI(String barcode) {
        // Replace "YOUR_NUTRITIONIX_API_KEY" with your actual API key
        String apiKey = "4734332193934d89d422e242ee4cad1c";
        String appId = "99f1be14"; // Add your actual app ID here

        // Construct the API URL with the barcode, appId, and apiKey
        String apiUrl = "https://trackapi.nutritionix.com/v2/search/item/?upc=" + barcode;
        Log.d(TAG, "We made it here " + barcode);
        // Create a new thread for making the HTTP request
        new Thread(() -> {
            try {
                Log.d(TAG, "hello1");

                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Set the request headers for authentication
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("x-app-id", appId);
                urlConnection.setRequestProperty("x-app-key", apiKey);


                try {
                    Log.d(TAG, "hello2");

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertStreamToString(in);

                    if(response!=null)
                    {
                        Log.d(TAG, "response" + response);
                        handleNutritionixApiResponse(response);
                    }
                    else
                    {
                        Log.d(TAG, "response is null");
                    }


                } finally {

                    // Disconnect the URL connection
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                // Print stack trace if an exception occurs
                e.printStackTrace();
            }
        }).start();
    }

    // Helper method to convert InputStream to String
    private String convertStreamToString(InputStream is) {
        try (Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public void handleNutritionixApiResponse( String response) {

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray foodsArray = jsonResponse.getJSONArray("foods");

            if (foodsArray.length() > 0) {
                JSONObject foodObject = foodsArray.getJSONObject(0); // Get the first food item

                // Extract values from the JSON response
                //noinspection DataFlowIssue
                String foodName = foodObject.optString("food_name", null);
                Double energyValue = foodObject.optDouble("nf_calories", 0);
                Double fatValue = foodObject.optDouble("nf_total_fat", 0);
                Double carbohydrateValue = foodObject.optDouble("nf_total_carbohydrate", 0);
                Double sodium = foodObject.optDouble("nf_sodium",0);
                Double calcium = foodObject.optDouble("calcium", 0); // Assuming this is available in the response
                Double protein = foodObject.optDouble("nf_protein",0);
                String vitamin = foodObject.optString("vitamin", null); // Example field, replace with actual field name
                String vitaminType = foodObject.optString("vitaminType", null); // Example field, replace with actual field name
                String allergens = foodObject.optString("allergens", null);

                // Print or use the extracted values as needed
                /*Log.d(TAG, "Food Name: " + foodName);
                Log.d(TAG, "Energy Value: " + energyValue);
                Log.d(TAG, "Fat Value: " + fatValue);
                Log.d(TAG, "Carbohydrate Value: " + carbohydrateValue);
                Log.d(TAG, "Sodium: " + sodium);
                Log.d(TAG, "Calcium: " + calcium);
                Log.d(TAG, "Protein: " + protein);
                Log.d(TAG, "Vitamin: " + vitamin);
                Log.d(TAG, "Vitamin Type: " + vitaminType);
                Log.d(TAG, "Allergens: " + allergens);*/



                setStringValue(getView(),R.id.tilProductName,foodName);
                setDoubleValue(getView(),R.id.tilEnergyValue,energyValue);
                setDoubleValue(getView(),R.id.tilFatValue,fatValue);
                setDoubleValue(getView(),R.id.tilCarbohydrateValue,carbohydrateValue);
                setDoubleValue(getView(),R.id.tilSodium,sodium);
                setDoubleValue(getView(),R.id.tilCalcium,calcium);
                setDoubleValue(getView(),R.id.tilProtein,protein);
                setStringValue(getView(),R.id.tilVitamin,vitamin);
                setStringValue(getView(),R.id.tilVitaminType,vitaminType);
                setStringValue(getView(),R.id.tilAllergens,allergens);


            } else {
                Log.d(TAG, "No food items found in the response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setButtonsAction(View rootView) {
        Button btnAdd = rootView.findViewById(R.id.btnAdd);
        Button btnBack = rootView.findViewById(R.id.btnBack);

        setDatePickerAction(rootView);

        btnAdd.setOnClickListener(view -> {
            var values = getValues(rootView);
            if (!validValues(rootView, values)) {
                return;
            }
            mViewModel.addProduct(values);
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private boolean validValues(View rootView, ProductFull values) {
        boolean isValid = true;

        isValid &= notEmptyString(values.productName(), rootView.findViewById(R.id.tilProductName), "Product name");
        isValid &= positiveNotEmptyFloat(values.quantity(), rootView.findViewById(R.id.tilQuantity), "Quantity");
        isValid &= notEmptyDate(values.expirationDate(), rootView.findViewById(R.id.tilExpirationDate), "Expiration date");

        isValid &= validSpinnerSelection(rootView.findViewById(R.id.spinnerProductContainer), rootView);

        isValid &= positiveEmptyFloat(values.energyValue(), rootView.findViewById(R.id.tilEnergyValue), "Energy value");
        isValid &= positiveEmptyFloat(values.fatValue(), rootView.findViewById(R.id.tilFatValue), "Fat value");
        isValid &= positiveEmptyFloat(values.carbohydrateValue(), rootView.findViewById(R.id.tilCarbohydrateValue), "Carbohydrate value");
        isValid &= positiveEmptyFloat(values.sodium(), rootView.findViewById(R.id.tilSodium), "Sodium");
        isValid &= positiveEmptyFloat(values.calcium(), rootView.findViewById(R.id.tilCalcium), "Calcium");
        isValid &= positiveEmptyFloat(values.protein(), rootView.findViewById(R.id.tilProtein), "Protein");
        isValid &= positiveEmptyFloat(values.vitamin(), rootView.findViewById(R.id.tilVitamin), "Vitamin");

        return isValid;
    }

    private boolean validSpinnerSelection(Spinner spinner, View rootView) {
        if (spinner.getSelectedItemPosition() <= 0) {
            spinner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
            return false;
        }
        spinner.setBackground(Objects.requireNonNull(((TextInputLayout) rootView
                .findViewById(R.id.tilProductName)).getEditText()).getBackground());
        return true;
    }

    private boolean notEmptyDate(Date value, TextInputLayout till, String fieldName) {
        if (value == null) {
            till.setError(fieldName + " cannot be empty");
            return false;
        }
        return true;
    }

    private boolean notEmptyString(String value, TextInputLayout till, String fieldName) {
        if (value.isEmpty()) {
            till.setError(fieldName + " cannot be empty");
            return false;
        }
        return true;
    }

    private boolean positiveNotEmptyFloat(Float value, TextInputLayout till, String fieldName) {
        if (value == null) {
            till.setError(fieldName + " cannot be empty");
            return false;
        }
        return positiveFloat(value, till, fieldName);
    }

    private boolean positiveEmptyFloat(Float value, TextInputLayout till, String fieldName) {
        return value == null || positiveFloat(value, till, fieldName);
    }

    private boolean positiveFloat(Float value, TextInputLayout till, String fieldName) {
        if (value < 0) {
            till.setError(fieldName + " cannot be less then 0");
            return false;
        }
        return true;
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

    private void setFieldDate(View rootView, int id, Date value) {
        if (value == null) {
            return;
        }
        Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .setText(dateFormat.format(value));
        Log.d(TAG, "date is shown");
    }

    private String getStringFromSpinner(View rootView, int id) {
        return ((Spinner) rootView.findViewById(id)).getSelectedItem().toString();
    }
    private void setStringValue(View rootView, int id, String value)
    {
        if(value!=null)
        {
            Log.d(TAG, "value is shown" +value);
            Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText(value);
           // Log.d(TAG, "value is shown pt2" +value);
        }else {
            Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText("");
        }
    }
    private void setDoubleValue(View rootView, int id, Double value)
    {
        if(!Double.isNaN(value) && value != null)
        {
            Log.d(TAG, "value2 is shown"+value);
            Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText(String.valueOf(value));
           // Log.d(TAG, "value2 is shown pt2 "+value);
        }else {
            Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText("");
        }
    }


}