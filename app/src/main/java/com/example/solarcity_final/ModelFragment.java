package com.example.solarcity_final;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModelFragment extends Fragment {

    EditText temperatureEditText, humidityEditText, precipitationEditText, dewPointEditText, solarPanelCapacityEditText, currentEditText, voltageEditText;
    Button predict;
    TextView resultTextView, alertTextView;

    RadioGroup radioGroupPanelClean, radioGroupCloudCover;
    RadioButton radioButtonPanelCleanYes, radioButtonPanelCleanNo, radioButtonCloudCoverYes, radioButtonCloudCoverNo;

    DatabaseReference databaseReference;

    String flaskUrl = "https://sachin2021.pythonanywhere.com/predict";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_model, container, false);

        temperatureEditText = view.findViewById(R.id.temperatureEditText);
        humidityEditText = view.findViewById(R.id.humidityEditText);
        precipitationEditText = view.findViewById(R.id.precipitationEditText);
        dewPointEditText = view.findViewById(R.id.dewPointEditText);
        solarPanelCapacityEditText = view.findViewById(R.id.solarPanelCapacityEditText);
        currentEditText = view.findViewById(R.id.currentEditText);
        voltageEditText = view.findViewById(R.id.voltageEditText);
        predict = view.findViewById(R.id.predict);
        resultTextView = view.findViewById(R.id.resultTextView);

        alertTextView = view.findViewById(R.id.alertTextView);


        radioGroupPanelClean = view.findViewById(R.id.radioGroupPanelClean);
        radioGroupCloudCover = view.findViewById(R.id.radioGroupCloudCover);
        radioButtonPanelCleanYes = view.findViewById(R.id.radioButtonPanelCleanYes);
        radioButtonPanelCleanNo = view.findViewById(R.id.radioButtonPanelCleanNo);
        radioButtonCloudCoverYes = view.findViewById(R.id.radioButtonCloudCoverYes);
        radioButtonCloudCoverNo = view.findViewById(R.id.radioButtonCloudCoverNo);


        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("features");

        // Add ValueEventListener to fetch data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get temperature from dataSnapshot and set it to EditText
                Float temperature = dataSnapshot.child("temperature").getValue(Float.class);
                if (temperature != null) {
                    temperatureEditText.setText(String.valueOf(temperature));
                }

                // Get humidity from dataSnapshot and set it to EditText
                Float humidity = dataSnapshot.child("humidity").getValue(Float.class);
                if (humidity != null) {
                    humidityEditText.setText(String.valueOf(humidity));
                }

                // Get precipitation from dataSnapshot and set it to EditText
                Float precipitation = dataSnapshot.child("precipitation").getValue(Float.class);
                if (precipitation != null) {
                    precipitationEditText.setText(String.valueOf(precipitation));
                }

                // Get dew point from dataSnapshot and set it to EditText
                Float dewPoint = dataSnapshot.child("dewpoint").getValue(Float.class);
                if (dewPoint != null) {
                    dewPointEditText.setText(String.valueOf(dewPoint));
                }

                // Get solarpanel capacity from dataSnapshot and set it to EditText
                Float solarPanelCapacity = dataSnapshot.child("solarpanelcapacity").getValue(Float.class);
                if (solarPanelCapacity != null) {
                    solarPanelCapacityEditText.setText(String.valueOf(solarPanelCapacity));
                }

                // Get solarpanel capacity from dataSnapshot and set it to EditText
                Float current = dataSnapshot.child("current").getValue(Float.class);
                if (current != null) {
                    currentEditText.setText(String.valueOf(current));
                }

                // Get solarpanel capacity from dataSnapshot and set it to EditText
                Float voltage = dataSnapshot.child("voltage").getValue(Float.class);
                if (voltage != null) {
                    voltageEditText.setText(String.valueOf(voltage));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(getContext(), "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values from EditText fields
                Map<String, String> params = new HashMap<String, String>();
                params.put("temperature", temperatureEditText.getText().toString());
                params.put("humidity", humidityEditText.getText().toString());
                params.put("dew_point", dewPointEditText.getText().toString());
                params.put("precipitation", precipitationEditText.getText().toString());

                // Send input data to Flask API
                sendPredictionRequest(params);
            }
        });

        return view;
    }

    private void sendPredictionRequest(Map<String, String> params) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, flaskUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Handle response from Flask API
                            JSONObject jsonObject = new JSONObject(response);
                            String prediction = jsonObject.getString("generation");

                            // Predicted generation
                            float capacity = Float.parseFloat(solarPanelCapacityEditText.getText().toString());
                            float predictedGeneration = Float.parseFloat(prediction);
                            predictedGeneration = ( predictedGeneration * capacity )/1000;

                            // Actual Genration
                            // Do here the calculation of the actual generation
                            float current = Float.parseFloat(currentEditText.getText().toString());
                            float voltage = Float.parseFloat(voltageEditText.getText().toString());
                            float power = (current * voltage)/1000;
                            float actualGeneration = power * 10;

                            // Radio button input
                            // Get the selected radio button values
                            String panelCleanValue = getRadioGroupValue(radioGroupPanelClean);
                            String cloudCoverValue = getRadioGroupValue(radioGroupCloudCover);

//                            resultTextView.setText("Predicted Generation: " + predictedGeneration+ "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh");
                            // Modify the if-else conditions based on radio button values
                            if (actualGeneration >= predictedGeneration) {
//                                resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n" + "Panel is Working Well");
                                resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n");
                                alertTextView.setText("Panel is Working");
                            } else {
                                // Add your condition here using panelCleanValue and cloudCoverValue
                                // For example:
                                if ("Clean".equals(panelCleanValue) && "Cloudy".equals(cloudCoverValue)) {
//                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n" + "Low Generation Expected");
                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n");
                                    alertTextView.setText("Low Generation Expected");
                                } else if ("Clean".equals(panelCleanValue) && "Sunny".equals(cloudCoverValue)) {
//                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n" + "Check for preventive maintenance");
                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n");
                                    alertTextView.setText("Check for preventive maintenance");
                                } else if ("Dirty".equals(panelCleanValue) && "Cloudy".equals(cloudCoverValue)) {
//                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n" + " Low Generation Expected");
                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n");
                                    alertTextView.setText("Low Generation Expected");
                                } else if ("Dirty".equals(panelCleanValue) && "Sunny".equals(cloudCoverValue)) {
//                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n" + "Clean the panel! Book your appointment");
                                    resultTextView.setText("Predicted Generation: " + predictedGeneration + "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh\n");
                                    alertTextView.setText("Clean the panel! Book your appointment");

                                }
                            }


//
//                            resultTextView.setText("Predicted Generation: " + predictedGeneration+ "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh");
//
//                            if(actualGeneration >= predictedGeneration){
//                                alertTextView.setText("Panel is working well");
//                            }
//                            else{
//                                // Add your condition here using panelCleanValue and cloudCoverValue
//                                // For example:
//                                if ("Yes".equals(panelCleanValue) && "No".equals(cloudCoverValue)) {
//                                    alertTextView.setText("Check for preventive maintainance");
//                                } else if("Yes".equals(panelCleanValue) && "Yes".equals(cloudCoverValue)) {
//                                    alertTextView.setText("Low Generation Expected");
//                                }
//                                else if("No".equals(panelCleanValue) && "No".equals(cloudCoverValue)){
//                                    alertTextView.setText("Clean the panel! Book your appointment");
//                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                resultTextView.setText("Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // For radio button
    private String getRadioGroupValue(RadioGroup radioGroup) {
        // Get the ID of the selected radio button in the radio group
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        // If no radio button is selected, return null or an empty string, depending on your requirement
        if (selectedRadioButtonId == -1) {
            return null;
        }

        // Find the selected radio button using its ID
        RadioButton selectedRadioButton = getView().findViewById(selectedRadioButtonId);

        // Return the text of the selected radio button
        return selectedRadioButton.getText().toString();
    }
}










//
//
//package com.example.solarcity_final;
//
//        import static android.service.controls.ControlsProviderService.TAG;
//
//        import android.annotation.SuppressLint;
//        import android.content.Intent;
//        import android.os.Bundle;
//
//        import androidx.annotation.NonNull;
//        import androidx.annotation.Nullable;
//        import androidx.fragment.app.Fragment;
//
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import com.android.volley.Request;
//        import com.android.volley.RequestQueue;
//        import com.android.volley.Response;
//        import com.android.volley.VolleyError;
//        import com.android.volley.toolbox.JsonObjectRequest;
//        import com.android.volley.toolbox.StringRequest;
//        import com.android.volley.toolbox.Volley;
//        import com.google.firebase.database.DataSnapshot;
//        import com.google.firebase.database.DatabaseError;
//        import com.google.firebase.database.DatabaseReference;
//        import com.google.firebase.database.FirebaseDatabase;
//        import com.google.firebase.database.ValueEventListener;
//
//        import org.json.JSONException;
//        import org.json.JSONObject;
//
//        import java.util.HashMap;
//        import java.util.Map;
//
//public class ModelFragment extends Fragment {
//
//    EditText temperatureEditText, humidityEditText, precipitationEditText, dewPointEditText, solarPanelCapacityEditText, currentEditText, voltageEditText;
//    Button predict;
//    TextView resultTextView;
//
//    DatabaseReference databaseReference;
//
//    String flaskUrl = "https://sachin2021.pythonanywhere.com/predict";
//
//    @SuppressLint("MissingInflatedId")
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_model, container, false);
//
//        temperatureEditText = view.findViewById(R.id.temperatureEditText);
//        humidityEditText = view.findViewById(R.id.humidityEditText);
//        precipitationEditText = view.findViewById(R.id.precipitationEditText);
//        dewPointEditText = view.findViewById(R.id.dewPointEditText);
//        solarPanelCapacityEditText = view.findViewById(R.id.solarPanelCapacityEditText);
//        currentEditText = view.findViewById(R.id.currentEditText);
//        voltageEditText = view.findViewById(R.id.voltageEditText);
//        predict = view.findViewById(R.id.predict);
//        resultTextView = view.findViewById(R.id.resultTextView);
//
//        // Initialize Firebase Database
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("features");
//
//        // Add ValueEventListener to fetch data from Firebase Realtime Database
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Get temperature from dataSnapshot and set it to EditText
//                Float temperature = dataSnapshot.child("temperature").getValue(Float.class);
//                if (temperature != null) {
//                    temperatureEditText.setText(String.valueOf(temperature));
//                }
//
//                // Get humidity from dataSnapshot and set it to EditText
//                Float humidity = dataSnapshot.child("humidity").getValue(Float.class);
//                if (humidity != null) {
//                    humidityEditText.setText(String.valueOf(humidity));
//                }
//
//                // Get precipitation from dataSnapshot and set it to EditText
//                Float precipitation = dataSnapshot.child("precipitation").getValue(Float.class);
//                if (precipitation != null) {
//                    precipitationEditText.setText(String.valueOf(precipitation));
//                }
//
//                // Get dew point from dataSnapshot and set it to EditText
//                Float dewPoint = dataSnapshot.child("dewpoint").getValue(Float.class);
//                if (dewPoint != null) {
//                    dewPointEditText.setText(String.valueOf(dewPoint));
//                }
//
//                // Get solarpanel capacity from dataSnapshot and set it to EditText
//                Float solarPanelCapacity = dataSnapshot.child("solarpanelcapacity").getValue(Float.class);
//                if (solarPanelCapacity != null) {
//                    solarPanelCapacityEditText.setText(String.valueOf(solarPanelCapacity));
//                }
//
//                // Get solarpanel capacity from dataSnapshot and set it to EditText
//                Float current = dataSnapshot.child("current").getValue(Float.class);
//                if (current != null) {
//                    currentEditText.setText(String.valueOf(current));
//                }
//
//                // Get solarpanel capacity from dataSnapshot and set it to EditText
//                Float voltage = dataSnapshot.child("voltage").getValue(Float.class);
//                if (voltage != null) {
//                    voltageEditText.setText(String.valueOf(voltage));
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle database error
//                Toast.makeText(getContext(), "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        predict.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get input values from EditText fields
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("temperature", temperatureEditText.getText().toString());
//                params.put("humidity", humidityEditText.getText().toString());
//                params.put("dew_point", dewPointEditText.getText().toString());
//                params.put("precipitation", precipitationEditText.getText().toString());
//
//                // Send input data to Flask API
//                sendPredictionRequest(params);
//            }
//        });
//
//        return view;
//    }
//
//    private void sendPredictionRequest(Map<String, String> params) {
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, flaskUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            // Handle response from Flask API
//                            JSONObject jsonObject = new JSONObject(response);
//                            String prediction = jsonObject.getString("generation");
//
//                            // Predicted generation
//                            float capacity = Float.parseFloat(solarPanelCapacityEditText.getText().toString());
//                            float predictedGeneration = Float.parseFloat(prediction);
//                            predictedGeneration = ( predictedGeneration * capacity )/1000;
//
//                            // Actual Genration
//                            // Do here the calculation of the actual generation
//                            float current = Float.parseFloat(currentEditText.getText().toString());
//                            float voltage = Float.parseFloat(voltageEditText.getText().toString());
//                            float power = (current * voltage)/1000;
//                            float actualGeneration = power * 10;
//
//
//                            resultTextView.setText("Predicted Generation: " + predictedGeneration+ "Kwh\n" + "Actual Generation: " + actualGeneration + "Kwh");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle error response
//                resultTextView.setText("Error: " + error.getMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                return params;
//            }
//        };
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
//}

