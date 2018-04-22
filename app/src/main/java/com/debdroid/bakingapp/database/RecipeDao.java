package com.debdroid.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleRecipe(RecipeEntity recipeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBulkRecipes(List<RecipeEntity> recipeEntities);

    @Delete
    int deleteRecipes(List<RecipeEntity> recipeEntities);

    @Query("SELECT * FROM recipe")
    List<RecipeEntity> loadAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    RecipeEntity loadSingleRecipe(int recipeId);

    @Query("SELECT * FROM recipe")
    LiveData<List<RecipeEntity>> loadAllRecipesAsLiveData();

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    LiveData<RecipeEntity> loadSingleRecipeAsLiveData(int recipeId);
}
