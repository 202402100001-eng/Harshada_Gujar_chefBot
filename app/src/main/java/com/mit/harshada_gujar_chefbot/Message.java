package com.mit.harshada_gujar_chefbot;

public class Message {
    private String text;
    private boolean isUser;
    private Recipe recipe;

    // For text messages
    public Message(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
        this.recipe = null;
    }

    // For bot text only (shortcut)
    public Message(String text) {
        this.text = text;
        this.isUser = false;
        this.recipe = null;
    }

    // For recipe messages
    public Message(Recipe recipe) {
        this.recipe = recipe;
        this.isUser = false;
        this.text = null;
    }

    public String getText() { return text; }
    public boolean isUser() { return isUser; }
    public Recipe getRecipe() { return recipe; }
}
