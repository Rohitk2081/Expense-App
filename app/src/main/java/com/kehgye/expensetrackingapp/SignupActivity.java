package com.kehgye.expensetrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextSignupUsername, editTextSignupEmail, editTextSignupPassword, editTextSignupConfirmPassword;
    private Button buttonSignup;
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextSignupUsername = findViewById(R.id.editTextSignupUsername);
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextSignupConfirmPassword = findViewById(R.id.editTextSignupConfirmPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Set click listener for the sign-up button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // Set click listener for the login text, to navigate to the login screen
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void signup() {
        String username = editTextSignupUsername.getText().toString().trim();
        String email = editTextSignupEmail.getText().toString().trim();
        String password = editTextSignupPassword.getText().toString().trim();
        String confirmPassword = editTextSignupConfirmPassword.getText().toString().trim();

        // Check if any field is empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make your API call here using Retrofit
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<SignupResponse> call = apiService.registerUser(new User(username, email, password));

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                    // Redirect to login or main activity after successful signup
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish(); // Close the SignupActivity so user can't come back with the back button
                } else {
                    Toast.makeText(SignupActivity.this, "Signup Failed! " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.e("SignupError", t.getMessage()); // Add this line to log the error
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}