package org.jzl.android.recyclerview.core;

import org.jzl.android.recyclerview.core.item.ItemViewFactory;

public class TypeOpts {

    private int itemType;
    private PositionType positionType;
    private ItemViewFactory itemViewFactory;

    private TypeOpts(int itemType, PositionType positionType, ItemViewFactory itemViewFactory) {
        this.itemType = itemType;
        this.positionType = positionType;
        this.itemViewFactory = itemViewFactory;
    }

    public int getItemType() {
        return itemType;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public ItemViewFactory getItemViewFactory() {
        return itemViewFactory;
    }

    public static TypeOpts of(int itemType, PositionType positionType, ItemViewFactory itemViewFactory){
        return new TypeOpts(itemType, positionType, itemViewFactory);
    }

}
