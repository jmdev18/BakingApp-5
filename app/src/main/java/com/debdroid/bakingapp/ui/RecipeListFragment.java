package com.debdroid.bakingapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.ui.adapter.RecipeListAdapter;
import com.debdroid.bakingapp.viewmodel.RecipeListViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecipeListFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecipeListFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    Picasso picasso;
    @BindView(R.id.rv_recipe_list)
    RecyclerView recyclerView;
    @BindBool(R.bool.tablet_mode)
    boolean isTabletMode;
    @BindDimen(R.dimen.single_recipe_image_width)
    float recipeCardWidth;

    private RecipeListAdapter recipeAdapter;
    private Unbinder unbinder;
    private OnRecipeListFragmentInteractionListener mListener;
    private Parcelable linearLayoutManagerState;
    private final String STATE_LINEAR_LAYOUT_MANAGER = "state_linear_layout_manager";

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach called");
        AndroidSupportInjection.inject(this);

        if (context instanceof OnRecipeListFragmentInteractionListener) {
            mListener = (OnRecipeListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate called");
        if (savedInstanceState != null) {
            linearLayoutManagerState = savedInstanceState.getParcelable(STATE_LINEAR_LAYOUT_MANAGER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView called");
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated called");
        recipeAdapter = new RecipeListAdapter(picasso,
                ((recipeId, recipeName, vh) -> mListener.onRecipeListFragmentInteraction(recipeId, recipeName)));
        if(isTabletMode) { // For table use the gridlayout
            int spanCount = determineNumOfColumns();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        //Set this to false for smooth scrolling of RecyclerView
        recyclerView.setNestedScrollingEnabled(false);
        //Set this to false so that activity starts the page from the beginning
        recyclerView.setFocusable(false);
        // Set this to true for better performance (adapter content is fixed)
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeAdapter);
        // Create the ViewModel
        RecipeListViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeListViewModel.class);
        viewModel.getRecipes().observe(this,
                (recipeEntities) -> {
                    recipeAdapter.swapData(recipeEntities);
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

    /**
     * This method dynamically determines the number of column for the RecyclerView based on
     * screen width and density
     * @return number of calculated columns
     */
    private int determineNumOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columnWidth = (int) (recipeCardWidth / displayMetrics.density);
        int numOfColumn = (int) dpWidth / columnWidth;
        return numOfColumn;
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
    public interface OnRecipeListFragmentInteractionListener {
        void onRecipeListFragmentInteraction(int recipeId, String recipeName);
    }
}
