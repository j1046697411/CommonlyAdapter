package org.jzl.android.recyclerview.component;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.holder.BinaryHolder;

import java.util.List;

public class ComponentManager<T, VH extends RecyclerView.ViewHolder> {

    private List<BinaryHolder<ItemBindingMatchPolicy, Component<T, VH>>> components = CollectionUtils.newArrayList();
    private List<BinaryHolder<Class<Component<T, VH>>, Consumer<Component<T, VH>>>> consumers = CollectionUtils.newArrayList();
    private List<Consumer<ComponentManager<T, VH>>> componentManagerConsumers = CollectionUtils.newArrayList();

    public void apply(AdapterConfigurator<T, VH> configurator) {
        ForeachUtils.each(this.components, target -> target.two.apply(configurator, target.one));
        ForeachUtils.each(this.consumers, target -> target.two.accept(getComponent(target.one)));
        ForeachUtils.each(this.componentManagerConsumers, target -> target.accept(this));
    }

    public void addComponent(Component<T, VH> component, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(component)) {
            components.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), component));
        }
    }

    @SuppressWarnings("all")
    public <C extends Component<T, VH>> C getComponent(Class<C> type) {
        if (ObjectUtils.nonNull(type)) {
            BinaryHolder<ItemBindingMatchPolicy, Component<T, VH>> binaryHolder = ForeachUtils.findByOne(this.components, target -> target.two.getClass() == type);
            if (ObjectUtils.nonNull(binaryHolder)) {
                return (C) binaryHolder.two;
            }
        }
        return null;
    }

    @SuppressWarnings("all")
    public <C extends Component<T, VH>> void component(Class<C> type, Consumer<C> consumer){
        if (ObjectUtils.nonNull(type) && ObjectUtils.nonNull(consumer)){
            this.consumers.add(BinaryHolder.of((Class<Component<T,VH>>) type, (Consumer<Component<T,VH>>) consumer));
        }
    }

    public void componentManager(Consumer<ComponentManager<T, VH>> consumer){
        if (ObjectUtils.nonNull(consumer)){
            componentManagerConsumers.add(consumer);
        }
    }
}
