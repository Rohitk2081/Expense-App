package com.kehgye.expensetrackingapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Header;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {

    // API call for user login
    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // API call for user signup
    @POST("/api/auth/register")
    Call<SignupResponse> registerUser(@Body User user);

    // API call to get all expenses, requires token in the header
    @GET("api/expenses")
    Call<List<Expense>> getAllExpenses(@Header("Authorization") String authToken);

    // API call to add a new expense, requires token in the header
    @POST("api/expenses")
    Call<Expense> addExpense(@Header("Authorization") String authToken, @Body Expense expense);


    @PUT("api/expenses/{id}")
    Call<Expense> updateExpense(
            @Header("Authorization") String authToken,
            @Path("id") String expenseId,
            @Body Expense expense
    );

    // API call to delete an expense, requires token in the header
    @DELETE("api/expenses/{id}") // Ensure this matches your backend API
    Call<Void> deleteExpense(@Header("Authorization") String authtoken, @Path("id") String expenseId);
}