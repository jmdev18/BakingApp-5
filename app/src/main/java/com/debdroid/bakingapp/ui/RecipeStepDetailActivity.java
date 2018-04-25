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

    private final String STATE_STEP_ID = "state_step_id";
    private final String STATE_IS_STEP_FRAGMENT = "state_is_step_fragment";

    private int recipeStepAdapterPosition;
    private int recipeStepCount;
    private int recipeId;
    private String recipeName;
    private int stepId;
    private boolean isStepFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate is called");
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
        recipeName = getIntent().getStringExtra(RECIPE_NAME_INTENT_EXTRA);

        if (!recipeName.isEmpty() && recipeName != null) {
            getSupportActionBar().setTitle(recipeName);
        }

        // If this is the first time then retrieve the stepId from Intent otherwise used the save value
        if (savedInstanceState == null) {
            stepId = getIntent().getIntExtra(RECIPE_STEP_ID_INTENT_EXTRA, -1);
        } else {
            stepId = savedInstanceState.getInt(STATE_STEP_ID);
            isStepFragment = savedInstanceState.getBoolean(STATE_IS_STEP_FRAGMENT);
        }

        // Handle button visibility and status while orientation change
        if (isStepFragment) {
            showHideButton(Button.VISIBLE);
            setButtonStatus();
        } else {
            showHideButton(Button.GONE);
        }

        Timber.d("recipeStepAdapterPosition " + recipeStepAdapterPosition);
        Timber.d("recipeStepCount " + recipeStepCount);
        Timber.d("recipeName " + recipeName);
        Timber.d("recipeId " + recipeId);
        Timber.d("stepId " + stepId);

        if (recipeId < 0) {
            Timber.e("Invalid recipe id: " + recipeId);
            return;
        }

        // First item in the adapter is Ingredient, so load Ingredients if adapter position is 0
        // Fragment is aut generated while orientation change, so create it if this is the first time
        if (recipeStepAdapterPosition == 0 && savedInstanceState == null) {
            loadRecipeIngredientFragment();
        } else if (stepId > -1 && savedInstanceState == null) {
            loadStepDetailFragment();
        } else {
            Timber.e("Invalid step id: " + stepId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume is called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("onPause is called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy is called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Timber.d("onSaveInstanceState is called");
        outState.putInt(STATE_STEP_ID, stepId);
        outState.putBoolean(STATE_IS_STEP_FRAGMENT, isStepFragment);
        super.onSaveInstanceState(outState);
    }

    private void loadPrevStep() {
        Timber.d("Prev step button is clicked");
        if (stepId > 0) {
            stepId--;
            loadStepDetailFragment();
        } else {
            Timber.e("Invalid step id :" + stepId);
        }
    }

    private void loadNextStep() {
        Timber.d("Next step button is clicked");
        if (stepId < recipeStepCount) {
            stepId++;
            loadStepDetailFragment();
        } else {
            Timber.e("Invalid step id :" + stepId);
        }
    }

    private void loadStepDetailFragment() {
        Timber.d("loadStepDetailFragment is called");
        showHideButton(Button.VISIBLE);
        isStepFragment = true;

        Timber.d("Fragment does not exist, create onw");
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
        Timber.d("loadRecipeIngredientFragment is called");
        showHideButton(Button.GONE);
        isStepFragment = false;

        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID_INTENT_EXTRA, recipeId);
        bundle.putString(RECIPE_NAME_INTENT_EXTRA, recipeName);

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
        if (stepId == 0) {
            Toast.makeText(this, "This is the first step", Toast.LENGTH_SHORT).show();
            recipePrevStepButton.setEnabled(false);
            recipePrevStepButton.setAlpha(0.4f);
        } else if (stepId == recipeStepCount - 1) {
            Toast.makeText(this, "This is the last step", Toast.LENGTH_SHORT).show();
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
