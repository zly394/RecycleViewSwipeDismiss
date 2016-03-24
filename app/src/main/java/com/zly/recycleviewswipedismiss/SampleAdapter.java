package com.zly.recycleviewswipedismiss;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhuleiyue on 16/3/24.
 */
public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mData;

    public SampleAdapter(Context context, ArrayList<String> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.list_item_sample, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).tvItem.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        View vBackground;
        View vItem;
        TextView tvItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            vBackground = itemView.findViewById(R.id.rl_background);
            vItem = itemView.findViewById(R.id.ll_item);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
