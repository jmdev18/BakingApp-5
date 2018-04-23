package com.debdroid.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.debdroid.bakingapp.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity
        implements HasSupportFragmentInjector,
        RecipeListFragment.OnRecipeListFragmentInteractionListener {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always Inject before super.onCreate, otherwise it fails while orientation change
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        Timber.d("onCreate is called");
        setContentView(R.layout.activity_recipe_list);
//        ButterKnife.bind(this);

        // Fragment is created while orientation change, so load it if it's the initial state
        if(savedInstanceState == null) {
            loadRecipeListFragment();
        }
    }

    private void loadRecipeListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        fragmentTransaction.replace(R.id.fl_recipe_list_fragment_container, recipeListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    @Override
    public void onRecipeListFragmentInteraction(int recipeId, String recipeName) {
        Timber.d("Recipe id"+recipeId);
        Intent recipeDetailActivity = new Intent(this, RecipeDetailActivity.class);
        recipeDetailActivity.putExtra(RecipeDetailActivity.RECIPE_ID_INTENT_EXTRA, recipeId);
        recipeDetailActivity.putExtra(RecipeDetailActivity.RECIPE_NAME_INTENT_EXTRA, recipeName);
        startActivity(recipeDetailActivity);
    }
}
