package com.debdroid.bakingapp.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import com.debdroid.bakingapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class RecipeStepDetailActivity extends AppCompatActivity
        implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @BindView(R.id.tb_recipe_step_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bt_recipe_step_prev_button)
    Button recipePrevStepButton;
    @BindView(R.id.bt_recipe_step_next_button)
    Button recipeNextStepButton;

    public static final String RECIPE_NAME_INTENT_EXTRA = "recipe_name_extra";
    public static final String RECIPE_ID_INTENT_EXTRA = "recipe_id_extra";
    public static final String RECIPE_STEP_ID_INTENT_EXTRA = "recipe_step_id_extra";
    public static final String RECIPE_STEP_POSITION_INTENT_EXTRA = "recipe_step_pos_extra";
    public static final String RECIPE_STEP_COUNT_INTENT_EXTRA = "recipe_step_count_extra";

    private int recipeStepAdapterPosition;
    private int recipeStepCount;
    private int recipeId;
    private int stepId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always Inject before super.onCreate, otherwise it fails while orientation change
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        recipePrevStepButton.setOnClickListener((recipePrevStepButton) -> loadPrevStep());
        recipeNextStepButton.setOnClickListener((recipeNextStepButton) -> loadNextStep());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeStepAdapterPosition = getIntent().getIntExtra(RECIPE_STEP_POSITION_INTENT_EXTRA, -1);
        recipeStepCount = getIntent().getIntExtra(RECIPE_STEP_COUNT_INTENT_EXTRA, -1);
        recipeId = getIntent().getIntExtra(RECIPE_ID_INTENT_EXTRA, -1);
        stepId = getIntent().getIntExtra(RECIPE_STEP_ID_INTENT_EXTRA, -1);
        String recipeName = getIntent().getStringExtra(RECIPE_NAME_INTENT_EXTRA);

        getSupportActionBar().setTitle(recipeName);

        Timber.d("recipeStepAdapterPosition " + recipeStepAdapterPosition);
        Timber.d("recipeStepCount " + recipeStepCount);
        Timber.d("recipeName " + recipeName);
        Timber.d("recipeId " + recipeId);
        Timber.d("stepId " + stepId);

        if (recipeId < 0) {
            Timber.e("Invalid recipe id.");
            Timber.e("Recipe Id: " + recipeId);
            return;
        }

        if (recipeStepAdapterPosition == 0) {
            loadRecipeIngredientFragment();
        } else
            if(stepId > -1) {
                loadStepDetailFragment();
            } else {
                Timber.e("Invalid step id.");
                Timber.e("Step Id: " + stepId);
            }
        }

    //    @OnClick(R.id.bt_recipe_step_prev_button)
    private void loadPrevStep() {
        Timber.d("Prev step button is clicked");
        if(stepId > 0) {
            stepId--;
            loadStepDetailFragment();
        } else {
            Timber.d("Invalid step id :" + stepId);
        }
    }

    //    @OnClick(R.id.bt_recipe_step_next_button)
    private void loadNextStep() {
        Timber.d("Next step button is clicked");
        if(stepId < recipeStepCount) {
            stepId++;
            loadStepDetailFragment();
        } else {
            Timber.d("Invalid step id :" + stepId);
        }
    }

    private void loadStepDetailFragment() {
        showHideButton(Button.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);
        bundle.putInt(RECIPE_STEP_ID_INTENT_EXTRA, stepId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_recipe_step_detail_fragment_container, recipeStepDetailFragment);
        fragmentTransaction.commit();
        setButtonStatus();
    }

    private void loadRecipeIngredientFragment() {
        showHideButton(Button.GONE);

        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment();
        recipeIngredientFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_recipe_step_detail_fragment_container, recipeIngredientFragment);
        fragmentTransaction.commit();
    }

    private void showHideButton(int visibility) {
        recipePrevStepButton.setVisibility(visibility);
        recipeNextStepButton.setVisibility(visibility);
    }

    private void setButtonStatus() {
        if(stepId == 0) {
            Toast.makeText(this, "This is the first step",Toast.LENGTH_SHORT).show();
            recipePrevStepButton.setEnabled(false);
            recipePrevStepButton.setAlpha(0.4f);
        } else if(stepId == recipeStepCount - 1) {
            Toast.makeText(this, "This is the last step",Toast.LENGTH_SHORT).show();
            recipeNextStepButton.setEnabled(false);
            recipeNextStepButton.setAlpha(0.4f);
        } else {
            recipePrevStepButton.setEnabled(true);
            recipePrevStepButton.setAlpha(1.0f);
            recipeNextStepButton.setAlpha(1.0f);
            recipeNextStepButton.setEnabled(true);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }
}
