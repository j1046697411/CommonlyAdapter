package org.jzl.android.recyclerview.data;

import org.jzl.android.recyclerview.core.CAContext;
import org.jzl.android.recyclerview.core.PositionType;
import org.jzl.lang.util.ArrayUtils;

public interface DataBindingMatchPolicy {

    DataBindingMatchPolicy MATCH_POLICY_ALL = context -> true;
    DataBindingMatchPolicy MATCH_POLICY_HEADER = context -> context.positionType == PositionType.HEADER;
    DataBindingMatchPolicy MATCH_POLICY_FOOTER = context -> context.positionType == PositionType.FOOTER;
    DataBindingMatchPolicy MATCH_POLICY_CONTENT = context -> context.positionType == PositionType.CONTENT;

    boolean match(CAContext context);

    default DataBindingMatchPolicy and(DataBindingMatchPolicy matchPolicy) {
        return context -> this.match(context) && matchPolicy.match(context);
    }

    default DataBindingMatchPolicy or(DataBindingMatchPolicy matchPolicy) {
        return context -> this.match(context) || matchPolicy.match(context);
    }

    default DataBindingMatchPolicy not() {
        return context -> !this.match(context);
    }

    static DataBindingMatchPolicy ofDataTypes(int... dataTypes) {
        return ArrayUtils.isEmpty(dataTypes) ? MATCH_POLICY_ALL : context -> ArrayUtils.contains(dataTypes, context.dataType);

    }

    static DataBindingMatchPolicy ofItemTypes(int... itemTypes) {
        return ArrayUtils.isEmpty(itemTypes) ? MATCH_POLICY_ALL : context -> ArrayUtils.contains(itemTypes, context.itemType);
    }

}
