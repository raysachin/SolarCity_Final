package com.example.solarcity_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editEmail, editUsername, editPassword;
    Button saveButton;
    String usernameUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(EditProfileActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        reference.child(usernameUser).child("name").setValue(name);
        reference.child(usernameUser).child("email").setValue(email);
        reference.child(usernameUser).child("password").setValue(password);

        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showData() {
        usernameUser = getIntent().getStringExtra("username");
        String nameUser = getIntent().getStringExtra("name");
        String emailUser = getIntent().getStringExtra("email");
        String passwordUser = getIntent().getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }
}










//package com.example.solarcity_final;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class EditProfileActivity extends AppCompatActivity {
//
//
//    EditText editName, editEmail, editUsername, editPassword;
//    Button saveButton;
//    String nameUser, emailUser, usernameUser, passwordUser;
//    DatabaseReference reference;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_profile);
//
//        reference = FirebaseDatabase.getInstance().getReference("users");
//
//        editName = findViewById(R.id.editName);
//        editEmail = findViewById(R.id.editEmail);
//        editUsername = findViewById(R.id.editUsername);
//        editPassword = findViewById(R.id.editPassword);
//        saveButton = findViewById(R.id.saveButton);
//        showData();
//    }
//
//    public boolean isNameChanged(){
//        if (!nameUser.equals(editName.getText().toString())){
//            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
//            nameUser = editName.getText().toString();
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean isEmailChanged() {
//        if (!emailUser.equals(editEmail.getText().toString())){
//            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
//            emailUser = editEmail.getText().toString();
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean isPasswordChanged() {
//        if (!passwordUser.equals(editPassword.getText().toString())){
//            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
//            passwordUser = editPassword.getText().toString();
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void showData(){
//        Intent intent = getIntent();
//        nameUser = intent.getStringExtra("name");
//        emailUser = intent.getStringExtra("email");
//        usernameUser = intent.getStringExtra("username");
//        passwordUser = intent.getStringExtra("password");
//        editName.setText(nameUser);
//        editEmail.setText(emailUser);
//        editUsername.setText(usernameUser);
//        editPassword.setText(passwordUser);
//    }
//}