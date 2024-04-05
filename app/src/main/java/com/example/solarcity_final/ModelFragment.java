package com.example.solarcity_final;

import static android.service.controls.ControlsProviderService.TAG;

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

    EditText temperatureEditText, humidityEditText, precipitationEditText, dewPointEditText;
    Button predict;
    TextView resultTextView;

    DatabaseReference databaseReference;

    String flaskUrl = "https://sachin2021.pythonanywhere.com/predict";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_model, container, false);

        temperatureEditText = view.findViewById(R.id.temperatureEditText);
        humidityEditText = view.findViewById(R.id.humidityEditText);
        precipitationEditText = view.findViewById(R.id.precipitationEditText);
        dewPointEditText = view.findViewById(R.id.dewPointEditText);
        predict = view.findViewById(R.id.predict);
        resultTextView = view.findViewById(R.id.resultTextView);

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
                            resultTextView.setText("Generation: " + prediction + "Kwh");
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
}
