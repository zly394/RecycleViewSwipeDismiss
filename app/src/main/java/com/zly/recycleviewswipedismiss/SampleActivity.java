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
        // new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) 表示不可以拖拽，只可以在左右方向滑动
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 不处理拖拽事件回调
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 处理滑动事件回调
                final int pos = viewHolder.getAdapterPosition();
                final String item = mData.get(pos);
                mData.remove(pos);
                mSampleAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                String text;
                // 判断方向，进行不同的操作
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
                // 释放View时回调，清除背景颜色，隐藏图标
                // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
                getDefaultUIUtil().clearView(((SampleAdapter.ItemViewHolder) viewHolder).vItem);
                ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundColor(Color.TRANSPARENT);
                ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.GONE);
                ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.GONE);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                // 当viewHolder的滑动或拖拽状态改变时回调
                if (viewHolder != null) {
                    // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
                    getDefaultUIUtil().onSelected(((SampleAdapter.ItemViewHolder) viewHolder).vItem);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // ItemTouchHelper的onDraw方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的下方
                // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
                getDefaultUIUtil().onDraw(c, recyclerView, ((SampleAdapter.ItemViewHolder) viewHolder).vItem, dX, dY, actionState, isCurrentlyActive);
                if (dX > 0) { // 向左滑动是的提示
                    ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundResource(R.color.colorDone);
                    ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.VISIBLE);
                    ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.GONE);
                }
                if (dX < 0) { // 向右滑动时的提示
                    ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundResource(R.color.colorSchedule);
                    ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.VISIBLE);
                    ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // ItemTouchHelper的onDrawOver方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的上方
                // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((SampleAdapter.ItemViewHolder) viewHolder).vItem, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(rvList);
    }
}
