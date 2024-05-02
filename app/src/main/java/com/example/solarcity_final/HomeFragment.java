package com.example.solarcity_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    TextView profileName, profileEmail, profileUsername, profilePassword;
    TextView titleName, titleUsername;
    Button editProfile, logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize TextViews
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profilePassword = view.findViewById(R.id.profilePassword);
        profilePassword.setVisibility(View.GONE);
        titleName = view.findViewById(R.id.titleName);
        titleUsername = view.findViewById(R.id.titleUsername);

        // Initialize Buttons
        editProfile = view.findViewById(R.id.editButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Read data from Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear previous data to prevent duplication
                titleName.setText("");
                titleUsername.setText("");
                profileName.setText("");
                profileEmail.setText("");
                profileUsername.setText("");
                profilePassword.setText("");

                // Iterate through each user data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    // Update UI with the latest data
                    titleName.setText(name);
                    titleUsername.setText(username);
                    profileName.setText(name);
                    profileEmail.setText(email);
                    profileUsername.setText(username);
                    profilePassword.setText(password);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        // Button click listener to pass user data to EditProfileActivity
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        // Button click listener to handle logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();

                // Navigate back to the login page
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    private void passUserData() {
        String userUsername = profileUsername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    // Start EditProfileActivity and pass user data
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }
}











//package com.example.solarcity_final;
//
//import static android.service.controls.ControlsProviderService.TAG;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//public class HomeFragment extends Fragment {
//
//    TextView profileName, profileEmail, profileUsername, profilePassword;
//    TextView titleName, titleUsername;
//    Button editProfile;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        // Initialize TextViews
//        profileName = view.findViewById(R.id.profileName);
//        profileEmail = view.findViewById(R.id.profileEmail);
//        profileUsername = view.findViewById(R.id.profileUsername);
//        profilePassword = view.findViewById(R.id.profilePassword);
//        profilePassword.setVisibility(View.GONE);
//        titleName = view.findViewById(R.id.titleName);
//        titleUsername = view.findViewById(R.id.titleUsername);
//
//        // Initialize Button
//        editProfile = view.findViewById(R.id.editButton);
//
//        // Read data from Firebase Database
//
//         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Clear previous data to prevent duplication
//                titleName.setText("");
//                titleUsername.setText("");
//                profileName.setText("");
//                profileEmail.setText("");
//                profileUsername.setText("");
//                profilePassword.setText("");
//
//                // Iterate through each user data
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String name = snapshot.child("name").getValue(String.class);
//                    String email = snapshot.child("email").getValue(String.class);
//                    String username = snapshot.child("username").getValue(String.class);
//                    String password = snapshot.child("password").getValue(String.class);
//
//                    // Update UI with the latest data
//                    titleName.setText(name);
//                    titleUsername.setText(username);
//                    profileName.setText(name);
//                    profileEmail.setText(email);
//                    profileUsername.setText(username);
//                    profilePassword.setText(password);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
//        // Button click listener to pass user data to EditProfileActivity
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                passUserData();
//            }
//        });
//
//        return view;
//    }
//
//    private void passUserData() {
//        String userUsername = profileUsername.getText().toString().trim();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
//
//        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
//                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
//                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
//                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
//
//                    // Start EditProfileActivity and pass user data
//                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
//                    intent.putExtra("name", nameFromDB);
//                    intent.putExtra("email", emailFromDB);
//                    intent.putExtra("username", usernameFromDB);
//                    intent.putExtra("password", passwordFromDB);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });
//    }
//}












//
//
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeFragment#} factory method to
// * create an instance of this fragment.
// */
//public class HomeFragment extends Fragment {
//
//    TextView profileName, profileEmail, profileUsername, profilePassword;
//    TextView titleName, titleUsername;
//    Button editProfile;
//
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout, fragment_home);
////
////
////    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        // Initialize TextViews
//        profileName = view.findViewById(R.id.profileName);
//        profileEmail = view.findViewById(R.id.profileEmail);
//        profileUsername = view.findViewById(R.id.profileUsername);
//        profilePassword = view.findViewById(R.id.profilePassword);
//        titleName = view.findViewById(R.id.titleName);
//        titleUsername = view.findViewById(R.id.titleUsername);
//
//        // Initialize Button
//        editProfile = view.findViewById(R.id.editButton);
//
//        // Retrieve data from arguments
//        Bundle args = getArguments();
//        if (args != null) {
//            String nameUser = args.getString("name");
//            String emailUser = args.getString("email");
//            String usernameUser = args.getString("username");
//            String passwordUser = args.getString("password");
//
//            titleName.setText(nameUser);
//            titleUsername.setText(usernameUser);
//            profileName.setText(nameUser);
//            profileEmail.setText(emailUser);
//            profileUsername.setText(usernameUser);
//            profilePassword.setText(passwordUser);
//        }
//
//        // Inflate the layout for this fragment
////        return view;
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
////    public void showAllUserData(){
////        Intent intent = getIntent();
////        String nameUser = intent.getStringExtra("name");
////        String emailUser = intent.getStringExtra("email");
////        String usernameUser = intent.getStringExtra("username");
////        String passwordUser = intent.getStringExtra("password");
////
////        titleName.setText(nameUser);
////        titleUsername.setText(usernameUser);
////        profileName.setText(nameUser);
////        profileEmail.setText(emailUser);
////        profileUsername.setText(usernameUser);
////        profilePassword.setText(passwordUser);
////    }
//
//}