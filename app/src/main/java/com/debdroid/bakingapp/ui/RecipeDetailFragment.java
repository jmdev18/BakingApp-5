package com.debdroid.bakingapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.debdroid.bakingapp.ui.adapter.RecipeDetailAdapter;
import com.debdroid.bakingapp.viewmodel.RecipeDetailViewModel;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnRecipeDetailFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecipeDetailFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    SharedPreferences sharedPreferences;
    @BindView(R.id.rv_recipe_step_description_list)
    RecyclerView recyclerView;
    @BindBool(R.bool.tablet_mode)
    boolean isTabletMode;

    private RecipeDetailAdapter recipeDetailAdapter;
    private Unbinder unbinder;
    private OnRecipeDetailFragmentInteractionListener mListener;
    private int recipeId;
    private String recipeName;

    private Parcelable linearLayoutManagerState;
    private final String STATE_LINEAR_LAYOUT_MANAGER = "state_linear_layout_manager";

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);

        if (context instanceof OnRecipeDetailFragmentInteractionListener) {
            mListener = (OnRecipeDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate called");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Timber.d("onCreate: restore State");
            linearLayoutManagerState = savedInstanceState.getParcelable(STATE_LINEAR_LAYOUT_MANAGER);
        } else {
            Timber.d("onCreate: initial State");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        recipeId = getArguments().getInt(RecipeDetailActivity.RECIPE_ID_INTENT_EXTRA, -1);
        recipeName = getArguments().getString(RecipeDetailActivity.RECIPE_NAME_INTENT_EXTRA);

        Timber.d("onCreateView:recipeName - "+recipeName);

        if(recipeId == -1) {
            Timber.e("Invalid Recipe id: " + recipeId);
            return null;
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recipeDetailAdapter = new RecipeDetailAdapter(isTabletMode,
                ((pos, stepId, stepCount, vh) ->
                        mListener.onRecipeDetailFragmentInteraction(pos, stepId, stepCount)));
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
        recyclerView.setAdapter(recipeDetailAdapter);
        // Create the ViewModel
        RecipeDetailViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeDetailViewModel.class);
        viewModel.getSteps(recipeId).observe(this,
                (stepEntities) -> {
                    recipeDetailAdapter.swapData(stepEntities);
                    // Restore the position
                    if (linearLayoutManagerState != null) {
                        Timber.d("linearLayoutManagerState is NOT null");
                        recyclerView.getLayoutManager().onRestoreInstanceState(linearLayoutManagerState);
                        // Set it to null so new value gets set
                        linearLayoutManagerState = null;
                    } else {
                        Timber.d("linearLayoutManagerState is NULL");
                    }
                });
        // Store the id in SharedPreference
        saveCurrentRecipeIdInSharedPreference();
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

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach called");
        mListener = null;
    }

    private void saveCurrentRecipeIdInSharedPreference() {
        Timber.d("saveCurrentRecipeIdInSharedPreference is called");
        Timber.d("saveCurrentRecipeIdInSharedPreference:recipeId - "+recipeId);
        Timber.d("saveCurrentRecipeIdInSharedPreference:recipeName - "+recipeName);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.preference_recipe_id_key), recipeId);
        editor.putString(getString(R.string.preference_recipe_name_key), recipeName);
        editor.apply(); // Write asynchronously
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeDetailFragmentInteractionListener {
        void onRecipeDetailFragmentInteraction(int adapterPosition, int stepId, int stepCount);
    }
}
