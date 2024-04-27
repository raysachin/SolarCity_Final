package com.example.solarcity_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.solarcity_final.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.graph:
                    replaceFragment(new GraphFragment());
                    break;
                case R.id.model:
                    replaceFragment(new ModelFragment());
                    break;
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.about:
                    replaceFragment(new AboutFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        // Retrieve the username from the intent extras
        String username = getIntent().getStringExtra("username");

        // Pass the username to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);

        // Start a fragment transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}











//package com.example.solarcity_final;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.example.solarcity_final.databinding.ActivityMainBinding;
//
//public class MainActivity extends AppCompatActivity {
//
//    ActivityMainBinding binding;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        replaceFragment(new HomeFragment());
//        binding.bottomNavigationView.setBackground(null);
//
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.graph:
//                    replaceFragment(new GraphFragment());
//                    break;
//                case R.id.model:
//                    replaceFragment(new ModelFragment());
//                    break;
//                case R.id.home:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.notification:
//                    replaceFragment(new NotificationFragment());
//                    break;
//                case R.id.about:
//                    replaceFragment(new AboutFragment());
//                    break;
//            }
//            return true;
//        });
//
//    }
//
//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//
//    }
//}