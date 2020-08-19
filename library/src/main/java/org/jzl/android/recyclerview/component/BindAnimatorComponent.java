package org.jzl.android.recyclerview.component;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.component.animator.AnimatorFactory;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.util.ObjectUtils;

public class BindAnimatorComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {
    private static final TimeInterpolator TIME_INTERPOLATOR_DEFAULT = new LinearInterpolator();

    private AnimatorFactory<VH> animatorFactory;
    private TimeInterpolator interpolator = TIME_INTERPOLATOR_DEFAULT;
    private long duration = 400;
    private long startDelay = 0;
    private int lastPlayAnimatorPosition = -1;
    private boolean enableAnimator = true;

    public BindAnimatorComponent(AnimatorFactory<VH> animatorFactory) {
        this.animatorFactory = ObjectUtils.requireNonNull(animatorFactory, "animatorFactory");
    }

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.addOnViewAttachedToWindowListener(holder -> {
            if (lastPlayAnimatorPosition < holder.getAdapterPosition() && isEnableAnimator()) {
                lastPlayAnimatorPosition = holder.getAdapterPosition();
                playAnimator(holder);
            }
        }, matchPolicy);
    }

    private void playAnimator(VH holder) {
        Animator animator = animatorFactory.animator(holder);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.start();
    }

    private boolean isEnableAnimator() {
        return enableAnimator;
    }

    public void setDuration(long duration) {
        this.duration = Math.max(0, duration);
    }

    public void setAnimatorFactory(AnimatorFactory<VH> animatorFactory) {
        this.animatorFactory = animatorFactory;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = ObjectUtils.get(interpolator, this.interpolator);
    }

    public void setStartDelay(long startDelay) {
        this.startDelay = Math.max(0, startDelay);
    }

    public void enableAnimator() {
        enableAnimator = true;
    }

    public void disableAnimator() {
        enableAnimator = false;
    }

}
