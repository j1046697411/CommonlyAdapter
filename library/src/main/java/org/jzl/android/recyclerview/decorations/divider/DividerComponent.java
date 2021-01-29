package org.jzl.android.recyclerview.decorations.divider;

import android.graphics.Rect;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.Component;
import org.jzl.android.recyclerview.core.configuration.Configuration;

public class DividerComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {

    private boolean enableDivider = true;
    private int offset;
    private final Rect bounds = new Rect();
    private RecyclerView recyclerView;

    @Override
    public void initialise(RecyclerView recyclerView, Configuration<T, VH> configuration) {
        this.recyclerView = recyclerView;

    }
}
