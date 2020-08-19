package org.jzl.android.recyclerview.component;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.util.Logger;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataBlock;

public class LayoutManagerComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {
    private static final Logger log = Logger.logger(LayoutManagerComponent.class);

    public static final LayoutManagerFactory LAYOUT_MANAGER_FACTORY_DEFAULT = contextProvider -> new LinearLayoutManager(contextProvider.provide());
    public static final SpanSizeLookup SPAN_SIZE_LOOKUP_DEFAULT = (position, itemType, positionType) -> 1;

    private LayoutManagerFactory layoutManagerFactory = LAYOUT_MANAGER_FACTORY_DEFAULT;
    private RecyclerView recyclerView;
    private ContextProvider contextProvider;
    private DataProvider<T> dataProvider;
    private SpanSizeLookup spanSizeLookup = SPAN_SIZE_LOOKUP_DEFAULT;

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.addOnAttachedToRecyclerViewListener((contextProvider, recyclerView, adapter) -> {
            this.recyclerView = recyclerView;
            this.contextProvider = contextProvider;
            switchLayoutManager(contextProvider, recyclerView, layoutManagerFactory);
        }).dataProvider(target -> this.dataProvider = target);
    }

    public LayoutManagerComponent<T, VH> setLayoutManagerFactory(LayoutManagerFactory layoutManagerFactory) {
        if (ObjectUtils.nonNull(recyclerView) && ObjectUtils.nonNull(contextProvider)) {
            switchLayoutManager(contextProvider, recyclerView, layoutManagerFactory);
        } else {
            this.layoutManagerFactory = ObjectUtils.get(layoutManagerFactory, this.layoutManagerFactory);
        }
        return this;
    }

    public LayoutManagerComponent<T, VH> linearLayoutManager(int orientation, boolean reverseLayout) {
        return setLayoutManagerFactory(contextProvider -> new LinearLayoutManager(contextProvider.provide(), orientation, reverseLayout));
    }

    public LayoutManagerComponent<T, VH> linearLayoutManager(int orientation) {
        return linearLayoutManager(orientation, false);
    }

    public LayoutManagerComponent<T, VH> linearLayoutManager() {
        return linearLayoutManager(LinearLayoutManager.VERTICAL);
    }

    public LayoutManagerComponent<T, VH> gridLayoutManager(int spanCount, int orientation, boolean reverseLayout) {
        return setLayoutManagerFactory(contextProvider -> new GridLayoutManager(contextProvider.provide(), spanCount, orientation, reverseLayout));
    }

    public LayoutManagerComponent<T, VH> gridLayoutManager(int spanCount, int orientation) {
        return gridLayoutManager(spanCount, orientation, false);
    }

    public LayoutManagerComponent<T, VH> gridLayoutManager(int spanCount) {
        return gridLayoutManager(spanCount, GridLayoutManager.VERTICAL);
    }

    public LayoutManagerComponent<T, VH> staggeredGridLayoutManager(int spanCount, int orientation) {
        return setLayoutManagerFactory(contextProvider -> new StaggeredGridLayoutManager(spanCount, orientation));
    }

    public LayoutManagerComponent<T, VH> staggeredGridLayoutManager(int spanCount) {
        return staggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }

    public LayoutManagerComponent<T, VH> setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = ObjectUtils.get(spanSizeLookup, this.spanSizeLookup);
        return this;
    }

    private void switchLayoutManager(ContextProvider contextProvider, RecyclerView recyclerView, LayoutManagerFactory layoutManagerFactory) {
        RecyclerView.LayoutManager layoutManager = layoutManagerFactory.createLayoutManager(contextProvider);
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (ObjectUtils.nonNull(dataProvider)) {
                        DataBlock<T> dataBlock = dataProvider.findDataBlockByPosition(position);
                        if (ObjectUtils.isNull(dataBlock)) {
                            return 1;
                        }
                        DataBlock.PositionType positionType = dataBlock.getPositionType();
                        int itemType = dataProvider.getDataType(position);
                        int spanSize = LayoutManagerComponent.this.spanSizeLookup.getSpanSize(position, itemType, positionType);
//                        log.d("SpanSize => spanSize =" + spanSize + ", position = " + position + ", positionType = " + positionType + ",itemType =" + itemType);
                        return spanSize;
                    }
                    return 1;
                }
            };
            spanSizeLookup.setSpanGroupIndexCacheEnabled(true);
            spanSizeLookup.setSpanIndexCacheEnabled(true);
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(spanSizeLookup);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    public interface LayoutManagerFactory {
        RecyclerView.LayoutManager createLayoutManager(ContextProvider contextProvider);
    }

    public interface SpanSizeLookup {
        int getSpanSize(int position, int itemType, DataBlock.PositionType positionType);
    }
}
