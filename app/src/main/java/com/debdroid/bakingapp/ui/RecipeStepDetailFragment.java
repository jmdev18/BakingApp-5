package com.debdroid.bakingapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.viewmodel.RecipeStepDetailViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.tv_recipe_step_instruction)
    TextView recipeInstructionTextView;

    private Unbinder unbinder;
    private int recipeId;
    private int stepId;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this,view);
        recipeId = getArguments().getInt(RecipeStepDetailActivity.RECIPE_ID_INTENT_EXTRA, -1);
        stepId = getArguments().getInt(RecipeStepDetailActivity.RECIPE_STEP_ID_INTENT_EXTRA, -1);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(recipeId < 0 || stepId < 0) {
            Timber.e("Invalid recipe id or step id.");
            Timber.e("Recipe Id: " + recipeId);
            Timber.e("Step Id: " + stepId);
            return;
        }
        RecipeStepDetailViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeStepDetailViewModel.class);
        viewModel.getStep(recipeId, stepId).observe(this,
                stepEntity -> {
                    recipeInstructionTextView.setText(stepEntity.description);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
