package org.jzl.android.recyclerview.core.item;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.context.ItemContext;
import org.jzl.android.recyclerview.core.ItemViewManager;
import org.jzl.android.recyclerview.core.ViewHolderFactory;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.holder.BinaryHolder;

import java.util.List;

public class ItemViewManagerImpl<T, VH extends RecyclerView.ViewHolder> implements ItemViewManager<T, VH> {

    private List<BinaryHolder<DataBindingMatchPolicy, DataBinder<T, VH>>> dataBinders = CollectionUtils.newArrayList();
    private List<BinaryHolder<ItemBindingMatchPolicy, ItemViewFactory>> itemViewFactories = CollectionUtils.newArrayList();

    private ViewHolderFactory<VH> viewHolderFactory;

    public ItemViewManagerImpl(ViewHolderFactory<VH> viewHolderFactory) {
        this.viewHolderFactory = ObjectUtils.requireNonNull(viewHolderFactory, "viewHolderFactory");
    }

    @Override
    public VH onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        BinaryHolder<ItemBindingMatchPolicy, ItemViewFactory> binaryHolder = ForeachUtils.findByOne(this.itemViewFactories, target -> target.one.match(viewType));
        ObjectUtils.requireNonNull(binaryHolder, "itemViewFactory(" + viewType + ")");
        return viewHolderFactory.createViewHolder(binaryHolder.two.createItemView(layoutInflater, parent), viewType);
    }

    @Override
    public void bindItemViewData(ItemContext context, VH holder, T data) {
        ForeachUtils.each(this.dataBinders, target -> {
            if (target.one.match(context)) {
                target.two.binding(holder, data);
            }
        });
    }

    public void dataBinding(DataBinder<T, VH> dataBinder, DataBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(dataBinder)) {
            this.dataBinders.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, DataBindingMatchPolicy.MATCH_POLICY_ALL), dataBinder));
        }
    }

    public void createItemView(ItemViewFactory itemViewFactory, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(itemViewFactory)) {
            this.itemViewFactories.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), itemViewFactory));
        }
    }

}
