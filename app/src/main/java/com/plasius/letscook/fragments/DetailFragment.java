package com.plasius.letscook.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.plasius.letscook.R;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;
import com.plasius.letscook.utils.PersistenceUtils;
import com.plasius.letscook.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DetailFragment extends Fragment {
    SimpleExoPlayer exoPlayer;
    SimpleExoPlayerView playerView;

    public static DetailFragment newInstance(List<Step> steps, List<Ingredient> ingredients, int currentStep) {
        DetailFragment f = new DetailFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelableArrayList("steps", new ArrayList<>(steps));
        args.putParcelableArrayList("ingredients", new ArrayList<>(ingredients));
        args.putInt("currentStep", currentStep);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!= null)
            getArguments().putLong("currentTime", savedInstanceState.getLong("currentTime", 0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);;
        if(!getActivity().getResources().getBoolean(R.bool.isTablet)){
            v.findViewById(R.id.fragment_detail_nav).setVisibility(View.VISIBLE);
        }
        return bindView(v);

    }

    private View bindView(View view){
        int currentStep = getArguments().getInt("currentStep");
        if(currentStep == 0){
            List<Ingredient> ingredientList = getArguments().getParcelableArrayList("ingredients");
            List<Step> stepList = getArguments().getParcelableArrayList("steps");

            initPlayer(view, stepList.get(currentStep).getVideoURL());
            ((TextView)view.findViewById(R.id.fragment_detail_tv_shortDescription)).setText(R.string.ingredients);
            ((TextView)view.findViewById(R.id.fragment_detail_tv_description)).setText(StringUtils.getStringFromIngredientList(ingredientList));

        }else{
            List<Step> stepList = getArguments().getParcelableArrayList("steps");

            if(stepList.get(currentStep).getVideoURL().isEmpty()){
                if(!stepList.get(currentStep).getThumbnailURL().isEmpty() && !stepList.get(currentStep).getThumbnailURL().endsWith(".mp4")){
                    ImageView imageView = view.findViewById(R.id.fragment_detail_iv_thumbnail);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.get().load(stepList.get(currentStep).getThumbnailURL()).into(imageView);
                }
            }else {
                initPlayer(view, stepList.get(currentStep).getVideoURL());
            }

            ((TextView)view.findViewById(R.id.fragment_detail_tv_shortDescription)).setText(stepList.get(currentStep).getShortDescription());
            ((TextView)view.findViewById(R.id.fragment_detail_tv_description)).setText(stepList.get(currentStep).getDescription());

        }

        return view;
    }

    private void initPlayer(View view, String url){
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector(), new DefaultLoadControl());
        playerView = view.findViewById(R.id.fragment_detail_videoview);
        playerView.setVisibility(View.VISIBLE);
        playerView.setPlayer(exoPlayer);



        String userAgent = Util.getUserAgent(getActivity(), "recipevideo");
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url), new DefaultDataSourceFactory(getActivity(), userAgent),
                new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.seekTo(getArguments().getLong("currentTime", 0));

        playerView.hideController();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer(){
        if(exoPlayer==null)
            return;


        getArguments().putLong("currentTime", exoPlayer.getCurrentPosition());

        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer= null;
    }

}
