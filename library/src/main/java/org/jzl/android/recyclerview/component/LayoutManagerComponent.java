package org.jzl.android.recyclerview.component;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.ObjectUtils;

public class LayoutManagerComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {

    private Function<Context, RecyclerView.LayoutManager> layoutManager = LinearLayoutManager::new;
    private SpanSizeLookup<VH> spanSizeLookup = (holder, position) -> 1;
    private RecyclerView recyclerView;
    private ContextProvider contextProvider;

    private LayoutManagerComponent() {
    }

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.addOnAttachedToRecyclerViewListener((contextProvider, recyclerView, adapter) -> {
            setLayoutManager(recyclerView, contextProvider, layoutManager, spanSizeLookup);
            this.contextProvider = contextProvider;
            this.recyclerView = recyclerView;
        });
    }

    public LayoutManagerComponent<T, VH> setSpanSizeLookup(SpanSizeLookup<VH> spanSizeLookup) {
        this.spanSizeLookup = ObjectUtils.get(spanSizeLookup, this.spanSizeLookup);
        return this;
    }

    private LayoutManagerComponent<T, VH> setLayoutManager(RecyclerView recyclerView, ContextProvider contextProvider, Function<Context, RecyclerView.LayoutManager> layoutManagerFunction, SpanSizeLookup<VH> spanSizeLookup) {
        if (ObjectUtils.nonNull(recyclerView) && ObjectUtils.nonNull(contextProvider)) {
            RecyclerView.LayoutManager layoutManager = layoutManagerFunction.apply(contextProvider.provide());
            if (layoutManager instanceof GridLayoutManager && ObjectUtils.nonNull(spanSizeLookup)) {
                ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    @SuppressWarnings("all")
                    public int getSpanSize(int position) {
                        return spanSizeLookup.getSpanSize((VH) recyclerView.findViewHolderForAdapterPosition(position), position);
                    }
                });
            }
            recyclerView.setLayoutManager(layoutManager);
        } else {
            this.layoutManager = layoutManagerFunction;
        }
        return this;
    }

    public LayoutManagerComponent<T, VH> layoutManager(Function<Context, RecyclerView.LayoutManager> layoutManager) {
        return setLayoutManager(this.recyclerView, contextProvider, ObjectUtils.get(layoutManager, this.layoutManager), spanSizeLookup);
    }

    public LayoutManagerComponent<T, VH> gridLayoutManager(int spanCount, int orientation, boolean reverseLayout) {
        return layoutManager(context -> new GridLayoutManager(context, spanCount, orientation, reverseLayout));
    }

    public LayoutManagerComponent<T, VH> gridLayoutManager(int spanCount, int orientation) {
        return gridLayoutManager(spanCount, orientation, false);
    }

    public LayoutManagerComponent<T, VH> gridLayoutManager(int spanCount) {
        return gridLayoutManager(spanCount, GridLayoutManager.VERTICAL);
    }

    public LayoutManagerComponent<T, VH> linearLayoutManager(int orientation, boolean reverseLayout) {
        return layoutManager(context -> new LinearLayoutManager(context, orientation, reverseLayout));
    }

    public LayoutManagerComponent<T, VH> linearLayoutManager(int orientation) {
        return linearLayoutManager(orientation, false);
    }

    public LayoutManagerComponent<T, VH> linearLayoutManager() {
        return linearLayoutManager(LinearLayoutManager.VERTICAL);
    }

    public LayoutManagerComponent<T, VH> staggeredGridLayoutManager(int spanCount, int orientation) {
        return layoutManager(context -> new StaggeredGridLayoutManager(spanCount, orientation));
    }

    public LayoutManagerComponent<T, VH> staggeredGridLayoutManager(int spanCount) {
        return staggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerComponent<T, VH> of() {
        return new LayoutManagerComponent<>();
    }

    public interface SpanSizeLookup<VH extends RecyclerView.ViewHolder> {

        int getSpanSize(VH holder, int position);

    }
}
