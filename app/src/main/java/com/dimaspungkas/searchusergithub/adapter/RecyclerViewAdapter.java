package com.dimaspungkas.searchusergithub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dimaspungkas.searchusergithub.R;
import com.dimaspungkas.searchusergithub.model.MainData;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MainData> mainDataList;

    public RecyclerViewAdapter(List<MainData> mainDataList) {
        this.mainDataList = mainDataList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final RecyclerViewHolder RecyclerVH = (RecyclerViewHolder) holder;
        RecyclerVH.username.setText(mainDataList.get(position).getLogin());
        Picasso.with(holder.itemView.getContext()).load(mainDataList.get(position)
                .getAvatar_url()).resize(200, 200).centerCrop().onlyScaleDown()
                .into(RecyclerVH.avatar);

    }

    @Override
    public int getItemCount() {
        return mainDataList.size();
    }


    public static class RecyclerViewHolder extends  RecyclerView.ViewHolder {
        TextView username;
        ImageView avatar;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
