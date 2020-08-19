package org.jzl.android.recyclerview.component.select;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.data.Selectable;

public interface SelectInterceptor<T extends Selectable, VH extends RecyclerView.ViewHolder> {

    void intercept(VH holder, T data, Selector<T> selector);

}
