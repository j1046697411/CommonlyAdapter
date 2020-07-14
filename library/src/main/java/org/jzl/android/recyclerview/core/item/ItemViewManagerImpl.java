package org.jzl.android.recyclerview.core.item;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.R;
import org.jzl.android.recyclerview.core.CAContext;
import org.jzl.android.recyclerview.core.DataClassifier;
import org.jzl.android.recyclerview.core.ViewHolderFactory;
import org.jzl.android.recyclerview.data.DataProvider;
import org.jzl.android.recyclerview.util.Binary;
import org.jzl.android.util.SparseArrayUtils;
import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemViewManagerImpl<T, VH extends RecyclerView.ViewHolder> implements ItemViewManager<T, VH> {

    private ViewHolderFactory<VH> viewHolderFactory;
    private SparseArray<ItemViewFactory> itemViewFactories = new SparseArray<>();

    private List<Binary<ItemBindingMatchPolicy, DataClassifier<T>>> dataClassifiers = new ArrayList<>();
    private List<Binary<ItemBindingMatchPolicy, ItemBinder<T, VH>>> itemBinders = new ArrayList<>();

    public ItemViewManagerImpl(ViewHolderFactory<VH> viewHolderFactory) {
        this.viewHolderFactory = viewHolderFactory;
    }

    @Override
    public VH onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        ItemViewFactory itemViewFactory = itemViewFactories.get(viewType);
        ObjectUtils.requireNonNull(itemViewFactory, "itemViewFactory: viewType = " + viewType);
        return viewHolderFactory.createViewHolder(itemViewFactory.createItemView(layoutInflater, parent), viewType);
    }

    @Override
    public void bindItemViewData(DataProvider.DataBlock dataBlock, VH holder, T data, List<Object> payloads) {
        updateContext(dataBlock, holder, data, payloads);
        for (Binary<ItemBindingMatchPolicy, ItemBinder<T, VH>> binary : this.itemBinders) {
            if (binary.one.match(holder.getItemViewType())) {
                binary.two.binding(holder, data, payloads);
            }
        }
    }

    private void updateContext(DataProvider.DataBlock dataBlock, VH holder, T data, List<Object> payloads) {
        CAContext context = (CAContext) holder.itemView.getTag(R.id.tag_item_context);
        DataClassifier<T> dataClassifier = findDataClassifier(holder.getItemViewType());
        ObjectUtils.requireNonNull(dataClassifier, "dataClassifier");
        if (ObjectUtils.isNull(context)) {
            context = new CAContext();
            holder.itemView.setTag(R.id.tag_item_context, context);
        }
        if (ObjectUtils.nonNull(dataClassifier)) {
            context.dataType = dataClassifier.getDataType(data, holder.getAdapterPosition());
        }
        context.itemType = holder.getItemViewType();
        context.payloads = payloads;
        context.position = holder.getAdapterPosition();
        context.positionType = dataBlock.getPositionType();
        context.blockId = dataBlock.getBlockId();
    }

    private DataClassifier<T> findDataClassifier(int itemType) {
        for (Binary<ItemBindingMatchPolicy, DataClassifier<T>> dataClassifierHolder : this.dataClassifiers) {
            if (dataClassifierHolder.one.match(itemType)) {
                return dataClassifierHolder.two;
            }
        }
        return null;
    }

    public void addItemBinder(ItemBindingMatchPolicy matchPolicy, ItemBinder<T, VH> itemBinder) {
        this.itemBinders.add(Binary.of(matchPolicy, itemBinder));
    }

    public void addItemViewFactory(ItemViewFactory itemViewFactory, int... itemTypes) {
        if (ArrayUtils.isEmpty(itemTypes)) {
            itemViewFactories.put(-1, itemViewFactory);
        } else {
            SparseArrayUtils.puts(this.itemViewFactories, itemViewFactory, itemTypes);
        }
    }

    public void addDataClassifier(DataClassifier<T> dataClassifier, ItemBindingMatchPolicy matchPolicy) {
        this.dataClassifiers.add(Binary.of(matchPolicy, dataClassifier));
    }

}
