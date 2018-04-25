package com.debdroid.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.ui.RecipeDetailActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class BakingAppWidgetProvider extends AppWidgetProvider {


    @Inject
    SharedPreferences sharedPreferences;

    private int recipeId;
    private String recipeName;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("onReceive is called");
        /** Always call this before super **/
        AndroidInjection.inject(this, context);
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                           String ingredientsList, int recipeId, String recipeName) {
        Timber.d("updateAppWidget is called");

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.RECIPE_ID_INTENT_EXTRA, recipeId);
            intent.putExtra(RecipeDetailActivity.RECIPE_NAME_INTENT_EXTRA, recipeName);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_appwidget_layout);
            views.setOnClickPendingIntent(R.id.tv_widget_recipe_ingredient, pendingIntent);

            // Set the data
            views.setTextViewText(R.id.tv_widget_recipe_header, recipeName);
            views.setTextViewText(R.id.tv_widget_recipe_ingredient, ingredientsList);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("onUpdate is called");
        Timber.d("onUpdate:recipeId : " + recipeId);
        Timber.d("onUpdate:recipeName : " + recipeName);

        readFromSharedPreference(context);

        BakingAppWidgetService.retrieveIngredientList(context,recipeId,recipeName);
    }

    private void readFromSharedPreference(Context context) {
        Timber.d("readFromSharedPreference is called");
        recipeId = sharedPreferences.getInt(context.getResources().getString(R.string.preference_recipe_id_key),-1);
        recipeName = sharedPreferences.getString(context.getResources().getString(R.string.preference_recipe_name_key),"");
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, String ingredientsList, int recipeId, String recipeName) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredientsList, recipeId, recipeName);
        }
    }
}
