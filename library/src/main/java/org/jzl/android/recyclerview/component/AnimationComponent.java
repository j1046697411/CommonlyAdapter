package org.jzl.android.recyclerview.component;

import android.animation.Animator;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.util.ObjectUtils;

public class AnimationComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {

    private Animation<VH> animation;
    private boolean enable = true;
    private int lastPosition = -1;

    private AnimationComponent(Animation<VH> animation) {
        this.animation = ObjectUtils.requireNonNull(animation, "animation");
    }

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.addOnViewAttachedToWindowListener(holder -> {
            int adapterPosition = holder.getAdapterPosition();
            if (ObjectUtils.nonNull(animation) && enable && lastPosition < adapterPosition) {
                animation.animator(holder).start();
                this.lastPosition = adapterPosition;
            }
        }, matchPolicy);
    }

    public interface Animation<VH extends RecyclerView.ViewHolder> {
        Animator animator(VH holder);
    }

    public AnimationComponent<T, VH> setAnimation(Animation<VH> animation) {
        this.animation = ObjectUtils.get(animation, this.animation);
        return this;
    }

    public AnimationComponent<T, VH> setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationComponent<T, VH> animation(Animation<VH> animation) {
        return new AnimationComponent<>(animation);
    }



}
