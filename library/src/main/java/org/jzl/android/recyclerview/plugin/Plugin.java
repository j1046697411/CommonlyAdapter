package org.jzl.android.recyclerview.plugin;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.Configurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;

public interface Plugin<T, VH extends RecyclerView.ViewHolder, CONFIG extends Configurator<T, VH, CONFIG>> {

    void setup(CONFIG configurator, ItemBindingMatchPolicy matchPolicy);

}
