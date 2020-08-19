package org.jzl.android.recyclerview.component;

import android.util.SparseArray;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.context.ExtraItemContext;
import org.jzl.android.recyclerview.core.data.Extractable;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.util.SparseArrayUtils;
import org.jzl.lang.util.ObjectUtils;

public class BindModeComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {

    private final SparseArray<ConditionMode> conditionKeys = new SparseArray<>();

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.addOnUpdatedItemContextListener((context, dataProvider, holder, data) -> {
            if (SparseArrayUtils.nonEmpty(conditionKeys) && data instanceof Extractable) {
                Extractable extraData = (Extractable) data;
                int size = conditionKeys.size();
                for (int i = 0; i < size; i++) {
                    updateCondition(context, extraData, conditionKeys.keyAt(i), conditionKeys.valueAt(i));
                }
            }
        });
    }

    public void addCondition(@IdRes int conditionKey, ConditionMode conditionMode) {
        if (ObjectUtils.nonNull(conditionMode)) {
            conditionKeys.put(conditionKey, conditionMode);
        }
    }

    private void updateCondition(ExtraItemContext context, Extractable extraData, int key, ConditionMode conditionMode) {
        Object value = extraData.getExtra(key, null);
        if (ObjectUtils.nonNull(value)) {
            context.putExtra(key, value);
            if (conditionMode == ConditionMode.SINGLE) {
                extraData.removeExtra(key);
            }
        }
    }

    public enum ConditionMode {
        SINGLE, STICKY
    }

}
