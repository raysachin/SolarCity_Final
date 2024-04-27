
package com.example.solarcity_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    // Declare all the necessary variables
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize all the declared variables
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Add onclick listener on signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if username already exists
                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Save data to Firebase
                            HelperClass helperClass = new HelperClass(name, email, username, password);
                            reference.child(username).setValue(helperClass);
                            Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignupActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Lastly add onClickListener on login Redirect text view
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}









//package com.example.solarcity_final;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class SignupActivity extends AppCompatActivity {
//    // Declare all the necessary variable
//    EditText signupName, signupEmail, signupUsername, signupPassword;
//    TextView loginRedirectText;
//    Button signupButton;
//    FirebaseDatabase database;
//    DatabaseReference reference;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        // Initialize all the declared variable
//        signupName = findViewById(R.id.signup_name);
//        signupEmail = findViewById(R.id.signup_email);
//        signupUsername = findViewById(R.id.signup_username);
//        signupPassword = findViewById(R.id.signup_password);
//        loginRedirectText = findViewById(R.id.loginRedirectText);
//        signupButton = findViewById(R.id.signup_button);
//
//        // Add onclick listener on signup button
//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                database = FirebaseDatabase.getInstance();
//                reference = database.getReference("users");
//
//                // users is refered as a parent name in firebase database
//                String name = signupName.getText().toString();
//                String email = signupEmail.getText().toString();
//                String username = signupUsername.getText().toString();
//                String password = signupPassword.getText().toString();
//
//                // Call the helper class and mention the associated variables
//                HelperClass helperClass = new HelperClass(name, email, username, password);
//                reference.child(username).setValue(helperClass);
//
//
//                Toast.makeText(SignupActivity.this, "You have signup Successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        // Lastly add on clickListener on login Redirect text view and where we will create an intent that will lead user from signup page to login page
//        loginRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//}