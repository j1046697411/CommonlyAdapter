package org.jzl.android.recyclerview.diff;

public interface DiffCallback<T> {

    default boolean areItemsTheSame(T oldItemData, T newItemData) {
        return false;
    }

    default boolean areContentsTheSame(T oldItemData, T newItemData) {
        return false;
    }

    default Object getChangePayload(T oldItemData, T newItemData) {
        return null;
    }
}
