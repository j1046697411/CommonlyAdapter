package org.jzl.android.recyclerview.config;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.data.DataBinder;
import org.jzl.android.recyclerview.data.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.util.Binary;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConfigurator<T, VH extends RecyclerView.ViewHolder, CONFIG extends AbstractConfigurator<T, VH, CONFIG>> implements Configurator<T, VH, CONFIG> {

    private ItemBindingMatchPolicy matchPolicy;

    private List<Binary<DataBindingMatchPolicy, Plugin<T, VH, CONFIG>>> plugins = new ArrayList<>();

    private List<Binary<DataBindingMatchPolicy, DataBinder<T, VH>>> dataBinders = new ArrayList<>();

    public AbstractConfigurator(ItemBindingMatchPolicy matchPolicy) {
        this.matchPolicy = matchPolicy;
    }

    @Override
    public CONFIG dataBinding(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy) {
        this.dataBinders.add(Binary.of(matchPolicy, binder));
        return own();
    }

    @Override
    public CONFIG plugin(Plugin<T, VH, CONFIG> plugin, DataBindingMatchPolicy matchPolicy) {
        this.plugins.add(Binary.of(matchPolicy, plugin));
        return own();
    }

    protected void plugins(int nesting) {
        if (nesting > 0) {
            List<Binary<DataBindingMatchPolicy, Plugin<T, VH, CONFIG>>> plugins = new ArrayList<>(this.plugins);
            this.plugins.clear();
            for (Binary<DataBindingMatchPolicy, Plugin<T, VH, CONFIG>> plugin : plugins) {
                plugin.two.setup(own(), plugin.one);
            }
        }
    }

    protected <C> C config(C configurator, Callback<T, VH, C> callback) {
        plugins(getPluginMaxNesting());
        callback.config(configurator, matchPolicy, dataBinders);
        return configurator;
    }

    protected abstract int getPluginMaxNesting();

    protected abstract CONFIG own();

    public interface Callback<T, VH extends RecyclerView.ViewHolder, CONFIG> {
        void config(CONFIG configurator, ItemBindingMatchPolicy matchPolicy, List<Binary<DataBindingMatchPolicy, DataBinder<T, VH>>> dataBinders);
    }

}
