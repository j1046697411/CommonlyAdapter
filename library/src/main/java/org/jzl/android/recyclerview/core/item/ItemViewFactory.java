package org.jzl.android.recyclerview.core.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@FunctionalInterface
public interface ItemViewFactory {
    View createItemView(LayoutInflater layoutInflater, ViewGroup parent);
}
