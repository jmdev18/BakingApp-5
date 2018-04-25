package com.debdroid.bakingapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.utility.CommonUtility;
import com.debdroid.bakingapp.viewmodel.RecipeWidgetViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class RecipeAppWidgetService extends IntentService {

    @Inject
    RecipeWidgetViewModel recipeWidgetViewModel;

    public static final String ACTION_RETRIEVE_INGREDIENT_LIST = "com.debdroid.bakingapp.action.retrieve_ingredient_list";
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";
    public static final String EXTRA_RECIPE_NAME = "extra_recipe_name";

    public RecipeAppWidgetService() {
        super("RecipeAppWidgetService");
    }

    /**
     * Starts this service to perform RetrieveIngredientList action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void retrieveIngredientList(Context context, int recipeId, String recipeName) {
        Intent intent = new Intent(context, RecipeAppWidgetService.class);
        intent.setAction(ACTION_RETRIEVE_INGREDIENT_LIST);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_RECIPE_NAME, recipeName);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        /** Always call this before super **/
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RETRIEVE_INGREDIENT_LIST.equals(action)) {
                final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                        -1);
                final String recipeName = intent.getStringExtra(EXTRA_RECIPE_NAME);
                handleRetrieveIngredientList(recipeId, recipeName);
            } else {
                Timber.e("Invalid action - " + action);
            }
        }
    }

    private void handleRetrieveIngredientList(int recipeId, String recipeName) {
        List<IngredientEntity> ingredientEntityList = recipeWidgetViewModel.getIngredientsAsList(recipeId);

        String ingredients = CommonUtility.createFriendlyIngredientList(ingredientEntityList);
        Timber.d("readFromSharedPreference:ingredients - " + ingredients);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidgetProvider.class));

        // Update all widgets
        RecipeAppWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds ,
                ingredients, recipeId, recipeName);
    }
}
