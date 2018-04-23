package com.debdroid.bakingapp.ui;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.utility.CommonUtility;
import com.debdroid.bakingapp.viewmodel.RecipeStepDetailViewModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    Picasso picasso;
    @BindView(R.id.tv_recipe_step_instruction)
    TextView recipeInstructionTextView;
    @BindView(R.id.recipe_step_exo_player_view)
    PlayerView exoPlayerView;
    @BindView(R.id.recipe_step_exo_player_fallback_image)
    ImageView exoPlayerFallbackImageView;
    @BindView(R.id.recipe_step_media_container)
    FrameLayout mediaContainerFrameLayout;

    private SimpleExoPlayer simpleExoPlayer;
    private Dialog exoPlayerFullScreenDialog;
    private boolean exoPlayerFullscreen = false;
    private ImageView exoPlayerFullScreenIcon;
    private FrameLayout exoPlayerFullScreenButtonContainer;
    private int exoPlayerResumeWindow;
    private long exoPlayerResumePosition;
    private Unbinder unbinder;
    private int recipeId;
    private int stepId;

    private final String STATE_EXO_PLAYER_RESUME_WINDOW = "exo_player_resume_window";
    private final String STATE_EXO_PLAYER_RESUME_POSITION = "exo_player_resume_position";
    private final String STATE_EXO_PLAYER_PLAYER_FULLSCREEN = "exo_player_player_full_screen";

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach is called");
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate is called");
        if (savedInstanceState != null) {
            Timber.d("onCreate: saved state");
            exoPlayerResumeWindow = savedInstanceState.getInt(STATE_EXO_PLAYER_RESUME_WINDOW);
            exoPlayerResumePosition = savedInstanceState.getLong(STATE_EXO_PLAYER_RESUME_POSITION);
            exoPlayerFullscreen = savedInstanceState.getBoolean(STATE_EXO_PLAYER_PLAYER_FULLSCREEN);
            Timber.d("onCreate: exoPlayerResumePosition -> " + exoPlayerResumePosition);
        } else {
            Timber.d("onCreate: initial state");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView is called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        recipeId = getArguments().getInt(RecipeStepDetailActivity.RECIPE_ID_INTENT_EXTRA, -1);
        stepId = getArguments().getInt(RecipeStepDetailActivity.RECIPE_STEP_ID_INTENT_EXTRA, -1);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated is called");
        if (recipeId < 0 || stepId < 0) {
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
//                    recipeInstructionTextView.setText(stepEntity.description + "\n ForTesting: VideoURL-> " + stepEntity.videoURL);
                    String videoUrl = stepEntity.videoURL;
                    String thumbnailUrl = stepEntity.thumbnailURL;

                    // If we have videoUrl then load the video
                    if (!videoUrl.isEmpty() && videoUrl != null) {
                        mediaContainerFrameLayout.setVisibility(FrameLayout.VISIBLE); // Make it visible for video irrespective of orientation
                        exoPlayerFallbackImageView.setVisibility(ImageView.GONE);
                        // Initialize the ExoPlayer
                        initializeExoPlayer(Uri.parse(stepEntity.videoURL));

                        // Show the image only in portrait mode
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        mediaContainerFrameLayout.setVisibility(FrameLayout.VISIBLE); // Make it visible always in portrait mode
                        if (!thumbnailUrl.isEmpty() && thumbnailUrl != null) { // If we do not have the videoUrl then check if we have a thumbnail image
                            exoPlayerFallbackImageView.setVisibility(ImageView.VISIBLE);
                            picasso.load(thumbnailUrl)
                                    .placeholder(CommonUtility.getFallbackImageId(recipeId - 1))
                                    .error(CommonUtility.getFallbackImageId(recipeId - 1))
                                    .into(exoPlayerFallbackImageView);

                        } else { // Else fallback to recipe image from local store
                            exoPlayerFallbackImageView.setVisibility(ImageView.VISIBLE);
                            exoPlayerFallbackImageView.setImageResource(CommonUtility.getFallbackImageId(recipeId - 1));
                        }
                    } else {
                        mediaContainerFrameLayout.setVisibility(FrameLayout.GONE);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume is called");
        if (exoPlayerView != null) {
            initFullscreenDialog();
            initFullscreenButton();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause is called");
        if (exoPlayerView != null && simpleExoPlayer != null) {
            exoPlayerResumeWindow = simpleExoPlayer.getCurrentWindowIndex();
            exoPlayerResumePosition = Math.max(0, simpleExoPlayer.getContentPosition());
            Timber.d("onPause:exoPlayerResumeWindow-> " + exoPlayerResumeWindow);
            Timber.d("onPause:exoPlayerResumePosition-> " + exoPlayerResumePosition);
        }

        if (exoPlayerFullScreenDialog != null) {
            exoPlayerFullScreenDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Timber.d("onSaveInstanceState is called");
        outState.putInt(STATE_EXO_PLAYER_RESUME_WINDOW, exoPlayerResumeWindow);
        outState.putLong(STATE_EXO_PLAYER_RESUME_POSITION, exoPlayerResumePosition);
        outState.putBoolean(STATE_EXO_PLAYER_PLAYER_FULLSCREEN, exoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView is called");
        super.onDestroyView();
        unbinder.unbind();
        releaseExoPlayer();
    }

    private void initializeExoPlayer(Uri mp4VideoUri) {
        Timber.d("initializeExoPlayer is called");
        if (simpleExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            // Bind the player to the view
            exoPlayerView.setPlayer(simpleExoPlayer);

            // Prepare the MediaSource.
            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // Create a user agent using ExoPlayer util library
            String userAgent = Util.getUserAgent(getActivity(),
                    getActivity().getResources().getString(R.string.app_name));
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    userAgent, bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mp4VideoUri);

            // Prepare the player with the source.
            simpleExoPlayer.prepare(videoSource);
            // Start as soon as it's ready
            simpleExoPlayer.setPlayWhenReady(true);

            // Check if we have resume position, if yes then start from there
            boolean haveResumePosition = exoPlayerResumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                Timber.d("Setting player to window: " + exoPlayerResumeWindow + " and to position: " + exoPlayerResumePosition);
                simpleExoPlayer.seekTo(exoPlayerResumeWindow, exoPlayerResumePosition);
            } else {
                Timber.d("ExoPlayer has no resume position saved");
            }

            // Check if it's landscape mode, if yes then always show the player in full screen
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                openFullscreenDialog();
                // Disable the fullscreen exit button, so that in landscape mode video is always fullscreen
                exoPlayerFullScreenIcon.setVisibility(ImageView.GONE);
                // Set this to false to return to normal behaviour
                exoPlayerFullscreen = false;
            } else {
                exoPlayerFullScreenIcon.setVisibility(ImageView.VISIBLE);
            }
        }
    }

    private void releaseExoPlayer() {
        Timber.d("releaseExoPlayer is called");
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    /**
     * Following section is to handle ExoPlayer in full screen
     * This is done based on the following guide
     * https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
     */
    private void initFullscreenDialog() {
        Timber.d("initFullscreenDialog is called");
        exoPlayerFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (exoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        Timber.d("openFullscreenDialog is called");
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        exoPlayerFullScreenDialog.addContentView(exoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        exoPlayerFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_fullscreen_skrink));
        exoPlayerFullscreen = true;
        exoPlayerFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        Timber.d("closeFullscreenDialog is called");
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        int exoPlayerHeight = getResources().getDimensionPixelOffset(R.dimen.recipe_step_exo_player_height);
//        ((FrameLayout) getActivity().findViewById(R.id.recipe_step_media_container))
        mediaContainerFrameLayout.addView(exoPlayerView, ViewGroup.LayoutParams.MATCH_PARENT, exoPlayerHeight);
        exoPlayerFullscreen = false;
        exoPlayerFullScreenDialog.dismiss();
        exoPlayerFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_fullscreen_expand));
    }

    private void initFullscreenButton() {
        Timber.d("initFullscreenButton is called");
        PlayerControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
        exoPlayerFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        exoPlayerFullScreenButtonContainer = controlView.findViewById(R.id.exo_fullscreen_button_container);
        exoPlayerFullScreenButtonContainer.setOnClickListener(v -> {
            if (!exoPlayerFullscreen)
                openFullscreenDialog();
            else
                closeFullscreenDialog();
        });
    }
}
