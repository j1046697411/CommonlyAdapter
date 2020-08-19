package org.jzl.android.recyclerview.component.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.util.Logger;

public class ScaleAnimatorFactory<VH extends RecyclerView.ViewHolder> implements AnimatorFactory<VH> {
    private static final Logger log = Logger.logger(ScaleAnimatorFactory.class);

    public static final int TYPE_SCALE_X = 1;
    public static final int TYPE_SCALE_Y = 2;
    public static final int TYPE_SCALE_XY = TYPE_SCALE_X | TYPE_SCALE_Y;

    public float fromValue;
    private float toValue = 1;
    private int type = 0;

    public ScaleAnimatorFactory(float fromValue, int type) {
        this.fromValue = fromValue;
        this.type = type;
    }

    @Override
    public Animator animator(VH holder) {
        if ((type & TYPE_SCALE_Y) == TYPE_SCALE_Y) {
            return scaleYObjectAnimator(holder);
        } else if ((type & TYPE_SCALE_X) == TYPE_SCALE_X) {
            return scaleXObjectAnimator(holder);
        } else if ((type & TYPE_SCALE_XY) == TYPE_SCALE_XY) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleXObjectAnimator(holder), scaleYObjectAnimator(holder));
            return animatorSet;
        } else {
            return scaleXObjectAnimator(holder);
        }
    }

    public ObjectAnimator scaleXObjectAnimator(VH holder) {
        return ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, fromValue, toValue);
    }

    public ObjectAnimator scaleYObjectAnimator(VH holder) {
        return ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, fromValue, toValue);
    }
}
