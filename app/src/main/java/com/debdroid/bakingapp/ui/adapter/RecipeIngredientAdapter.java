package com.debdroid.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.database.IngredientEntity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder> {

    private List<IngredientEntity> ingredientEntityList = new ArrayList<>();

    public RecipeIngredientAdapter() {
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_ingredient_item)
        TextView ingredientItemTextView;

        private RecipeIngredientViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
//            view.setOnClickListener(v -> {
//                int adapterPosition = getAdapterPosition();
//                int stepId = -1;
//                // Again due to the "Recipe Ingredients" hack, read "position - 1"
//                if(adapterPosition > 0) {
//                    stepId = ingredientEntityList.get(adapterPosition - 1).stepId;
//                }
//                int stepCount = ingredientEntityList.size();
//                recipeDetailAdapterOnClickHandler.onRecipeItemClick(adapterPosition, stepId,
//                        stepCount, this);
//            });
        }
    }

    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recipe_ingredient,
                parent, false);
        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        NumberFormat numberFormat = new DecimalFormat("0.#");
        String quantity = numberFormat.format(ingredientEntityList.get(position).quantity);

        String ingredientItem = ingredientEntityList.get(position).ingredient +
                " - " +
                quantity +
                " " +
                ingredientEntityList.get(position).measure;
        ingredientItem.trim();
        holder.ingredientItemTextView.setText(ingredientItem);
    }

    @Override
    public int getItemCount() {
        if (ingredientEntityList.isEmpty()) {
            return 0;
        } else {
            return ingredientEntityList.size();
        }
    }

    public void swapData(List<IngredientEntity> ingredientEntities) {
        if(ingredientEntities != null) ingredientEntityList = ingredientEntities;
        notifyDataSetChanged();
    }
}
