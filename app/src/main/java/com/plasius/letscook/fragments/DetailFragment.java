package com.plasius.letscook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plasius.letscook.R;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;
import com.plasius.letscook.utils.PersistenceUtils;
import com.plasius.letscook.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DetailFragment extends Fragment {
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
        if(getArguments().getInt("currentStep") == 0){
            List<Ingredient> ingredientList = getArguments().getParcelableArrayList("ingredients");
            //TODO video
            // Step currentStep = getActivity().getIntent().getParcelableExtra("step");

            ((TextView)view.findViewById(R.id.fragment_detail_tv_shortDescription)).setText("Ingredients:");
            ((TextView)view.findViewById(R.id.fragment_detail_tv_description)).setText(StringUtils.getStringFromIngredientList(ingredientList));
        }else{
            int currentStep = getArguments().getInt("currentStep", 1);

            List<Step> stepList = getArguments().getParcelableArrayList("steps");
            ((TextView)view.findViewById(R.id.fragment_detail_tv_shortDescription)).setText(stepList.get(currentStep).getShortDescription());
            ((TextView)view.findViewById(R.id.fragment_detail_tv_description)).setText(stepList.get(currentStep).getDescription());

        }

        return view;
    }
}
