package org.jzl.android.recyclerview.core.context;

import android.os.Build;
import android.util.SparseArray;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.CommonlyAdapter;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataBlock;

import java.util.List;

public final class ItemContextImpl<T, VH extends RecyclerView.ViewHolder> implements ExtraItemContext, ItemContentUpdatable<T, VH> {

    private CommonlyAdapter<T, VH> adapter;

    private BindMode bindMode = BindMode.NORMAL;

    private int layoutPosition;
    private int adapterPosition;
    private int itemViewType;

    private DataBlock.PositionType positionType;
    private int blockId;

    private List<Object> payloads;
    private DataProvider<T> dataProvider;
    private final SparseArray<Object> extras = new SparseArray<>();

    public ItemContextImpl(CommonlyAdapter<T, VH> adapter, DataProvider<T> dataProvider) {
        this.adapter = ObjectUtils.requireNonNull(adapter, "adapter");
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
    }

    @Override
    public void update(VH holder, T data, List<Object> payloads) {
        this.layoutPosition = holder.getLayoutPosition();
        this.adapterPosition = holder.getAdapterPosition();
        this.payloads = payloads;
        this.itemViewType = holder.getItemViewType();
        DataBlock<T> dataBlock = dataProvider.findDataBlockByPosition(holder.getAdapterPosition());
        this.blockId = dataBlock.getBlockId();
        this.positionType = dataBlock.getPositionType();
        this.bindMode = CollectionUtils.nonEmpty(payloads) ? BindMode.PAYLOADS : BindMode.NORMAL;
    }

    @Override
    public int getLayoutPosition() {
        return layoutPosition;
    }

    @Override
    public int getAdapterPosition() {
        return adapterPosition;
    }

    @Override
    public int getItemViewType() {
        return itemViewType;
    }

    @Override
    public DataBlock.PositionType getPositionType() {
        return positionType;
    }

    @Override
    public int getBlockId() {
        return blockId;
    }

    @Override
    public List<Object> getPayloads() {
        return payloads;
    }

    @Override
    public CommonlyAdapter<T, VH> getAdapter() {
        return adapter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E getExtra(int key, E defValue) {
        return (E) extras.get(key, defValue);
    }

    @Override
    public void putExtra(int key, Object value) {
        extras.put(key, value);
    }

    @Override
    public boolean hasExtra(int key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return extras.contains(key);
        } else {
            return ObjectUtils.nonNull(extras.get(key));
        }
    }

    @Override
    public void removeExtra(int key) {
        extras.remove(key);
    }

    @Override
    public BindMode getBindMode() {
        return bindMode;
    }
}
