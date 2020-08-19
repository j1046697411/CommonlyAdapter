package org.jzl.android.recyclerview.config;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.component.BindAnimatorComponent;
import org.jzl.android.recyclerview.component.BindModeComponent;
import org.jzl.android.recyclerview.component.Component;
import org.jzl.android.recyclerview.component.ComponentManager;
import org.jzl.android.recyclerview.component.DiffUpdateComponent;
import org.jzl.android.recyclerview.component.LayoutManagerComponent;
import org.jzl.android.recyclerview.component.animator.AnimatorFactory;
import org.jzl.android.recyclerview.component.diff.DiffDispatcherFactory;
import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.AdapterDataObserverFactory;
import org.jzl.android.recyclerview.core.CommonlyAdapter;
import org.jzl.android.recyclerview.core.CommonlyViewHolder;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.android.recyclerview.core.ItemViewManager;
import org.jzl.android.recyclerview.core.ListenerManager;
import org.jzl.android.recyclerview.core.ViewHolderFactory;
import org.jzl.android.recyclerview.core.context.ItemContextFactory;
import org.jzl.android.recyclerview.core.context.ItemContextImpl;
import org.jzl.android.recyclerview.core.data.AdapterDataObserverManager;
import org.jzl.android.recyclerview.core.data.DataClassifier;
import org.jzl.android.recyclerview.core.data.DataManager;
import org.jzl.android.recyclerview.core.data.IdentityGetter;
import org.jzl.android.recyclerview.core.item.DataBinder;
import org.jzl.android.recyclerview.core.item.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemViewFactory;
import org.jzl.android.recyclerview.core.item.ItemViewManagerImpl;
import org.jzl.android.recyclerview.core.listener.ListenerManagerImpl;
import org.jzl.android.recyclerview.core.listener.OnAttachedToRecyclerViewListener;
import org.jzl.android.recyclerview.core.listener.OnCreatedViewHolderListener;
import org.jzl.android.recyclerview.core.listener.OnDetachedFromRecyclerViewListener;
import org.jzl.android.recyclerview.core.listener.OnFailedToRecycleViewListener;
import org.jzl.android.recyclerview.core.listener.OnUpdatedItemContextListener;
import org.jzl.android.recyclerview.core.listener.OnViewAttachedToWindowListener;
import org.jzl.android.recyclerview.core.listener.OnViewDetachedFromWindowListener;
import org.jzl.android.recyclerview.core.listener.OnViewRecycledListener;
import org.jzl.android.recyclerview.core.util.DividingLineItemDecoration;
import org.jzl.android.recyclerview.util.ScreenUtils;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.MathUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataBlock;
import org.jzl.lang.util.datablcok.DataBlockProvider;

import java.util.Collection;

public class AdapterConfigurator<T, VH extends RecyclerView.ViewHolder> extends AbstractConfigurator<T, VH, AdapterConfigurator<T, VH>> {

    private final ItemViewManager<T, VH> itemViewManager;
    private final ListenerManager<T, VH> listenerManager;
    private final DataManager<T> dataManager;
    private final ComponentManager<T, VH> componentManager = new ComponentManager<>();
    private final AdapterDataObserverManager<T, VH> adapterDataObserverManager = new AdapterDataObserverManager<>();

    private ItemContextFactory<T, VH> contextFactory = (adapter, dataProvider, itemViewManager1, listenerManager1) -> new ItemContextImpl<>(adapter, dataProvider);

    private int pluginMaxNesting = 10;

    public AdapterConfigurator(ViewHolderFactory<VH> viewHolderFactory) {
        this.itemViewManager = new ItemViewManagerImpl<>(viewHolderFactory);
        this.listenerManager = new ListenerManagerImpl<>();
        this.dataManager = new DataManager<>();
        initComponents();
        initPlugins();
    }

    private void initComponents() {
        addComponent(new BindModeComponent<>());
        addComponent(new LayoutManagerComponent<>());
    }

    private void initPlugins() {
        plugin(adapterDataObserverManager, ItemBindingMatchPolicy.MATCH_POLICY_ALL);
    }

    public <E> DataTypeConfigurator<T, E, VH> dataTypeConfigurator(Function<T, E> mapper, ItemBindingMatchPolicy matchPolicy) {
        return new DataTypeConfigurator<T, E, VH>(this, mapper, matchPolicy);
    }

    public <E> DataTypeConfigurator<T, E, VH> dataTypeConfigurator(Function<T, E> mapper, int... itemViewTypes) {
        return dataTypeConfigurator(mapper, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    public AdapterConfigurator<T, VH> setDataClassifier(DataClassifier<T> dataClassifier) {
        dataManager.setDataClassifier(dataClassifier);
        return this;
    }

    public AdapterConfigurator<T, VH> setDividingLine(int width){
        return addOnAttachedToRecyclerViewListener((contextProvider, recyclerView, adapter) -> recyclerView.addItemDecoration(new DividingLineItemDecoration(ScreenUtils.dip2px(contextProvider.provide(), width))));
    }

    public AdapterConfigurator<T, VH> setIdentityGetter(IdentityGetter<T> identityGetter) {
        dataManager.setIdentityGetter(identityGetter);
        return this;
    }

    public AdapterConfigurator<T, VH> setAdapterDataObserverFactory(AdapterDataObserverFactory<VH> adapterDataObserverFactory) {
        adapterDataObserverManager.setAdapterDataObserverFactory(adapterDataObserverFactory);
        return this;
    }

    public AdapterConfigurator<T, VH> adapterDataObserver(Consumer<AdapterDataObserver> consumer) {
        adapterDataObserverManager.adapterDataObserver(consumer);
        return this;
    }

    public AdapterConfigurator<T, VH> wrapAdapterDataObserver(Function<AdapterDataObserver, AdapterDataObserver> wrapper) {
        this.adapterDataObserverManager.wrap(wrapper);
        return this;
    }

    public AdapterConfigurator<T, VH> dataProvider(Consumer<DataProvider<T>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            consumer.accept(dataManager);
        }
        return this;
    }

    public AdapterConfigurator<T, VH> adapter(Consumer<CommonlyAdapter<T, VH>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            return addOnAttachedToRecyclerViewListener((contextProvider, recyclerView, adapter) -> consumer.accept(adapter));
        }
        return this;
    }

    public AdapterConfigurator<T, VH> diff(DiffDispatcherFactory<T> diffDispatcherFactory, DiffUtil.ItemCallback<T> itemCallback) {
        if (ObjectUtils.nonNull(diffDispatcherFactory) && ObjectUtils.nonNull(itemCallback)) {
            return addComponent(new DiffUpdateComponent<>(diffDispatcherFactory, itemCallback));
        }
        return this;
    }

    public AdapterConfigurator<T, VH> bindAnimator(AnimatorFactory<VH> animatorFactory) {
        if (ObjectUtils.nonNull(animatorFactory)) {
            return addComponent(new BindAnimatorComponent<>(animatorFactory));
        }
        return this;
    }

    /**************************** 数据添加 ****************************/

    @SafeVarargs
    public final AdapterConfigurator<T, VH> addAll(T... data) {
        dataManager.addAllToContent(data);
        return this;
    }

    public AdapterConfigurator<T, VH> addAll(Collection<T> data) {
        dataManager.addAllToContent(data);
        return this;
    }

    public AdapterConfigurator<T, VH> dataBlockProvider(Consumer<DataBlockProvider<T>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            consumer.accept(dataManager);
        }
        return this;
    }

    public AdapterConfigurator<T, VH> dataBlock(DataBlock.PositionType positionType, int dataBlockId, Consumer<DataBlock<T>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            DataBlock<T> dataBlock = dataManager.dataBlock(positionType, dataBlockId);
            consumer.accept(dataBlock);
        }
        return this;
    }

    public AdapterConfigurator<T, VH> defaultDataBlock(Consumer<DataBlock<T>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            consumer.accept(dataManager.defaultDataBlock());
        }
        return this;
    }

    public AdapterConfigurator<T, VH> headerDataBlock(int dataBlockId, Consumer<DataBlock<T>> consumer) {
        return dataBlock(DataBlock.PositionType.HEADER, dataBlockId, consumer);
    }

    public AdapterConfigurator<T, VH> footerDataBlock(int dataBlockId, Consumer<DataBlock<T>> consumer) {
        return dataBlock(DataBlock.PositionType.FOOTER, dataBlockId, consumer);
    }

    public AdapterConfigurator<T, VH> contentDataBlock(int dataBlockId, Consumer<DataBlock<T>> consumer) {
        return dataBlock(DataBlock.PositionType.CONTENT, dataBlockId, consumer);
    }

    public AdapterConfigurator<T, VH> setPluginMaxNesting(int pluginMaxNesting) {
        this.pluginMaxNesting = MathUtils.clamp(pluginMaxNesting, 1, 20);
        return this;
    }

    @Override
    protected int getPluginMaxNesting() {
        return pluginMaxNesting;
    }

    public AdapterConfigurator<T, VH> setContextFactory(ItemContextFactory<T, VH> contextFactory) {
        this.contextFactory = ObjectUtils.get(contextFactory, this.contextFactory);
        return this;
    }

    @Override
    protected AdapterConfigurator<T, VH> own() {
        return this;
    }

    @Override
    public AdapterConfigurator<T, VH> dataBinding(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy) {
        itemViewManager.dataBinding(binder, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> createItemView(ItemViewFactory itemViewFactory, ItemBindingMatchPolicy matchPolicy) {
        itemViewManager.createItemView(itemViewFactory, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> createItemView(ItemViewFactory itemViewFactory, int... itemViewTypes) {
        return createItemView(itemViewFactory, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    public AdapterConfigurator<T, VH> createItemView(@LayoutRes int layoutResId, int... itemViewTypes) {
        return createItemView((layoutInflater, parent) -> layoutInflater.inflate(layoutResId, parent, false), itemViewTypes);
    }

    /**************************** 事件监听 ****************************/

    public AdapterConfigurator<T, VH> setFailedToRecycleViewListener(OnFailedToRecycleViewListener<VH> failedToRecycleViewListener) {
        listenerManager.setFailedToRecycleViewListener(failedToRecycleViewListener);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnUpdatedItemContextListener(OnUpdatedItemContextListener<T, VH> updatedItemContextListener) {
        listenerManager.addOnUpdatedItemContextListener(updatedItemContextListener);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnDetachedFromRecyclerViewListener(OnDetachedFromRecyclerViewListener detachedFromRecyclerViewListener) {
        listenerManager.addOnDetachedFromRecyclerViewListener(detachedFromRecyclerViewListener);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener) {
        listenerManager.addOnAttachedToRecyclerViewListener(attachedToRecyclerViewListener);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, ItemBindingMatchPolicy matchPolicy) {
        listenerManager.addOnCreatedViewHolderListener(createdViewHolderListener, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, int... itemViewTypes) {
        return addOnCreatedViewHolderListener(createdViewHolderListener, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    public AdapterConfigurator<T, VH> addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, ItemBindingMatchPolicy matchPolicy) {
        listenerManager.addOnViewAttachedToWindowListener(viewAttachedToWindowListener, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, int... itemViewTypes) {
        return addOnViewAttachedToWindowListener(viewAttachedToWindowListener, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    public AdapterConfigurator<T, VH> addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<VH> viewDetachedFromWindowListener, ItemBindingMatchPolicy matchPolicy) {
        listenerManager.addOnViewDetachedFromWindowListener(viewDetachedFromWindowListener, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<VH> viewDetachedFromWindowListener, int... itemViewTypes) {
        return addOnViewDetachedFromWindowListener(viewDetachedFromWindowListener, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    public AdapterConfigurator<T, VH> addOnViewRecycledListener(OnViewRecycledListener<VH> viewRecycledListener, ItemBindingMatchPolicy matchPolicy) {
        listenerManager.addOnViewRecycledListener(viewRecycledListener, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnViewRecycledListener(OnViewRecycledListener<VH> viewRecycledListener, int... itemViewTypes) {
        return addOnViewRecycledListener(viewRecycledListener, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    /**************************** 组件相关 ****************************/

    public AdapterConfigurator<T, VH> addComponent(Component<T, VH> component, ItemBindingMatchPolicy matchPolicy) {
        componentManager.addComponent(component, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addComponent(Component<T, VH> component) {
        return addComponent(component, ItemBindingMatchPolicy.MATCH_POLICY_ALL);
    }

    public AdapterConfigurator<T, VH> addComponent(Component<T, VH> component, int... itemViewTypes) {
        return addComponent(component, ItemBindingMatchPolicy.of(itemViewTypes));
    }

    public <C extends Component<T, VH>> AdapterConfigurator<T, VH> component(Class<C> type, Consumer<C> consumer) {
        componentManager.component(type, consumer);
        return this;
    }

    public AdapterConfigurator<T, VH> componentManager(Consumer<ComponentManager<T, VH>> consumer) {
        componentManager.componentManager(consumer);
        return this;
    }

    @SuppressWarnings("all")
    public AdapterConfigurator<T, VH> bindModeComponent(Consumer<BindModeComponent<T, VH>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            return componentManager(target -> {
                consumer.accept(target.getComponent(BindModeComponent.class));
            });
        }
        return this;
    }

    @SuppressWarnings("all")
    public AdapterConfigurator<T, VH> layoutManager(Consumer<LayoutManagerComponent<T, VH>> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            return componentManager(target -> consumer.accept(target.getComponent(LayoutManagerComponent.class)));
        }
        return this;
    }

    public void attachedToRecyclerView(RecyclerView recyclerView) {
        componentManager.apply(this);
        config();
        CommonlyAdapter<T, VH> adapter = new CommonlyAdapter<>(dataManager, itemViewManager, contextFactory, listenerManager);
        recyclerView.setAdapter(adapter);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AdapterConfigurator<T, VH> of(ViewHolderFactory<VH> viewHolderFactory) {
        return new AdapterConfigurator<>(viewHolderFactory);
    }

    public static <T> AdapterConfigurator<T, CommonlyViewHolder> of() {
        return of((itemView, viewType) -> new CommonlyViewHolder(itemView));
    }

}
