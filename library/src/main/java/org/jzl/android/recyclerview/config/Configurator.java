package org.jzl.android.recyclerview.config;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.item.DataBinder;
import org.jzl.android.recyclerview.core.item.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.plugin.Plugin;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataBlock;

public interface Configurator<T, VH extends RecyclerView.ViewHolder, CONFIG extends Configurator<T, VH, CONFIG>> {

    CONFIG dataBinding(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy);

    default CONFIG dataBindingByItemViewType(DataBinder<T, VH> binder, int... itemViewTypes) {
        return dataBinding(binder, DataBindingMatchPolicy.ofItemViewTypes(itemViewTypes));
    }

    default CONFIG dataBindingByPositionType(DataBinder<T, VH> binder, DataBlock.PositionType... positionTypes) {
        return dataBinding(binder, DataBindingMatchPolicy.ofPositionType(positionTypes));
    }

    default CONFIG dataBindingByBindModeNormal(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy) {
        return dataBinding(binder, ObjectUtils.get(matchPolicy, DataBindingMatchPolicy.MATCH_POLICY_ALL).and(DataBindingMatchPolicy.MATCH_POLICY_BIND_MODE_NORMAL));
    }

    default CONFIG dataBindingByBindModeNormalAndItemViewType(DataBinder<T, VH> binder, int... itemViewTypes) {
        return dataBinding(binder, DataBindingMatchPolicy.ofItemViewTypes(itemViewTypes).and(DataBindingMatchPolicy.MATCH_POLICY_BIND_MODE_NORMAL));
    }

    default CONFIG dataBindingByBindModeNormalAndPositionType(DataBinder<T, VH> binder, DataBlock.PositionType... positionTypes) {
        return dataBinding(binder, DataBindingMatchPolicy.ofPositionType(positionTypes).and(DataBindingMatchPolicy.MATCH_POLICY_BIND_MODE_NORMAL));
    }

    default CONFIG dataBindingByBindModePayloads(DataBinder<T, VH> binder, DataBindingMatchPolicy matchPolicy, Object... payloads) {
        return ObjectUtils.nonNull(matchPolicy) ?
                dataBinding(binder, matchPolicy.and(DataBindingMatchPolicy.ofPayloads(payloads)).or(matchPolicy.and(DataBindingMatchPolicy.MATCH_POLICY_BIND_MODE_NORMAL)))
                : dataBindingByBindModePayloads(binder, payloads);
    }

    default CONFIG dataBindingByBindModePayloads(DataBinder<T, VH> binder, Object... payloads) {
        return dataBinding(binder, DataBindingMatchPolicy.ofPayloads(payloads).or(DataBindingMatchPolicy.MATCH_POLICY_BIND_MODE_NORMAL));
    }

    CONFIG plugin(Plugin<T, VH, CONFIG> plugin, ItemBindingMatchPolicy matchPolicy);

    default CONFIG plugin(Plugin<T, VH, CONFIG> plugin, int... itemViewTypes) {
        return plugin(plugin, ItemBindingMatchPolicy.of(itemViewTypes));
    }

}