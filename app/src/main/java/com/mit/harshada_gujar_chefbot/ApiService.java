package com.mit.harshada_gujar_chefbot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // 🔹 Search recipes by keywords
    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            @Query("query") String query,
            @Query("number") int number,
            @Query("addRecipeInformation") boolean addInfo,
            @Query("apiKey") String apiKey
    );

    // 🔹 Get detailed recipe info by recipe ID
    @GET("recipes/{id}/information")
    Call<RecipeDetailResponse> getRecipeInformation(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );

    // 🔹 Search recipes by ingredients
    @GET("recipes/findByIngredients")
    Call<List<Recipe>> searchByIngredients(
            @Query("ingredients") String ingredients,  // comma separated
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );
}
