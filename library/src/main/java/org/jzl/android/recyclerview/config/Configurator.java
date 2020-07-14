package org.jzl.android.recyclerview.config;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.data.DataBinder;
import org.jzl.android.recyclerview.data.DataBindingMatchPolicy;

public interface Configurator<T, VH extends RecyclerView.ViewHolder, CONFIG extends Configurator<T, VH, CONFIG>> {

    CONFIG dataBinding(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy);

    default CONFIG dataBinding(DataBinder<T, VH> binder, int... dataTypes) {
        return dataBinding(binder, DataBindingMatchPolicy.ofDataTypes(dataTypes));
    }

    CONFIG plugin(Plugin<T, VH, CONFIG> plugin, DataBindingMatchPolicy matchPolicy);

    default CONFIG plugin(Plugin<T, VH, CONFIG> plugin, int... dataTypes) {
        return plugin(plugin, DataBindingMatchPolicy.ofDataTypes(dataTypes));
    }

    interface Plugin<T, VH extends RecyclerView.ViewHolder, CONFIG extends Configurator<T, VH, CONFIG>> {

        void setup(CONFIG configurator, DataBindingMatchPolicy matchPolicy);

    }

}
