package org.jzl.android.recyclerview.core.util;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jzl.android.recyclerview.util.Logger;
import org.jzl.lang.util.MathUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class DividingLineItemDecoration extends RecyclerView.ItemDecoration {
    private static final Logger log = Logger.logger(DividerItemDecoration.class);

    private final Rect dividingLine = new Rect();
    private final int width;
    private Map<Class<?>, LayoutManagerDividingLineCalculator<?>> dividingLineCalculators = new HashMap<>();

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
        addLayoutManagerDividingLineCalculator(StaggeredGridLayoutManager.class, new StaggeredGridLayoutManagerDividingLineCalculator());
    }


    public DividingLineItemDecoration(int width) {
        this(width, width, width, width, width);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        Rect dividingLine = new Rect();
        LayoutManagerDividingLineCalculator<RecyclerView.LayoutManager> dividingLineCalculator = findLayoutManagerDividingLineCalculator(layoutManager);
        if (ObjectUtils.nonNull(dividingLineCalculator)) {
            dividingLineCalculator.calculationDividingLine(dividingLine, this.dividingLine, width, layoutManager, parent.getChildLayoutPosition(view), view);
        } else {
            dividingLine.set(this.dividingLine);
        }
        outRect.set(dividingLine);
    }

    @SuppressWarnings("all")
    private <L extends RecyclerView.LayoutManager> LayoutManagerDividingLineCalculator<L> findLayoutManagerDividingLineCalculator(RecyclerView.LayoutManager layoutManager) {
        return (LayoutManagerDividingLineCalculator<L>) dividingLineCalculators.get(layoutManager.getClass());
    }

    public <L extends RecyclerView.LayoutManager> void addLayoutManagerDividingLineCalculator(Class<L> type, LayoutManagerDividingLineCalculator<L> dividingLineCalculator) {
        this.dividingLineCalculators.put(type, dividingLineCalculator);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    public interface LayoutManagerDividingLineCalculator<L extends RecyclerView.LayoutManager> {
        void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, L layoutManager, int layoutPosition, View view);
    }

    public static class LinearLayoutManagerDividingLineCalculator implements LayoutManagerDividingLineCalculator<LinearLayoutManager> {
        @Override
        public void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, LinearLayoutManager layoutManager, int layoutPosition, View view) {
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
        public void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, GridLayoutManager layoutManager, int layoutPosition, View view) {
            long start = System.currentTimeMillis();

            int spanCount = layoutManager.getSpanCount();
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanIndex = layoutParams.getSpanIndex();
            int spanSize = layoutParams.getSpanSize();
            int groupIndex;
            int groupCount;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            if (ObjectUtils.nonNull(spanSizeLookup)) {
                groupIndex = spanSizeLookup.getSpanGroupIndex(layoutPosition, spanCount);
                groupCount = spanSizeLookup.getSpanGroupIndex(layoutManager.getItemCount() - 1, spanCount);
            } else {
                groupIndex = layoutPosition / spanCount;
                groupCount = (layoutManager.getItemCount() - 1) / spanCount;
            }
            log.d("spanCount = " + spanCount + ", spanIndex = " + spanIndex + ", spanSize = " + spanSize + ", groupIndex = " + groupIndex + ", groupCount = " + groupCount);

            if (layoutManager.canScrollHorizontally()) {
                if (spanIndex == 0) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (spanIndex + spanSize >= spanCount) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
                if (groupIndex == 0) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (groupIndex == groupCount) {
                    outDividingLine.right = roundDividingLine.right;
                }

            } else {
                if (spanIndex == 0) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (spanIndex + spanSize >= spanCount) {
                    outDividingLine.right = roundDividingLine.right;
                }
                if (groupIndex == 0) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (groupIndex == groupCount) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
            }
            log.d("time:" + (System.currentTimeMillis() - start));
        }
    }

    public static class StaggeredGridLayoutManagerDividingLineCalculator implements LayoutManagerDividingLineCalculator<StaggeredGridLayoutManager> {

        @Override
        public void calculationDividingLine(Rect outDividingLine, Rect roundDividingLine, int width, StaggeredGridLayoutManager layoutManager, int layoutPosition, View view) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

            int spanIndex = layoutParams.getSpanIndex();
            int spanCount = layoutManager.getSpanCount();
            if (layoutManager.canScrollHorizontally()) {
                if (isFirst(layoutPosition, spanIndex, layoutManager)) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (isLast(layoutPosition, spanIndex, layoutManager)) {
                    outDividingLine.right = roundDividingLine.right;
                }
                if (spanIndex == 0) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (spanIndex == spanCount - 1) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
            } else {
                if (isFirst(layoutPosition, spanIndex, layoutManager)) {
                    outDividingLine.top = roundDividingLine.top;
                } else {
                    outDividingLine.top = width;
                }
                if (isLast(layoutPosition, spanIndex, layoutManager)) {
                    outDividingLine.bottom = roundDividingLine.bottom;
                }
                outDividingLine.top = width;
                if (spanIndex == 0) {
                    outDividingLine.left = roundDividingLine.left;
                } else {
                    outDividingLine.left = width;
                }
                if (spanIndex == spanCount - 1) {
                    outDividingLine.right = roundDividingLine.right;
                }
            }
        }

        private boolean isFirst(int layoutPosition, int spanIndex, StaggeredGridLayoutManager staggeredGridLayoutManager) {
            if (layoutPosition == 0) {
                return true;
            }
            for (int i = layoutPosition - 1; i >= 0; i--) {
                View frontView = staggeredGridLayoutManager.findViewByPosition(i);
                if (isSpanIndexOrFullSpan(frontView, spanIndex)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isLast(int layoutPosition, int spanIndex, StaggeredGridLayoutManager staggeredGridLayoutManager) {
            int itemCount = staggeredGridLayoutManager.getItemCount();
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            for (int i = layoutPosition + 1; i < itemCount; i++) {
                View frontView = staggeredGridLayoutManager.findViewByPosition(i);
                if (isSpanIndexOrFullSpan(frontView, spanIndex)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isSpanIndexOrFullSpan(View view, int spanIndex) {
            if (ObjectUtils.isNull(view)) {
                return true;
            }
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            return layoutParams.isFullSpan() || layoutParams.getSpanIndex() == spanIndex;
        }

    }

}
