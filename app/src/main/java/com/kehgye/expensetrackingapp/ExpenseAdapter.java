package com.kehgye.expensetrackingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenseList;
    private Context context;
    private OnExpenseClickListener listener;

    // Constructor
    public ExpenseAdapter(Context context, List<Expense> expenseList, OnExpenseClickListener listener) {
        this.context = context;
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.textViewTitle.setText(expense.getTitle());
        holder.textViewAmount.setText("₹" + expense.getAmount());
        holder.textViewDescription.setText(expense.getDescription());
//        holder.textViewDate.setText(expense.getDate());

        String originalDate = expense.getDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        try {
            Date date = originalFormat.parse(originalDate);
            if (date != null) {
                String formattedDate = desiredFormat.format(date);
                holder.textViewDate.setText(formattedDate);
            } else {
                holder.textViewDate.setText(originalDate); // Fallback in case parsing fails
            }
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textViewDate.setText(originalDate); // Fallback in case of exception
        }

        // Set Click Listeners for Edit and Delete
        holder.buttonEditExpense.setOnClickListener(v -> {
            if (expense.getExpenseId() == null) {
                Toast.makeText(context, "Error: Expense ID is null", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onEditClick(expense); // Pass expense to the listener
        });

        holder.buttonDeleteExpense.setOnClickListener(v -> {
            if (expense.getExpenseId() == null) {
                Toast.makeText(context, "Error: Expense ID is null", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onDeleteClick(expense);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAmount, textViewDescription, textViewDate;
        Button buttonEditExpense, buttonDeleteExpense;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonEditExpense = itemView.findViewById(R.id.buttonEditExpense);
            buttonDeleteExpense = itemView.findViewById(R.id.buttonDeleteExpense);
        }
    }

    // Interface for click listener
    public interface OnExpenseClickListener {
        void onEditClick(Expense expense);
        void onDeleteClick(Expense expense);
    }
}