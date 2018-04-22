package com.debdroid.bakingapp.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.debdroid.bakingapp.dagger.RecipeCustomScope;
import com.debdroid.bakingapp.database.IngredientDao;
import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.database.RecipeDao;
import com.debdroid.bakingapp.database.RecipeEntity;
import com.debdroid.bakingapp.database.StepDao;
import com.debdroid.bakingapp.database.StepEntity;
import com.debdroid.bakingapp.datamodel.Recipe;
import com.debdroid.bakingapp.retrofit.BakingJsonApiService;
import com.debdroid.bakingapp.utility.DatabaseUtility;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@RecipeCustomScope.BakingApplicationScope
//Let Dagger know that this class should be constructed only once
public class RecipeRepository {
    private BakingJsonApiService bakingJsonApiService;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;
    private static boolean isFirstTimeDataLoad = true;

    @Inject
    public RecipeRepository(BakingJsonApiService bakingJsonApiService, RecipeDao recipeDao,
                            IngredientDao ingredientDao, StepDao stepDao) {
        this.bakingJsonApiService = bakingJsonApiService;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.stepDao = stepDao;
    }

    public LiveData<List<RecipeEntity>> getRecipes() {
        // Initiate the load api
        // Load the data only once
        if(isFirstTimeDataLoad) {
            loadRecipeListApi();
        }
        // Return current data from the database
        return recipeDao.loadAllRecipesAsLiveData();
    }

    public LiveData<List<StepEntity>> getSteps(int recipeId) {
        // If program reaches at this stage then the data is already loaded, so no need to
        // kick-off the data load; just return the steps
        return stepDao.loadStepsForRecipeIdAsLiveData(recipeId);
    }

    public LiveData<StepEntity> getStep(int recipeId, int stepId) {
        // If program reaches at this stage then the data is already loaded, so no need to
        // kick-off the data load; just return the steps
        return stepDao.loadStepsForRecipeIdAndStepIdAsLiveData(recipeId, stepId);
    }

    public LiveData<List<IngredientEntity>> getIngredients(int recipeId) {
        // If program reaches at this stage then the data is already loaded, so no need to
        // kick-off the data load; just return the steps
        return ingredientDao.loadIngredientsForRecipeIdAsLiveData(recipeId);
    }

    private void loadRecipeListApi() {
        Call<List<Recipe>> recipeListDataCall = bakingJsonApiService.getRecipeList();
        recipeListDataCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {
                    insertData(response);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.e("Error happened -> " + t.toString());
            }
        });

    }

    private void insertData(final Response<List<Recipe>> response) {
        @SuppressLint("StaticFieldLeak")
        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Recipe> recipeList = response.body();
                if(recipeList != null) {
                    List<RecipeEntity> recipeEntityList = DatabaseUtility.getPriceEntityList(recipeList);
                    List<IngredientEntity> ingredientEntityList = DatabaseUtility.getIngredientEntityList(recipeList);
                    List<StepEntity> stepEntityList = DatabaseUtility.getStepEntityList(recipeList);

                    recipeDao.insertBulkRecipes(recipeEntityList);
                    ingredientDao.insertBulkIngredients(ingredientEntityList);
                    stepDao.insertBulkSteps(stepEntityList);
                    isFirstTimeDataLoad = false; // Ensure load happened only once
                } else {
                    Timber.d("Json response is null");
                }
                return null;
            }
        };
        asyncTask.execute();
    }
}
