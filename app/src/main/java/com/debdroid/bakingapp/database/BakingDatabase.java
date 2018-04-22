package com.debdroid.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(version = 1, entities = {RecipeEntity.class, IngredientEntity.class, StepEntity.class})
public abstract class BakingDatabase extends RoomDatabase {
    //Zero argument Recipe @Dao abstract method
    public abstract RecipeDao getRecipeDao();

    //Zero argument Ingredient @Dao abstract method
    public abstract IngredientDao getIngredientDao();

    //Zero argument Step @Dao abstract method
    public abstract StepDao getStepDao();

}
