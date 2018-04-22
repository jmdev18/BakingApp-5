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

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class RecipeDetailActivity extends AppCompatActivity
        implements HasSupportFragmentInjector,
                    RecipeDetailFragment.OnRecipeDetailFragmentInteractionListener {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @BindView(R.id.tb_recipe_detail_toolbar)
    Toolbar toolbar;
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

        getSupportActionBar().setTitle(recipeName);

        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_recipe_detail_fragment_container, recipeDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        Timber.d("supportFragmentInjector is called");
        return supportFragmentInjector;
    }

    @Override
    public void onRecipeDetailFragmentInteraction(int adapterPosition, int stepId, int stepCount) {
        Timber.d("Adapter position " + adapterPosition);
        Timber.d("stepId " + stepId);
        Timber.d("stepCount " + stepCount);

        // This is due to hack! First item of the adapter is "Recipe Ingredient"
//        if(adapterPosition > 0) {
            Intent recipeStepDetailActivity = new Intent(this, RecipeStepDetailActivity.class);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_STEP_POSITION_INTENT_EXTRA, adapterPosition);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_STEP_COUNT_INTENT_EXTRA, stepCount);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_NAME_INTENT_EXTRA, recipeName);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_ID_INTENT_EXTRA, recipeId);
            recipeStepDetailActivity.putExtra(RecipeStepDetailActivity.RECIPE_STEP_ID_INTENT_EXTRA, stepId);
            startActivity(recipeStepDetailActivity);
//        }
    }
}
