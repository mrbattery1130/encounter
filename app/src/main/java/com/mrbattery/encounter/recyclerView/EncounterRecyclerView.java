package com.mrbattery.encounter.recyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EncounterRecyclerView extends RecyclerView {
    private OnScrollListener onScrolledListener;
    private EncounterAdapter adapter;
    private LayoutManager layout;

    //保证图片加载顺序


    public EncounterRecyclerView(Context context) {
        this(context, null);
    }

    public EncounterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public EncounterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new ImageAutoLoadScrollListener());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        this.layout = layout;
        super.setLayoutManager(layout);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof EncounterAdapter) {
            this.adapter = (EncounterAdapter) adapter;
        }
        super.setAdapter(adapter);
    }

    private class ImageAutoLoadScrollListener extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (onScrolledListener != null) {
                onScrolledListener.onScrolled(recyclerView, dx, dy);
            }
        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case SCROLL_STATE_IDLE:// The RecyclerView is not currently scrolling.
                    //对于滚动不加载图片的尝试
                    adapter.setScrolling(false);
                    adapter.notifyDataSetChanged();
                    break;
                case SCROLL_STATE_DRAGGING:// The RecyclerView is currently being dragged by outside input such as user touch input.
                    adapter.setScrolling(false);
                    break;
                case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under
                    adapter.setScrolling(true);
                    break;
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    public void setOnScrolledListener(OnScrollListener onScrolledListener) {
        this.onScrolledListener = onScrolledListener;
    }

    public interface OnScrolledListener {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }





}
