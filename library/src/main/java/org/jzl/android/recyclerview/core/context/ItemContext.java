package org.jzl.android.recyclerview.core.context;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.lang.util.datablcok.DataBlock;

import java.util.List;

public interface ItemContext {

    int getLayoutPosition();

    int getAdapterPosition();

    int getItemViewType();

    DataBlock.PositionType getPositionType();

    int getBlockId();

    List<Object> getPayloads();

    RecyclerView.Adapter<?> getAdapter();

    BindMode getBindMode();

    <T> T getExtra(@IdRes int key, T def);
}
