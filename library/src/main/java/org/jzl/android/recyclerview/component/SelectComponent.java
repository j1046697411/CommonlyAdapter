package org.jzl.android.recyclerview.component;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.R;
import org.jzl.android.recyclerview.component.select.SelectInterceptor;
import org.jzl.android.recyclerview.component.select.SelectMode;
import org.jzl.android.recyclerview.component.select.Selector;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.CommonlyViewHolder;
import org.jzl.android.recyclerview.core.data.Extractable;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.data.Selectable;
import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataBlockProvider;

import java.util.List;

public class SelectComponent<T extends Selectable, VH extends RecyclerView.ViewHolder> implements Component<T, VH>, Selector<T> {

    public static final String PAYLOAD_SELECT = "select_update";

    public static int CONDITION_SELECT = R.id.condition_select;

    private DataBlockProvider<T> dataBlockProvider;

    private SelectInterceptor<T, VH> interceptor;
    private SelectMode selectMode = SelectMode.SINGLE;
    private AdapterDataObserver adapterDataObserver;

    protected SelectComponent(SelectInterceptor<T, VH> interceptor) {
        this.interceptor = ObjectUtils.requireNonNull(interceptor, "interceptor");
    }

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.adapterDataObserver(target -> this.adapterDataObserver = target)
                .bindModeComponent(target -> target.addCondition(R.id.condition_select, BindModeComponent.ConditionMode.SINGLE))
                .dataBinding((holder, data) -> interceptor.intercept(holder, data, this), matchPolicy.toDataBindingMatchPolicy())
                .dataBlockProvider(target -> this.dataBlockProvider = target);
    }

    @Override
    public void checked(T data, boolean checked) {
        if (selectMode == SelectMode.SINGLE && checked) {
            uncheckedAll();
        }
        updateDataModel(-1, data, checked);
    }

    public void checkedAll() {
        ForeachUtils.each(dataBlockProvider, (index, target) -> {
            if (!target.isChecked()) {
                updateDataModel(index, target, true);
            }
        });
    }

    private void notifyItemRangeChanged(int position) {
        if (ObjectUtils.nonNull(adapterDataObserver) && CollectionUtils.nonEmpty(dataBlockProvider) && position >= 0) {
            adapterDataObserver.notifyItemChanged(position, PAYLOAD_SELECT);
        }
    }

    private void updateDataModel(int position, T data, boolean checked) {
        if (data instanceof Extractable && data.isChecked() != checked) {
            ((Extractable) data).putExtra(CONDITION_SELECT, Boolean.TRUE);
            data.checked(checked);
        }
        if (position == -1 && ObjectUtils.nonNull(dataBlockProvider)) {
            position = dataBlockProvider.indexOf(data);
        }
        notifyItemRangeChanged(position);
    }

    @Override
    public void uncheckedAll() {
        ForeachUtils.each(dataBlockProvider, (index, target) -> {
            if (target.isChecked()) {
                updateDataModel(index, target, false);
            }
        });
    }

    public void setSelectMode(SelectMode selectMode) {
        this.selectMode = ObjectUtils.get(selectMode, this.selectMode);
    }

    public void switchSelectMode() {
        setSelectMode(selectMode == SelectMode.SINGLE ? SelectMode.MULTI : SelectMode.SINGLE);
    }

    public List<T> getResult(@NonNull final List<T> result) {
        result.clear();
        if (CollectionUtils.nonEmpty(dataBlockProvider)) {
            ForeachUtils.each(dataBlockProvider, target -> {
                if (target.isChecked()) {
                    result.add(target);
                }
            });
        }
        return result;
    }

    public List<T> getResult() {
        return getResult(CollectionUtils.newArrayList());
    }

    public static <T extends Selectable, VH extends RecyclerView.ViewHolder> SelectComponent<T, VH> of(SelectInterceptor<T, VH> interceptor) {
        return new SelectComponent<>(interceptor);
    }

    public static <T extends Selectable, VH extends RecyclerView.ViewHolder> SelectComponent<T, VH> of(int... viewIds) {
        return of(new SimpleSelectInterceptor<>(viewIds));
    }

    private static class SimpleSelectInterceptor<T extends Selectable, VH extends RecyclerView.ViewHolder> implements SelectInterceptor<T, VH> {

        private final int[] ids;

        public SimpleSelectInterceptor(int... ids) {
            this.ids = ids;
        }

        @Override
        public void intercept(VH holder, T data, Selector<T> selector) {
            if (ArrayUtils.nonEmpty(ids)) {
                bindClicks(holder, ids, view -> selector.checked(data, !data.isChecked()));
            } else {
                holder.itemView.setOnClickListener(view -> selector.checked(data, !data.isChecked()));
            }
        }

        private void bindClicks(VH holder, int[] ids, View.OnClickListener clickListener) {
            for (int id : ids) {
                bindClicks(holder, id, clickListener);
            }
        }

        private void bindClicks(VH holder, int id, View.OnClickListener clickListener) {
            if (holder instanceof CommonlyViewHolder) {
                ((CommonlyViewHolder) holder).provide().bindClickListener(id, clickListener);
            } else {
                holder.itemView.findViewById(id).setOnClickListener(clickListener);
            }
        }
    }

}
