package org.jzl.android.recyclerview.component.animator;

import android.animation.Animator;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.holder.BinaryHolder;

import java.util.List;

public class MatchPolicyAnimatorFactory<VH extends RecyclerView.ViewHolder> implements AnimatorFactory<VH> {

    private List<BinaryHolder<MatchPolicy<VH>, AnimatorFactory<VH>>> animatorFactories = CollectionUtils.newArrayList();
    private AnimatorFactory<VH> defaultAnimatorFactory = new SlideAnimatorFactory<>(SlideAnimatorFactory.Direction.BOTTOM);

    public MatchPolicyAnimatorFactory(AnimatorFactory<VH> defaultAnimatorFactory) {
        this.defaultAnimatorFactory = defaultAnimatorFactory;
    }

    @Override
    public Animator animator(VH holder) {
        BinaryHolder<MatchPolicy<VH>, AnimatorFactory<VH>> binaryHolder = ForeachUtils.findByOne(this.animatorFactories, target -> target.one.match(holder));
        if (ObjectUtils.nonNull(binaryHolder)) {
            return binaryHolder.two.animator(holder);
        } else {
            return defaultAnimatorFactory.animator(holder);
        }
    }

    public MatchPolicyAnimatorFactory<VH> addAnimatorFactory(MatchPolicy<VH> matchPolicy, AnimatorFactory<VH> animatorFactory) {
        if (ObjectUtils.nonNull(animatorFactory) && ObjectUtils.nonNull(matchPolicy)) {
            animatorFactories.add(BinaryHolder.of(matchPolicy, animatorFactory));
        }
        return this;
    }

    public interface MatchPolicy<VH extends RecyclerView.ViewHolder> {
        boolean match(VH holder);
    }

}
