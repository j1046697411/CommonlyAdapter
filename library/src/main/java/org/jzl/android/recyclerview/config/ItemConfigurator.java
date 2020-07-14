package org.jzl.android.recyclerview.config;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.DataClassifier;
import org.jzl.android.recyclerview.core.item.ItemBinder;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.fun.Function;

public interface ItemConfigurator<T, VH extends RecyclerView.ViewHolder, CONFIG extends ItemConfigurator<T, VH, CONFIG>> extends Configurator<T, VH, CONFIG> {

    int getPluginMaxNesting();

    CONFIG setPluginMaxNesting(int pluginMaxNesting);

    CONFIG itemBinding(ItemBinder<T, VH> binder, ItemBindingMatchPolicy matchPolicy);

    CONFIG dataTypes(DataClassifier<T> dataClassifier, ItemBindingMatchPolicy matchPolicy);

    default CONFIG itemBinding(ItemBinder<T, VH> binder, int... itemTypes) {
        return itemBinding(binder, ItemBindingMatchPolicy.of(itemTypes));
    }

    <E> ItemTypeConfigurator<T, E, VH, CONFIG> itemTypes(Function<T, E> mapper, DataClassifier<E> dataClassifier, int... itemTypes);

}
