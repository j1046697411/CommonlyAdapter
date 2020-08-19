package org.jzl.android.recyclerview.app;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;

import org.jzl.android.ViewBinder;
import org.jzl.android.recyclerview.component.Component;
import org.jzl.android.recyclerview.component.LayoutManagerComponent;
import org.jzl.android.recyclerview.component.SelectComponent;
import org.jzl.android.recyclerview.component.animator.MatchPolicyAnimatorFactory;
import org.jzl.android.recyclerview.component.animator.SlideAnimatorFactory;
import org.jzl.android.recyclerview.component.diff.AsyncDiffDispatcher;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.CommonlyViewHolder;
import org.jzl.android.recyclerview.core.data.Extractable;
import org.jzl.android.recyclerview.core.data.Identity;
import org.jzl.android.recyclerview.core.item.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.core.util.DividingLineItemDecoration;
import org.jzl.android.recyclerview.data.Selectable;
import org.jzl.android.recyclerview.plugin.Plugin;
import org.jzl.android.recyclerview.util.ScreenUtils;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.RandomUtils;
import org.jzl.lang.util.StringRandomUtils;
import org.jzl.lang.util.datablcok.DataBlock;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "-MainActivity-";

    private DataBlock<Student> dataBlock;
    private LayoutManagerComponent<Student, CommonlyViewHolder> layoutManager;
    private static AtomicInteger type = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Student> array = CollectionUtils.newArrayList();
        for (int i = 0; i < 1000; i++) {
            Student student = randomStudent();
            student.checked = student.getDataId() % 3 == 0;
            array.add(student);
        }
        SelectComponent<Student, CommonlyViewHolder> selectComponent = SelectComponent.of();
        AdapterConfigurator.<Student>of()
                .layoutManager(target -> {
                    target.gridLayoutManager(4);
                    target.setSpanSizeLookup((position, itemType, positionType) -> positionType == DataBlock.PositionType.HEADER || positionType == DataBlock.PositionType.FOOTER || position % 5 == 1 ? 4 : 1);
                    this.layoutManager = target;
                })
                .diff(AsyncDiffDispatcher::new, new DiffUtil.ItemCallback<Student>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                        return oldItem.id == newItem.id;
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                        return true;
                    }
                })
                .setDividingLine(20)
                .bindAnimator(new MatchPolicyAnimatorFactory<CommonlyViewHolder>(new SlideAnimatorFactory<>(SlideAnimatorFactory.Direction.BOTTOM))
                        .addAnimatorFactory(holder -> holder.getAdapterPosition() % 3 == 0, new SlideAnimatorFactory<>(SlideAnimatorFactory.Direction.LEFT))
                        .addAnimatorFactory(holder -> holder.getAdapterPosition() % 3 == 2, new SlideAnimatorFactory<>(SlideAnimatorFactory.Direction.RIGHT)))
                .setDataClassifier((dataProvider, position) -> (int) (dataProvider.getData(position).id % 2))
                .addAll(array)
                .defaultDataBlock(dataBlock -> this.dataBlock = dataBlock)
                .footerDataBlock(1, dataBlock -> dataBlock.add(randomStudent()))
                .headerDataBlock(1, dataBlock -> dataBlock.add(randomStudent()))
                .createItemView(R.layout.item_text_test)
                .dataBindingByBindModeNormalAndItemViewType((holder, data) -> holder.provide().setText(R.id.tv_test, "#" + data.text), 0)
                .dataBindingByBindModeNormalAndItemViewType((holder, data) -> holder.provide().setText(R.id.tv_test, "*" + data.text), 1)
                .dataBindingByBindModeNormal((holder, data) -> holder.provide().setText(R.id.tv_test, "id = " + data.id), context -> context.getExtra(R.id.tag_item_id, 0L) % 5 == 1)
                .dataBindingByBindModeNormalAndItemViewType((holder, data) -> Log.d(TAG, "data->" + data.getDataId() + ":dataBindingByBindModeNormal"))
                .dataBindingByBindModePayloads((holder, data) -> {
                    holder.provide().setBackground(R.id.tv_test, data.isChecked() ? R.color.colorAccent : R.color.colorPrimaryDark);
                    Log.d(TAG, "data->" + data.isChecked() + "|" + data.getDataId());
                }, SelectComponent.PAYLOAD_SELECT)
                .addOnUpdatedItemContextListener((context, dataProvider, holder, data) -> {
                    context.putExtra(R.id.tag_item_id, dataProvider.getDataId(holder.getAdapterPosition()));
                })
                .plugin(new ColorPlugin())
                .addComponent(new TestComponent())
                .component(TestComponent.class, target -> {
                    DataBlock<Student> dataBlock = target.getDataBlock();
                    Log.d(TAG, "data -> " + dataBlock);
                    Log.d(TAG, "type -> " + dataBlock.getPositionType());
                    Log.d(TAG, "id -> " + dataBlock.getBlockId());
                    Log.d(TAG, "size ->" + dataBlock.size());
                })
                .addComponent(selectComponent, 0)
                .attachedToRecyclerView(findViewById(R.id.rv_test));
        ViewBinder.bind(this)
                .bindClickListener(R.id.btn_add, view -> {
                    dataBlock.add(0, randomStudent());
                })
                .bindClickListener(R.id.btn_remove, view -> {
                    if (CollectionUtils.nonEmpty(dataBlock)) {
                        dataBlock.remove(0);
                    }
                })
                .bindClickListener(R.id.btn_random_remove, view -> {
                    if (CollectionUtils.nonEmpty(dataBlock)) {
                        Set<Student> students = CollectionUtils.newHashSet();
                        int random = RandomUtils.random(dataBlock.size());
                        for (int i = 0; i < random; i++) {
                            students.add(dataBlock.get(RandomUtils.random(dataBlock.size())));
                        }
                        dataBlock.removeAll(students);
                        Log.d(TAG, "随机移除：" + random + "|" + students.size());
                    }
                })
                .bindClickListener(R.id.btn_select_all, view -> {
                    if (ObjectUtils.nonNull(layoutManager)) {
                        if (type.compareAndSet(0, 1)) {
                            layoutManager.linearLayoutManager();
                        } else if (type.compareAndSet(1, 0)) {
                            layoutManager.gridLayoutManager(4);
                        }
                    }

                });
    }

    private static long ID = 0;

    private Student randomStudent() {
        return new Student(false, StringRandomUtils.randomUpperString(5), ID++);
    }

    private static class Student implements Selectable, Identity, Extractable {
        private boolean checked;
        private String text;
        private long id;
        private SparseArray<Object> extras = new SparseArray<>();

        public Student(boolean checked, String text, long id) {
            this.checked = checked;
            this.text = text;
            this.id = id;
        }

        @Override
        public boolean isChecked() {
            return checked;
        }

        @Override
        public void checked(boolean checked) {
            this.checked = checked;
        }

        @Override
        public long getDataId() {
            return id;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "checked=" + checked +
                    ", text='" + text + '\'' +
                    ", id=" + id +
                    ", extras=" + extras +
                    '}';
        }

        @Override
        @SuppressWarnings("all")
        public <E> E getExtra(int key, E defValue) {
            return (E) extras.get(key, defValue);
        }

        @Override
        public void putExtra(int key, Object value) {
            extras.put(key, value);
        }

        @Override
        public boolean hasExtra(int key) {
            return ObjectUtils.nonNull(extras.get(key));
        }

        @Override
        public void removeExtra(int key) {
            extras.remove(key);
        }
    }


    public static class ColorPlugin implements Plugin<Student, CommonlyViewHolder, AdapterConfigurator<Student, CommonlyViewHolder>> {

        @Override
        public void setup(AdapterConfigurator<Student, CommonlyViewHolder> configurator, ItemBindingMatchPolicy matchPolicy) {
            configurator.dataBinding((holder, data) -> holder.itemView.setBackgroundResource(R.color.colorPrimary), DataBindingMatchPolicy.ofPositionType(DataBlock.PositionType.HEADER))
                    .dataBinding((holder, data) -> holder.itemView.setBackgroundColor(0x00ffffff), DataBindingMatchPolicy.ofPositionType(DataBlock.PositionType.CONTENT))
                    .dataBinding((holder, data) -> holder.itemView.setBackgroundResource(R.color.colorAccent), DataBindingMatchPolicy.ofPositionType(DataBlock.PositionType.FOOTER));
        }
    }

    public static class TestComponent implements Component<Student, CommonlyViewHolder> {

        private DataBlock<Student> dataBlock;

        @Override
        public void apply(AdapterConfigurator<Student, CommonlyViewHolder> configurator, ItemBindingMatchPolicy matchPolicy) {
            configurator.headerDataBlock(1, dataBlock -> this.dataBlock = dataBlock);
        }

        public DataBlock<Student> getDataBlock() {
            return dataBlock;
        }
    }

}