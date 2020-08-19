package org.jzl.android.recyclerview.core.item;

import org.jzl.android.recyclerview.core.context.BindMode;
import org.jzl.android.recyclerview.core.context.ItemContext;
import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.datablcok.DataBlock;

import java.util.List;

public interface DataBindingMatchPolicy {

    DataBindingMatchPolicy MATCH_POLICY_ALL = context -> true;

    DataBindingMatchPolicy MATCH_POLICY_BIND_MODE_NORMAL = context -> context.getBindMode() == BindMode.NORMAL;

    DataBindingMatchPolicy MATCH_POLICY_BIND_MODE_PAYLOADS = context -> context.getBindMode() == BindMode.PAYLOADS;

    boolean match(ItemContext context);

    default DataBindingMatchPolicy and(DataBindingMatchPolicy matchPolicy) {
        return context -> match(context) && matchPolicy.match(context);
    }

    default DataBindingMatchPolicy or(DataBindingMatchPolicy matchPolicy) {
        return context -> match(context) || matchPolicy.match(context);
    }

    default DataBindingMatchPolicy and(ItemBindingMatchPolicy matchPolicy) {
        return context -> match(context) && matchPolicy.match(context.getItemViewType());
    }

    default DataBindingMatchPolicy or(ItemBindingMatchPolicy matchPolicy) {
        return context -> match(context) || matchPolicy.match(context.getItemViewType());
    }

    default DataBindingMatchPolicy not() {
        return context -> !match(context);
    }

    static DataBindingMatchPolicy ofItemViewTypes(int... itemViewTypes) {
        if (ArrayUtils.nonEmpty(itemViewTypes)) {
            return context -> ArrayUtils.contains(itemViewTypes, context.getItemViewType());
        } else {
            return MATCH_POLICY_ALL;
        }
    }

    static DataBindingMatchPolicy ofPositionType(DataBlock.PositionType... positionTypes) {
        if (ArrayUtils.nonEmpty(positionTypes)) {
            return context -> ArrayUtils.contains(positionTypes, context.getPositionType(), true);
        }
        return MATCH_POLICY_ALL;
    }

    static DataBindingMatchPolicy ofPayloads(Object... payloads) {
        if (ArrayUtils.nonEmpty(payloads)) {
            return context -> {
                List<Object> objects = context.getPayloads();
                if (CollectionUtils.nonEmpty(objects)) {
                    for (Object o : objects) {
                        if (ArrayUtils.contains(payloads, o)) {
                            return true;
                        }
                    }
                }
                return false;
            };
        } else {
            return MATCH_POLICY_ALL;
        }
    }

}
