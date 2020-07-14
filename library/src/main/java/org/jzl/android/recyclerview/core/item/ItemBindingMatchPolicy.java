package org.jzl.android.recyclerview.core.item;

import org.jzl.lang.util.ArrayUtils;

public interface ItemBindingMatchPolicy {

    ItemBindingMatchPolicy MATCH_POLICY_ALL = itemType -> true;

    boolean match(int itemType);

    static ItemBindingMatchPolicy of(int... itemTypes) {
        return ArrayUtils.isEmpty(itemTypes) ? MATCH_POLICY_ALL : itemType -> ArrayUtils.contains(itemTypes, itemType);
    }

}
