package com.mit.harshada_gujar_chefbot;

import com.google.gson.annotations.SerializedName;

public class ExtendedIngredient {

    @SerializedName("id")
    private int id;

    @SerializedName("original")
    private String original;      // full text of ingredient, e.g., "1 cup sugar"

    @SerializedName("name")
    private String name;          // ingredient name, e.g., "sugar"

    @SerializedName("amount")
    private double amount;        // quantity

    @SerializedName("unit")
    private String unit;          // unit of measurement

    // Getter methods
    public int getId() { return id; }
    public String getOriginal() { return original; }
    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getUnit() { return unit; }
}
