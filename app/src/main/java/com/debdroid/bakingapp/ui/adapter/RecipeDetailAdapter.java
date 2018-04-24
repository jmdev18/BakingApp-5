package com.debdroid.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.database.StepEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeStepViewHolder> {

    private List<StepEntity> stepEntityList = new ArrayList<>();
    private RecipeDetailAdapterOnClickHandler recipeDetailAdapterOnClickHandler;

    public RecipeDetailAdapter(RecipeDetailAdapterOnClickHandler clickHandler) {
        recipeDetailAdapterOnClickHandler = clickHandler;
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_step_short_description)
        TextView recipeStepShortDescription;

        private RecipeStepViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                int stepId = -1;
                // Again due to the "Recipe Ingredients" hack, read "position - 1"
                if(adapterPosition > 0) {
                    stepId = stepEntityList.get(adapterPosition - 1).stepId;
                }
                int stepCount = stepEntityList.size();
                recipeDetailAdapterOnClickHandler.onRecipeItemClick(adapterPosition, stepId,
                        stepCount, this);
            });
        }
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recipe_step_description,
                parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        if(position == 0) {
            holder.recipeStepShortDescription.setText("Recipe Ingredients");
        } else {
            // Again due to the "Recipe Ingredients" hack, read position - 1
            holder.recipeStepShortDescription
                    // Remove trailing "."
                    .setText(stepEntityList.get(position - 1).shortDescription.replace(".",""));
        }
    }

    @Override
    public int getItemCount() {
        if (stepEntityList.isEmpty()) {
            return 0;
        } else {
            // Doing a bit of hack
            // Add 1 to the size because we want to show "Recipe Ingredients" at the top which is
            // not part of Step
            return stepEntityList.size() + 1;
        }
    }

    public void swapData(List<StepEntity> stepEntities) {
        if(stepEntities != null) stepEntityList = stepEntities;
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recipeDetailAdapterOnClickHandler = null;
    }

    /**
     * This is the interface which will be implemented by HostActivity
     */
    public interface RecipeDetailAdapterOnClickHandler {
        void onRecipeItemClick(int position, int stepId, int stepCount,
                               RecipeDetailAdapter.RecipeStepViewHolder vh);
    }
}

