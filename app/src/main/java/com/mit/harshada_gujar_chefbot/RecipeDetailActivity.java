package com.mit.harshada_gujar_chefbot;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeTitle, recipeTime, recipeServings, recipeIngredients, recipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail2);

        recipeImage = findViewById(R.id.recipe_image);
        recipeTitle = findViewById(R.id.recipe_title);
        recipeTime = findViewById(R.id.recipe_time);
        recipeServings = findViewById(R.id.recipe_servings);
        recipeIngredients = findViewById(R.id.recipe_ingredients);
        recipeInstructions = findViewById(R.id.recipe_instructions);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId != -1) {
            fetchRecipeDetails(recipeId);
        } else {
            Toast.makeText(this, "Invalid recipe", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchRecipeDetails(int recipeId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String apiKey = "eeab8836c1b14f3bbbda352155aacb93";

        Call<RecipeDetailResponse> call = apiService.getRecipeInformation(recipeId, apiKey);

        call.enqueue(new Callback<RecipeDetailResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailResponse> call, Response<RecipeDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetailResponse recipe = response.body();

                    recipeTitle.setText(recipe.getTitle());
                    recipeTime.setText("⏱ " + recipe.getReadyInMinutes() + " mins");
                    recipeServings.setText("👥 Servings: " + recipe.getServings());

                    Glide.with(RecipeDetailActivity.this)
                            .load(recipe.getImage() != null ? recipe.getImage() : R.drawable.placeholder)
                            .into(recipeImage);

                    StringBuilder ingredientsText = new StringBuilder();
                    for (RecipeDetailResponse.ExtendedIngredient ing : recipe.getExtendedIngredients()) {
                        ingredientsText.append("• ")
                                .append(ing.getAmount())
                                .append(" ")
                                .append(ing.getUnit())
                                .append(" ")
                                .append(ing.getName())
                                .append("\n");
                    }
                    recipeIngredients.setText(ingredientsText.toString());

                    String instructions = recipe.getInstructions();
                    if (instructions != null && !instructions.isEmpty()) {
                        recipeInstructions.setText(HtmlCompat.fromHtml(instructions, HtmlCompat.FROM_HTML_MODE_LEGACY));
                    } else {
                        recipeInstructions.setText("No instructions available.");
                    }

                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Error loading recipe details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponse> call, Throwable t) {
                Toast.makeText(RecipeDetailActivity.this, "Failed to load recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
