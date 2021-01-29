package org.jzl.android.recyclerview.core.configuration;

import android.view.LayoutInflater;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.R;
import org.jzl.android.recyclerview.core.CommonlyAdapter;
import org.jzl.android.recyclerview.core.Component;
import org.jzl.android.recyclerview.core.data.Classifiable;
import org.jzl.android.recyclerview.core.data.DataClassifier;
import org.jzl.android.recyclerview.core.data.DataProvider;
import org.jzl.android.recyclerview.core.factory.AdapterDataObserverFactory;
import org.jzl.android.recyclerview.core.factory.AdapterFactory;
import org.jzl.android.recyclerview.core.factory.ItemDataHolderFactory;
import org.jzl.android.recyclerview.core.factory.ItemViewFactory;
import org.jzl.android.recyclerview.core.factory.LayoutManagerFactory;
import org.jzl.android.recyclerview.core.item.DefaultItemDataUpdateHelper;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemDataHolderImpl;
import org.jzl.android.recyclerview.core.item.ItemDataUpdate;
import org.jzl.android.recyclerview.core.item.ItemDataUpdateHelper;
import org.jzl.android.recyclerview.manager.listener.DefaultListenerManager;
import org.jzl.android.recyclerview.manager.listener.ListenerManager;
import org.jzl.android.recyclerview.manager.listener.OnAttachedToRecyclerViewListener;
import org.jzl.android.recyclerview.manager.listener.OnCreatedViewHolderListener;
import org.jzl.android.recyclerview.manager.listener.OnDetachedFromRecyclerViewListener;
import org.jzl.android.recyclerview.manager.listener.OnItemClickListener;
import org.jzl.android.recyclerview.manager.listener.OnItemLongClickListener;
import org.jzl.android.recyclerview.manager.listener.OnViewAttachedToWindowListener;
import org.jzl.android.recyclerview.manager.listener.OnViewDetachedFromWindowListener;
import org.jzl.android.recyclerview.manager.listener.OnViewRecycledListener;
import org.jzl.android.recyclerview.core.observer.AdapterDataObserver;
import org.jzl.android.recyclerview.core.observer.DefaultAdapterDataObserver;
import org.jzl.android.recyclerview.core.Plugin;
import org.jzl.android.recyclerview.core.vh.DataBinder;
import org.jzl.android.recyclerview.core.vh.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.core.vh.DefaultViewBindHelper;
import org.jzl.android.recyclerview.core.vh.ViewBindHelper;
import org.jzl.android.recyclerview.core.vh.ViewHolderFactory;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.holder.BinaryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ConfigurationImpl<T, VH extends RecyclerView.ViewHolder> implements Configuration<T, VH> {

    private final ItemDataHolderFactory<T, VH> itemDataHolderFactory;
    private final ViewHolderFactory<VH> viewHolderFactory;
    private final ListenerManager<T, VH> listenerManager;
    private final LayoutManagerFactory layoutManagerFactory;
    private final AdapterFactory<T, VH> adapterFactory;
    private final ViewBindHelper<T, VH> viewBindHelper;
    private final DataProvider<T, VH> dataProvider;
    private final ItemDataUpdateHelper<T, VH> itemDataUpdateHelper;
    private final DataClassifier<T, VH> dataClassifier;

    private final AdapterDataObserverFactory<T, VH> adapterDataObserverFactory;

    private final List<BinaryHolder<ItemBindingMatchPolicy, ItemViewFactory>> itemViewFactories;
    private final List<BinaryHolder<DataBindingMatchPolicy, DataBinder<T, VH>>> dataBinders;
    private final List<ItemDataUpdate<T, VH>> itemDataUpdates;

    private final List<OnAttachedToRecyclerViewListener<T, VH>> attachedToRecyclerViewListeners;
    private final List<OnCreatedViewHolderListener<T, VH>> createdViewHolderListeners;
    private final List<OnDetachedFromRecyclerViewListener<T, VH>> detachedFromRecyclerViewListeners;
    private final List<OnViewAttachedToWindowListener<T, VH>> viewAttachedToWindowListeners;
    private final List<OnViewDetachedFromWindowListener<T, VH>> viewDetachedFromWindowListeners;
    private final List<OnViewRecycledListener<T, VH>> viewRecycledListeners;
    private final List<BinaryHolder<DataBindingMatchPolicy, OnItemClickListener<T, VH>>> itemClickListeners;
    private final List<BinaryHolder<DataBindingMatchPolicy, OnItemLongClickListener<T, VH>>> itemLongClickListeners;

    private final List<Component<T, VH>> components;

    private LayoutInflater layoutInflater;
    private AdapterDataObserver adapterDataObserver;
    private final RecyclerView recyclerView;

    public ConfigurationImpl(RecyclerView recyclerView, ConfigurationBuilderImpl<T, VH> configurationBuilder) {
        this.recyclerView = recyclerView;

        this.itemDataHolderFactory = configurationBuilder.itemDataHolderFactory;
        this.viewHolderFactory = configurationBuilder.viewHolderFactory;
        this.listenerManager = configurationBuilder.listenerManager;
        this.layoutManagerFactory = configurationBuilder.layoutManagerFactory;
        this.adapterFactory = configurationBuilder.adapterFactory;
        this.adapterDataObserverFactory = configurationBuilder.adapterDataObserverFactory;
        this.itemDataUpdateHelper = configurationBuilder.itemDataUpdateHelper;
        this.dataClassifier = configurationBuilder.dataClassifier;

        this.dataProvider = configurationBuilder.dataProvider;

        this.viewBindHelper = configurationBuilder.viewBindHelper;
        this.itemViewFactories = new ArrayList<>(configurationBuilder.itemViewFactories);
        this.dataBinders = new ArrayList<>(configurationBuilder.dataBinders);
        this.itemDataUpdates = new ArrayList<>(configurationBuilder.itemDataUpdates);
        this.components = new ArrayList<>(configurationBuilder.components);

        this.attachedToRecyclerViewListeners = new CopyOnWriteArrayList<>(configurationBuilder.attachedToRecyclerViewListeners);
        this.createdViewHolderListeners = new CopyOnWriteArrayList<>(configurationBuilder.createdViewHolderListeners);
        this.detachedFromRecyclerViewListeners = new CopyOnWriteArrayList<>(configurationBuilder.detachedFromRecyclerViewListeners);
        this.viewAttachedToWindowListeners = new CopyOnWriteArrayList<>(configurationBuilder.viewAttachedToWindowListeners);
        this.viewDetachedFromWindowListeners = new CopyOnWriteArrayList<>(configurationBuilder.viewDetachedFromWindowListeners);
        this.viewRecycledListeners = new CopyOnWriteArrayList<>(configurationBuilder.viewRecycledListeners);
        this.itemClickListeners = new CopyOnWriteArrayList<>(configurationBuilder.itemClickListeners);
        this.itemLongClickListeners = new CopyOnWriteArrayList<>(configurationBuilder.itemLongClickListeners);

        bind(recyclerView);
    }


    private void bind(RecyclerView recyclerView) {

        layoutInflater = LayoutInflater.from(recyclerView.getContext());

        RecyclerView.Adapter<VH> adapter = adapterFactory.createAdapter(recyclerView, this);
        RecyclerView.LayoutManager layoutManager = layoutManagerFactory.createLayoutManager(recyclerView);
        adapterDataObserver = adapterDataObserverFactory.createAdapterDataObserver(this, adapter);
        initialise(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setTag(R.id.tag_configuration, this);
    }

    private void initialise(RecyclerView recyclerView) {
        for (Component<T, VH> component : components) {
            component.initialise(recyclerView, this);
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public ItemDataHolderFactory<T, VH> getItemDataHolderFactory() {
        return itemDataHolderFactory;
    }

    @Override
    public DataProvider<T, VH> getDataProvider() {
        return dataProvider;
    }

    @Override
    public DataClassifier<T, VH> getDataClassifier() {
        return dataClassifier;
    }

    @Override
    public ViewHolderFactory<VH> getViewHolderFactory() {
        return viewHolderFactory;
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public ItemViewFactory matchItemViewFactory(int viewType) {
        BinaryHolder<ItemBindingMatchPolicy, ItemViewFactory> holder = ForeachUtils.findByOne(this.itemViewFactories, target -> target.one.match(viewType));
        if (ObjectUtils.nonNull(holder)) {
            return holder.two;
        }
        return null;
    }

    @Override
    public ViewBindHelper<T, VH> getViewBindHelper() {
        return viewBindHelper;
    }

    @Override
    public ItemDataUpdateHelper<T, VH> getItemDataUpdateHelper() {
        return itemDataUpdateHelper;
    }

    @Override
    public ListenerManager<T, VH> getListenerManager() {
        return listenerManager;
    }

    @Override
    public AdapterDataObserver getAdapterDataObserver() {
        return adapterDataObserver;
    }

    @Override
    public Iterable<ItemDataUpdate<T, VH>> getItemDataUpdates() {
        return itemDataUpdates;
    }


    @Override
    public Iterable<BinaryHolder<DataBindingMatchPolicy, DataBinder<T, VH>>> getDataBinders() {
        return dataBinders;
    }

    @Override
    public Iterable<OnAttachedToRecyclerViewListener<T, VH>> getOnAttachedToRecyclerViewListeners() {
        return this.attachedToRecyclerViewListeners;
    }

    @Override
    public Iterable<OnCreatedViewHolderListener<T, VH>> getOnCreatedViewHolderListeners() {
        return this.createdViewHolderListeners;
    }

    @Override
    public Iterable<OnDetachedFromRecyclerViewListener<T, VH>> getOnDetachedFromRecyclerViewListeners() {
        return this.detachedFromRecyclerViewListeners;
    }

    @Override
    public Iterable<OnViewAttachedToWindowListener<T, VH>> getOnViewAttachedToWindowListeners() {
        return this.viewAttachedToWindowListeners;
    }

    @Override
    public Iterable<OnViewDetachedFromWindowListener<T, VH>> getOnViewDetachedFromWindowListeners() {
        return this.viewDetachedFromWindowListeners;
    }

    @Override
    public Iterable<OnViewRecycledListener<T, VH>> getOnViewRecycledListeners() {
        return this.viewRecycledListeners;
    }

    @Override
    public Iterable<BinaryHolder<DataBindingMatchPolicy, OnItemClickListener<T, VH>>> getOnItemClickListeners() {
        return itemClickListeners;
    }

    @Override
    public Iterable<BinaryHolder<DataBindingMatchPolicy, OnItemLongClickListener<T, VH>>> getOnItemLongClickListeners() {
        return itemLongClickListeners;
    }

    @Override
    @SuppressWarnings("all")
    public <C extends Component<T, VH>> C getComponent(Class<C> type) {
        for (Component<T, VH> component : components) {
            if (type.isInstance(component)) {
                return (C) component;
            }
        }
        return null;
    }

    @Override
    public Configuration<T, VH> addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener) {
        if (ObjectUtils.nonNull(attachedToRecyclerViewListener)) {
            this.attachedToRecyclerViewListeners.add(attachedToRecyclerViewListener);
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnCreatedViewHolderListener(OnCreatedViewHolderListener<T, VH> createdViewHolderListener) {
        if (ObjectUtils.nonNull(createdViewHolderListener)) {
            this.createdViewHolderListeners.add(createdViewHolderListener);
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnDetachedFromRecyclerViewListener(OnDetachedFromRecyclerViewListener<T, VH> detachedFromRecyclerViewListener) {
        if (ObjectUtils.nonNull(detachedFromRecyclerViewListener)) {
            this.detachedFromRecyclerViewListeners.add(detachedFromRecyclerViewListener);
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<T, VH> viewAttachedToWindowListener) {
        if (ObjectUtils.nonNull(viewAttachedToWindowListener)) {
            this.viewAttachedToWindowListeners.add(viewAttachedToWindowListener);
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<T, VH> viewDetachedFromWindowListener) {
        if (ObjectUtils.nonNull(viewDetachedFromWindowListener)) {
            this.viewDetachedFromWindowListeners.add(viewDetachedFromWindowListener);
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnViewRecycledListener(OnViewRecycledListener<T, VH> viewRecycledListener) {
        if (ObjectUtils.nonNull(viewRecycledListener)) {
            this.viewRecycledListeners.add(viewRecycledListener);
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnItemClickListener(OnItemClickListener<T, VH> itemClickListener, DataBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(itemClickListener) && ObjectUtils.nonNull(matchPolicy)) {
            this.itemClickListeners.add(BinaryHolder.of(matchPolicy, itemClickListener));
        }
        return this;
    }

    @Override
    public Configuration<T, VH> addOnItemLongClickListener(OnItemLongClickListener<T, VH> itemLongClickListener, DataBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(itemLongClickListener) && ObjectUtils.nonNull(matchPolicy)) {
            this.itemLongClickListeners.add(BinaryHolder.of(matchPolicy, itemLongClickListener));
        }
        return this;
    }

    static class ConfigurationBuilderImpl<T, VH extends RecyclerView.ViewHolder> implements ConfigurationBuilder<T, VH> {

        private AdapterFactory<T, VH> adapterFactory = (recyclerView, configuration) -> new CommonlyAdapter<>(configuration);
        private ItemDataHolderFactory<T, VH> itemDataHolderFactory = ItemDataHolderImpl::new;
        private AdapterDataObserverFactory<T, VH> adapterDataObserverFactory = (configuration, adapter) -> new DefaultAdapterDataObserver(adapter);
        private LayoutManagerFactory layoutManagerFactory = recyclerView -> new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        private ListenerManager<T, VH> listenerManager = new DefaultListenerManager<>();
        private ViewBindHelper<T, VH> viewBindHelper = new DefaultViewBindHelper<>();
        private ItemDataUpdateHelper<T, VH> itemDataUpdateHelper = new DefaultItemDataUpdateHelper<>();

        private DataProvider<T, VH> dataProvider;
        private DataClassifier<T, VH> dataClassifier = (dataProvider, position) -> {
            T data = dataProvider.get(position);
            if (data instanceof Classifiable) {
                return ((Classifiable) data).getType();
            } else {
                return 0;
            }
        };

        private final List<BinaryHolder<ItemBindingMatchPolicy, ItemViewFactory>> itemViewFactories = new ArrayList<>();
        private final List<BinaryHolder<DataBindingMatchPolicy, DataBinder<T, VH>>> dataBinders = new ArrayList<>();
        private final List<ItemDataUpdate<T, VH>> itemDataUpdates = new ArrayList<>();

        private final List<OnAttachedToRecyclerViewListener<T, VH>> attachedToRecyclerViewListeners = new ArrayList<>();
        private final List<OnCreatedViewHolderListener<T, VH>> createdViewHolderListeners = new ArrayList<>();
        private final List<OnDetachedFromRecyclerViewListener<T, VH>> detachedFromRecyclerViewListeners = new ArrayList<>();
        private final List<OnViewAttachedToWindowListener<T, VH>> viewAttachedToWindowListeners = new ArrayList<>();
        private final List<OnViewDetachedFromWindowListener<T, VH>> viewDetachedFromWindowListeners = new ArrayList<>();
        private final List<OnViewRecycledListener<T, VH>> viewRecycledListeners = new ArrayList<>();
        private final List<BinaryHolder<DataBindingMatchPolicy, OnItemClickListener<T, VH>>> itemClickListeners = new ArrayList<>();
        private final List<BinaryHolder<DataBindingMatchPolicy, OnItemLongClickListener<T, VH>>> itemLongClickListeners = new ArrayList<>();

        private final List<Component<T, VH>> components = new ArrayList<>();

        private final ViewHolderFactory<VH> viewHolderFactory;


        private final List<Plugin<T, VH>> plugins = new ArrayList<>();

        public ConfigurationBuilderImpl(ViewHolderFactory<VH> viewHolderFactory) {
            this.viewHolderFactory = ObjectUtils.requireNonNull(viewHolderFactory, "viewHolderFactory");
        }

        @Override
        public ConfigurationBuilder<T, VH> setAdapterFactory(AdapterFactory<T, VH> adapterFactory) {
            this.adapterFactory = ObjectUtils.get(adapterFactory, this.adapterFactory);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setLayoutManagerFactory(LayoutManagerFactory layoutManagerFactory) {
            this.layoutManagerFactory = ObjectUtils.get(layoutManagerFactory, this.layoutManagerFactory);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setViewBindHelper(ViewBindHelper<T, VH> viewBindHelper) {
            this.viewBindHelper = ObjectUtils.get(viewBindHelper, this.viewBindHelper);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setItemDataUpdateHelper(ItemDataUpdateHelper<T, VH> itemDataUpdateHelper) {
            this.itemDataUpdateHelper = ObjectUtils.get(itemDataUpdateHelper, this.itemDataUpdateHelper);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setAdapterDataObserverFactory(AdapterDataObserverFactory<T, VH> adapterDataObserverFactory) {
            this.adapterDataObserverFactory = ObjectUtils.get(adapterDataObserverFactory, this.adapterDataObserverFactory);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setDataProvider(DataProvider<T, VH> dataProvider) {
            this.dataProvider = ObjectUtils.get(dataProvider, this.dataProvider);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setDataClassifier(DataClassifier<T, VH> dataClassifier) {
            this.dataClassifier = ObjectUtils.get(dataClassifier, this.dataClassifier);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setItemDataHolderFactory(ItemDataHolderFactory<T, VH> itemDataHolderFactory) {
            this.itemDataHolderFactory = ObjectUtils.get(itemDataHolderFactory, this.itemDataHolderFactory);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> setListenerManager(ListenerManager<T, VH> listenerManager) {
            this.listenerManager = ObjectUtils.get(listenerManager, this.listenerManager);
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> createItemView(int layoutId, int... itemTypes) {
            return createItemView((layoutInflater, parent, viewType) -> layoutInflater.inflate(layoutId, parent, false), itemTypes);
        }

        @Override
        public ConfigurationBuilder<T, VH> createItemView(ItemViewFactory itemViewFactory, int... itemTypes) {
            return createItemView(itemViewFactory, ItemBindingMatchPolicy.of(itemTypes));
        }

        @Override
        public ConfigurationBuilder<T, VH> createItemView(ItemViewFactory itemViewFactory, ItemBindingMatchPolicy matchPolicy) {
            if (ObjectUtils.nonNull(itemViewFactory) && ObjectUtils.nonNull(matchPolicy)) {
                this.itemViewFactories.add(BinaryHolder.of(matchPolicy, itemViewFactory));
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> updateItemData(ItemDataUpdate<T, VH> itemDataUpdate) {
            if (ObjectUtils.nonNull(itemDataUpdate)){
                this.itemDataUpdates.add(itemDataUpdate);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> dataBinding(DataBinder<T, VH> dataBinder, DataBindingMatchPolicy matchPolicy) {
            if (ObjectUtils.nonNull(dataBinder) && ObjectUtils.nonNull(matchPolicy)) {
                this.dataBinders.add(BinaryHolder.of(matchPolicy, dataBinder));
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> dataBindingByItemTypes(DataBinder<T, VH> dataBinder, int... itemTypes) {
            return dataBinding(dataBinder, DataBindingMatchPolicy.ofItemTypes(itemTypes));
        }

        @Override
        public ConfigurationBuilder<T, VH> dataBindingByPayloads(DataBinder<T, VH> dataBinder, Object... payloads) {
            return dataBinding(dataBinder, DataBindingMatchPolicy.ofPayloads(payloads));
        }

        @Override
        public ConfigurationBuilder<T, VH> dataBindingByPayloadsOrNotIncludedPayloads(DataBinder<T, VH> dataBinder, Object... payloads) {
            return dataBinding(dataBinder, DataBindingMatchPolicy.MATCH_POLICY_NOT_INCLUDED_PAYLOADS.or(DataBindingMatchPolicy.ofPayloads(payloads)));
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener) {
            if (ObjectUtils.nonNull(attachedToRecyclerViewListener)) {
                this.attachedToRecyclerViewListeners.add(attachedToRecyclerViewListener);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnCreatedViewHolderListener(OnCreatedViewHolderListener<T, VH> createdViewHolderListener) {
            if (ObjectUtils.nonNull(createdViewHolderListener)) {
                this.createdViewHolderListeners.add(createdViewHolderListener);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnDetachedFromRecyclerViewListener(OnDetachedFromRecyclerViewListener<T, VH> detachedFromRecyclerViewListener) {
            if (ObjectUtils.nonNull(detachedFromRecyclerViewListener)) {
                this.detachedFromRecyclerViewListeners.add(detachedFromRecyclerViewListener);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<T, VH> viewAttachedToWindowListener) {
            if (ObjectUtils.nonNull(viewAttachedToWindowListener)) {
                this.viewAttachedToWindowListeners.add(viewAttachedToWindowListener);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<T, VH> viewDetachedFromWindowListener) {
            if (ObjectUtils.nonNull(viewDetachedFromWindowListener)) {
                this.viewDetachedFromWindowListeners.add(viewDetachedFromWindowListener);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnViewRecycledListener(OnViewRecycledListener<T, VH> viewRecycledListener) {
            if (ObjectUtils.nonNull(viewRecycledListener)) {
                this.viewRecycledListeners.add(viewRecycledListener);
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnItemClickListener(OnItemClickListener<T, VH> itemClickListener, DataBindingMatchPolicy matchPolicy) {
            if (ObjectUtils.nonNull(itemClickListener) && ObjectUtils.nonNull(matchPolicy)) {
                this.itemClickListeners.add(BinaryHolder.of(matchPolicy, itemClickListener));
            }
            return this;
        }

        @Override
        public ConfigurationBuilder<T, VH> addOnItemLongClickListener(OnItemLongClickListener<T, VH> itemLongClickListener, DataBindingMatchPolicy matchPolicy) {
            if (ObjectUtils.nonNull(itemLongClickListener) && ObjectUtils.nonNull(matchPolicy)) {
                this.itemLongClickListeners.add(BinaryHolder.of(matchPolicy, itemLongClickListener));
            }
            return this;
        }

        @Override
        public final ConfigurationBuilder<T, VH> component(Component<T, VH> component) {
            if (ObjectUtils.nonNull(component)) {
                this.components.add(component);
            }
            return this;
        }

        @Override
        public final ConfigurationBuilder<T, VH> plugin(Plugin<T, VH> plugin) {
            if (ObjectUtils.nonNull(plugin)) {
                this.plugins.add(plugin);
            }
            return this;
        }

        @Override
        public Configuration<T, VH> build(RecyclerView recyclerView) {
            applyComponents();
            applyPlugins();
            return new ConfigurationImpl<>(recyclerView,this);
        }

        private void applyPlugins() {
            int i = 0;
            while (i < plugins.size()) {
                plugins.get(i).setup(this);
                i++;
            }
            plugins.clear();
        }

        private void applyComponents() {
            component(listenerManager)
                    .component(itemDataUpdateHelper)
                    .component(dataProvider)
                    .component(viewBindHelper);
        }
    }

}
