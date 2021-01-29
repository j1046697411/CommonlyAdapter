package org.jzl.android.recyclerview.core.data;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.Component;
import org.jzl.android.recyclerview.core.configuration.Configuration;

public interface DataProvider<T, VH extends RecyclerView.ViewHolder> extends Component<T, VH> {

    @Override
    void initialise(RecyclerView recyclerView, Configuration<T, VH> configuration);

    int size();

    T get(int position);

    int indexOf(T data);

}
