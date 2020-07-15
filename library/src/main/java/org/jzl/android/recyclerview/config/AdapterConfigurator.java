package org.jzl.android.recyclerview.config;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.CommonlyViewHolder;
import org.jzl.android.recyclerview.R;
import org.jzl.android.recyclerview.component.Component;
import org.jzl.android.recyclerview.component.ComponentManager;
import org.jzl.android.recyclerview.component.LayoutManagerComponent;
import org.jzl.android.recyclerview.core.CAContext;
import org.jzl.android.recyclerview.core.DataClassifier;
import org.jzl.android.recyclerview.core.EntityFactory;
import org.jzl.android.recyclerview.core.ObjectBinder;
import org.jzl.android.recyclerview.core.PositionType;
import org.jzl.android.recyclerview.core.ViewHolderFactory;
import org.jzl.android.recyclerview.core.item.ItemBinder;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemViewFactory;
import org.jzl.android.recyclerview.core.item.ItemViewManagerImpl;
import org.jzl.android.recyclerview.data.DataBinder;
import org.jzl.android.recyclerview.data.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.data.DataManager;
import org.jzl.android.recyclerview.data.DataManagerImpl;
import org.jzl.android.recyclerview.data.DataSource;
import org.jzl.android.recyclerview.data.model.CyModel;
import org.jzl.android.recyclerview.listener.ListenerManagerImpl;
import org.jzl.android.recyclerview.listener.OnAttachedToRecyclerViewListener;
import org.jzl.android.recyclerview.listener.OnCreatedViewHolderListener;
import org.jzl.android.recyclerview.listener.OnViewAttachedToWindowListener;
import org.jzl.android.recyclerview.util.Binary;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.MathUtils;

public class AdapterConfigurator<T, VH extends RecyclerView.ViewHolder> extends AbstractConfigurator<T, VH, AdapterConfigurator<T, VH>> implements ItemConfigurator<T, VH, AdapterConfigurator<T, VH>> {

    public static final int DATA_BLOCK_DEFAULT = 1;

    private int pluginMaxNesting = 10;
    private ItemViewManagerImpl<T, VH> itemViewManager;
    private ListenerManagerImpl<T, VH> listenerManager;
    private DataManager<T> dataManager;
    private ComponentManager<T, VH> componentManager;

    protected AdapterConfigurator(ViewHolderFactory<VH> viewHolderFactory, EntityFactory<T> entityFactory) {
        super(ItemBindingMatchPolicy.MATCH_POLICY_ALL);
        this.itemViewManager = new ItemViewManagerImpl<>(viewHolderFactory);
        this.listenerManager = new ListenerManagerImpl<>();
        this.dataManager = new DataManagerImpl<>(entityFactory);
        this.componentManager = new ComponentManager<>();
        init();
    }

    private void init() {
        component(LayoutManagerComponent.of());
    }

    public <E> AdapterConfigurator<T, VH> dataBlock(PositionType positionType, int block, ObjectBinder<DataSource<E>> objectBinder) {
        objectBinder.bind(dataManager.dataSource(positionType, block));
        return this;
    }

    public AdapterConfigurator<T, VH> contentDataBlock(ObjectBinder<DataSource<T>> objectBinder) {
        objectBinder.bind(dataManager.dataSource(PositionType.CONTENT, DATA_BLOCK_DEFAULT));
        return this;
    }

    @Override
    protected AdapterConfigurator<T, VH> own() {
        return this;
    }

    @Override
    public int getPluginMaxNesting() {
        return pluginMaxNesting;
    }

    @Override
    public AdapterConfigurator<T, VH> setPluginMaxNesting(int pluginMaxNesting) {
        this.pluginMaxNesting = MathUtils.clamp(pluginMaxNesting, 1, 20);
        return this;
    }

    @Override
    public AdapterConfigurator<T, VH> itemBinding(ItemBinder<T, VH> binder, ItemBindingMatchPolicy matchPolicy) {
        itemViewManager.addItemBinder(matchPolicy, binder);
        return this;
    }

    @Override
    public AdapterConfigurator<T, VH> dataTypes(DataClassifier<T> dataClassifier, ItemBindingMatchPolicy matchPolicy) {
        itemViewManager.addDataClassifier(dataClassifier, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> createItemView(ItemViewFactory itemViewFactory, int... itemTypes) {
        itemViewManager.addItemViewFactory(itemViewFactory, itemTypes);
        return this;
    }

    public AdapterConfigurator<T, VH> createItemView(@LayoutRes int layoutResId, boolean attachToRoot, int... itemTypes) {
        return createItemView((layoutInflater, parent) -> layoutInflater.inflate(layoutResId, parent, attachToRoot), itemTypes);
    }

    public AdapterConfigurator<T, VH> createItemView(@LayoutRes int layoutResId, int... itemTypes) {
        return createItemView(layoutResId, false, itemTypes);
    }

    /******************* 监听者系统 start *******************/

    public AdapterConfigurator<T, VH> addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener) {
        listenerManager.addOnAttachedToRecyclerViewListener(attachedToRecyclerViewListener);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, ItemBindingMatchPolicy matchPolicy) {
        listenerManager.addOnCreatedViewHolderListener(createdViewHolderListener, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, int... itemTypes) {
        return addOnCreatedViewHolderListener(createdViewHolderListener, ItemBindingMatchPolicy.of(itemTypes));
    }

    public AdapterConfigurator<T, VH> addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, ItemBindingMatchPolicy matchPolicy) {
        listenerManager.addOnViewAttachedToWindowListener(viewAttachedToWindowListener, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, int... itemTypes) {
        return addOnViewAttachedToWindowListener(viewAttachedToWindowListener, ItemBindingMatchPolicy.of(itemTypes));
    }

    public AdapterConfigurator<T, VH> adapter(ObjectBinder<CommonlyAdapter<T, VH>> objectBinder) {
        listenerManager.addObjectBinder(objectBinder);
        return this;
    }

    public AdapterConfigurator<T, VH> recyclerView(ObjectBinder<RecyclerView> objectBinder) {
        listenerManager.addRecyclerViewObjectBinder(objectBinder);
        return this;
    }

    @Override
    public <E> ItemTypeConfigurator<T, E, VH, AdapterConfigurator<T, VH>> itemTypes(Function<T, E> mapper, DataClassifier<E> dataClassifier, int... itemTypes) {
        return new ItemTypeConfigurator<>(mapper, ItemBindingMatchPolicy.of(itemTypes), this, dataClassifier);
    }

    /******************* 监听者系统 end *******************/

    public AdapterConfigurator<T, VH> component(Component<T, VH> component, ItemBindingMatchPolicy matchPolicy) {
        componentManager.addComponent(component, matchPolicy);
        return this;
    }

    public AdapterConfigurator<T, VH> component(Component<T, VH> component, int... itemTypes) {
        return component(component, ItemBindingMatchPolicy.of(itemTypes));
    }

    public <C extends Component<T, VH>> AdapterConfigurator<T, VH> component(Class<C> type, Consumer<C> consumer) {
        consumer.accept(componentManager.getComponent(type));
        return this;
    }

    @SuppressWarnings("all")
    public AdapterConfigurator<T, VH> layoutManager(Consumer<LayoutManagerComponent<T, VH>> consumer) {
        return component(LayoutManagerComponent.class, target -> {
            consumer.accept(target);
        });
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        componentManager.apply(this);
        config(this, (configurator, matchPolicy, dataBinders) -> configurator.itemBinding((holder, data, payloads) -> {
            CAContext context = (CAContext) holder.itemView.getTag(R.id.tag_item_context);
            for (Binary<DataBindingMatchPolicy, DataBinder<T, VH>> binary : dataBinders) {
                if (binary.one.match(context)) {
                    binary.two.bind(context, holder, data);
                }
            }
        }, matchPolicy));
        adapter(target -> dataManager.bind(target));
        recyclerView.setAdapter(CommonlyAdapter.of(dataManager, itemViewManager, listenerManager, componentManager));

    }

    public static <T, VH extends RecyclerView.ViewHolder> AdapterConfigurator<T, VH> of(ViewHolderFactory<VH> viewHolderFactory, EntityFactory<T> entityFactory) {
        return new AdapterConfigurator<>(viewHolderFactory, entityFactory);
    }

    public static <VH extends RecyclerView.ViewHolder> AdapterConfigurator<CyModel, VH> of(ViewHolderFactory<VH> viewHolderFactory) {
        return of(viewHolderFactory, (itemType, data) -> data instanceof CyModel ? (CyModel) data : new CyModel(data, itemType));
    }

    public static AdapterConfigurator<CyModel, CommonlyViewHolder> of() {
        return of((itemView, viewType) -> new CommonlyViewHolder(itemView));
    }

}
