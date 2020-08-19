package org.jzl.android.recyclerview.core.data;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.AdapterDataObserverFactory;
import org.jzl.android.recyclerview.core.CommonlyAdapter;
import org.jzl.android.recyclerview.core.CommonlyAdapterDataObserver;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.plugin.Plugin;
import org.jzl.android.recyclerview.util.Logger;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataObserver;

import java.util.List;

public class AdapterDataObserverManager<T, VH extends RecyclerView.ViewHolder> implements Plugin<T, VH, AdapterConfigurator<T, VH>>, DataObserver {

    private static final Logger log = Logger.logger(CommonlyAdapter.class);

    private AdapterDataObserverFactory<VH> adapterDataObserverFactory = CommonlyAdapterDataObserver::of;
    private List<Consumer<AdapterDataObserver>> adapterDataObserverBinders = CollectionUtils.newArrayList();
    private List<Function<AdapterDataObserver, AdapterDataObserver>> wrappers = CollectionUtils.newArrayList();
    private AdapterDataObserver adapterDataObserver;

    public void setAdapterDataObserverFactory(AdapterDataObserverFactory<VH> adapterDataObserverFactory) {
        this.adapterDataObserverFactory = ObjectUtils.get(adapterDataObserverFactory, this.adapterDataObserverFactory);
    }

    public void adapterDataObserver(Consumer<AdapterDataObserver> adapterDataObserverBinder) {
        if (ObjectUtils.nonNull(adapterDataObserverBinder)) {
            this.adapterDataObserverBinders.add(adapterDataObserverBinder);
        }
    }

    public void wrap(Function<AdapterDataObserver, AdapterDataObserver> wrapper) {
        if (ObjectUtils.nonNull(wrapper)) {
            wrappers.add(wrapper);
        }
    }

    @Override
    public void setup(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.adapter(target -> {
            AdapterDataObserver adapterDataObserver = adapterDataObserverFactory.createAdapterDataObserver(target);
            if (CollectionUtils.nonEmpty(wrappers)) {
                for (Function<AdapterDataObserver, AdapterDataObserver> wrapper : wrappers) {
                    adapterDataObserver = wrapper.apply(adapterDataObserver);
                }
            }
            this.adapterDataObserver = adapterDataObserver;
            bindAdapterDataObserver(adapterDataObserver);
        }).dataBlockProvider(target -> target.addDataObserver(this));
    }

    private void bindAdapterDataObserver(AdapterDataObserver adapterDataObserver) {
        ForeachUtils.each(this.adapterDataObserverBinders, target -> target.accept(adapterDataObserver));
    }

    @Override
    public void onInserted(int position, int count) {
        log.d("onInserted -> " + position + "|" + count);
        if (ObjectUtils.nonNull(adapterDataObserver)) {
            adapterDataObserver.notifyItemRangeInserted(position, count);
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        log.d("onRemoved -> " + position + "|" + count);
        if (ObjectUtils.nonNull(adapterDataObserver)) {
            adapterDataObserver.notifyItemRangeRemoved(position, count);
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        log.d("onMoved -> " + fromPosition + "|" + toPosition);
        if (ObjectUtils.nonNull(adapterDataObserver)) {
            adapterDataObserver.notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onChanged(int position) {
        log.d("onChanged -> " + position);
        if (ObjectUtils.nonNull(adapterDataObserver)) {
            adapterDataObserver.notifyItemChanged(position);
        }
    }

    @Override
    public void onBeforeAllChanged() {
        log.d("onBeforeAllChanged");
        if (ObjectUtils.nonNull(adapterDataObserver)) {
            adapterDataObserver.notifyObtainSnapshot();
        }
    }

    @Override
    public void onAllChanged() {
        log.d("onAllChanged");
        if (ObjectUtils.nonNull(adapterDataObserver)) {
            adapterDataObserver.notifyDataSetChanged();
        }
    }
}
