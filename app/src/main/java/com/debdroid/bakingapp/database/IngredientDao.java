package com.debdroid.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleIngredient(IngredientEntity ingredientEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBulkIngredients(List<IngredientEntity> ingredientEntities);

    @Delete
    int deleteIngredients(List<IngredientEntity> ingredientEntities);

    @Query("SELECT * FROM ingredient")
    List<IngredientEntity> loadAllIngredients();

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipeId")
    List<IngredientEntity> loadIngredientsForRecipeId(int recipeId);

    @Query("SELECT * FROM ingredient")
    LiveData<List<IngredientEntity>> loadAllIngredientsAsLiveData();

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipeId")
    LiveData<List<IngredientEntity>> loadIngredientsForRecipeIdAsLiveData(int recipeId);
}
