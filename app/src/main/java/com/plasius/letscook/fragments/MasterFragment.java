package com.plasius.letscook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plasius.letscook.R;
import com.plasius.letscook.adapters.MasterAdapter;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MasterFragment extends Fragment {
    RecyclerView rv;

    boolean isDualPane;
    int currentPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master, container, false);
        rv = v.findViewById(R.id.fragment_rv_steps);
        return v;
    }


    public void loadSteps(List<Step> steps, int ingredients, int index){
        rv.setAdapter(new MasterAdapter(getActivity(), steps, ingredients, index));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.scrollToPosition(index);


    }
}
