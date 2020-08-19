package org.jzl.android.recyclerview.config;


import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.plugin.Plugin;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.holder.BinaryHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConfigurator<T, VH extends RecyclerView.ViewHolder, CONFIG extends AbstractConfigurator<T, VH, CONFIG>> implements Configurator<T, VH, CONFIG> {

    private List<BinaryHolder<ItemBindingMatchPolicy, Plugin<T, VH, CONFIG>>> plugins = CollectionUtils.newArrayList();

    protected abstract int getPluginMaxNesting();

    protected abstract CONFIG own();

    @Override
    public CONFIG plugin(Plugin<T, VH, CONFIG> plugin, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(plugin)) {
            plugins.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), plugin));
        }
        return own();
    }

    protected void plugins(int nesting) {
        if (nesting > 0) {
            List<BinaryHolder<ItemBindingMatchPolicy, Plugin<T, VH, CONFIG>>> plugins = new ArrayList<>(this.plugins);
            this.plugins.clear();
            ForeachUtils.each(plugins, target -> target.two.setup(own(), target.one));
            plugins(nesting - 1);
        }
    }

    protected void config() {
        plugins(getPluginMaxNesting());
    }

}
