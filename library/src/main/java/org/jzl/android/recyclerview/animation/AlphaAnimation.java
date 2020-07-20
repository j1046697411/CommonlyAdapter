package org.jzl.android.recyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.component.AnimationComponent;
import org.jzl.lang.util.MathUtils;

public class AlphaAnimation<VH extends RecyclerView.ViewHolder> implements AnimationComponent.Animation<VH> {

    private float from;

    public AlphaAnimation(float from) {
        this.from = MathUtils.clamp(from, 0, 1);
    }

    @Override
    public Animator animator(VH holder) {
        return ObjectAnimator.ofFloat(holder.itemView, "alpha", from, 1);
    }
}
