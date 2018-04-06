package com.plasius.letscook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.plasius.letscook.MasterActivity;
import com.plasius.letscook.R;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder>{
    List<Step> steps;
    Context context;
    View currentSelected;

    private static OnItemClickListener listener;

    public MasterAdapter(Context c, List<Step> stepList, int ingredientCount){
        steps= stepList;
        context = c;

        listener = (MasterActivity) c;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_shortDescription.setText(steps.get(position).getShortDescription());
        holder.tv_longDescription.setText(steps.get(position).getDescription());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);

                if(context.getResources().getBoolean(R.bool.isTablet)) {
                    if (currentSelected != null)
                        currentSelected.setBackgroundColor(Color.WHITE);

                    v.setBackgroundColor(Color.LTGRAY);
                    currentSelected = v;
                }
            }
        });

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cardView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_shortDescription;
        TextView tv_longDescription;
        View cardView;

        private ViewHolder(View itemView) {
            super(itemView);
            tv_shortDescription = itemView.findViewById(R.id.fragment_master_tv_shortDescription);
            tv_longDescription = itemView.findViewById(R.id.fragment_master_tv_longDescription);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
