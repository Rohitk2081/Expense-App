package com.kehgye.expensetrackingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {

    private RecyclerView recyclerViewExpenses;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList = new ArrayList<>();
    private FloatingActionButton fabAddExpense;
    private SharedPreferences sharedPreferences;
    private ApiService apiService;
    private TextView totalAmountTextView;
    private ImageButton buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("EXPAINSE");
        toolbar.setTitleTextColor(Color.WHITE);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("ExpenseAppPrefs", MODE_PRIVATE);

        // Initialize UI components
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fabAddExpense);
        totalAmountTextView = findViewById(R.id.textViewTotalSum);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Set up RecyclerView
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(this, expenseList, this);
        recyclerViewExpenses.setAdapter(expenseAdapter);

        // Initialize Retrofit API service
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Fetch expenses from the database
        fetchExpenses();

        // Set click listener for the Floating Action Button to add new expenses
        fabAddExpense.setOnClickListener(v -> showAddExpenseDialog());

        // Set logout button click listener
        buttonLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void fetchExpenses() {
        // Retrieve the stored token from SharedPreferences
        String token = sharedPreferences.getString("authToken", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "User token not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the API with the Authorization token
        Call<List<Expense>> call = apiService.getAllExpenses("Bearer " + token);
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    expenseList.clear();
                    expenseList.addAll(response.body());
                    expenseAdapter.notifyDataSetChanged();
                    calculateTotalAmount(expenseList);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load expenses.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load expenses.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateTotalAmount(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount(); // Assuming getAmount() returns a double
        }
        totalAmountTextView.setText("Total: ₹" + total);
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);
        builder.setView(view);

        EditText editTextTitle = view.findViewById(R.id.editTextExpenseTitle);
        EditText editTextAmount = view.findViewById(R.id.editTextExpenseAmount);
        EditText editTextDescription = view.findViewById(R.id.editTextExpenseDescription);
        Button buttonAddExpense = view.findViewById(R.id.buttonAddExpense);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonAddExpense.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String amountStr = editTextAmount.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // Creating the Expense object to send
            Expense expense = new Expense(title, amount, description);
            addExpense(expense);
            dialog.dismiss();
        });
    }

    private void addExpense(Expense expense) {
        String token = sharedPreferences.getString("authToken", ""); // Retrieve the JWT token
        if (token.isEmpty()) {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Expense> call = apiService.addExpense("Bearer " + token, expense);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful() && response.body() != null) {
                    expenseList.add(response.body());
                    expenseAdapter.notifyItemInserted(expenseList.size() - 1);
                    calculateTotalAmount(expenseList);
                    Toast.makeText(MainActivity.this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("AddExpense", "Failed to add expense: " + response.message());
                    Toast.makeText(MainActivity.this, "Failed to add expense.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Log.e("AddExpense", "Error: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Failed to add expense.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditExpenseDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);
        builder.setView(view);

        EditText editTextTitle = view.findViewById(R.id.editTextExpenseTitle);
        EditText editTextAmount = view.findViewById(R.id.editTextExpenseAmount);
        EditText editTextDescription = view.findViewById(R.id.editTextExpenseDescription);
        Button buttonAddExpense = view.findViewById(R.id.buttonAddExpense);

        // Pre-populate existing values
        editTextTitle.setText(expense.getTitle());
        editTextAmount.setText(String.valueOf(expense.getAmount()));
        editTextDescription.setText(expense.getDescription());
        buttonAddExpense.setText("Update Expense");

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonAddExpense.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String amountStr = editTextAmount.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // Update the existing expense
            expense.setTitle(title);
            expense.setAmount(amount);
            expense.setDescription(description);
            updateExpense(expense);
            dialog.dismiss();
        });
    }

    private void updateExpense(Expense expenseToUpdate) {
        // Retrieve the stored token from SharedPreferences
        String token = sharedPreferences.getString("authToken", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "User token not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (expenseToUpdate.getExpenseId() == null || expenseToUpdate.getExpenseId().isEmpty()) {
            Toast.makeText(this, "Expense ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense updatedExpense = new Expense(expenseToUpdate.getUserId(), expenseToUpdate.getTitle(),
                expenseToUpdate.getAmount(), expenseToUpdate.getDescription());

        Call<Expense> call = apiService.updateExpense("Bearer " + token, expenseToUpdate.getExpenseId(), updatedExpense);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Expense updated successfully.", Toast.LENGTH_SHORT).show();
                    fetchExpenses();  // Refresh the expense list
                } else {
                    Log.e("UpdateExpense", "Failed to update expense: " + response.message());
                    Toast.makeText(MainActivity.this, "Failed to update expense.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Log.e("UpdateExpense", "Error: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Failed to update expense.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteExpense(Expense expense) {
        String token = sharedPreferences.getString("authToken", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.deleteExpense("Bearer " + token, expense.getExpenseId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    expenseList.remove(expense);
                    expenseAdapter.notifyDataSetChanged();
                    calculateTotalAmount(expenseList);
                    Toast.makeText(MainActivity.this, "Expense deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("DeleteExpense", "Failed to delete expense: " + response.message());
                    Toast.makeText(MainActivity.this, "Failed to delete expense.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DeleteExpense", "Error: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Failed to delete expense.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> logout())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout() {
        // Clear the stored authentication token from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("authToken");  // Remove the auth token
        editor.remove("userId");     // Optionally remove the userId as well
        editor.apply();

        // Redirect the user to the Login Activity
        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onEditClick(Expense expense) {
        showEditExpenseDialog(expense);
    }

    @Override
    public void onDeleteClick(Expense expense) {
        deleteExpense(expense);
    }
}