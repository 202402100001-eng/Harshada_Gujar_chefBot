package com.mit.harshada_gujar_chefbot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private Handler handler;
    private FirebaseAuth mAuth;

    private static final String API_KEY = "eeab8836c1b14f3bbbda352155aacb93";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        handler = new Handler();

        topAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.action_sign_out) {
                signOutUser();
            }
            return true;
        });

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString().trim();
            if (!text.isEmpty()) {
                addMessage(new Message(text, true));
                messageEditText.setText("");

                String lower = text.toLowerCase();

                // Ingredient-based detection
                if (lower.startsWith("i have") || lower.contains("with ")) {
                    String ingredients = lower
                            .replace("i have", "")
                            .replace("with", "")
                            .trim();
                    fetchRecipesByIngredients(ingredients);
                }
                // Default → keyword search
                else {
                    fetchRecipes(lower);
                }
            }
        });

    }

    // 🔹 Keyword search
    private void fetchRecipes(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<RecipeResponse> call = apiService.searchRecipes(query, 10, true, API_KEY);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();
                    if (recipes == null || recipes.isEmpty()) {
                        addMessage(new Message("Bot: Sorry, no recipes found 😞"));
                        return;
                    }

                    addMessage(new Message("Bot: Here are some recipes you can try 👇"));

                    for (Recipe r : recipes) {
                        messageList.add(new Message(r)); // 👈 recipe card
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                    }
                } else {
                    addMessage(new Message("Bot: Error, could not get recipes."));
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                addMessage(new Message("Bot: Failed to connect to API 😞"));
            }
        });
    }

    // 🔹 Ingredient-based search
    private void fetchRecipesByIngredients(String ingredients) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Recipe>> call = apiService.searchByIngredients(ingredients, 10, API_KEY);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body();
                    if (recipes.isEmpty()) {
                        addMessage(new Message("Bot: Sorry, no recipes found 😞"));
                        return;
                    }

                    addMessage(new Message("Bot: Based on your ingredients, here are some dishes 🍲"));

                    for (Recipe r : recipes) {
                        messageList.add(new Message(r)); // 👈 recipe card
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                    }

                } else {
                    addMessage(new Message("Bot: Couldn’t fetch recipes right now."));
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                addMessage(new Message("Bot: Failed to connect to API 😞"));
            }
        });
    }

    private void addMessage(Message message) {
        messageList.add(message);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
    }

    private void signOutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }
}
