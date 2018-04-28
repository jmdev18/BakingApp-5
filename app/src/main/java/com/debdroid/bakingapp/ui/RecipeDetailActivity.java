package com.debdroid.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.debdroid.bakingapp.R;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

import static com.debdroid.bakingapp.ui.RecipeStepDetailActivity.RECIPE_STEP_ID_INTENT_EXTRA;

public class RecipeDetailActivity extends AppCompatActivity
        implements HasSupportFragmentInjector,
        RecipeDetailFragment.OnRecipeDetailFragmentInteractionListener {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @BindView(R.id.tb_recipe_detail_toolbar)
    Toolbar toolbar;
    @BindBool(R.bool.tablet_mode)
    boolean isTabletMode;

    int recipeId;
    String recipeName;

    public static final String RECIPE_ID_INTENT_EXTRA = "recipe_id_extra";
    public static final String RECIPE_NAME_INTENT_EXTRA = "recipe_name_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always Inject before super.onCreate, otherwise it fails while orientation change
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeId = getIntent().getIntExtra(RECIPE_ID_INTENT_EXTRA, -1);
        recipeName = getIntent().getStringExtra(RECIPE_NAME_INTENT_EXTRA);

        Timber.d("onCreate:recipeId - " + recipeId);
        Timber.d("onCreate:recipeName - " + recipeName);

        if (!recipeName.isEmpty() && recipeName != null) {
            getSupportActionBar().setTitle(recipeName);
        }

        if (recipeId == -1) {
            Timber.e("Invalid recipeId : " + recipeId);
            return;
        }

        // Fragment is created automatically while orientation change, so create it if it's initial state
        if (savedInstanceState == null) {
            loadRecipeDetailPhone();
        }
    }

    private void loadRecipeDetailPhone() {
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);
        bundle.putString(RECIPE_NAME_INTENT_EXTRA, recipeName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_recipe_detail_fragment_container, recipeDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRecipeDetailFragmentInteraction(int adapterPosition, int stepId, int stepCount) {
        Timber.d("Adapter position " + adapterPosition);
        Timber.d("stepId " + stepId);
        Timber.d("stepCount " + stepCount);

        if (isTabletMode && adapterPosition == 0) { // Load the ingredient
            Bundle bundle = new Bundle();
            bundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);
            bundle.putString(RECIPE_NAME_INTENT_EXTRA, recipeName);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment();
            recipeIngredientFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fl_recipe_step_detail_fragment_container, recipeIngredientFragment);
            fragmentTransaction.commit();
        } else if (isTabletMode && adapterPosition > 0) { // Load the stepdetail
            Bundle recipeStepDetailBundle = new Bundle();
            recipeStepDetailBundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);
            recipeStepDetailBundle.putInt(RECIPE_STEP_ID_INTENT_EXTRA, stepId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(recipeStepDetailBundle);
            fragmentTransaction.replace(R.id.fl_recipe_step_detail_fragment_container, recipeStepDetailFragment);
            fragmentTransaction.commit();
        } else { // Load new Activity for phone
            Intent recipeStepDetailActivity = new Intent(this, RecipeStepDetailActivity.class);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_STEP_POSITION_INTENT_EXTRA, adapterPosition);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_STEP_COUNT_INTENT_EXTRA, stepCount);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_NAME_INTENT_EXTRA, recipeName);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_ID_INTENT_EXTRA, recipeId);
            recipeStepDetailActivity.putExtra(RECIPE_STEP_ID_INTENT_EXTRA, stepId);
            startActivity(recipeStepDetailActivity);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }
}
