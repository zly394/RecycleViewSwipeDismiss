package com.zly.recycleviewswipedismiss;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private SampleAdapter mSampleAdapter;
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        initView();
    }

    private void initView() {
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 20; i++) {
            mData.add("RecyclerView" + i);
        }
        mSampleAdapter = new SampleAdapter(this, mData);
        rvList.setAdapter(mSampleAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                final String item = mData.get(pos);
                mData.remove(pos);
                mSampleAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                String text;
                if (direction == ItemTouchHelper.RIGHT) {
                    text = "删除一项";
                } else {
                    text = "延迟一项";
                }
                Snackbar.make(viewHolder.itemView, text, Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mData.add(pos, item);
                                mSampleAdapter.notifyItemInserted(pos);
                            }
                        }).show();
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((SampleAdapter.ItemViewHolder) viewHolder).vItem);
                ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundColor(Color.TRANSPARENT);
                ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.GONE);
                ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.GONE);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected(((SampleAdapter.ItemViewHolder) viewHolder).vItem);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, recyclerView, ((SampleAdapter.ItemViewHolder) viewHolder).vItem, dX, dY, actionState, isCurrentlyActive);
                if (Math.abs(dX) < 200) { // 降低设置背景的频率
                    if (dX > 0) {
                        ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundResource(R.color.colorDone);
                        ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.VISIBLE);
                        ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.GONE);
                    } else {
                        ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundResource(R.color.colorSchedule);
                        ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.VISIBLE);
                        ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((SampleAdapter.ItemViewHolder) viewHolder).vItem, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(rvList);
    }
}
