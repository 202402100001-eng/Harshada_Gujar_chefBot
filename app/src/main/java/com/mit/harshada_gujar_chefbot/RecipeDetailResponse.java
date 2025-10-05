package com.mit.harshada_gujar_chefbot;

import java.util.List;

public class RecipeDetailResponse {
    private int id;
    private String title;
    private int readyInMinutes;
    private int servings;
    private String image;
    private String instructions;
    private List<ExtendedIngredient> extendedIngredients;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getReadyInMinutes() { return readyInMinutes; }
    public int getServings() { return servings; }
    public String getImage() { return image; }
    public String getInstructions() { return instructions; }
    public List<ExtendedIngredient> getExtendedIngredients() { return extendedIngredients; }

    public static class ExtendedIngredient {
        private double amount;
        private String unit;
        private String name;
        private String original;  // full text like "1 cup sugar"

        public double getAmount() { return amount; }
        public String getUnit() { return unit; }
        public String getName() { return name; }
        public String getOriginal() { return original; } // ✅ Added
    }
}
