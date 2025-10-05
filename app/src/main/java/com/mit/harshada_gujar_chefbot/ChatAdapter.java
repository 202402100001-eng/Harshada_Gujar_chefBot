package com.mit.harshada_gujar_chefbot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;
    private static final int TYPE_RECIPE = 2;

    private List<Message> messageList;
    private Context context;

    public ChatAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = messageList.get(position);
        if (msg.isUser()) {
            return TYPE_USER;
        } else if (msg.getRecipe() != null) {
            return TYPE_RECIPE;
        } else {
            return TYPE_BOT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == TYPE_RECIPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recipe_message, parent, false);
            return new RecipeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot_message, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = messageList.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).textMessage.setText(msg.getText());
        } else if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).textMessage.setText(msg.getText());
        } else if (holder instanceof RecipeViewHolder) {
            Recipe recipe = msg.getRecipe();
            ((RecipeViewHolder) holder).recipeTitle.setText(recipe.getTitle());

            // Load recipe image
            Glide.with(context)
                    .load(recipe.getImage())
                    .into(((RecipeViewHolder) holder).recipeImage);

            // On click → open RecipeDetailActivity
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("recipe_id", recipe.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.user_message);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        BotViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.bot_message);
        }
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        ImageView recipeImage;
        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}
