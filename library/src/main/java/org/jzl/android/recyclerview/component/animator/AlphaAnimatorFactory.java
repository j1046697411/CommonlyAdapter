package org.jzl.android.recyclerview.component.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.lang.util.MathUtils;

public class AlphaAnimatorFactory<VH extends RecyclerView.ViewHolder> implements AnimatorFactory<VH> {

    private float fromValue;
    private float toValue;

    public AlphaAnimatorFactory(float fromValue, float toValue) {
        this.fromValue = MathUtils.clamp(fromValue, 0, 1);
        this.toValue = MathUtils.clamp(toValue, 0, 1);
    }

    @Override
    public Animator animator(VH holder) {
        return ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, fromValue, toValue);
    }
}
