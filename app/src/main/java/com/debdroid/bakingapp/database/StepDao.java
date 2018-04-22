package com.debdroid.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleStep(StepEntity stepEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBulkSteps(List<StepEntity> stepEntities);

    @Delete
    int deleteSteps(List<StepEntity> stepEntities);

    @Query("SELECT * FROM step")
    List<StepEntity> loadAllSteps();

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId ORDER BY step_id")
    List<StepEntity> loadStepsForRecipeId(int recipeId);

    @Query("SELECT * FROM step")
    LiveData<List<StepEntity>> loadAllStepsAsLiveData();

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId ORDER BY step_id")
    LiveData<List<StepEntity>> loadStepsForRecipeIdAsLiveData(int recipeId);

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId AND step_id = :stepId")
    LiveData<StepEntity> loadStepsForRecipeIdAndStepIdAsLiveData(int recipeId, int stepId);
}
