package org.jzl.android.recyclerview.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.app.databinding.ActivityMainBinding;
import org.jzl.android.recyclerview.core.configuration.Configuration;
import org.jzl.android.recyclerview.core.data.DataProvider;
import org.jzl.android.recyclerview.core.data.Selectable;
import org.jzl.android.recyclerview.decorations.divider.DividerItemDecoration;
import org.jzl.android.recyclerview.manager.animator.AnimatorManager;
import org.jzl.android.recyclerview.manager.animator.factory.SlideInLeftAnimationFactory;
import org.jzl.android.recyclerview.manager.selection.SelectionManager;
import org.jzl.android.recyclerview.manager.selection.SelectionMode;
import org.jzl.android.recyclerview.core.observer.AdapterDataObserver;
import org.jzl.android.recyclerview.core.vh.CommonlyViewHolder;
import org.jzl.android.recyclerview.core.vh.DataBindingMatchPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final List<Student> data = new ArrayList<>();
    private AdapterDataObserver adapterDataObserver;
    private final static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        for (int i = 0; i < 100; i++) {
            Student student = new Student(String.valueOf(i));
            student.setIcon(random.nextBoolean() ? R.mipmap.test :  R.mipmap.test2);
            data.add(student);
        }

        Configuration<Student, CommonlyViewHolder> configuration = Configuration.<Student>builder()
                .createItemView(R.layout.item_text_test)
                .setDataProvider(new DataProvider<Student, CommonlyViewHolder>() {

                    @Override
                    public void initialise(RecyclerView recyclerView, Configuration<Student, CommonlyViewHolder> configuration) {
                        adapterDataObserver = configuration.getAdapterDataObserver();
                    }

                    @Override
                    public int size() {
                        return data.size();
                    }

                    @Override
                    public Student get(int position) {
                        return data.get(position);
                    }

                    @Override
                    public int indexOf(Student data) {
                        return MainActivity.this.data.indexOf(data);
                    }
                })
                .updateItemData(dataHolder -> {
                    Log.v(MainActivity.class.getSimpleName(), "updateItemData => " + dataHolder.getAdapterPosition());
                })
                .plugin(SelectionManager.of(SelectionMode.MULTIPLE))
                .plugin(AnimatorManager.<Student, CommonlyViewHolder>of(SlideInLeftAnimationFactory.of()).enableAnimator(false))
//                .setLayoutManagerFactory(recyclerView -> {
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 4);
//                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                        @Override
//                        public int getSpanSize(int position) {
//                            return position % 5 == 0 ? 4 : position % 6 == 0 || position % 6 == 1 ? 2 : 1;
//                        }
//                    });
//                    return gridLayoutManager;
//                })
//                .setLayoutManagerFactory(recyclerView -> {
//                    return new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//                })
                .dataBinding((holder, data1) -> {
                    holder.<TextView>findViewById(R.id.tv_test).setText(data1.name);
                    holder.getViewBinder().setImageResource(R.id.iv_test, data1.icon);
                }, DataBindingMatchPolicy.MATCH_POLICY_ALL)
                .dataBindingByPayloadsOrNotIncludedPayloads((holder, data1) -> {
                    holder.itemView.setBackgroundColor(data1.isChecked() ? 0xff00ff00 : 0xffffffff);
                }, SelectionManager.PAYLOAD)
                .build(findViewById(R.id.rv_test));
        configuration.getRecyclerView().addItemDecoration(new DividerItemDecoration(20, 20, 20, 20, 50));

        findViewById(R.id.btn_add).setOnClickListener(v -> {
            data.add(new Student(String.valueOf(random.nextInt())));
            adapterDataObserver.notifyItemChanged(data.size() - 1);
        });

        findViewById(R.id.btn_remove).setOnClickListener(v -> {
            data.remove(data.size() - 1);
            adapterDataObserver.notifyItemRemoved(data.size());
        });

        findViewById(R.id.btn_random_remove).setOnClickListener(v -> {
            int index = new Random().nextInt(data.size());
            data.remove(index);
            adapterDataObserver.notifyItemRemoved(index);
        });
    }

    private static class Student implements Selectable {

        private String name;

        @DrawableRes
        private int icon;
        private boolean checked = false;

        public Student(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        @Override
        public boolean isChecked() {
            return checked;
        }

        @Override
        public void checked(boolean checked) {
            this.checked = checked;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}