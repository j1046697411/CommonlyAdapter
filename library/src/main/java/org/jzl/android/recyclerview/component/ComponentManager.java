package org.jzl.android.recyclerview.component;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.util.Binary;
import org.jzl.lang.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ComponentManager<T, VH extends RecyclerView.ViewHolder> {

    private List<Binary<ItemBindingMatchPolicy, Component<T, VH>>> components = new ArrayList<>();

    public void apply(AdapterConfigurator<T, VH> configurator) {
        CollectionUtils.each(components, target -> target.two.apply(configurator, target.one));
    }

    @SuppressWarnings("all")
    public void addComponent(Component<T, VH> component, ItemBindingMatchPolicy matchPolicy) {
        if (hasComponent((Class<? extends Component<T, VH>>) component.getClass())) {
            return;
        }
        this.components.add(Binary.of(matchPolicy, component));
    }

    public boolean hasComponent(Class<? extends Component<T, VH>> type) {
        return getComponent(type) != null;
    }

    @SuppressWarnings("all")
    public <C extends Component<T, VH>> C getComponent(Class<C> type) {
        for (Binary<ItemBindingMatchPolicy, Component<T, VH>> binary : components) {
            if (type.isInstance(binary.two)) {
                return (C) binary.two;
            }
        }
        return null;
    }

}
