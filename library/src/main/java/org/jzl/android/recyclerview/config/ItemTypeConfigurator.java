package org.jzl.android.recyclerview.config;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.R;
import org.jzl.android.recyclerview.core.CAContext;
import org.jzl.android.recyclerview.core.DataClassifier;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.data.DataBinder;
import org.jzl.android.recyclerview.data.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.util.Binary;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.ObjectUtils;


public class ItemTypeConfigurator<E, T, VH extends RecyclerView.ViewHolder, C extends ItemConfigurator<E, VH, C>> extends AbstractConfigurator<T, VH, ItemTypeConfigurator<E, T, VH, C>> {

    private C itemConfigurator;
    private Function<E, T> mapper;
    private DataClassifier<T> dataClassifier;

    public ItemTypeConfigurator(Function<E, T> mapper, ItemBindingMatchPolicy matchPolicy, C itemConfigurator, DataClassifier<T> dataClassifier) {
        super(matchPolicy);
        this.itemConfigurator = ObjectUtils.requireNonNull(itemConfigurator, "itemConfigurator");
        this.mapper = ObjectUtils.requireNonNull(mapper, "mapper");
        this.dataClassifier = ObjectUtils.requireNonNull(dataClassifier, "dataClassifier");
    }

    @Override
    protected int getPluginMaxNesting() {
        return itemConfigurator.getPluginMaxNesting();
    }

    @Override
    protected ItemTypeConfigurator<E, T, VH, C> own() {
        return this;
    }

    public C and() {
        return config(itemConfigurator, (configurator, matchPolicy, dataBinders) -> {
            configurator.dataTypes((data, position) -> this.dataClassifier.getDataType(mapper.apply(data), position), matchPolicy);
            configurator.itemBinding((holder, data, payloads) -> {
                CAContext context = (CAContext) holder.itemView.getTag(R.id.tag_item_context);
                T entity = mapper.apply(data);
                for (Binary<DataBindingMatchPolicy, DataBinder<T, VH>> binary : dataBinders) {
                    if (binary.one.match(context)) {
                        binary.two.bind(context, holder, entity);
                    }
                }
            }, matchPolicy);
        });
    }

}
