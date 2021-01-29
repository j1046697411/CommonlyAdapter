package org.jzl.android.recyclerview.core.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.configuration.Configuration;
import org.jzl.android.recyclerview.core.observer.AdapterDataObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultListDataProvider<T, VH extends RecyclerView.ViewHolder> implements DataProvider<T, VH> {

    private final List<T> data = new ArrayList<>();
    private AdapterDataObserver adapterDataObserver;


    @Override
    public void initialise(RecyclerView recyclerView, Configuration<T, VH> configuration) {
        this.adapterDataObserver = configuration.getAdapterDataObserver();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public T get(int position) {
        return data.get(position);
    }

    @Override
    public int indexOf(T data) {
        return this.data.indexOf(data);
    }

    public boolean add(T t) {
        return data.add(t);
    }

    public boolean remove(@Nullable Object o) {
        return data.remove(o);
    }

    public boolean addAll(@NonNull Collection<? extends T> c) {
        return data.addAll(c);
    }

    public boolean addAll(int index, @NonNull Collection<? extends T> c) {
        return data.addAll(index, c);
    }

    public boolean removeAll(@NonNull Collection<?> c) {
        return data.removeAll(c);
    }

    public boolean retainAll(@NonNull Collection<?> c) {
        return data.retainAll(c);
    }

    public T remove(int index) {
        return data.remove(index);
    }
}
