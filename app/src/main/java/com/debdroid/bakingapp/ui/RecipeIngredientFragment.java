package com.debdroid.bakingapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.ui.adapter.RecipeIngredientAdapter;
import com.debdroid.bakingapp.viewmodel.RecipeIngredientViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class RecipeIngredientFragment extends Fragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.rv_recipe_ingredient_list)
    RecyclerView recyclerView;

    private RecipeIngredientAdapter recipeIngredientAdapter;
    private Unbinder unbinder;
    private int recipeId;

    private Parcelable linearLayoutManagerState;
    private final String STATE_LINEAR_LAYOUT_MANAGER = "state_linear_layout_manager";

    public RecipeIngredientFragment() {
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
        if (savedInstanceState != null) {
            linearLayoutManagerState = savedInstanceState.getParcelable(STATE_LINEAR_LAYOUT_MANAGER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recipe_ingredient, container, false);
        unbinder = ButterKnife.bind(this,view);
        recipeId = getArguments().getInt(RecipeStepDetailActivity.RECIPE_ID_INTENT_EXTRA, -1);
        Timber.d("Recipe id" + recipeId);
        if(recipeId == -1) {
            Timber.e("Invalid recipe id " + recipeId);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recipeIngredientAdapter = new RecipeIngredientAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Set this to false for smooth scrolling of RecyclerView
        recyclerView.setNestedScrollingEnabled(false);
        //Set this to false so that activity starts the page from the beginning
        recyclerView.setFocusable(false);
        // Set this to true for better performance (adapter content is fixed)
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeIngredientAdapter);
        // Create the ViewModel
        RecipeIngredientViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeIngredientViewModel.class);
        viewModel.getIngredients(recipeId).observe(this,
                (ingredientSteps) -> {
                    recipeIngredientAdapter.swapData(ingredientSteps);
                    // Restore the position
                    if (linearLayoutManagerState != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(linearLayoutManagerState);
                        // Set it to null so new value gets set
                        linearLayoutManagerState = null;
                    }
                });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Timber.d("onSaveInstanceState called");
        linearLayoutManagerState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_LINEAR_LAYOUT_MANAGER, linearLayoutManagerState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d("onDestroyView called");
        unbinder.unbind();
    }
}
