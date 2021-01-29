package org.jzl.android.recyclerview.decorations.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.lang.util.ObjectUtils;

public class GridLayoutManagerDividingLineCalculator implements LayoutManagerDividingLineCalculator<GridLayoutManager> {

    @Override
    public void calculationDividingLine(@NonNull RecyclerView recyclerView, @NonNull GridLayoutManager layoutManager, @NonNull Rect outRect, @NonNull DividerItemDecoration dividerItemDecoration, @NonNull View view, @NonNull RecyclerView.State state) {

        Rect roundDividingLine = dividerItemDecoration.getDividingLineBounds();
        int offset = dividerItemDecoration.getOffset();
        int layoutPosition = recyclerView.getChildLayoutPosition(view);
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
        if (layoutManager.canScrollHorizontally()) {
            if (spanIndex == 0) {
                outRect.top = roundDividingLine.top;
            } else {
                outRect.top = offset;
            }
            if (spanIndex + spanSize >= spanCount) {
                outRect.bottom = roundDividingLine.bottom;
            }
            if (groupIndex == 0) {
                outRect.left = roundDividingLine.left;
            } else {
                outRect.left = offset;
            }
            if (groupIndex == groupCount) {
                outRect.right = roundDividingLine.right;
            }

        } else {
            if (spanIndex == 0) {
                outRect.left = roundDividingLine.left;
            } else {
                outRect.left = offset;
            }
            if (spanIndex + spanSize >= spanCount) {
                outRect.right = roundDividingLine.right;
            }
            if (groupIndex == 0) {
                outRect.top = roundDividingLine.top;
            } else {
                outRect.top = offset;
            }
            if (groupIndex == groupCount) {
                outRect.bottom = roundDividingLine.bottom;
            }
        }
    }
}
