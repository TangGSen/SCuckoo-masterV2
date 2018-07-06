package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/30 20:50
 * Des    : RecycleCommonAdapter 通用Adapter
 */

public abstract class RecycleCommonAdapter<D> extends RecyclerView.Adapter<SViewHolder> {
    //布局layoutId
    private int layoutId;

    private List<D> mData;
    private final LayoutInflater mLayoutInflater;

    public static final Executor EXECUTOR = Executors.newCachedThreadPool();
    public Handler mHandler = new Handler(Looper.getMainLooper());

    public RecycleCommonAdapter(Context context, List<D> data, int layoutId) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mData = data;
        this.layoutId = layoutId;
    }

    @Override
    public SViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(layoutId, parent, false);
        return new SViewHolder(view);
    }

    private OnItemOnclickLinstener linstener;

    public void setOnItemClickLinstener(OnItemOnclickLinstener linstener) {
        this.linstener = linstener;
    }

    @Override
    public void onBindViewHolder(SViewHolder holder, final int position) {
        bindItemData(holder, mData.get(position), position);
        setOnItemClickBg(holder.itemView);
        if (linstener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linstener != null) {
                        linstener.itemOnclickLinstener(position);
                    }
                }
            });
        }
    }

    public void setOnItemClickBg(View holder) {

    }

    public void addData(D data, int position) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void addDatasInLast(List<D> data) {
        int size = mData.size();
        mData.addAll(size, data);
        notifyItemRangeInserted(size, data.size());
    }

    public abstract void bindItemData(SViewHolder holder, D itemData, int position);


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItemData(final D vote) {
        if (vote == null) {
            return;
        }
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {

                final int position = mData.indexOf(vote);
                if (position < 0) {
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mData.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        });
    }

    public void addDatasInFirst(int start, List<D> data) {
        mData.addAll(start, data);
        notifyItemRangeInserted(start, data.size());
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }
}
