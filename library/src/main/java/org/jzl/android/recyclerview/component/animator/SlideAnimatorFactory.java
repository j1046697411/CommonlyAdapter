package org.jzl.android.recyclerview.component.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SlideAnimatorFactory<VH extends RecyclerView.ViewHolder> implements AnimatorFactory<VH> {

    private Direction direction;

    public SlideAnimatorFactory(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Animator animator(VH holder) {
        switch (direction) {
            case BOTTOM: {
                return ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y, holder.itemView.getMeasuredHeight(), 0);
            }
            case LEFT: {
                View root = holder.itemView.getRootView();
                return ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_X, -root.getWidth(), 0);
            }
            case RIGHT: {
                View root = holder.itemView.getRootView();
                return ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_X, root.getWidth(), 0);
            }
        }
        return ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y, holder.itemView.getMeasuredHeight(), 0);
    }

    public enum Direction {
        BOTTOM, RIGHT, LEFT
    }
}
