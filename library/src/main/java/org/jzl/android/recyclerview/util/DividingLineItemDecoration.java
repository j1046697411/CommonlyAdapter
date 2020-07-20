package org.jzl.android.recyclerview.util;

import android.graphics.Canvas;
import android.graphics.Rect;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jzl.lang.util.MathUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.HashMap;

public class DividingLineItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "DividingLineItemDecoration";

    private final Rect dividingLine = new Rect();
    private final Rect temDividingLine = new Rect();
    private final int width;
    private java.util.Map<Class<?>, LayoutManagerDividingLineCalculator<?>> dividingLineCalculators = new HashMap<>();

    public DividingLineItemDecoration(int left, int top, int right, int bottom, int width) {
        dividingLine.set(
                MathUtils.clamp(left, 0, Integer.MAX_VALUE),
                MathUtils.clamp(top, 0, Integer.MAX_VALUE),
                MathUtils.clamp(right, 0, Integer.MAX_VALUE),
                MathUtils.clamp(bottom, 0, Integer.MAX_VALUE)
        );
        this.width = MathUtils.clamp(width, 0, Integer.MAX_VALUE);
        addLayoutManagerDividingLineCalculator(LinearLayoutManager.class, new LinearLayoutManagerDividingLineCalculator());
        addLayoutManagerDividingLineCalculator(GridLayoutManager.class, new GridLayoutManagerDividingLineCalculator());
    }

    @SuppressWarnings("all")
    public DividingLineItemDecoration(int width) {
        this(width, width, width, width, width);
    }

    @Override
    public void getItemOffsets(@androidx.annotation.NonNull Rect outRect, @androidx.annotation.NonNull android.view.View view, @androidx.annotation.NonNull RecyclerView parent, @androidx.annotation.NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        temDividingLine.set(0, 0, 0, 0);
        LayoutManagerDividingLineCalculator<RecyclerView.LayoutManager> dividingLineCalculator = getLayoutManagerDividingLineCalculator(layoutManager);
        if (ObjectUtils.nonNull(dividingLineCalculator)) {
            dividingLineCalculator.calculationDividingLine(temDividingLine, dividingLine, width, layoutManager, parent.getChildLayoutPosition(view));
        } else {
            temDividingLine.set(dividingLine);
        }
        outRect.set(temDividingLine);
    }

    @SuppressWarnings("all")
    private <L extends RecyclerView.LayoutManager> LayoutManagerDividingLineCalculator<L> getLayoutManagerDividingLineCalculator(RecyclerView.LayoutManager layoutManager) {
        return (LayoutManagerDividingLineCalculator<L>) dividingLineCalculators.get(layoutManager.getClass());
    }

    public <L extends RecyclerView.LayoutManager> void addLayoutManagerDividingLineCalculator(Class<L> type, LayoutManagerDividingLineCalculator<L> dividingLineCalculator) {
        this.dividingLineCalculators.put(type, dividingLineCalculator);
    }

    @Override
    public void onDraw(@androidx.annotation.NonNull Canvas c, @androidx.annotation.NonNull RecyclerView parent, @androidx.annotation.NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(@androidx.annotation.NonNull Canvas c, @androidx.annotation.NonNull RecyclerView parent, @androidx.annotation.NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    public interface LayoutManagerDividingLineCalculator<L extends RecyclerView.LayoutManager> {
        void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, L layoutManager, int layoutPosition);
    }

    public static class LinearLayoutManagerDividingLineCalculator implements LayoutManagerDividingLineCalculator<LinearLayoutManager> {
        @Override
        @SuppressWarnings("all")
        public void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, LinearLayoutManager layoutManager, int layoutPosition) {
            int itemCount = layoutManager.getItemCount();
            if (layoutManager.canScrollHorizontally()) {
                outDividingLine.top = roundDividingLine.top;
                outDividingLine.bottom = roundDividingLine.bottom;
                if (layoutPosition == 0) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (layoutPosition == itemCount - 1) {
                    outDividingLine.right = roundDividingLine.right;
                }
            } else {
                outDividingLine.right = roundDividingLine.right;
                outDividingLine.left = roundDividingLine.left;
                if (layoutPosition == 0) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (layoutPosition == itemCount - 1) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
            }
        }

    }

    public static class GridLayoutManagerDividingLineCalculator implements LayoutManagerDividingLineCalculator<GridLayoutManager> {

        @Override
        @SuppressWarnings("all")
        public void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, GridLayoutManager layoutManager, int layoutPosition) {
            int consumeSpanCount = 0;
            int totalSpanCount = 0;
            int itemCount = layoutManager.getItemCount();
            int spanCount = layoutManager.getSpanCount();

            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            if (ObjectUtils.nonNull(spanSizeLookup)) {
                for (int i = 0; i < itemCount; i++) {
                    int itemSpanSize = spanSizeLookup.getSpanSize(i);
                    if (i <= layoutPosition) {
                        consumeSpanCount += itemSpanSize;
                    }
                    totalSpanCount += itemSpanSize;
                }
            } else {
                consumeSpanCount = layoutPosition;
                totalSpanCount = itemCount;
            }

            //减一是方便计算同一行
            int row = (consumeSpanCount - 1) / spanCount;
            int mod = (consumeSpanCount - 1) % spanCount;

            int totalRow = (totalSpanCount - 1) / spanCount;
            if (layoutManager.canScrollHorizontally()) {
                if (row == 0) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (row == totalRow) {
                    outDividingLine.right = roundDividingLine.right;
                }
                if (mod == 0) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (mod == spanCount - 1) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
            } else {
                if (row == 0) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (row == totalRow) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
                if (mod == 0) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (mod == spanCount - 1) {
                    outDividingLine.right = roundDividingLine.right;
                }
            }
        }
    }

}
