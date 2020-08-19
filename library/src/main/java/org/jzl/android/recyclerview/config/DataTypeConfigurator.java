package org.jzl.android.recyclerview.config;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.item.DataBinder;
import org.jzl.android.recyclerview.core.item.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.ObjectUtils;

public class DataTypeConfigurator<E, T, VH extends RecyclerView.ViewHolder> extends AbstractConfigurator<T, VH, DataTypeConfigurator<E, T, VH>> {

    private AdapterConfigurator<E, VH> configurator;
    private Function<E, T> mapper;
    private ItemBindingMatchPolicy matchPolicy;

    public DataTypeConfigurator(AdapterConfigurator<E, VH> configurator, Function<E, T> mapper, ItemBindingMatchPolicy matchPolicy) {
        this.configurator = ObjectUtils.requireNonNull(configurator, "configurator");
        this.mapper = ObjectUtils.requireNonNull(mapper, "mapper");
        this.matchPolicy = ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL);
    }

    @Override
    protected int getPluginMaxNesting() {
        return configurator.getPluginMaxNesting();
    }

    @Override
    protected DataTypeConfigurator<E, T, VH> own() {
        return this;
    }

    @Override
    public DataTypeConfigurator<E, T, VH> dataBinding(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy) {
        configurator.dataBinding((holder, data) -> binder.binding(holder, mapper.apply(data)), matchPolicy.and(this.matchPolicy.toDataBindingMatchPolicy()));
        return this;
    }

    public AdapterConfigurator<E, VH> and(){
        config();
        return configurator;
    }
}
