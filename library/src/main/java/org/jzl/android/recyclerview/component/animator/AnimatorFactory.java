package org.jzl.android.recyclerview.component.animator;

import android.animation.Animator;

import androidx.recyclerview.widget.RecyclerView;

public interface AnimatorFactory<VH extends RecyclerView.ViewHolder> {
    Animator animator(VH holder);
}
