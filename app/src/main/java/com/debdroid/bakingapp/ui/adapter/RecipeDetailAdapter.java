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

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeStepViewHolder> {

    private List<StepEntity> stepEntityList = new ArrayList<>();
    private RecipeDetailAdapterOnClickHandler recipeDetailAdapterOnClickHandler;
    private static int currPosition = -1; // Define as static so that it gets retained along with class (orientation saved!)
    private boolean isTabletMode;

    public RecipeDetailAdapter(boolean isTabletMode, RecipeDetailAdapterOnClickHandler clickHandler) {
        recipeDetailAdapterOnClickHandler = clickHandler;
        this.isTabletMode = isTabletMode;
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_step_short_description)
        TextView recipeStepShortDescription;
        @BindColor(R.color.accent)
        int colorAccent;
        @BindColor(R.color.primary_background)
        int colorPrimaryBackground;

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

                // Store the position of the view clicked - need this to highlight clicked item for tablet only
                if(isTabletMode) {
                    currPosition = adapterPosition;
                    notifyDataSetChanged(); // Call this to re-populate recyclerview, it helps to remove previously colored item
                }
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
            holder.recipeStepShortDescription
                    .setText(holder.recipeStepShortDescription.getRootView()
                            .getContext().getString(R.string.recipe_ingredients_title));
        } else {
            // Again due to the "Recipe Ingredients" hack, read position - 1
            holder.recipeStepShortDescription
                    // Remove trailing "."
                    .setText(stepEntityList.get(position - 1).shortDescription.replace(".",""));
        }

        // If it's tablet mode then set the background color of the clicked item
        if(isTabletMode) {
            if (position == currPosition) {
                holder.itemView.setBackgroundColor(holder.colorAccent);
            } else {
                holder.itemView.setBackgroundColor(holder.colorPrimaryBackground);
            }
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

