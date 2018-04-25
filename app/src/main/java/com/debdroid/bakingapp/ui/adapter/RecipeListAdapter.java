package com.debdroid.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.database.RecipeEntity;
import com.debdroid.bakingapp.utility.CommonUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private List<RecipeEntity> recipeEntityList = new ArrayList<>();
    private Picasso picasso;
    private RecipeListAdapterOnClickHandler recipeListAdapterOnClickHandler;

    public RecipeListAdapter(Picasso picasso, RecipeListAdapterOnClickHandler clickHandler) {
        recipeListAdapterOnClickHandler = clickHandler;
        this.picasso = picasso;
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_single_recipe_image) ImageView recipeImage;
        @BindView(R.id.tv_single_recipe_name) TextView recipeName;

        private RecipeListViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                int recipeId = recipeEntityList.get(adapterPosition).id;
                String recipeName = recipeEntityList.get(adapterPosition).name;
                Timber.d("RecipeListViewHolder:Recipe id - "+recipeId);
                Timber.d("RecipeListViewHolder:Recipe name - "+recipeName);
                recipeListAdapterOnClickHandler.onRecipeItemClick(recipeId, recipeName,this);
            });
        }
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recipe_item,
                parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        holder.recipeName.setText(recipeEntityList.get(position).name);
        String imagePath = recipeEntityList.get(position).image;
        if(imagePath == null || imagePath.isEmpty()) {
            picasso.load(CommonUtility.getFallbackImageId(position)).into(holder.recipeImage);
        } else {
        picasso.load(recipeEntityList.get(position).image)
                    .placeholder(CommonUtility.getFallbackImageId(position))
                    .error(CommonUtility.getFallbackImageId(position))
                    .into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        if (recipeEntityList.isEmpty()) {
            return 0;
        } else {
            return recipeEntityList.size();
        }
    }

    public void swapData(List<RecipeEntity> recipeEntities) {
        if(recipeEntities != null) recipeEntityList = recipeEntities;
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recipeListAdapterOnClickHandler = null;
    }

    /**
     * This is the interface which will be implemented by HostActivity
     */
    public interface RecipeListAdapterOnClickHandler {
        void onRecipeItemClick(int recipeId, String recipeName, RecipeListViewHolder vh);
    }

}
