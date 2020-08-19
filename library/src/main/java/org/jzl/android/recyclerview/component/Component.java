package org.jzl.android.recyclerview.component;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;

public interface Component<T, VH extends RecyclerView.ViewHolder> {

    void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy);

}
